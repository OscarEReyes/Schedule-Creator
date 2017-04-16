package scheduleCreator.view;

	
import java.io.IOException;
import java.util.List;

import alertMessages.CoursesSelectedAlert;
import alertMessages.SchedGenErrorAlert;
import alertMessages.SelectionAlert;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import scheduleCreator.MainApp;
import scheduleCreator.model.Course;
import scheduleCreator.model.CourseClass;


public class ScheduleOverviewController {
	@FXML
	private TableView<Course> CourseTable;
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
    
    @FXML
    private Label crnLabel;
    @FXML
    private Label sectionLabel;
    @FXML
    private Label professorLabel;
    @FXML
    private Label scheduleLabel;
    @FXML
    private Label placesLeftLabel;
    @FXML
    private Label labLabel;


	// Reference to the main application in the program.
	private MainApp mainApp;

	
	/**
	* Constructor for ScheduleOverviewController.
	* This is called before the initialize() method is called.
    */
	public ScheduleOverviewController() {
   }


	/**
     * Initializes the controller class. 
     * Automatically called once the fxml file has been loaded.
     */
	@FXML
	private void initialize() {
		// Initialize the Schedule table with its column.
		courseNameColumn.setCellValueFactory(cellData -> cellData.getValue().courseNameProperty());
		 // Clear the CollegeCourse Information.
	    showCollegeCourseDetails(null);
	    // "Listen" for selection changes and display the selected course's details
	    // when selection is changed
	    CourseTable.getSelectionModel().selectedItemProperty().addListener(
	            (observable, oldValue, newValue) -> showCollegeCourseDetails(newValue));
	    // "Listen" for selection changes and display the selected course's chosen class details
	    // when selection is changed
	    CourseTable.getSelectionModel().selectedItemProperty().addListener(
          (observable, oldValue, newValue) -> showClassDetails(newValue));
	}

	
	/**
	 * Gives the mainApp a reference to itself when called by mainApp
	 * 
	 * @param mainApp var for self-referencing
	 */
	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;

