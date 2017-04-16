package alertMessages;

import javafx.scene.control.Alert;
import javafx.stage.Stage;

/**
 * Created by Oscar Reyes on 4/15/2017.
 */
public class InvalidFieldHandling {
    public static boolean checkErrorMessage(Stage dialogStage, String errorMessage) {
        if (errorMessage.length() == 0) {
            return true;
        } else {
            // Create error message window.
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Invalid Fields");
            alert.setContentText(errorMessage);

            // Show error message window and wait for a response.
            alert.showAndWait();

            return false;
        }
    }
}
