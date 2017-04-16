package scheduleCreator.view;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import scheduleCreator.model.Course;


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
     * @return true when input is valid.
     */
    private boolean isInputValid() {
        String errorMessage = "";

        if (courseDepartmentField.getText() == null || 
        		courseDepartmentField.getText().length() != 4) {
            errorMessage += "No valid course department!\n"; 
        }
        if (courseNumberField.getText() == null || 
        		courseNumberField.getText().length() != 4) {
            errorMessage += "No valid course number!\n"; 
        }
        if (courseNameField.getText() == null || 
        		courseNameField.getText().length() == 0) {
            errorMessage += "No valid course name!\n"; 
        }

        if (errorMessage.length() == 0) {
            return true;
        } else {
            // Create error message window.
            Alert alert = new Alert(AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Invalid Fields");
            alert.setContentText(errorMessage);
            
            // Show error message window and wait for a response.
            alert.showAndWait();

            return false;
        }
    }


}
