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

    @FXML private Label tilesFolderPath;
    @FXML private Label objectsFolderPath;
    @FXML private Label animationsFolderPath;

    @FXML private TextField tileSize;
    @FXML private TextField filterMode;

    public void initialize() {
        // Listen for changes in tileSizeField
        tileSize.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                int newSize = Integer.parseInt(newValue);
                SubImage.setTileSize(newSize);
            } catch (NumberFormatException e) {
                // Handle non-integer input if needed
            }
        });
        tileSize.setText(String.valueOf(SubImage.getTileSize()));

        // Listen for changes in filterModeField
        filterMode.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                int newMode = Integer.parseInt(newValue);
                SubImage.setFilterMode(newMode);
            } catch (NumberFormatException e) {
                // Handle non-integer input if needed
            }
        });
        filterMode.setText(String.valueOf(SubImage.getFilterMode()));
    }

    public void update() {
        tilesFolderPath.setText(SubImage.getTileFolderPath());
        objectsFolderPath.setText(SubImage.getObjectFolderPath());
        animationsFolderPath.setText(SubImage.getAnimationFolderPath());
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

    private String selectFolderAndSetPath() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select Folder");
        Stage stage = (Stage) tilesFolderPath.getScene().getWindow();  // Assuming tilesFolderPath is in the same window
        File selectedDirectory = directoryChooser.showDialog(stage);
        return selectedDirectory != null ? selectedDirectory.getAbsolutePath() : null;
    }
}
