package duchess.gui;

import duchess.Duchess;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

/** Controller for the main Duchess chat window. */
public class MainWindow extends AnchorPane {
    @FXML
    private Header header;

    @FXML
    private QuickCommands quickCommands;

    @FXML
    private ConversationView conversationView;

    @FXML
    private Composer composer;

    /** The shared command processor used by the GUI. */
    private Duchess duchess;

    /** Avatar shown beside user messages. */
    private final Image userImage = createAvatar(Color.web("#5367A7"));

    /** Avatar shown beside Duchess responses. */
    private final Image duchessImage = createAvatar(Color.web("#6C4BDC"));

    /** Configures automatic scrolling after a new dialog is added. */
    @FXML
    public void initialize() {
        header.setHelpAction(this::handleHelp);
        quickCommands.setListAction(this::handleList);
        quickCommands.setTodoAction(this::handleTodo);
        quickCommands.setFindAction(this::handleFind);
        composer.setSubmitAction(this::handleUserInput);
    }

    /**
     * Injects the command processor and adds the initial greeting.
     *
     * @param duchess the command processor used to answer user input
     */
    public void setDuchess(Duchess duchess) {
        this.duchess = duchess;
        conversationView.addDialogs(
                DialogBox.getDuchessDialog("👋 Hello! I'm Duchess.\n\nType help or click Help to "
                                + "see the available commands.", duchessImage, "welcome"));
        composer.requestInputFocus();
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
    private void handleUserInput() {
        submitCommand(composer.getInput());
    }

    /** Displays the command guide when the GUI Help button is pressed. */
    private void handleHelp() {
        submitCommand("help");
    }

    /** Shows the current task list from the quick-command strip. */
    private void handleList() {
        submitCommand("list");
    }

    /** Places a todo command starter in the composer for quick task entry. */
    private void handleTodo() {
        prepareCommand("todo ");
    }

    /** Places a find command starter in the composer for quick searching. */
    private void handleFind() {
        prepareCommand("find ");
    }

    /** Sends a command through the shared processor and appends the conversation bubbles. */
    private void submitCommand(String input) {
        String response = duchess.getResponse(input);
        String commandType = duchess.getCommandType();

        conversationView.addDialogs(
                DialogBox.getUserDialog(input, userImage),
                DialogBox.getDuchessDialog(response, duchessImage, commandType));
        updateStatus(commandType);
        composer.clearInput();

        if (duchess.isExitRequested()) {
            composer.setInputDisabled(true);
            header.setHelpDisabled(true);
        }
    }

    /** Places a command starter in the input field and returns focus to the composer. */
    private void prepareCommand(String commandStarter) {
        composer.prepareInput(commandStarter);
    }

    /** Updates the compact header status to reflect the latest command result. */
    private void updateStatus(String commandType) {
        switch (commandType) {
            case "add", "mark", "unmark" -> {
                header.updateStatus("✦ Court updated", "status-success");
            }
            case "delete" -> {
                header.updateStatus("✦ Record removed", "status-success");
            }
            case "error" -> {
                header.updateStatus("! Check command", "status-warning");
            }
            case "bye" -> {
                header.updateStatus("✦ See you soon", "status-muted");
            }
            case "help" -> {
                header.updateStatus("✦ Guide open", "status-ready");
            }
            default -> {
                header.updateStatus("✦ Palace ready", "status-ready");
            }
        }
    }
}
