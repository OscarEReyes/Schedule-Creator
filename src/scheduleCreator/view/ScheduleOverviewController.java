package scheduleCreator.view;

	
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import scheduleCreator.MainApp;
import scheduleCreator.model.CollegeCourse;

public class ScheduleOverviewController {
	@FXML
	private TableView<CollegeCourse> ScheduleTable;
    @FXML
    private TableColumn<CollegeCourse, String> courseNameColumn;

    @FXML
    private Label courseLabel;
    @FXML
    private Label courseNameLabel;
    @FXML
    private Label creditHoursLabel;
    @FXML
    private Label prefProfLabel;

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
	// Initialize the Schedule table with the two columns.
	courseNameColumn.setCellValueFactory(cellData -> cellData.getValue().courseNameProperty());
	
	 // Clear the CollegeCourse Information.
    showCollegeCourseDetails(null);

    // "Listen" for selection changes and display the selected course's details
    // when selection is changed
    
    ScheduleTable.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) -> showCollegeCourseDetails(newValue));
	}

	/**
	 * Gives the mainApp a reference to itself when called by mainApp
	 * 
	 * @param mainApp
	 */
	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;

	    // Add observable list data to the table
	    ScheduleTable.setItems(mainApp.getCollegeCourseData());
	}
	
	/**
	 * Fills every text field to display information about the college course.
	 * If the specified college course is null, clears all text fields.
	 * 
	 * @param collegecourse or null
	 */
	private void showCollegeCourseDetails(CollegeCourse course) {
	    if (course != null) {
	        // Fill the labels with info from the course object.
	        courseLabel.setText(course.getCourseDepartment() + 
	        					course.getCourseNumber());
	        courseNameLabel.setText(course.getCourseName());
	        creditHoursLabel.setText(Integer.toString(course.getCreditHours()));
	        prefProfLabel.setText(course.getPrefProf());
	  
	    } else {
	        // CollegeCourse is null, clear all text.
	        courseLabel.setText("");
	        courseNameLabel.setText("");
	        creditHoursLabel.setText("");
	        prefProfLabel.setText("");

	    }
	}
	
	/**
	 * Called when the user clicks on the remove course button.
	 */
	@FXML
	private void handleRemoveCourse() {
	    int selectedIndex = ScheduleTable.getSelectionModel().getSelectedIndex();
	    ScheduleTable.getItems().remove(selectedIndex);
	}
}

