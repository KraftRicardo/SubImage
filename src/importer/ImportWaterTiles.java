package importer;

import util.Logger;
import util.Util;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

public class ImportWaterTiles {
    public static void subImageWaterTiles(String imagePath, int subImageHeight) {
        try {
            // Create SubImage folder
            Path targetDirectory = Paths.get(imagePath.replace(".png", "") + "_subImages");
            if (!Files.exists(targetDirectory)) {
                Files.createDirectories(targetDirectory);
            }

            // Cut image into SubImages
            cutToSubImages(imagePath, subImageHeight, targetDirectory);

            // Cut each subImage into tiles
            for (final File f : new File(targetDirectory.toString()).listFiles((dir, name) -> name.endsWith(".png"))) {
                String filePath = f.getAbsolutePath();
                Logger.info("Cutting: %s", filePath);
                SubImageTiles.subImageTiles(filePath);
            }

            Path animationFolderPath = Paths.get(imagePath.replace(".png", ""));
            if (!Files.exists(animationFolderPath)) {
                Files.createDirectories(animationFolderPath);
            }

            // Move the cut tiles in their corresponding folder
            sort(new File(targetDirectory.toString()), new File(animationFolderPath.toString()));

            // Delete the directories that were used during image cutting
            Util.deleteDir(new File(targetDirectory.toString()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static void cutToSubImages(String filePath, int subImageHeight, Path targetDirectory) throws IOException {
        BufferedImage image = ImageIO.read(Paths.get(filePath).toFile());
        if (image.getHeight() % subImageHeight != 0) {
            Logger.error("Image: %s is not dividable height by %d", filePath, subImageHeight);
        }

        int subImageCount = 0;
        BufferedImage subImage = new BufferedImage(image.getWidth(), subImageHeight, BufferedImage.TYPE_INT_ARGB);

        while (subImageCount * subImageHeight < image.getHeight()) {
            for (int y = 0; y < subImageHeight; y++) {
                for (int x = 0; x < image.getWidth(); x++) {
                    subImage.setRGB(x, y, image.getRGB(x, y + subImageCount * subImageHeight));
                }
            }
            ImageIO.write(subImage, "PNG", targetDirectory.resolve((subImageCount++) + ".png").toFile());
            subImage = new BufferedImage(image.getWidth(), subImageHeight, BufferedImage.TYPE_INT_ARGB);
        }
    }

    static void sort(File inputFolder, File outputFolder) {
        boolean firstFolder = true;

        for (final File subFolder : Objects.requireNonNull(inputFolder.listFiles())) {
            if (subFolder.isDirectory()) {
                for (File png : Objects.requireNonNull(subFolder.listFiles((dir, name) -> name.endsWith(".png")))) {
                    if (firstFolder) {
                        String newDirPath = outputFolder + "\\" + png.getName().substring(0, png.getName().length() - 4);
                        Logger.info(String.format("Creating folder %s", newDirPath));
                        new File(newDirPath).mkdirs();
                    }

                    String newPNGName = outputFolder + "\\" + png.getName().substring(0, png.getName().length() - 4) +
                            "\\" + subFolder.getName().charAt(0) + ".png";

                    if (!png.renameTo(new File(newPNGName))) {
                        Logger.error("Fail @ moving PNG to %s", newPNGName);
                    }
                }
                firstFolder = false;
            }
        }
    }

}