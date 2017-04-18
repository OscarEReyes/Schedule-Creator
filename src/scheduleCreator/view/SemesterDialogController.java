 package scheduleCreator.view;

import java.util.ArrayList;
import java.util.List;
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
		List<String> seasonList = new ArrayList<>();
		seasonList.add("Fall");
		seasonList.add("Winter Intersession");
		seasonList.add("Spring");
		seasonList.add("Spring Intersession");
		seasonList.add("Summer");
		seasonList.add("Summer Intersession");

		List<String> yearList = new ArrayList<>();
		yearList.add("2016");
		yearList.add("2017");
		yearList.add("2018");
		yearList.add("2019");
		yearList.add("2020");

		ObservableList<String> obSeasonList = FXCollections.observableList(seasonList);
		ObservableList<String> obYearList = FXCollections.observableList(yearList);

		seasonChoiceBox.getItems().clear();
		seasonChoiceBox.setItems(obSeasonList);

		yearChoiceBox.getItems().clear();
		yearChoiceBox.setItems(obYearList);
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
