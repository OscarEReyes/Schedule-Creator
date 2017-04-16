package alertMessages;

import javafx.scene.control.Alert;

/**
 * Created by oscarjr on 4/16/2017.
 */
public class unavailableClassAlert {
    public static void alertClassesUnavailable(int unavCount) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.initOwner(null);
        alert.setTitle("Be aware of unavailable classes");
        alert.setContentText(unavCount + " classes were not available due to being for Dual-enrollment "+
                             "because they are full.");
        // Show error message window and wait for a response.
        alert.showAndWait();
    }
}
