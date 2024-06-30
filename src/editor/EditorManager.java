package editor;

import editor.windows.MainWindowController;
import importer.SubImage;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import save.Config;

import java.util.concurrent.CountDownLatch;

public class EditorManager extends Application {
    private static Stage stage;
    private static MainWindowController mainWindowController;
    public static final CountDownLatch latch = new CountDownLatch(1);

    @Override
    public void start(Stage primaryStage) throws Exception {
        setUserAgentStylesheet(STYLESHEET_MODENA);
        stage = primaryStage;
        Platform.setImplicitExit(false);

        // define root and mainWindowController
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/window/main_window.fxml"));
        Parent root = loader.load();
        mainWindowController = loader.getController();

        // define scene
        Scene scene = new Scene(root, 600, 400);
//        scene.getStylesheets().add("/editor/themes/darktheme.css");

        // define stage
        stage.setScene(scene);
        stage.setTitle("Editor - SubImage");
        stage.getIcons().add(new Image("window/images/32_greatball.png"));
        stage.setX(600);
        stage.setY(400);

        stage.setOnCloseRequest(event -> {
            handleWindowClose(event);
        });

        stage.show();

        // Notify that JavaFX initialization is complete
        latch.countDown();
    }

    private void handleWindowClose(WindowEvent event) {
        // Save configuration
        SubImage.saveConfig();

        event.consume();
        Platform.exit();
        System.exit(0);
    }

    public static void update() {
        Platform.runLater(() -> {
            if (mainWindowController != null) {
                mainWindowController.update();
            }
        });
    }

    public static void init(String[] args) {
        new Thread(() -> Application.launch(EditorManager.class, args)).start();
    }
}
