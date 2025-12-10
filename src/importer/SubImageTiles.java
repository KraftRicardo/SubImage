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

public class SubImageTiles {
    public final static String ORIGINAL_NAME = "/(original).png";

    private static BufferedImage image;
    private static Path targetDirectory;

    public static void subImageTiles(String pathToImage) {
        if(init(pathToImage)){
            cut();
            copyInOriginalPNG(pathToImage);
        } else {
            Logger.info("Skipping image due to error: %s", pathToImage);
        }
    }

    private static boolean init(String pathToImage) {
        try {
            image = ImageIO.read(Paths.get(pathToImage).toFile());
        } catch (IOException e) {
            Logger.error("Fail @ reading the image PATH : %s", pathToImage);
            return false;
        }

        if(image.getWidth() % SubImage.getCfg().getTileWidth() != 0 || image.getHeight() % SubImage.getCfg().getTileHeight() != 0){
            Logger.error("Fail @%s image size not dividable by tile size WIDTH : %d, HEIGHT : %d",
                    pathToImage, image.getWidth(), image.getHeight());
            return false;
        }

        Path path = Paths.get(pathToImage);
        String name = path.getFileName().toString();
        name = name.substring(0, name.lastIndexOf('.'));
        targetDirectory = path.getParent().resolve(name);

        if(!Files.exists(targetDirectory)) {
            try {
                Files.createDirectories(targetDirectory);
            } catch (IOException e) {
                Logger.error("Fail @ creating folder PATH : %s", targetDirectory);
                return false;
            }
        }

        return true;
    }

    private static void cut() {
        int tileWidth = SubImage.getCfg().getTileWidth();
        int tileHeight = SubImage.getCfg().getTileHeight();

        for(int y = 0; y < image.getHeight(); y = y + tileHeight) {
            for (int x = 0; x < image.getWidth(); x = x + tileWidth) {

                if (isTileEmpty(x, y, tileWidth, tileHeight)) {
                    continue;
                }

                //save tile in new image
                BufferedImage newImage = new BufferedImage(tileWidth, tileHeight, BufferedImage.TYPE_INT_ARGB);
                for (int newX = 0; newX < newImage.getWidth(); newX++) {
                    for (int newY = 0; newY < newImage.getHeight(); newY++) {
                        newImage.setRGB(newX, newY, image.getRGB(x + newX, y + newY));
                    }
                }

                int pos = calcPos(x, y, image.getWidth());
                try {
                    ImageIO.write(newImage, "PNG", targetDirectory.resolve((pos) + ".png").toFile());
                } catch (IOException e) {
                    Logger.error("Fail @ saving image PATH : %s", targetDirectory.resolve((pos) + ".png").toFile());
                }
//                Logger.info("Subimage %d.png written", pos);
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

    private static int calcPos(int x, int y, int width) {
        int tileWidth = SubImage.getCfg().getTileWidth();
        int tileHeight = SubImage.getCfg().getTileHeight();

        int pos = 0;
        pos += (x / tileWidth);
        pos += (y / tileHeight) * (width / tileWidth);
        return pos;
    }

    private static boolean isAlphaZero(int x, int y) {
        return x <0 || y < 0 || x >= image.getWidth() || y >= image.getHeight() || new Color(image.getRGB(x, y), true).getAlpha() == 0;
    }

    private static boolean isTileEmpty(int x0, int y0, int width, int height){
        for(int y = y0;y < y0 + height - 1;y++) {
            for (int x = x0; x < x0 + width - 1; x++) {
                if(!isAlphaZero(x, y)){
                    return false;
                }
            }
        }
        //Logger.info("Tile is (%d, %d) is empty", x0, y0);
        return true;
    }
}