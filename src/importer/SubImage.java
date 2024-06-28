package importer;

import lombok.Getter;
import lombok.Setter;
import org.json.JSONException;
import save.Config;
import util.Logger;
import org.json.JSONObject;

import java.io.*;

public class SubImage {
    private static final String CONFIG_FILE_PATH = "res/save/config.json";

    private static Config cfg;

    public static void cutAll() {
        cutTiles();
        cutObjects();
        cutAnimations();
    }

    public static void cutTiles() {
        Logger.info("Cutting tiles ...");
        if (cfg.getTileFolder().isDirectory()) {
            for (final File f : cfg.getTileFolder().listFiles((dir, name) -> name.endsWith(".png"))) {
                String imagePath = f.getAbsolutePath();
                Logger.info("Cutting: %s", imagePath);
                SubImageTiles.subImageTiles(imagePath);
            }
        }
    }

    public static void cutObjects() {
        Logger.info("Cutting objects ...");
        if (cfg.getObjectFolder().isDirectory()) {
            for (final File f : cfg.getObjectFolder().listFiles((dir, name) -> name.endsWith(".png"))) {
                String imagePath = f.getAbsolutePath();
                Logger.info("Cutting: %s", imagePath);
                SubImageObjects.subImageObjects(imagePath, cfg.getFilterMode(), true);
            }
        }
    }

    public static void cutAnimations() {
        Logger.info("Cutting animations ...");
        if (cfg.getAnimationFolder().isDirectory()) {
            for (final File f : cfg.getAnimationFolder().listFiles((dir, name) -> name.endsWith(".png"))) {
                String imagePath = f.getAbsolutePath();
                Logger.info(imagePath);
                SubImageObjects.subImageObjects(imagePath, cfg.getFilterMode(), false);
            }
        }
    }

    public static void init() {cfg = new Config(CONFIG_FILE_PATH);}
    public static void saveConfig() {cfg.saveValuesInJson(CONFIG_FILE_PATH);}

    public static String getTileFolderPath() {return cfg.getTileFolder().getPath();}
    public static String getObjectFolderPath() {
        return cfg.getObjectFolder().getPath();
    }
    public static String getAnimationFolderPath() {
        return cfg.getAnimationFolder().getPath();
    }
    public static int getTileSize() {return cfg.getTileSize();}
    public static int getFilterMode() {return cfg.getFilterMode();}

    public static void setTileFolderPath(String path) {cfg.setTileFolder(path);}
    public static void setObjectFolderPath(String path) {cfg.setObjectFolder(path);}
    public static void setAnimationFolderPath(String path) {cfg.setAnimationFolder(path);}
    public static void setTileSize(int newSize) {cfg.setTileSize(newSize);}
    public static void setFilterMode(int newMode) {cfg.setFilterMode(newMode);}


}
