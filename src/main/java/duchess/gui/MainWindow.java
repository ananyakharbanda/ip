package duchess.gui;

import duchess.Duchess;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;

/** Controller for the main Duchess chat window. */
public class MainWindow extends VBox {
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
                DialogBox.getDuchessDialog("👋 Good day! I'm Duchess, your royal steward.\n\n"
                                + "Give me a command, or open Help for the court guide.", "welcome"));
        composer.requestInputFocus();
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
                DialogBox.getUserDialog(input),
                DialogBox.getDuchessDialog(response, commandType));
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
                header.updateStatus("✦ Court ready", "status-ready");
            }
        }
    }
}