	    // Add observable list data to the table
	    CourseTable.setItems(mainApp.getCollegeCourseData());
	}
	
	
	/**
	 * Fills every text field to display information about the college course.
	 * If the specified college course is null, clears all text fields.
	 * 
	 * @param course or null
	 */
	private void showCollegeCourseDetails(Course course) {
	    if (course != null) {
            setCourseLabels(course);
	    } else {
	        clearCollegeCourseLabels();
	    }
	}

    /**
     * Sets the text of Labels related to a Course object, using the values in a passed Course object course.
     * @param course The selected Course, whose details the user wishes to see.
     */
    private void setCourseLabels(Course course) {
        courseLabel.setText(course.getCourseDep() +
                course.getCourseNumber());
        courseNameLabel.setText(course.getCourseName());
        creditHoursLabel.setText(course.getCreditHours());
        prefProfLabel.setText(course.getPrefProf());
    }

    /**
     * Clears the labels associated with a Course object.
     * Ran when the user has selected a blank field and there is no Course to load.
     */
	private void clearCollegeCourseLabels() {
        courseLabel.setText("");
        courseNameLabel.setText("");
        creditHoursLabel.setText("");
        prefProfLabel.setText("");
    }
	
	/**
	 * Called when the user clicks on the remove course button.
	 */
	@FXML
	private void handleRemoveCourse() {
	    int selectedIndex = CourseTable.getSelectionModel().getSelectedIndex();
	    if (selectedIndex >= 0) {
	    	CourseTable.getItems().remove(selectedIndex);
	   	} else {
            SelectionAlert.alertSelectionError(mainApp);
	    }
	}
	
	
	/**
	 * Called when the user clicks the confirm button. 
	 * Opens a dialog for the user to input data and create a new CollegeCourse
	 */
	@FXML
	private void handleNewCollegeCourse() {
	    Course tempCollegeCourse = new Course.CollegeCourseBuilder("Course")
	    		.courseDepartment()
	    		.courseNumber()
	    		.build();
	    
	    boolean confirmedClicked = mainApp.showCollegeCourseEditDialog(tempCollegeCourse);
	    if (confirmedClicked) {
	        mainApp.getCollegeCourseData().add(tempCollegeCourse);
	    }
	}

	
	/**
	 * Called when the user clicks the edit button. 
	 * Opens a dialog the user uses to edit a CollegeCourse object.
	 */
	@FXML
	private void handleEditCollegeCourse() {
	    Course selectedCourse = CourseTable.getSelectionModel().getSelectedItem();
	    if (selectedCourse != null) {
	        boolean confirmedClicked = mainApp.showCollegeCourseEditDialog(selectedCourse);
	        if (confirmedClicked) {
	            showCollegeCourseDetails(selectedCourse);
	        }
	    } else {
	        // Handle no course being selected.
            SelectionAlert.alertSelectionError(mainApp);
        }
	}
	
	/**
	 * Called when the user clicks the generate schedule button. 
	 * This method results in the schedule being generated, but specifically,
	 * this method handles alerting the user of what classes were chosen or if an error occurred
	 * 
	 * The SchedulePlanner object chooses the appropriate classes and sets the chosenClass
	 * attribute in a Course object to the CourseClass instance of the appropriate class.
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	@FXML
	private void handleGenerateSchedule() throws IOException, InterruptedException {	
		List<CourseClass> chosenClasses = mainApp.generateSchedule();
		if (chosenClasses == null) {
            SchedGenErrorAlert.alertSchedGenError(mainApp);
		} else {
			StringBuilder classesBuilder = getChosenClasses(chosenClasses);
            CoursesSelectedAlert.informCoursesSelected(mainApp, classesBuilder.toString());
        }
    }

    /**
     * Returns a StringBuilder object, which when turned to as String represents the classes chosen by the user.
     *
     * @param chosenClasses List of CourseClass objects representing classes chosen by the user.
     * @return classesBuilder The StringBuilder which when converted to a string represents a chosen class.
     */
    private StringBuilder getChosenClasses(List<CourseClass> chosenClasses) {
        StringBuilder classesBuilder = new StringBuilder();
        for (CourseClass c: chosenClasses) {
            classesBuilder.append(c.toString()).append("\n");
        }
        return classesBuilder;
    }

    /**
	 * Shows details of the chosen class for the selected course if one has been chosen.
	 * Other wise it will show empty fields.
	 * @param course Course object that represents a Class section
	 */
	@FXML
	private void showClassDetails(Course course) {
        if (course != null && course.getChosenClass() != null) {
            CourseClass chosenClass = course.getChosenClass();
            setLabelsText(chosenClass);
        } else {
            clearLabelsText();
        }
    }

    /**
     * Sets the labels related to CourseClasses using the class selected by the user.
     * @param chosenClass The class the user selected, whose values will be used to populate the appropiate labels.
     */
    private void setLabelsText(CourseClass chosenClass) {
        crnLabel.setText(chosenClass.getClassCrn());
        sectionLabel.setText(chosenClass.getClassSection());
        professorLabel.setText(chosenClass.getClassProf());
        scheduleLabel.setText(chosenClass.getSchedule().toString());
        placesLeftLabel.setText(Integer.toString(chosenClass.getPlacesLeft()));
        if (chosenClass.getLab() != null) {
            labLabel.setText(chosenClass.getLab().toString());
        } else {
            labLabel.setText("No lab");
        }
    }

    /**
     *  Clears course class related labels.
     */
    private void clearLabelsText() {
        crnLabel.setText("");
        sectionLabel.setText("");
        professorLabel.setText("");
        scheduleLabel.setText("");
        placesLeftLabel.setText("");
        labLabel.setText("");
    }


}

