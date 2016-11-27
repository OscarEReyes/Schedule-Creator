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
	
	/**
     * Initializes the controller class. 
     * Automatically called once the fxml file has loaded.
     */
    @FXML
    private void initialize() {
    }

    
    /**
     * Sets this dialog's stage.
     * 
     * @param dialogStage
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }
    
    
    /**
     * Sets fields to the values of the course that is being edited.
     * 
     * @param course
     */
    public void setCourse(CollegeCourse course) {
        this.course = course;

        courseDepartmentField.setText(course.getCourseDepartment());
        courseNumberField.setText(course.getCourseNumber());
        courseNameField.setText(course.getCourseName());
        prefProfField.setText(course.getPrefProf());
    }

}
