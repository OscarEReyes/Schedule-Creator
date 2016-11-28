package scheduleCreator.view;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import scheduleCreator.model.CollegeCourse;

public class LoginDialogController {
	@FXML
	private TextField userName;
	@FXML
	private TextField password;
	
	private Stage dialogStage;
	private Boolean loginClicked = false;
	
}
