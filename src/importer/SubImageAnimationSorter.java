package importer;

import util.Logger;

import java.io.File;
import java.util.Objects;

public class SubImageAnimationSorter {

    //INPUT
    static File INPUT_FOLDER = new File("C:\\Users\\Ricardo\\Desktop\\SIA_input");
    //OUTPUT
    static File OUTPUT_FOLDER = new File("C:\\Users\\Ricardo\\Desktop\\SIA_output");

    public static void main(String[] args){
       init(INPUT_FOLDER, OUTPUT_FOLDER);
    }

    public static void init(File inputFolder, File outputFolder){
        Logger.info("\nSTARTING SubImageAnimationSorter\nINPUT: %s OUTPUT: %s\n", inputFolder, outputFolder);
        boolean firstFolder = true;

        for(final File subFolder : Objects.requireNonNull(inputFolder.listFiles())){
            if(subFolder.isDirectory()){
                for(File png : Objects.requireNonNull(subFolder.listFiles((dir, name) -> name.endsWith(".png")))){
                    if(firstFolder){
                        String newDirPath = outputFolder + "\\" + png.getName().substring(0, png.getName().length() - 4);
                        Logger.info(String.format("Creating folder %s", newDirPath));
                        new File(newDirPath).mkdirs();
                    }

                    String newPNGName = outputFolder + "\\" + png.getName().substring(0, png.getName().length() - 4) +
                                        "\\" + subFolder.getName().charAt(0) + ".png";

                    if (!png.renameTo(new File(newPNGName))) {
                        Logger.error("fail @ moving png");
                    }
                }
                firstFolder = false;
            }
        }
    }
}




