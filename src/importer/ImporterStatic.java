package importer;

import util.Logger;
import util.Util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

public class ImporterStatic {

    private final static String INPUT_FOLDER_TILE = "res/importing/importer_static/input/tiles";
    private final static String INPUT_FOLDER_OBJ = "res/importing/importer_static/input/objects";
    private final static String OUTPUT_FOLDER_TILE = "res/images/map/tiles/statics";
    private final static String OUTPUT_FOLDER_OBJ = "res/images/map/objects/statics";

    private static String src;
    private static String dst;

    // WorldInput: Put images and folder with images in there: /importing/importer_static/input/tiles or objects folder
    // Output: res/importing/importer_static/output
    public static void main(String[] args) {
//        TODO dont run this, this breaks stuff in the images and fucks with your configs!
//        Logger.info("Starting ImporterStatic ...");
//        loadNewInputImages();
//        Logger.info("End ImporterStatic");
    }

    public static void loadNewInputImages(){
        refreshMapTilesFolder();
        refreshObjectsFolder();
    }

    private static void refreshMapTilesFolder(){
        src = INPUT_FOLDER_TILE;
        dst = OUTPUT_FOLDER_TILE;
        Util.deleteDir(new File(dst));
        if(!new File(dst).mkdirs()){
            Logger.error("Fail @ directory creation: %s", dst);
        }

        copyOverFolder(new File(src));
    }

    private static void refreshObjectsFolder(){
        src = INPUT_FOLDER_OBJ;
        dst = OUTPUT_FOLDER_OBJ;
        Util.deleteDir(new File(dst));
        if(new File(dst).mkdirs()){
            Logger.error("Fail @ directory creation: %s", dst);
        }

        copyOverFolder(new File(INPUT_FOLDER_OBJ));
    }

    private static void copyOverFolder(File file){
        for(File f : Objects.requireNonNull(file.listFiles())){
            if(f.isDirectory()){
                createFolder(f);
                copyOverFolder(f);
            } else if("png".equals(Util.getFileType(f.getPath()))){
                String pathToImage = copyOverImage(f);
                cut(pathToImage);
            }
        }
    }

    private static void createFolder(File file) {
        Path targetDirectory = Paths.get(dst + file.getPath().substring(src.length()));
        if(!Files.exists(targetDirectory)) {
            try {
                Files.createDirectories(targetDirectory);
            } catch (IOException e) {
                Logger.error("Fail @ creating folder PATH : %s", targetDirectory, e);
            }
        }
    }

    private static String copyOverImage(File file) {
        Path pathToImage = Paths.get(file.getPath());
        Path moveImageHere = getPath(file);

        try {
            Files.copy(pathToImage, moveImageHere, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            Logger.error("Fail @ moving png SRC : %s DST: %s", pathToImage, moveImageHere, e);
        }

        return moveImageHere.toString();
    }

    private static void cut(String pathToImage){
        if(src.equals(INPUT_FOLDER_TILE)){
            SubImageTiles.subImageTiles(pathToImage);
        } else {
            SubImageObjects.subImageObjects(pathToImage, 0, true);
        }

        if(!new File(pathToImage).delete()){
            Logger.error("Fail @ deleting copy PATH %s", dst);
        }
    }

    private static Path getPath(File file){
        return Paths.get(dst + file.getPath().substring(src.length()));
    }
}
