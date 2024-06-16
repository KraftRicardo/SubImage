package importer;

import lombok.Getter;
import lombok.Setter;
import org.json.JSONException;
import util.Logger;
import org.json.JSONObject;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class SubImage {
    private static final String CONFIG_FILE_PATH = "res/save/config.json";
    private static final String DEFAULT_TILE_FOLDER = "res/tiles";
    private static final String DEFAULT_OBJECTS_FOLDER = "res/objects";
    private static final String DEFAULT_ANIMATIONS_FOLDER = "res/animation";

    // State
    private static File tileFolder = new File(DEFAULT_TILE_FOLDER);
    private static File objectFolder = new File(DEFAULT_OBJECTS_FOLDER);
    private static File animationFolder = new File(DEFAULT_ANIMATIONS_FOLDER);
    @Getter @Setter private static int filterMode = 0; // 0 = no filter, 1 = filter (0,0) pixel color, 2 = filter (width-1, height-1) pixel color
    @Getter @Setter private static int tileSize = 16;

    static {
        // Load configuration on class initialization
        loadConfig();
    }

    public static void cutAll() {
        cutTiles();
        cutObjects();
        cutAnimations();
    }

    public static void cutTiles() {
        Logger.info("Cutting tiles ...");
        if (tileFolder.isDirectory()) {
            for (final File f : tileFolder.listFiles((dir, name) -> name.endsWith(".png"))) {
                String imagePath = f.getAbsolutePath();
                Logger.info("Cutting: %s", imagePath);
                SubImageTiles.subImageTiles(imagePath);
            }
        }
    }

    public static void cutObjects() {
        Logger.info("Cutting objects ...");
        if (objectFolder.isDirectory()) {
            for (final File f : objectFolder.listFiles((dir, name) -> name.endsWith(".png"))) {
                String imagePath = f.getAbsolutePath();
                Logger.info("Cutting: %s", imagePath);
                SubImageObjects.subImageObjects(imagePath, filterMode, true);
            }
        }
    }

    public static void cutAnimations() {
        Logger.info("Cutting animations ...");
        if (animationFolder.isDirectory()) {
            for (final File f : animationFolder.listFiles((dir, name) -> name.endsWith(".png"))) {
                String imagePath = f.getAbsolutePath();
                Logger.info(imagePath);
                SubImageObjects.subImageObjects(imagePath, filterMode, false);
            }
        }
    }

    public static String getTileFolderPath() {
        return tileFolder.getPath();
    }

    public static String getObjectFolderPath() {
        return objectFolder.getPath();
    }

    public static String getAnimationFolderPath() {
        return animationFolder.getPath();
    }

    public static void setTileFolderPath(String path) {
        tileFolder = new File(path);
        saveConfig();
    }

    public static void setObjectFolderPath(String path) {
        objectFolder = new File(path);
        saveConfig();
    }

    public static void setAnimationFolderPath(String path) {
        animationFolder = new File(path);
        saveConfig();
    }

    public static void loadConfig() {
        try {
            File configFile = new File(CONFIG_FILE_PATH);
            if (configFile.exists()) {
                String workingDir = System.getProperty("user.dir");
                Logger.info("Working Directory: " + workingDir);

                FileReader reader = new FileReader(configFile);
                JSONObject json = new JSONObject(reader);
                reader.close();

                // Log the full JSON content
                Logger.info("Loaded JSON configuration:");
                Logger.info(json.toString(4)); // Using 4 for pretty printing

                // Extract values from JSON
                tileFolder = new File(json.getString("tileFolder"));
                objectFolder = new File(json.getString("objectFolder"));
                animationFolder = new File(json.getString("animationFolder"));
                filterMode = json.getInt("filterMode");
                tileSize = json.getInt("tileSize");

                Logger.info("Configuration loaded from %s", CONFIG_FILE_PATH);
            } else {
                Logger.warning("Configuration file %s not found. Using default values.", CONFIG_FILE_PATH);
            }
        } catch (IOException | JSONException e) {
            Logger.error("Error reading configuration from %s", CONFIG_FILE_PATH);
            e.printStackTrace();
        }
    }


    public static void saveConfig() {
        try {
            // Create the directory if it doesn't exist
            File saveDir = new File("save");
            if (!saveDir.exists()) {
                if (!saveDir.mkdirs()) {
                    Logger.error("Failed to create directory: save");
                    return;
                }
            }

            JSONObject json = new JSONObject();
            json.put("tileFolder", tileFolder.getPath());
            json.put("objectFolder", objectFolder.getPath());
            json.put("animationFolder", animationFolder.getPath());
            json.put("filterMode", filterMode);
            json.put("tileSize", tileSize);

            FileWriter writer = new FileWriter(CONFIG_FILE_PATH);
            writer.write(json.toString(4));  // Using 4 for pretty printing
            writer.close();

            Logger.info("Configuration saved to %s", CONFIG_FILE_PATH);
        } catch (IOException | JSONException e) {
            Logger.error("Error writing configuration to %s", CONFIG_FILE_PATH);
            e.printStackTrace();
        }
    }

}
