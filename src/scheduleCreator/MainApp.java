package scheduleCreator;

import java.io.IOException;

import java.io.File;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.Select;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import scheduleCreator.model.CollegeCourse;
import scheduleCreator.view.CourseEditDialogController;
import scheduleCreator.view.LoginDialogController;
import scheduleCreator.view.LoginDialogController.User;
import scheduleCreator.view.ScheduleOverviewController;

public class MainApp extends Application {

    private Stage primaryStage;
    private BorderPane rootLayout;
    private ObservableList<CollegeCourse> CollegeCourseData = FXCollections.observableArrayList();

    /**
     * Constructor
     */
    public MainApp() {

    }

    /**
     * Returns the data as an observable list of CollegeCourses. 
     * @return
     */
    public ObservableList<CollegeCourse> getCollegeCourseData() {
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
     * @param CollegeCourse -  the CollegeCourse object to be edited
     * @return true if the user clicked the Confirm button, otherwise false
     */
    public boolean showCollegeCourseEditDialog(CollegeCourse collegeCourse) {
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
     
    
}