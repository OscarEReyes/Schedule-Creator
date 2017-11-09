package scheduleCreator;

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
import scheduleCreator.model.CourseSection;
import scheduleCreator.view.*;
import scheduleCreator.view.PreferenceDialogController.Preferences;
import siteClasses.Semester;
import siteClasses.User;

import java.io.IOException;
import java.util.*;

public class MainApp extends Application {
    private Stage primaryStage;
    private BorderPane rootLayout;
    private ObservableList<Course> Courses = FXCollections.observableArrayList();
    private User user;
    private Semester semester;

    private static final String ROOT_LAYOUT_FXML = "view/RootLayout.fxml";
    private static final String SCHEDULE_OVERVIEW_DIALOG_FXML = "view/scheduleOverview.fxml";
    private static final String COURSE_EDIT_DIALOG_FXML = "view/CourseEditDialog.fxml";
    private static final String LOGIN_DIALOG_FXML = "view/LoginDialog.fxml";
    private static final String SEMESTER_DIALOG_FXML = "view/SemesterDialog.fxml";
    private static final String PREFERENCE_DIALOG_FXML = "view/PreferenceDialog.fxml";

    public MainApp() {}

    public static void main(String[] args) {
        launch(args);
    }

    public ObservableList<Course> getCourses() {
        return Courses;
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Schedule Creator");

        initializeRootLayout();
        requestUserLoginInformation();
        showOverview();
    }

    private void initializeRootLayout() {
        FXMLLoader loader = createFXMLLoader(ROOT_LAYOUT_FXML);
        setWithRootLayout(loader);
    }

    private FXMLLoader createFXMLLoader(String dialogFileLocation) {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(MainApp.class.getResource(dialogFileLocation));
        return fxmlLoader;
    }

    private Stage createDialogStage(String stageTitle) {
        Stage dialogStage = new Stage();
        dialogStage.setTitle(stageTitle);
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(primaryStage);
        return dialogStage;
    }

    // TODO Handle exception with an alert
    private void setWithRootLayout(FXMLLoader loader) {
        try {
            setSceneWithRootLayout(loader);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setSceneWithRootLayout(FXMLLoader loader) throws IOException {
        rootLayout = loader.load();
        Scene scene = new Scene(rootLayout);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void showOverview() {
        try {
            showScheduleOverview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showScheduleOverview() throws IOException {
        FXMLLoader loader = setOverview();
        ScheduleOverviewController controller = loader.getController();
        controller.setMainApp(this);
    }

    private FXMLLoader setOverview() throws IOException {
        FXMLLoader loader = createFXMLLoader(SCHEDULE_OVERVIEW_DIALOG_FXML);
        AnchorPane scheduleOverview = loader.load();
        rootLayout.setCenter(scheduleOverview);
        return loader;
    }

    // TODO make a way for user to cancel login (Really, I could just add or user clicked cancel on the loop used)
    private void requestUserLoginInformation() {
        do {
            user = showLoginDialog();
            semester = showSemesterDialog();

        } while (user == null || semester == null);
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public HashMap<String, String> showCourseEditDialog(Course course) {
        FXMLLoader fxmlLoader = createFXMLLoader(COURSE_EDIT_DIALOG_FXML);
        Stage dialogStage = setUpStage("Edit Course", fxmlLoader);
        CourseEditDialogController controller = fxmlLoader.getController();

        controller.setDialogStage(dialogStage);
        controller.setCourse(course);

        controller.setSectionFieldsText();
        dialogStage.showAndWait();
        return controller.getValuesForCourse();
    }

    private Stage setUpStage(String title, FXMLLoader fxmlLoader) {
        Stage dialogStage = createDialogStage(title);
        setWithAnchorPane(dialogStage, fxmlLoader);
        return dialogStage;
    }

    // TODO Handle exception with an alert
    private void setWithAnchorPane(Stage dialogStage, FXMLLoader loader) {
        try {
            setSceneWithAnchorPane(dialogStage, loader);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setSceneWithAnchorPane(Stage dialogStage, FXMLLoader fxmlLoader) throws IOException {
        AnchorPane loadedFXML  = fxmlLoader.load();
        Scene scene = new Scene(loadedFXML);
        dialogStage.setScene(scene);
    }

    private User showLoginDialog() {
        FXMLLoader fxmlLoader = createFXMLLoader(LOGIN_DIALOG_FXML);
        LoginDialogController controller = fxmlLoader.getController();
        Stage dialogStage = setUpStage("Login", fxmlLoader);
        controller.setDialogStage(dialogStage);
        dialogStage.showAndWait();
        return controller.getUser();
    }

    private Semester showSemesterDialog() {
        FXMLLoader fxmlLoader = createFXMLLoader(SEMESTER_DIALOG_FXML);
        SemesterDialogController controller = fxmlLoader.getController();
        Stage dialogStage = setUpStage("Select the semester", fxmlLoader);
        controller.setDialogStage(dialogStage);
        dialogStage.showAndWait();
        return controller.getSemester();
    }

    public Preferences showPreferencesDialog() {
        FXMLLoader fxmlLoader = createFXMLLoader(PREFERENCE_DIALOG_FXML);
        PreferenceDialogController controller = fxmlLoader.getController();
        Stage dialogStage = setUpStage("Edit Your Preferences", fxmlLoader);
        controller.setDialogStage(dialogStage);
        dialogStage.showAndWait();
        return controller.getPreferences();
    }

    public List<CourseSection> generateSchedule() throws IOException, InterruptedException {
        return new ArrayList<>();
    }

}