package scheduleCreator.view;

import alertMessages.InvalidFieldHandling;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import scheduleCreator.model.Course;

import java.util.HashMap;

public class CourseEditDialogController {
	@FXML
	private TextField courseDepartmentField;
	@FXML
	private TextField crnField;
	@FXML 
	private TextField courseNameField;
	@FXML
	private TextField preferredProfField;

    HashMap<String, String> valuesForCourse = new HashMap<>();

    private Stage dialogStage;
	private Course course;
	private Boolean confirmClicked = false;
    private static final int COURSE_NUM_SIZE = 4;
    private static final int COURSE_DEPARTMENT_CODE_SIZE = 4;

    @FXML
    private void initialize() {
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }
    
    public void setCourse(Course course) {
        this.course = course;
    }

    public void setSectionFieldsText() {
        courseDepartmentField.setText(course.getDepartment());
        crnField.setText(course.getCourseNumber());
        courseNameField.setText(course.getCourseName());
        preferredProfField.setText(course.getPreferredProf());
    }

    public HashMap<String, String> getValuesForCourse() {
        return valuesForCourse;
    }

    public boolean isConfirmClicked() {
        return confirmClicked;
    }

    @FXML
    private void handleConfirm() {
        if (checkInputValidity()) {
            valuesForCourse.put("name", courseNameField.getText());
            valuesForCourse.put("department", courseDepartmentField.getText());
            valuesForCourse.put("courseNumber", crnField.getText());
            valuesForCourse.put("pref.professor", preferredProfField.getText());
        }
        confirmClicked = true;
        dialogStage.close();
    }

    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    private boolean checkInputValidity() {
        String errorMessage = "";
        String courseDepartmentCode = courseDepartmentField.getText();

        if (null == courseDepartmentCode || courseDepartmentCode.length() != COURSE_DEPARTMENT_CODE_SIZE) {
            errorMessage += "No valid course department!\n"; 
        }
        if (null == crnField.getText() || crnField.getText().length() != COURSE_NUM_SIZE) {
            errorMessage += "No valid course number!\n"; 
        }
        if ("".equals(courseNameField.getText())) {
            errorMessage += "No valid course name!\n"; 
        }
        return InvalidFieldHandling.checkErrorMessage(dialogStage, errorMessage);
    }
}
