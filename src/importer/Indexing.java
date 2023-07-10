package importer;

import util.Logger;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Indexing {

    // INPUT
    private final static String inputMainFolder = "C:\\Users\\Ricardo\\Desktop\\4";
    private static int[] framesPerAnimation = {3, 3}; //idle, move

    // (INPUT usually unchanged)
    private final static String[] inputPNGNames = {"idle.png", "move.png"};
    private static int limit = 100;

    private final static String[] midStepFolders = {"idle_objects", "move_objects"};
    private final static String[] outputFolders = {"idle", "move"};
    private final static String[] outputSubFolders = {"BM", "TM", "ML", "MR", "BL", "BR", "TL", "TR"};

    public static void main(String[] args){
        cleanAndCutBaseImages();
        putInCorrectSubFolder();
        correctIndexingInSubFolder();
    }

    private static void cleanAndCutBaseImages(){
        for(String s : inputPNGNames){
            String path = inputMainFolder + "\\" + s;
            SubImageObjects.cut(path, 1, true);
        }
    }

    private static void putInCorrectSubFolder(){
        for(int i = 0; i < midStepFolders.length; i++){
            int counter = 0;
            String basePath = inputMainFolder + "\\" + outputFolders[i];
            new File(basePath).mkdirs();

            for(String s : outputSubFolders){
                String baseSubPath = basePath + "\\" + s;
                new File(baseSubPath).mkdirs();

                int innerLimit = counter + framesPerAnimation[i];
                for(int j = counter; j < innerLimit; j++){
                    Path source = Paths.get(inputMainFolder + "\\" + midStepFolders[i] + "\\" + counter + ".png");
                    Path baseSubSubPath =  Paths.get(baseSubPath + "\\" + j + ".png");
                    try {
                        Files.copy(source, baseSubSubPath);
                        counter++;
                        System.out.println(source + " move to " + baseSubSubPath);
                    } catch (IOException e) {
                        Logger.error("Fail at copying", e);
                    }
                }
            }
        }
    }

    private static void correctIndexingInSubFolder() {
        for(String base : outputFolders){
            for(String subName : outputSubFolders){
                int counter = 0;
                for(int i = 0; i < limit; i++){
                    String path = inputMainFolder + "\\" + base + "\\" + subName + "\\" + i + ".png";
                    File source = new File(path);
                    if(source.exists()){
                        String newPath = inputMainFolder + "\\" + base + "\\" + subName + "\\" + counter + ".png";
                        System.out.println(path + " rename to " + newPath);
                        File dest = new File(newPath);
                        source.renameTo(dest);
                        counter++;
                    }
                }
            }
        }
    }
    
}
