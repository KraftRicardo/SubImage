import editor.EditorManager;
import importer.SubImage;


public class Main {
    private final static int TARGET_FPS = 30;
    private final static long OPTIMAL_TIME = 1000 / TARGET_FPS;

    public static void main(String[] args) {
        EditorManager.init(args);

        SubImage.init();
        try {
            // Wait for JavaFX to initialize
            EditorManager.latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return;
         }

        mainLoop();
    }

    public static void mainLoop() {
        while (true) {
            long startTime = System.currentTimeMillis();

            // Update the editor manager
            EditorManager.update();

            // Calculate how long the update took
            long updateDuration = System.currentTimeMillis() - startTime;

            // Calculate the time to sleep
            long sleepTime = OPTIMAL_TIME - updateDuration;

            // Ensure that the sleep time is positive
            if (sleepTime > 0) {
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
