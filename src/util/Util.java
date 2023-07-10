package util;

import java.io.File;

public class Util {

    public static String getFileType(String path){
        String extension = "";
        int i = path.lastIndexOf('.');
        if (i > 0) {
            extension = path.substring(i+1);
        }
        return extension;
    }

    public static void deleteDir(File file) {
        File[] contents = file.listFiles();
        if (contents != null) {
            for (File f : contents) {
                deleteDir(f);
            }
        }
        if(!file.delete()){
            Logger.warning("Fail @ deleting folder Path : %s", file.getPath());
        }
    }

    public static int modulo(int a, int b){
        return (((a % b) + b) % b);
    }
}