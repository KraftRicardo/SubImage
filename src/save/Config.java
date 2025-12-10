package save;

import lombok.Getter;
import lombok.Setter;
import org.json.JSONException;
import org.json.JSONObject;
import util.Logger;

import java.io.*;

@Getter
public class Config {
    private static final int DEFAULT_TILE_SIZE = 16;

    private File tileFolder;
    private File objectFolder;
    private File animationFolder;
    private File waterTilesFolder;

    private String consoleText = "All good.";

    private int tileWidth = DEFAULT_TILE_SIZE;
    private int tileHeight = DEFAULT_TILE_SIZE;
    @Setter private int filterMode; // TODO needs proper Setter
    @Setter private int waterTileSubImageHeight; // TODO needs proper Setter

    @Setter private boolean nameAfterPixelPosition = true;
    @Setter private boolean cutAsRectangle = true;

    public Config(String filePath) {
        readValuesFromJson(filePath);
        Logger.info("%s", this);
    }

    public void readValuesFromJson(String filePath) {
        File jsonFile = new File(filePath);
        if (!jsonFile.exists() || jsonFile.isDirectory()) {
            Logger.warning("File not found or is a directory: " + filePath);
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(jsonFile))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            JSONObject json = new JSONObject(sb.toString());

            tileFolder = new File(json.getString("tileFolder"));
            objectFolder = new File(json.getString("objectFolder"));
            animationFolder = new File(json.getString("animationFolder"));
            waterTilesFolder = new File(json.getString("waterTilesFolder"));

            tileWidth = json.getInt("tileWidth");
            tileHeight = json.getInt("tileHeight");
            filterMode = json.getInt("filterMode");
            waterTileSubImageHeight = json.getInt("waterTileSubImageHeight");

            nameAfterPixelPosition = json.getBoolean("nameAfterPixelPosition");
            cutAsRectangle = json.getBoolean("cutAsRectangle");

            Logger.info("Configuration loaded successfully.");
        } catch (IOException | JSONException e) {
            Logger.error("Error reading the JSON file: " + filePath);
        }
    }

    public void saveValuesInJson(String filePath) {
        JSONObject json = new JSONObject();
        try {
            json.put("tileFolder", tileFolder.getPath());
            json.put("objectFolder", objectFolder.getPath());
            json.put("animationFolder", animationFolder.getPath());
            json.put("waterTilesFolder", waterTilesFolder.getPath());

            json.put("tileWidth", tileWidth);
            json.put("tileHeight", tileHeight);
            json.put("filterMode", filterMode);
            json.put("waterTileSubImageHeight", waterTileSubImageHeight);

            json.put("nameAfterPixelPosition", nameAfterPixelPosition);
            json.put("cutAsRectangle", cutAsRectangle);

            try (FileWriter file = new FileWriter(filePath)) {
                file.write(json.toString(4)); // Using 4 for pretty printing
                file.flush();
                Logger.info("Configuration saved successfully to " + filePath);
            } catch (IOException e) {
                Logger.error("Error writing the JSON file: " + filePath);
            }
        } catch (JSONException e) {
            Logger.error("Error creating the JSON object");
        }
    }

    public String getTileFolderPath() {
        return tileFolder.getPath();
    }

    public String getObjectFolderPath() {
        return objectFolder.getPath();
    }

    public String getAnimationFolderPath() {
        return animationFolder.getPath();
    }

    public String getWaterTilesFolderPath() {
        return waterTilesFolder.getPath();
    }

    public void setTileFolderPath(String path) {
        try {
            tileFolder = new File(path);
            Logger.info("Tile folder set to: " + path);
        } catch (Exception e) {
            setConsoleText("Error setting tile folder to: " + path);
        }
    }

    public void setObjectFolderPath(String path) {
        try {
            objectFolder = new File(path);
            Logger.info("Object folder set to: " + path);
        } catch (Exception e) {
            setConsoleText("Error setting object folder to: " + path);
        }
    }

    public void setAnimationFolderPath(String path) {
        try {
            animationFolder = new File(path);
            Logger.info("Animation folder set to: " + path);
        } catch (Exception e) {
            setConsoleText("Error setting animation folder to: " + path);
        }
    }

    public void setWaterTilesFolderPath(String path) {
        try {
            waterTilesFolder = new File(path);
            Logger.info("WaterTilesFolder folder set to: " + path);
        } catch (Exception e) {
            setConsoleText("Error setting water-tiles folder to: " + path);
        }
    }

    public void setTileWidth(int tileWidth) {
        if (tileWidth < 1) {
            Logger.warning("Bad input for tile width");
            return;
        }

        this.tileWidth = tileWidth;
    }

    public void setTileHeight(int tileHeight) {
        if (tileHeight < 1) {
            setConsoleText("Bad input for tile height");
            return;
        }

        this.tileHeight = tileHeight;
    }

    @Override
    public String toString() {
        return "Config{" +
                "tileFolder='" + tileFolder.getPath() + '\'' +
                ", objectFolder='" + objectFolder.getPath() + '\'' +
                ", animationFolder='" + animationFolder.getPath() + '\'' +
                ", filterMode=" + filterMode +
                '}';
    }

    private void setConsoleText(String message){
        Logger.error(message);
        consoleText = message;
    }
}
