package duchess.gui;

import duchess.Duchess;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

/** Controller for the main Duchess chat window. */
public class MainWindow extends AnchorPane {
    @FXML
    private ScrollPane scrollPane;

    @FXML
    private VBox dialogContainer;

    @FXML
    private TextField userInput;

    @FXML
    private Button sendButton;

    @FXML
    private Button helpButton;

    @FXML
    private Label statusLabel;

    /** The shared command processor used by the GUI. */
    private Duchess duchess;

    /** Avatar shown beside user messages. */
    private final Image userImage = createAvatar(Color.web("#5367A7"));

    /** Avatar shown beside Duchess responses. */
    private final Image duchessImage = createAvatar(Color.web("#6C4BDC"));

    /** Configures automatic scrolling after a new dialog is added. */
    @FXML
    public void initialize() {
        dialogContainer.heightProperty().addListener(observable -> scrollPane.setVvalue(1.0));
    }

    /**
     * Injects the command processor and adds the initial greeting.
     *
     * @param duchess the command processor used to answer user input
     */
    public void setDuchess(Duchess duchess) {
        this.duchess = duchess;
        dialogContainer.getChildren().add(
                DialogBox.getDuchessDialog("👋 Hello! I'm Duchess.\n\nType help or click Help to "
                                + "see the available commands.",
                        duchessImage, "welcome"));
        userInput.requestFocus();
    }

    /** Creates a simple in-memory avatar so the GUI has no external image dependency. */
    private static Image createAvatar(Color background) {
        WritableImage image = new WritableImage(48, 48);
        PixelWriter writer = image.getPixelWriter();
        for (int y = 0; y < 48; y++) {
            for (int x = 0; x < 48; x++) {
                double distance = Math.hypot(x - 23.5, y - 23.5);
                writer.setColor(x, y, distance <= 23.5 ? background : Color.TRANSPARENT);
            }
        }
        return image;
    }

    /** Sends the current text to Duchess and appends both sides of the conversation. */
    @FXML
    private void handleUserInput() {
        submitCommand(userInput.getText());
    }

    /** Displays the command guide when the GUI Help button is pressed. */
    @FXML
    private void handleHelp() {
        submitCommand("help");
    }

    /** Sends a command through the shared processor and appends the conversation bubbles. */
    private void submitCommand(String input) {
        String response = duchess.getResponse(input);
        String commandType = duchess.getCommandType();

        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(input, userImage),
                DialogBox.getDuchessDialog(response, duchessImage, commandType));
        updateStatus(commandType);
        userInput.clear();

        if (duchess.isExitRequested()) {
            userInput.setDisable(true);
            sendButton.setDisable(true);
            helpButton.setDisable(true);
        }
    }

    /** Updates the compact header status to reflect the latest command result. */
    private void updateStatus(String commandType) {
        String status = switch (commandType) {
            case "add", "mark", "unmark" -> "● Updated";
            case "delete" -> "● Removed";
            case "error" -> "● Check input";
            case "bye" -> "● See you soon";
            case "help" -> "● Guide open";
            default -> "● Ready";
        };
        statusLabel.setText(status);
    }
}
