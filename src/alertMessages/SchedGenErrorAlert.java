package alertMessages;

import javafx.scene.control.Alert;
import scheduleCreator.MainApp;

/**
 * Created by Oscar Reyes on 4/16/2017.
 */
public class SchedGenErrorAlert {
    public static void alertSchedGenError(MainApp mainApp) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.initOwner(mainApp.getPrimaryStage());
        alert.setTitle("Error generating schedule");
        alert.setContentText("There was an error generating the schedule.");
        alert.showAndWait();
    }
}
