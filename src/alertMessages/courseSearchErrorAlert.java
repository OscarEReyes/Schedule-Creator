package alertMessages;

import javafx.scene.control.Alert;
import javafx.stage.Stage;

/**
 * Class holding error alerts related to course searches.
 * Created by Oscar Reyes on 4/17/2017.
 */
public class courseSearchErrorAlert {
    public static void informCourseSearchError(Stage dialogStage) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.initOwner(dialogStage);
        alert.setTitle("Department or Course Not Found");
        alert.setContentText("ERROR");
        alert.showAndWait();
    }
}
