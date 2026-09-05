package duchess.gui;

import java.io.IOException;

import duchess.Duchess;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/** The JavaFX application that hosts Duchess's chat window. */
public class Main extends Application {
    /** The initial width of the chat window. */
    private static final double WINDOW_WIDTH = 520.0;

    /** The initial height of the chat window. */
    private static final double WINDOW_HEIGHT = 680.0;

    /**
     * Loads the FXML view, injects the Duchess command processor, and shows the window.
     *
     * @param stage the primary JavaFX stage
     */
    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/MainWindow.fxml"));
            AnchorPane root = fxmlLoader.load();
            Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
            scene.getStylesheets().add(Main.class.getResource("/css/main.css").toExternalForm());

            MainWindow controller = fxmlLoader.getController();
            controller.setDuchess(new Duchess());

            stage.setTitle("Duchess");
            stage.setMinWidth(WINDOW_WIDTH);
            stage.setMinHeight(WINDOW_HEIGHT);
            stage.setScene(scene);
            stage.show();
        } catch (IOException | NullPointerException exception) {
            throw new IllegalStateException("Unable to load the Duchess GUI resources.", exception);
        }
    }
}
