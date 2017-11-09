package alertMessages;

import javafx.scene.control.Alert;
import javafx.stage.Stage;

/**
 * Created by Oscar Reyes on 4/21/2017.
 */
public class unexpectedErrorAlert {
    public static void informUnexpectedError(String errorMessage) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.initOwner(null);
        alert.setTitle("Unexpected Error");
        alert.setContentText(errorMessage);
        alert.showAndWait();
    }
}
