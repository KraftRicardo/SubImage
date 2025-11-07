package importer;

import util.Logger;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.LinkedList;
import java.util.List;

public class SubImageObjects {
    public final static String ORIGINAL_NAME = "/(original).png";

    //Object cutting settings
    private static boolean jump10PixelsPerRow = true;

    private static Color backGroundColor;
    private static Color backGroundColor2;
    private static Path targetDirectory;

    private static boolean isCutRectanglesStyle = false;
    private static int rectangleWidth = 64;
    private static int rectangleHeight = 64;

    // cutStyle = true : name cuts after their position on the original image and create (original).png
    // nameAfterPixelPosition = false : name after pixel position in the original png and not (original).png
    // cutRectanglesStyle: if true cut not by composite but into rectangles of size defined in the function "cutIntoRectangles"
    public static void subImageObjects(String imagePath, int filterOutBackgroundColor, boolean nameAfterPixelPosition, boolean cutRectanglesStyle) {
        createResultFolder(imagePath);

        // Cut
        if (cutRectanglesStyle) {
            cutIntoRectangles(imagePath, nameAfterPixelPosition);
        } else {
            cutIntoComposites(imagePath, nameAfterPixelPosition, filterOutBackgroundColor);
        }

        // Add the original image to the result folder as orientation for the pixel positions
        if (nameAfterPixelPosition) {
            copyInOriginalPNG(imagePath);
        }
    }

    private static void createResultFolder(String pathToImage) {
        Path path = Paths.get(pathToImage);
        String name = path.getFileName().toString();
        name = name.substring(0, name.lastIndexOf('.'));
        targetDirectory = path.getParent().resolve(name);

        if (!Files.exists(targetDirectory)) {
            try {
                Files.createDirectories(targetDirectory);
            } catch (IOException e) {
                Logger.error("Fail @ creating folder PATH : %s", targetDirectory);
            }
        }
    }

