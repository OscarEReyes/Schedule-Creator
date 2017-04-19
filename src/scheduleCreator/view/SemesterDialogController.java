 package scheduleCreator.view;

import java.util.ArrayList;
import java.util.List;

import fieldSetup.choiceBoxSetUp;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;
import siteClasses.Semester;

 public class SemesterDialogController {
	@FXML
	private ChoiceBox<String> seasonChoiceBox;
	@FXML
	private ChoiceBox<String> yearChoiceBox;

	private Stage dialogStage;
	private Semester semester;

	/**
	 * Initializes the controller class. 
	 * Automatically called once the fxml file has loaded.
	 */
	@FXML
	private void initialize() {
		// Set Up season ChoiceBox
		String[] seasonArr = {"Fall", "Winter Intersession", "Spring", "Summer", "Summer Intersession"};
		choiceBoxSetUp.setUpChoiceBox(seasonArr, seasonChoiceBox);
		// Set Up Year ChoiceBox
		String[] yearArr = {"2015", "2016", "2017", "2018", "2019", "2020"};
		choiceBoxSetUp.setUpChoiceBox(yearArr, yearChoiceBox);
	}


	/**
	 * Sets this dialog's stage.
	 * 
	 * @param dialogStage
	 */
	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}


	/**
	 * Executed if the user clicks confirm.
	 */
	@FXML
	private void handleConfirm() {
		this.semester = new Semester(seasonChoiceBox.getValue(), yearChoiceBox.getValue());
		dialogStage.close();
	}

	/**
	 * Executed when the cancel button is clicked.
	 */
	@FXML
	private void handleCancel() {
		dialogStage.close();
	}

	public Semester getSemester() {
		return this.semester;
	}

}
