package importer;

import util.Logger;
import java.io.File;

public class SubImage {
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
