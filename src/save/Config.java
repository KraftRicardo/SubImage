package save;

import lombok.Getter;
import lombok.Setter;
import org.json.JSONException;
import org.json.JSONObject;
import util.Logger;

import java.io.*;

@Getter
public class Config {
    File tileFolder;
    File objectFolder;
    File animationFolder;
    File waterTilesFolder;
    @Setter int tileSize;
    @Setter int filterMode;
    @Setter int waterTileSubImageHeight;

    public Config(String filePath){
        readValuesFromJson(filePath);
        Logger.info("%s",this);
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
            tileSize = json.getInt("tileSize");
            filterMode = json.getInt("filterMode");
            waterTileSubImageHeight = json.getInt("waterTileSubImageHeight");

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
            json.put("tileSize", tileSize);
            json.put("filterMode", filterMode);
            json.put("waterTileSubImageHeight", waterTileSubImageHeight);

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

    public void setTileFolder(String path) {
        try {
            tileFolder = new File(path);
            Logger.info("Tile folder set to: " + path);
        } catch (Exception e) {
            Logger.error("Error setting tile folder to: " + path);
        }
    }

    public void setObjectFolder(String path) {
        try {
            objectFolder = new File(path);
            Logger.info("Object folder set to: " + path);
        } catch (Exception e) {
            Logger.error("Error setting object folder to: " + path);
        }
    }

    public void setAnimationFolder(String path) {
        try {
            animationFolder = new File(path);
            Logger.info("Animation folder set to: " + path);
        } catch (Exception e) {
            Logger.error("Error setting animation folder to: " + path);
        }
    }

    public void setWaterTilesFolder(String path) {
        try {
            waterTilesFolder = new File(path);
            Logger.info("WaterTilesFolder folder set to: " + path);
        } catch (Exception e) {
            Logger.error("Error setting water-tiles folder to: " + path);
        }
    }

    @Override
    public String toString() {
        return "Config{" +
                "tileFolder='" + tileFolder.getPath() + '\'' +
                ", objectFolder='" + objectFolder.getPath() + '\'' +
                ", animationFolder='" + animationFolder.getPath() + '\'' +
                ", tileSize=" + tileSize +
                ", filterMode=" + filterMode +
                '}';
    }
}
