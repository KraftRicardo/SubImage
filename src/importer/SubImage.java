package importer;

import lombok.Getter;
import save.Config;
import util.Logger;

import java.io.*;

public class SubImage {
    private static final String CONFIG_FILE_PATH = "data/config.json";

    @Getter private static Config cfg;

    public static void cutAll() {
        cutTiles();
        cutObjects();
        cutAnimations();
        cutWaterTiles();
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
                // TODO make the last 2 parameters tracked by cfg
                SubImageObjects.subImageObjects(imagePath, cfg.getFilterMode(), false, true);
            }
        }
    }

    public static void cutAnimations() {
        Logger.info("Cutting animations ...");
        if (cfg.getAnimationFolder().isDirectory()) {
            for (final File f : cfg.getAnimationFolder().listFiles((dir, name) -> name.endsWith(".png"))) {
                String imagePath = f.getAbsolutePath();
                Logger.info(imagePath);
                // TODO make the last 2 parameters tracked by cfg
                SubImageObjects.subImageObjects(imagePath, cfg.getFilterMode(), false, false);
            }
        }
    }

    public static void cutWaterTiles() {
        Logger.info("Cutting water tiles ...");
        if (cfg.getWaterTilesFolder().isDirectory()) {
            for (final File f : cfg.getWaterTilesFolder().listFiles((dir, name) -> name.endsWith(".png"))) {
                String imagePath = f.getAbsolutePath();
                Logger.info(imagePath);
                ImportWaterTiles.subImageWaterTiles(imagePath, cfg.getWaterTileSubImageHeight());
            }
        }
    }

    public static void init() {
        cfg = new Config(CONFIG_FILE_PATH);
    }

    public static void saveConfig() {
        cfg.saveValuesInJson(CONFIG_FILE_PATH);
    }
}
