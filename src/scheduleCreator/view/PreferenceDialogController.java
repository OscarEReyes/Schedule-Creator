package scheduleCreator.view;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

public class PreferenceDialogController {
	@FXML
	private RadioButton finishEarly;
	@FXML
	private RadioButton startEarly;
	@FXML
	private CheckBox MWF;
	@FXML
	private CheckBox TR;
	@FXML
	private CheckBox MW;
	@FXML
	private CheckBox oneDay;
	@FXML 
	private ComboBox<String> startTime; 
	@FXML
	private ComboBox<String> endTime;
	@FXML
	private Button confirm;
	@FXML
	private Button cancel;
	private Preferences preferences;
	private Stage dialogStage;
	private Boolean confirmClicked = false;

	
	/**
	 * Initializes the controller class. 
	 * Automatically called once the fxml file has loaded.
	 */
	@FXML
	private void initialize() {
		ToggleGroup startToggle = new ToggleGroup();
		
		startEarly.setToggleGroup(startToggle);
		finishEarly.setToggleGroup(startToggle);
  	    List<String> startTimeList = new ArrayList<>();
  	    String[] startArr = {"8:00 am", "09:00 am", "10:00 am", "11:00 am", "12:00 pm",
                        "01:00 pm", "02:00 pm","03:00 pm", "04:00 pm", "05:00 pm"};
        List<String> temp = Arrays.asList(startArr);
        startTimeList.addAll(temp);

    ObservableList<String> obStartTimeList = FXCollections.observableList(startTimeList);
    startTime.getItems().clear();
    startTime.setItems(obStartTimeList);
    
    List<String> endTimeList = new ArrayList<>();
    String[] endArr = {"12:00 pm", "01:00 pm", "02:00 pm", "03:00 pm", "04:00 pm",
                        "05:00 pm", "06:00 pm", "07:00 pm", "08:00 pm", "09:00 pm"};

    ObservableList<String> obEndTimeList = FXCollections.observableList(endTimeList);
    endTime.getItems().clear();
    endTime.setItems(obEndTimeList);
  	
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
		String startPreferences;
		if (isInputValid()) {
			if (startEarly.isSelected() == true) {
				startPreferences = "startEarly";
			} else {
				startPreferences = "finishEarly";
			}
			
			ArrayList<String> daysPreferred = new ArrayList<String>();
			if (MWF.isSelected() == true){
				daysPreferred.add("MWF");
			}
			if (MW.isSelected() == true){
				daysPreferred.add("MW");
			}
			if (TR.isSelected() == true){
				daysPreferred.add("TR");
			}
			if (oneDay.isSelected() == true){
				daysPreferred.add("OneDay");
			}
			this.preferences = new Preferences(daysPreferred, startPreferences, startTime.getValue(), endTime.getValue());
			confirmClicked = true;
			dialogStage.close();
		}
	}

	/**
	 * Executed when the cancel button is clicked.
	 */
	@FXML
	private void handleCancel() {
		dialogStage.close();
	}


	/**
	 * Verifies that the user input is valid.
	 * 
	 * @return true when input is valid.
	 */
	private boolean isInputValid() {
		String errorMessage = "";

		if (startEarly.isSelected() == false && finishEarly.isSelected() == false) {
			errorMessage += "No finish preference selected!\n"; 
		}

		if (startTime.getValue() == null || endTime.getValue() == null) {
			errorMessage += "No valid start time or end time!"; 
		}
		
		if (MWF.isSelected() == false && TR.isSelected() == false &&
				MW.isSelected() == false && oneDay.isSelected() == false) {
			errorMessage += "No day preference selected!";
		}
		
		if (errorMessage.length() == 0) {
			return true;
		} else {
			// Create error message window.
			Alert alert = new Alert(AlertType.ERROR);
			alert.initOwner(dialogStage);
			alert.setTitle("Invalid Fields");
			alert.setContentText(errorMessage);

			// Show error message window and wait for a response.
			alert.showAndWait();

			return false;
		}
	}
	
	public Preferences getPreferences() {
		return this.preferences;
	}
	
	public class Preferences {
		private final List<String> daysPreferred;
		private final String startPreference;
		private final String startTime;
		private final String endTime;

		public Preferences(final List<String> daysPreferred, 
				final String startPreference, 
				final String startTime,
				final String endTime){
			this.daysPreferred = daysPreferred;
			this.startPreference = startPreference;
			this.startTime = startTime;
			this.endTime = endTime;
		}

		public String getStartPreference() {
			return this.startPreference;
		}

		public List<String> getDaysPreferred() {
			return this.daysPreferred;
		}
		
		public String getStartTime() {
			return this.startTime;
		}
		
		public String getEndTime() {
			return this.endTime;
		}


	}
	
	
}
