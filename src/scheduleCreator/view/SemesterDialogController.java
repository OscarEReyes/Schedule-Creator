 package scheduleCreator.view;

 import fieldSetup.choiceBoxSetUp;
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

	@FXML
	private void initialize() {
		String[] seasonArr = {"Fall", "Winter Intersession", "Spring", "Summer", "Summer Intersession"};
		choiceBoxSetUp.setUpChoiceBox(seasonArr, seasonChoiceBox);
		String[] yearArr = {"2015", "2016", "2017", "2018", "2019", "2020"};
		choiceBoxSetUp.setUpChoiceBox(yearArr, yearChoiceBox);
	}

	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}

	@FXML
	private void handleConfirm() {
		this.semester = new Semester(seasonChoiceBox.getValue(), yearChoiceBox.getValue());
		dialogStage.close();
	}

	@FXML
	private void handleCancel() {
		dialogStage.close();
	}

	public Semester getSemester() {
		return this.semester;
	}

}