    private static void copyInOriginalPNG(String pathToImage) {
        Path dst = Paths.get(targetDirectory + ORIGINAL_NAME);
        try {
            Files.copy(Paths.get(pathToImage), dst, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            Logger.error("Fail @ copying image PATH : %s", pathToImage);
        }
    }

    // Cuts into images with touching pixels
    // if filterOutBackgroundColor == true, then the pixel [0,0] has the backgroundcolor
    public static void cutIntoComposites(String pathToImage, boolean nameAfterPosition, int filterOutBackgroundColor) {
        BufferedImage image = null;
        try {
            image = ImageIO.read(Paths.get(pathToImage).toFile());
        } catch (IOException e) {
            Logger.error("Fail @ reading the image PATH : %s", pathToImage);
        }

        if (image == null) {
            return;
        }

        boolean[][] alreadyProcessed = new boolean[image.getWidth()][image.getHeight()];
        int counter = 0;

        backGroundColor = new Color(image.getRGB(0, 0), true);
        backGroundColor2 = new Color(image.getRGB(image.getWidth() - 1, image.getHeight() - 1), true);

        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                if (alreadyProcessed[x][y] || isNotPartOfNewImage(x, y, image, filterOutBackgroundColor)) {
                    continue;
                }
                alreadyProcessed[x][y] = true;

                boolean[][] connected = new boolean[image.getWidth()][image.getHeight()];
                connected[x][y] = true;
                List<Point> open = new LinkedList<>();
                open.add(new Point(x, y));

                int maxX = Integer.MIN_VALUE, maxY = Integer.MIN_VALUE;
                int minX = Integer.MAX_VALUE, minY = Integer.MAX_VALUE;

                while (!open.isEmpty()) {
                    Point p = open.remove(0);

                    maxX = Math.max(maxX, p.x);
                    maxY = Math.max(maxY, p.y);
                    minX = Math.min(minX, p.x);
                    minY = Math.min(minY, p.y);

                    Point[] neighbors = new Point[]{
                            new Point(0, 1),
                            new Point(0, -1),
                            new Point(1, 0),
                            new Point(-1, 0)
                    };

                    for (Point neighbor : neighbors) {
                        int nx = p.x + neighbor.x;
                        int ny = p.y + neighbor.y;

                        if (!isNotPartOfNewImage(nx, ny, image, filterOutBackgroundColor) && !connected[nx][ny]) {
                            open.add(new Point(nx, ny));
                            alreadyProcessed[nx][ny] = true;
                            connected[nx][ny] = true;
                        }
                    }
                }

                // save
                BufferedImage newImage = new BufferedImage(maxX - minX + 1, maxY - minY + 1, BufferedImage.TYPE_INT_ARGB);
                for (int newX = 0; newX < newImage.getWidth(); newX++) {
                    for (int newY = 0; newY < newImage.getHeight(); newY++) {
                        if (image.getRGB(minX + newX, minY + newY) != image.getRGB(0, 0)) {
                            newImage.setRGB(newX, newY, image.getRGB(minX + newX, minY + newY));
                        }
                    }
                }

                try {
                    String name;
                    if (nameAfterPosition) {
                        name = minX + "_" + minY + ".png";
                    } else {
                        name = "" + counter++ + ".png";
                    }
                    ImageIO.write(newImage, "PNG", targetDirectory.resolve(name).toFile());
                } catch (IOException e) {
                    Logger.error("Fail @ writing the image PATH : %s", targetDirectory.resolve((counter++) + ".png"));
                }
            }
            if (jump10PixelsPerRow) {
                y += 9;
            }
        }
    }

    // if filterOutBackgroundColor == true, then the pixel [0,0] has the backgroundcolor
    public static void cutIntoRectangles(String pathToImage, boolean nameAfterPixelPosition) {
        final int rectangleWidth = 512;
        final int rectangleHeight = 256;

        BufferedImage image;
        try {
            image = ImageIO.read(Paths.get(pathToImage).toFile());
        } catch (IOException e) {
            Logger.error("Fail @ reading the image PATH : %s", pathToImage);
            return;
        }

        if (image == null) {
            Logger.error("Image could not be loaded: %s", pathToImage);
            return;
        }

        int counter = 0;

        for (int y = 0; y <= image.getHeight() - rectangleHeight; y += rectangleHeight) {
            for (int x = 0; x <= image.getWidth() - rectangleWidth; x += rectangleWidth) {
                BufferedImage newImage = new BufferedImage(rectangleWidth, rectangleHeight, BufferedImage.TYPE_INT_ARGB);

                for (int newX = 0; newX < rectangleWidth; newX++) {
                    for (int newY = 0; newY < rectangleHeight; newY++) {
                        newImage.setRGB(newX, newY, image.getRGB(x + newX, y + newY));
                    }
                }

                try {
                    String name = nameAfterPixelPosition ? (x + "_" + y + ".png") : (counter++ + ".png");
                    Path outputPath = targetDirectory.resolve(name);
                    ImageIO.write(newImage, "PNG", outputPath.toFile());
                } catch (IOException e) {
                    Logger.error("Fail @ writing image to PATH: %s", targetDirectory.resolve(counter + ".png"));
                }
            }
        }
    }

    // 0 = false
    // 1 = filter (0,0) pixel color
    // 2 = filter (0,0) , (width-1, height-1) pixel color
    private static boolean isNotPartOfNewImage(int x, int y, BufferedImage image, int filterOutBackgroundColor) {
        if (x < 0 || y < 0 || x >= image.getWidth() || y >= image.getHeight()) {
            return true;
        }
        Color color = new Color(image.getRGB(x, y), true);

        if (filterOutBackgroundColor == 0) {
            return color.getAlpha() == 0;
        }

        if (filterOutBackgroundColor == 1) {
            return color.getAlpha() == 0 || color.equals(backGroundColor);
        }

        if (filterOutBackgroundColor == 2) {
            return color.getAlpha() == 0 || color.equals(backGroundColor) || color.equals(backGroundColor2);
        }

        Logger.error("Wrong filterOutBackgroundColor value = " + filterOutBackgroundColor);
        return true;
    }
}
