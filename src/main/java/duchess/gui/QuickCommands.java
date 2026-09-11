package duchess.gui;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.HBox;

/** A reusable JavaFX strip containing shortcuts for common Duchess commands. */
public class QuickCommands extends HBox {
    /** Action to run when the list shortcut is pressed. */
    private Runnable listAction = () -> { };

    /** Action to run when the todo shortcut is pressed. */
    private Runnable todoAction = () -> { };

    /** Action to run when the find shortcut is pressed. */
    private Runnable findAction = () -> { };

    /** Loads the quick-command layout from its FXML view. */
    public QuickCommands() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(QuickCommands.class.getResource("/view/QuickCommands.fxml"));
            fxmlLoader.setRoot(this);
            fxmlLoader.setController(this);
            fxmlLoader.load();
        } catch (IOException | NullPointerException exception) {
            throw new IllegalStateException("Unable to load the quick-command resource.", exception);
        }
    }

    /** Runs the configured list action when the FXML shortcut is pressed. */
    @FXML
    private void handleList() {
        listAction.run();
    }

    /** Runs the configured todo action when the FXML shortcut is pressed. */
    @FXML
    private void handleTodo() {
        todoAction.run();
    }

    /** Runs the configured find action when the FXML shortcut is pressed. */
    @FXML
    private void handleFind() {
        findAction.run();
    }

    /**
     * Sets the action to run for the list shortcut.
     *
     * @param listAction the action to run
     */
    public void setListAction(Runnable listAction) {
        this.listAction = listAction;
    }

    /**
     * Sets the action to run for the todo shortcut.
     *
     * @param todoAction the action to run
     */
    public void setTodoAction(Runnable todoAction) {
        this.todoAction = todoAction;
    }

    /**
     * Sets the action to run for the find shortcut.
     *
     * @param findAction the action to run
     */
    public void setFindAction(Runnable findAction) {
        this.findAction = findAction;
    }
}
