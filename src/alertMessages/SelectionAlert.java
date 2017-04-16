package alertMessages;

import javafx.scene.control.Alert;
import scheduleCreator.MainApp;

/**
 * Created by Oscar Reyes on 4/16/2017.
 */
public class SelectionAlert {
    public static void alertSelectionError(MainApp mainApp){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.initOwner(mainApp.getPrimaryStage());
        alert.setTitle("No Course Selected");
        alert.setContentText("Select a course from the table.");
        alert.showAndWait();
    }
}
