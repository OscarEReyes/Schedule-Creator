package scheduleCreator;

import java.io.IOException;
import java.util.List;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import scheduleCreator.model.Course;
import scheduleCreator.model.CourseClass;
import scheduleCreator.model.SchedulePlanner;
import scheduleCreator.view.CourseEditDialogController;
import scheduleCreator.view.LoginDialogController;
import scheduleCreator.view.LoginDialogController.User;
import scheduleCreator.view.PreferenceDialogController;
import scheduleCreator.view.PreferenceDialogController.Preferences;
import scheduleCreator.view.ScheduleOverviewController;
import scheduleCreator.view.SemesterDialogController;
import scheduleCreator.view.SemesterDialogController.Semester;

public class MainApp extends Application {
	private Stage primaryStage;
	private BorderPane rootLayout;
	private ObservableList<Course> CollegeCourseData = FXCollections.observableArrayList();
	private User user;
	private Semester semester;

	/**
	 * Constructor
	 */
	public MainApp() {

	}

	/**
	 * Returns the data as an observable list of CollegeCourses. 
	 * @return
	 */
	public ObservableList<Course> getCollegeCourseData() {
		return CollegeCourseData;
	}

	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("Schedule Creator");

		initRootLayout();
		showScheduleOverview();  
	}

	/**
	 * Initializes the root layout.
	 */

	public void initRootLayout() {
		try {     
			// Load root layout from fxml file.     
			FXMLLoader loader = new FXMLLoader();       
			loader.setLocation(MainApp.class.getResource("view/RootLayout.fxml"));     
			rootLayout = (BorderPane) loader.load();
			// Show the scene containing the root layout.  
			Scene scene = new Scene(rootLayout);            
			primaryStage.setScene(scene);            
			primaryStage.show();        
		} catch (IOException e) {           
			e.printStackTrace();       
		}
	}

	/**
	 * Shows the schedule overview inside of the root layout.
	 */

	public void showScheduleOverview() {     
		try {   
			// Loads the schedule overview.       
			FXMLLoader loader = new FXMLLoader();    
			loader.setLocation(MainApp.class.getResource("view/scheduleOverview.fxml"));     
			AnchorPane scheduleOverview = (AnchorPane) loader.load();   

			// Sets the schedule overview into the center of root layout.     
			rootLayout.setCenter(scheduleOverview);           

			// Give mainApp access to the ScheduleOverviewController class.   
			ScheduleOverviewController controller = loader.getController();   
			controller.setMainApp(this);  

			do {
				user = showLoginDialog();
				semester = showSemesterDialog();

			} while (user == null || semester == null);

		} catch (IOException e) {  
			e.printStackTrace();
		}

	}

	/**
	 * Returns the main stage.
	 * @return
	 */

	public Stage getPrimaryStage() {
		return primaryStage;
	}

	public static void main(String[] args) {    
		launch(args);    
	}


	/**
	 * Opens a dialog to edit details for the specified CollegeCourse course object. 
	 * If the user clicks Confirm, the changes made are saved 
	 * into the passed CollegeCourse object and 
	 * true is returned.
	 * 
	 * @param collegeCourse -  the CollegeCourse object to be edited
	 * @return true if the user clicked the Confirm button, otherwise false
	 */
	public boolean showCollegeCourseEditDialog(Course collegeCourse) {
		try {
			// Load the course edit dialog fxml file 
			// Create a new stage for the dialog.
			FXMLLoader fxmlLoader = new FXMLLoader();
			fxmlLoader.setLocation(MainApp.class.getResource("view/CourseEditDialog.fxml"));
			AnchorPane page = (AnchorPane) fxmlLoader.load();

			// Create the dialog Stage.
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Edit Course");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);

			// Create Scene and set scene
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);

			// Set the controller
			CourseEditDialogController controller = fxmlLoader.getController();
			controller.setDialogStage(dialogStage);
			controller.setCourse(collegeCourse);

			// Show the dialog and wait until the user closes it
			dialogStage.showAndWait();

			return controller.isConfirmClicked();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 *  Opens a login prompt for the user to input his login information.
	 *  @return User object with login information
	 */
	public User showLoginDialog() {
		try {
			// Load the login dialog fxml file 
			// Create a new stage for the dialog.
			FXMLLoader fxmlLoader = new FXMLLoader();
			fxmlLoader.setLocation(MainApp.class.getResource("view/LoginDialog.fxml"));
			AnchorPane login = (AnchorPane) fxmlLoader.load();

			// Create the dialog Stage
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Login");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);

			// Create Scene and set scene
			Scene scene = new Scene(login);
			dialogStage.setScene(scene);

			// Set the controller
			LoginDialogController controller = fxmlLoader.getController();
			controller.setDialogStage(dialogStage);

			// Show the dialog and wait until the user closes it
			dialogStage.showAndWait();
			return controller.getUser();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 *  Opens a semester prompt for the user to input his login information.
	 *  @return User object with login information
	 */
	public Semester showSemesterDialog() {
		try{
			// Load the Semester dialog fxml file 
			// Create a new stage for the dialog.
			FXMLLoader fxmlLoader = new FXMLLoader();
			fxmlLoader.setLocation(MainApp.class.getResource("view/SemesterDialog.fxml"));
			AnchorPane semesterDialog = (AnchorPane) fxmlLoader.load();

			// Create the dialog Stage
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Select the Semester");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);

			// Create Scene and set scene
			Scene scene = new Scene(semesterDialog);
			dialogStage.setScene(scene);

			// Set the controller
			SemesterDialogController controller = fxmlLoader.getController();
			controller.setDialogStage(dialogStage);

			// Show the dialog and wait until the user closes it
			dialogStage.showAndWait();
			return controller.getSemester();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 *  Opens a semester prompt for the user to input his preferences
	 *  @return Preferences object
	 */
	public Preferences showPreferencesDialog() {
		try{
			// Load the Semester dialog fxml file 
			// Create a new stage for the dialog.
			FXMLLoader fxmlLoader = new FXMLLoader();
			fxmlLoader.setLocation(MainApp.class.getResource("view/PreferenceDialog.fxml"));
			AnchorPane preferenceDialog = (AnchorPane) fxmlLoader.load();

			// Create the dialog Stage
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Edit Your Preferences");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);

			// Create Scene and set scene
			Scene scene = new Scene(preferenceDialog);
			dialogStage.setScene(scene);

			// Set the controller
			PreferenceDialogController controller = fxmlLoader.getController();
			controller.setDialogStage(dialogStage);

			// Show the dialog and wait until the user closes it
			dialogStage.showAndWait();
			return controller.getPreferences();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<CourseClass> generateSchedule() throws IOException, InterruptedException {
		SchedulePlanner sp = new SchedulePlanner.SchedulePlannerBuilder()
				.user(user)
				.semester(semester)
				.build();
		try {
			return sp.genSchedule(CollegeCourseData);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return null;

	}

}