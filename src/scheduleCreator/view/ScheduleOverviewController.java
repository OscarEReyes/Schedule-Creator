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
}

