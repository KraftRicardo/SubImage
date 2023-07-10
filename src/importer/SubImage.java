package importer;

import util.Logger;
import java.io.File;

public class SubImage {
    // #### WorldInput ####
    static int filterOutBackgroundColor = 0; // 0 = no filter, 1 = filter (0,0) pixel color, 2 = filter (0,0) , (width-1, height-1) pixel color
    // #### WorldInput ####

    static File TILE_FOLDER = new File("res/importing/sub_image_input/tiles");
    static File OBJECT_FOLDER = new File("res/importing/sub_image_input/objects");
    static File ANIM_OBJECT_FOLDER = new File("res/importing/sub_image_input/animation");

    public static void main(String[] args){
        Logger.info("Cutting tiles ...");
        cutStatics(TILE_FOLDER, false, true, 0);
        Logger.info("Cutting objects ...");
        cutStatics(OBJECT_FOLDER, true, false, filterOutBackgroundColor);
        Logger.info("Cutting animations ...");
        cutAnimations(ANIM_OBJECT_FOLDER, filterOutBackgroundColor);
    }

    public static void cutStatics(File inputFolder, boolean createObjects, boolean createTiles, int filterMode){
        if(inputFolder.isDirectory()){
            for(final File f : inputFolder.listFiles((dir, name) -> name.endsWith(".png"))){
                String imagePath = f.getAbsolutePath();
                Logger.info(imagePath);
                if(createObjects){
                    SubImageObjects.subImageObjects(imagePath, filterMode, true);
                }
                if (createTiles) {
                    SubImageTiles.subImageTiles(imagePath);
                }
            }
        }
    }

    public static void cutAnimations(File inputFolder, int filterMode){
        if(inputFolder.isDirectory()){
            for(final File f : inputFolder.listFiles((dir, name) -> name.endsWith(".png"))){
                String imagePath = f.getAbsolutePath();
                Logger.info(imagePath);
                SubImageObjects.subImageObjects(imagePath, filterMode, false);
            }
        }
    }
}
