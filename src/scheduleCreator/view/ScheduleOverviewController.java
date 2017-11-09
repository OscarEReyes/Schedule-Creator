package scheduleCreator.view;

import alertMessages.SelectionAlert;
import javafx.beans.property.ReadOnlyProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import scheduleCreator.MainApp;
import scheduleCreator.model.Course;
import scheduleCreator.model.CourseSection;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class ScheduleOverviewController {
    private ObservableList<Course> courses;
    private HashMap<String, String> defaultCourseValues = new HashMap<>();
    private MainApp mainApp;
    private TableView.TableViewSelectionModel<Course> courseTableSelectionModel;

    @FXML
    private TableView<Course> courseTable;
    @FXML
    private TableColumn<Course, String> courseNameColumn;
    @FXML
    private Label courseLabel;
    @FXML
    private Label courseNameLabel;
    @FXML
    private Label creditHoursLabel;
    @FXML
    private Label prefProfLabel;

    public ScheduleOverviewController() {}

    @FXML
    private void initialize() {
        courseTableSelectionModel = courseTable.getSelectionModel();
        ReadOnlyProperty<Course> selectedCourseProperty = courseTableSelectionModel.selectedItemProperty();

        courseNameColumn.setCellValueFactory(cellData -> cellData.getValue().courseNameProperty());
        selectedCourseProperty.addListener((observable, oldValue, newValue) -> setLabels(newValue));

        setLabelsEmpty();
        initializeDefaultCourseValues();

    }

    private void initializeDefaultCourseValues() {
        defaultCourseValues.put("name", "");
        defaultCourseValues.put("department", "");
        defaultCourseValues.put("courseNumber", "");
        defaultCourseValues.put("pref.professor", "");
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
        courses = mainApp.getCourses();
        courseTable.setItems(mainApp.getCourses());
    }

    private void setLabels(Course course) {
        courseLabel.setText(course.getDepartment() + course.getCourseNumber());
        courseNameLabel.setText(course.getCourseName());
        creditHoursLabel.setText(course.getCreditHours());
        prefProfLabel.setText(course.getPreferredProf());
    }

    @FXML
    private void handleNewCollegeCourse() {
        Course tempCourse = createCourse(defaultCourseValues);
        HashMap<String, String> courseValues = mainApp.showCourseEditDialog(tempCourse);
        addCourse(courseValues);
    }

    @FXML
    private void handleEditCollegeCourse() {
        Course selectedCourse = courseTableSelectionModel.getSelectedItem();
        if (selectedCourse != null) {
            editCourse(selectedCourse);
        } else {
            SelectionAlert.alertSelectionError(mainApp);
        }
    }

    private void editCourse(Course selectedCourse) {
        HashMap<String, String> courseValues = mainApp.showCourseEditDialog(selectedCourse);
        if (addCourse(courseValues)) {
            courses.remove(selectedCourse);
        }
    }

    private Boolean addCourse(HashMap<String, String> courseValues) {
        if (!courseValues.isEmpty()) {
            Course course = createCourse(courseValues);
            courses.add(course);
            return true;
        }
        return false;
    }

    @FXML
    private void handleRemoveCourse() {
        int index = courseTableSelectionModel.getSelectedIndex();
        if (index >= 0) {
            courseTable.getItems().remove(index);
        } else {
            SelectionAlert.alertSelectionError(mainApp);
        }
    }

    @FXML
    private void handleGenerateSchedule() throws IOException, InterruptedException {
        List<CourseSection> chosenClasses = mainApp.generateSchedule();
    }

    private Course createCourse(HashMap<String, String> courseValues) {
        return new Course.CourseBuilder(courseValues.get("name"))
                .courseDepartment(courseValues.get("department"))
                .courseNumber(courseValues.get("courseNumber"))
                .preferredProfessor(courseValues.get("pref.professor"))
                .build();
    }

    private void setLabelsEmpty() {
        courseLabel.setText("");
        courseNameLabel.setText("");
        creditHoursLabel.setText("");
        prefProfLabel.setText("");
    }
}

