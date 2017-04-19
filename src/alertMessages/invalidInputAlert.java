package alertMessages;

import javafx.scene.control.Alert;
import javafx.stage.Stage;

/**
 * Created by Oscar Reyes on 4/19/2017.
 */
public class invalidInputAlert {
    public static void informInvalidFields(Stage dialogStage, String errorMessage) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.initOwner(dialogStage);
        alert.setTitle("Invalid Fields");
        alert.setContentText(errorMessage);
        // Show error message window and wait for a response.
        alert.showAndWait();
    }
}
