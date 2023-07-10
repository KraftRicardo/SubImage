package data_collection;

import util.Logger;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class RenameFiles {

    static String dirPath = "C:\\Users\\Ricardo\\Documents\\Pokemon Projekt\\Assets\\art\\characters\\pokemon_gen5\\down2";

    static final File dir = new File("C:\\Users\\Ricardo\\Documents\\Pokemon Projekt\\Assets\\art\\characters\\pokemon_gen5\\down2");

    public static void main(String[] args) {
        int startIndex = 495;

        Path pathFrame2 = null;
        try {
            pathFrame2 = Files.createDirectory(Paths.get(dirPath + "frame2"));
        } catch (IOException e) {
            Logger.error("failed creating frame2", e);
        }

        if (dir.isDirectory()) {
            for(int i = 0; i < 1400; i++){
                File tmp = new File(dirPath +i);
                if(tmp.exists()){
                    //rename
                    //and the move the next higher file in frame
                    int j = i+1;
                    File tmp2 = new File(dirPath + j);
                    while(!tmp2.exists()){
                        j++;
                        tmp2 = new File(dirPath + j);
                    }

//                    Files.move(tmp2.toPath(), pathFrame2);
                }
            }

            for (final File f : dir.listFiles((dir, name) -> name.endsWith(".png"))) {
                String imagePath = f.getAbsolutePath();
                Logger.info(imagePath);
            }
        }
    }
}
