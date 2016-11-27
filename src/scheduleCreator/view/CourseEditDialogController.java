package scheduleCreator.view;
import java.awt.TextField;

import javafx.fxml.FXML;
import javafx.stage.Stage;
import scheduleCreator.model.CollegeCourse;


/*
 * Controller for CourseEditDialog
 * Edits CollegeCourse data.
 */
public class CourseEditDialogController {
	@FXML
	private TextField courseDepartmentField;
	
	@FXML
	private TextField courseNumberField;
	
	@FXML 
	private TextField courseNameField;
	
	@FXML
	private TextField prefProfField;
	
	private Stage dialogStage;
	private CollegeCourse course;
	private Boolean confirmClicked = false;
}
