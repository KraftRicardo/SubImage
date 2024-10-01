package editor.windows;

import importer.SubImage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;

public class MainWindowController {
    @FXML Label tilesFolderPath;
    @FXML Label objectsFolderPath;
    @FXML Label animationsFolderPath;
    @FXML Label waterTilesFolderPath;

    @FXML TextField tileSize;
    @FXML TextField filterMode;
    @FXML TextField waterTileSubImageHeight;

    public void initialize() {
        // Listen for changes in tileSizeField
        tileSize.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                int newSize = Integer.parseInt(newValue);
                SubImage.setTileSize(newSize);
            } catch (NumberFormatException ignored) {
            }
        });
        tileSize.setText(String.valueOf(SubImage.getTileSize()));

        // Listen for changes in filterModeField
        filterMode.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                int newMode = Integer.parseInt(newValue);
                SubImage.setFilterMode(newMode);
            } catch (NumberFormatException ignored) {
            }
        });
        filterMode.setText(String.valueOf(SubImage.getFilterMode()));

        // Listen for changes in waterTileSubImageHeight
        waterTileSubImageHeight.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                int newMode = Integer.parseInt(newValue);
                SubImage.setWaterTileSubImageHeight(newMode);
            } catch (NumberFormatException ignored) {
            }
        });
        waterTileSubImageHeight.setText(String.valueOf(SubImage.getWaterTileSubImageHeight()));
    }

    public void update() {
        tilesFolderPath.setText(SubImage.getTileFolderPath());
        objectsFolderPath.setText(SubImage.getObjectFolderPath());
        animationsFolderPath.setText(SubImage.getAnimationFolderPath());
        waterTilesFolderPath.setText(SubImage.getWaterTilesFolderPath());
    }

    public void setTilesFolder(ActionEvent actionEvent) {
        String path = selectFolderAndSetPath();
        if (path != null) {
            SubImage.setTileFolderPath(path);
            update();
        }
    }

    public void setObjectsFolder(ActionEvent actionEvent) {
        String path = selectFolderAndSetPath();
        if (path != null) {
            SubImage.setObjectFolderPath(path);
            update();
        }
    }

    public void setAnimationsFolder(ActionEvent actionEvent) {
        String path = selectFolderAndSetPath();
        if (path != null) {
            SubImage.setAnimationFolderPath(path);
            update();
        }
    }

    public void setWaterTilesFolder(ActionEvent actionEvent) {
        String path = selectFolderAndSetPath();
        if (path != null) {
            SubImage.setWaterTilesFolderPath(path);
            update();
        }
    }

    public void cutAll(ActionEvent actionEvent) {SubImage.cutAll();}
    public void cutTiles(ActionEvent actionEvent) {
        SubImage.cutTiles();
    }
    public void cutObjects(ActionEvent actionEvent) {
        SubImage.cutObjects();
    }
    public void cutAnimations(ActionEvent actionEvent) {
        SubImage.cutAnimations();
    }
    public void cutWaterTiles(ActionEvent actionEvent) {
        SubImage.cutWaterTiles();
    }

    String selectFolderAndSetPath() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select Folder");
        Stage stage = (Stage) tilesFolderPath.getScene().getWindow();  // Assuming tilesFolderPath is in the same window
        File selectedDirectory = directoryChooser.showDialog(stage);
        return selectedDirectory != null ? selectedDirectory.getAbsolutePath() : null;
    }
}
