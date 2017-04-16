package scheduleCreator.view;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import scheduleCreator.model.Course;
import alertMessages.InvalidFieldHandling;

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
	private Course course;
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
     * @param dialogStage Stage Instance
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }
    
    
    /**
     * Sets fields to the values of the course that is being edited.
     * 
     * @param course Course Object Instance, Course being edited
     */
    public void setCourse(Course course) {
        this.course = course;

        courseDepartmentField.setText(course.getCourseDep());
        courseNumberField.setText(course.getCourseNumber());
        courseNameField.setText(course.getCourseName());
        prefProfField.setText(course.getPrefProf());
    }
    
    /**
     * Returns true if user has clicked the confirm button,
     * Returns false if it is not the case.
     * @return Boolean stating if Confirm was clicked
     */
    public boolean isConfirmClicked() {
        return confirmClicked;
    }

    /**
     * Executed if the user clicks confirm.
     * Sets the StringProperty variables in the course object to its appropiate value
     * to replace the placeholder text.
     */
    @FXML
    private void handleConfirm() {
        if (isInputValid()) {
            course.setCourseDep(courseDepartmentField.getText());
            course.setCourseNumber(courseNumberField.getText());
            course.setCourseName(courseNameField.getText());
            course.setPrefProf(prefProfField.getText());
            course.setCreditHours(Character.toString(courseNumberField.getText().charAt(1)));

            confirmClicked = true;
            dialogStage.close();
        }
    }
    
    /**
     * Executed when the cancel button is clicked.
     */
    @FXML
    private void handleCancel() {
        dialogStage.close();
    }
    
    
    /**
     * Verifies that the user input is valid.
     * 
     * @return true if the input is valid.
     */
    private boolean isInputValid() {
        String errorMessage = "";
        String courseDep = courseDepartmentField.getText();
        String courseNum = courseNumberField.getText();
        String courseName = courseNameField.getText();

        if (courseDep == null || courseDep.length() != 4) {
            errorMessage += "No valid course department!\n"; 
        }
        if (courseNum == null || courseNum.length() != 4) {
            errorMessage += "No valid course number!\n"; 
        }
        if (courseName == null || courseName.length() == 0) {
            errorMessage += "No valid course name!\n"; 
        }
        return InvalidFieldHandling.checkErrorMessage(dialogStage, errorMessage);
    }


}
