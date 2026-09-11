package duchess.gui;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/** Represents one chat message and its speaker avatar. */
public class DialogBox extends HBox {
    @FXML
    private Label dialog;

    @FXML
    private Label speakerLabel;

    @FXML
    private VBox messageColumn;

    @FXML
    private ImageView displayPicture;

    /** Loads the reusable dialog layout and fills it with the message content. */
    private DialogBox(String text, Image image, String speaker) {
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
        displayPicture.setImage(image);
    }

    /**
     * Creates a right-aligned dialog for user input.
     *
     * @param text the user message
     * @param image the user avatar
     * @return a dialog box containing the user message
     */
    public static DialogBox getUserDialog(String text, Image image) {
        DialogBox dialogBox = new DialogBox(text, image, "YOU");
        dialogBox.getStyleClass().add("user-row");
        return dialogBox;
    }

    /**
     * Creates a left-aligned dialog for Duchess's response.
     *
     * @param text Duchess's response
     * @param image Duchess's avatar
     * @param commandType the command category used to style the response
     * @return a dialog box containing Duchess's response
     */
    public static DialogBox getDuchessDialog(String text, Image image, String commandType) {
        DialogBox dialogBox = new DialogBox(text, image, "DUCHESS");
        dialogBox.getStyleClass().add("duchess-row");
        dialogBox.flip();
        dialogBox.changeDialogStyle(commandType);
        return dialogBox;
    }

    /** Flips the avatar and text so Duchess's response appears on the left. */
    private void flip() {
        getChildren().setAll(displayPicture, messageColumn);
        setAlignment(Pos.TOP_LEFT);
        dialog.getStyleClass().add("reply-label");
    }

    /** Applies the Part 5 response colour corresponding to the command category. */
    private void changeDialogStyle(String commandType) {
        switch (commandType) {
            case "add" -> dialog.getStyleClass().add("add-label");
            case "mark", "unmark" -> dialog.getStyleClass().add("marked-label");
            case "delete" -> dialog.getStyleClass().add("delete-label");
            default -> {
                // Neutral styling is used for list, find, error, bye, and greeting messages.
            }
        }
    }
}
