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

public class ImportWaterTiles {
    // #### WorldInput ####
    static final int SUB_IMAGE_HEIGHT = 48;
    // #### WorldInput ####

    static final File INPUT_FOLDER = new File("res/importing/water_tiles_importer");
    private static BufferedImage image;
    private static Path targetDirectory;

    public static void main(String[] args) throws IOException {
        if(INPUT_FOLDER.isDirectory()){
            for(File png : INPUT_FOLDER.listFiles((dir, name) -> name.endsWith(".png"))){
                String filePath = png.toString();
                Logger.info("Cutting: %s", filePath);
                init(filePath);
            }
        }
    }

    private static void init(String filePath) throws IOException {
        createSubImagesFolder(filePath);
        cutToSubImages(filePath);
        SubImage.cutStatics(new File(targetDirectory.toString()), false, true, 0);

        Path animations = Paths.get(filePath.replace(".png", ""));
        if(!Files.exists(animations)) {
            Files.createDirectories(animations);
        }

        SubImageAnimationSorter.init(new File(targetDirectory.toString()), new File(animations.toString()));
        Util.deleteDir(new File(targetDirectory.toString()));
    }

    private static void createSubImagesFolder(String filePath) throws IOException {
        targetDirectory = Paths.get(filePath.replace(".png", "")+"_subImages");
        if(!Files.exists(targetDirectory)) {
            Files.createDirectories(targetDirectory);
        }
    }

    private static void cutToSubImages(String filePath) throws IOException {
        image = ImageIO.read(Paths.get(filePath).toFile());
        if(image.getHeight() % SUB_IMAGE_HEIGHT != 0){
            Logger.error("Image: %s is not dividable height by %d", filePath, SUB_IMAGE_HEIGHT);
        }

        int subImageCount = 0;
        BufferedImage subImage = new BufferedImage(image.getWidth(), SUB_IMAGE_HEIGHT, BufferedImage.TYPE_INT_ARGB);

        while(subImageCount * SUB_IMAGE_HEIGHT < image.getHeight()){
            for(int y = 0; y < SUB_IMAGE_HEIGHT; y++){
                for(int x = 0; x < image.getWidth(); x++){
                    subImage.setRGB(x, y, image.getRGB(x, y + subImageCount * SUB_IMAGE_HEIGHT));
                }
            }
            ImageIO.write(subImage, "PNG", targetDirectory.resolve((subImageCount++) + ".png").toFile());
//            Logger.info("Subimage %d.png written", subImageCount - 1);
            subImage = new BufferedImage(image.getWidth(), SUB_IMAGE_HEIGHT, BufferedImage.TYPE_INT_ARGB);
        }
    }
}