package editor.windows;

import importer.SubImage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import save.Config;
import util.Logger;

import java.io.File;

public class MainWindowController {
    @FXML public Label tilesFolderPath;
    @FXML public Label objectsFolderPath;
    @FXML public Label animationsFolderPath;
    @FXML public Label waterTilesFolderPath;
    @FXML public Label consoleLabel;

    @FXML public TextField tileWidth;
    @FXML public TextField tileHeight;
    @FXML public TextField filterMode;
    @FXML public TextField waterTileSubImageHeight;

    public void initialize() {
        tileWidth.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                int newWidth = Integer.parseInt(newValue);
                SubImage.getCfg().setTileWidth(newWidth);
            } catch (NumberFormatException ignored) {
            }
        });
        tileWidth.setText(String.valueOf(SubImage.getCfg().getTileWidth()));

        tileHeight.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                int newHeight = Integer.parseInt(newValue);
                SubImage.getCfg().setTileHeight(newHeight);
            } catch (NumberFormatException ignored) {

            }
        });
        tileHeight.setText(String.valueOf(SubImage.getCfg().getTileHeight()));

        filterMode.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                int newMode = Integer.parseInt(newValue);
                SubImage.getCfg().setFilterMode(newMode);
            } catch (NumberFormatException ignored) {
            }
        });
        filterMode.setText(String.valueOf(SubImage.getCfg().getFilterMode()));

        waterTileSubImageHeight.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                int newMode = Integer.parseInt(newValue);
                SubImage.getCfg().setWaterTileSubImageHeight(newMode);
            } catch (NumberFormatException ignored) {
            }
        });
        waterTileSubImageHeight.setText(String.valueOf(SubImage.getCfg().getWaterTileSubImageHeight()));
    }

    public void update() {
        Config cfg = SubImage.getCfg();

        tilesFolderPath.setText(cfg.getTileFolderPath());
        objectsFolderPath.setText(cfg.getObjectFolderPath());
        animationsFolderPath.setText(cfg.getAnimationFolderPath());
        waterTilesFolderPath.setText(cfg.getWaterTilesFolderPath());

        Logger.debug("msg" + cfg.getConsoleText());
        consoleLabel.setText(cfg.getConsoleText());
    }

    public void setTilesFolder(ActionEvent actionEvent) {
        String path = selectFolderAndSetPath();
        if (path != null) {
            SubImage.getCfg().setTileFolderPath(path);
            update();
        }
    }

    public void setObjectsFolder(ActionEvent actionEvent) {
        String path = selectFolderAndSetPath();
        if (path != null) {
            SubImage.getCfg().setObjectFolderPath(path);
            update();
        }
    }

    public void setAnimationsFolder(ActionEvent actionEvent) {
        String path = selectFolderAndSetPath();
        if (path != null) {
            SubImage.getCfg().setAnimationFolderPath(path);
            update();
        }
    }

    public void setWaterTilesFolder(ActionEvent actionEvent) {
        String path = selectFolderAndSetPath();
        if (path != null) {
            SubImage.getCfg().setWaterTilesFolderPath(path);
            update();
        }
    }

    public void clearConsoleTextField(ActionEvent actionEvent) {
        consoleLabel.setText("");
    }

    public void cutAll(ActionEvent actionEvent) {
        SubImage.cutAll();
    }

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
        Stage stage = (Stage) tilesFolderPath.getScene().getWindow(); // Assuming tilesFolderPath is in the same window
        File selectedDirectory = directoryChooser.showDialog(stage);
        return selectedDirectory != null ? selectedDirectory.getAbsolutePath() : null;
    }


}
