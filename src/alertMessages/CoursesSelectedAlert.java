package alertMessages;

import javafx.scene.control.Alert;
import scheduleCreator.MainApp;

/**
 * Created by oscarjr on 4/16/2017.
 */
public class CoursesSelectedAlert {
    public static void informCoursesSelected(MainApp mainApp, String coursesSelected){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.initOwner(mainApp.getPrimaryStage());
        alert.setTitle("Courses Selected");
        alert.setContentText("The courses chosen were: " + "\n" + coursesSelected);
        alert.showAndWait();
    }
}
