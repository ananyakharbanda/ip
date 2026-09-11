package duchess.gui;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

/** A reusable JavaFX input composer containing the command field and Send button. */
public class Composer extends HBox {
    @FXML
    private TextField userInput;

    @FXML
    private Button sendButton;

    /** Action to run when the command is submitted. */
    private Runnable submitAction = () -> { };

    /** Loads the composer layout from its FXML view. */
    public Composer() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Composer.class.getResource("/view/Composer.fxml"));
            fxmlLoader.setRoot(this);
            fxmlLoader.setController(this);
            fxmlLoader.load();
        } catch (IOException | NullPointerException exception) {
            throw new IllegalStateException("Unable to load the composer resource.", exception);
        }
    }

    /** Runs the configured submit action when the FXML composer is submitted. */
    @FXML
    private void handleSubmit() {
        submitAction.run();
    }

    /**
     * Sets the action to run when the command is submitted.
     *
     * @param submitAction the action to run
     */
    public void setSubmitAction(Runnable submitAction) {
        this.submitAction = submitAction;
    }

    /**
     * Returns the current command text.
     *
     * @return the current command text
     */
    public String getInput() {
        return userInput.getText();
    }

    /** Clears the command field. */
    public void clearInput() {
        userInput.clear();
    }

    /**
     * Places a command starter in the input field and focuses it.
     *
     * @param commandStarter the text to place in the field
     */
    public void prepareInput(String commandStarter) {
        userInput.setText(commandStarter);
        userInput.positionCaret(commandStarter.length());
        userInput.requestFocus();
    }

    /**
     * Enables or disables the input field and Send button.
     *
     * @param disabled whether input should be disabled
     */
    public void setInputDisabled(boolean disabled) {
        userInput.setDisable(disabled);
        sendButton.setDisable(disabled);
    }

    /** Gives keyboard focus to the command field. */
    public void requestInputFocus() {
        userInput.requestFocus();
    }
}
