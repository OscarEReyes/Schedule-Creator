package scheduleCreator.view;

import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;

public class SemesterDialogController {
	@FXML
	private ChoiceBox<String> seasonChoiceBox;
	@FXML
	private ChoiceBox<String> yearChoiceBox;

	private Stage dialogStage;
	private Boolean confirmClicked = false;
	private Semester semester;

	/**
	 * Initializes the controller class. 
	 * Automatically called once the fxml file has loaded.
	 */
	@FXML
	private void initialize() {
		List<String> seasonList = new ArrayList<String>();
		seasonList.add("Fall");
		seasonList.add("Winter Intersession");
		seasonList.add("Spring");
		seasonList.add("Spring Intersession");
		seasonList.add("Summer");
		seasonList.add("Summer Intersession");

		List<String> yearList = new ArrayList<String>();
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
	 * Returns true if user has clicked the confirm button,
	 * Returns false if it is not the case.
	 * @return
	 */
	public boolean isConfirmClicked() {
		return confirmClicked;
	}


	/**
	 * Executed if the user clicks confirm.
	 */
	@FXML
	private void handleConfirm() {
		this.semester = new Semester(seasonChoiceBox.getValue(), yearChoiceBox.getValue());
		confirmClicked = true;
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


	public class Semester {
		private final String season;
		private final String year;

		public Semester(final String season, final String year){
			this.season = season;
			this.year = year;
		}

		public String getSeason() {
			return this.season;
		}

		public String getYear() {
			return this.year;
		}


	}
}
