package duchess.gui;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

/** Represents one compact message in the Duchess conversation. */
public class DialogBox extends HBox {
    @FXML
    private Label dialog;

    @FXML
    private Label speakerLabel;

    /** Loads the reusable dialog layout and fills it with the message content. */
    private DialogBox(String text, String speaker) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainWindow.class.getResource("/view/DialogBox.fxml"));
            fxmlLoader.setController(this);
            fxmlLoader.setRoot(this);
            fxmlLoader.load();
            getStylesheets().add(MainWindow.class.getResource("/css/dialog-box.css").toExternalForm());
        } catch (IOException | NullPointerException exception) {
            throw new IllegalStateException("Unable to load the dialog box resource.", exception);
        }

        dialog.setText(text);
        speakerLabel.setText(speaker);
    }

    /**
     * Creates a right-aligned dialog for user input.
     *
     * @param text the user message
     * @return a dialog box containing the user message
     */
    public static DialogBox getUserDialog(String text) {
        DialogBox dialogBox = new DialogBox(text, "YOU");
        dialogBox.getStyleClass().add("user-row");
        return dialogBox;
    }

    /**
     * Creates a left-aligned dialog for Duchess's response.
     *
     * @param text Duchess's response
     * @param commandType the command category used to style the response
     * @return a dialog box containing Duchess's response
     */
    public static DialogBox getDuchessDialog(String text, String commandType) {
        DialogBox dialogBox = new DialogBox(text, "DUCHESS");
        dialogBox.getStyleClass().add("duchess-row");
        dialogBox.changeDialogStyle(commandType);
        return dialogBox;
    }

    /** Applies the Part 5 response colour corresponding to the command category. */
    private void changeDialogStyle(String commandType) {
        switch (commandType) {
            case "add" -> dialog.getStyleClass().add("add-label");
            case "mark", "unmark" -> dialog.getStyleClass().add("marked-label");
            case "delete" -> dialog.getStyleClass().add("delete-label");
            case "error" -> {
                getStyleClass().add("error-row");
                dialog.getStyleClass().add("error-label");
            }
            default -> {
                // Neutral styling is used for list, find, bye, and greeting messages.
            }
        }
    }
}
