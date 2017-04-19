package scheduleCreator.view;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import alertMessages.invalidInputAlert;
import fieldSetup.comboBoxSetUp;
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

	private Preferences preferences;
	private Stage dialogStage;
	private Boolean confirmClicked = false;

	
	/**
	 * Initializes the controller class. 
	 * Automatically called once the fxml file has loaded.
	 */
	@FXML
	private void initialize() {
	    // Sets Toggle Group
		ToggleGroup startToggle = new ToggleGroup();
		startEarly.setToggleGroup(startToggle);
		finishEarly.setToggleGroup(startToggle);
		initializeStartTimeComboBox();
        initializeEndTimeComboBox();
	}


	private void initializeStartTimeComboBox() {
        String[] startArr = {"8:00 am", "09:00 am", "10:00 am", "11:00 am", "12:00 pm",
                             "01:00 pm", "02:00 pm","03:00 pm", "04:00 pm", "05:00 pm"};
		comboBoxSetUp.setUpComboBox(startArr, startTime);

	}

    /**
     * Initializes the List of Strings endTimeList representing ending times, and returns it.
     * @return returns a List of Strings representing end times.
     */
    private void initializeEndTimeComboBox() {
        String[] endArr = {"12:00 pm", "01:00 pm", "02:00 pm", "03:00 pm", "04:00 pm",
                           "05:00 pm", "06:00 pm", "07:00 pm", "08:00 pm", "09:00 pm"};
        comboBoxSetUp.setUpComboBox(endArr, endTime);
    }

	/**
	 * Sets this dialog's stage.
	 * 
	 * @param dialogStage DialogStage to set this Class' dialog stage.
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
		if (isInputValid()) {
		    String startPreferences = setStartFinishPriorityPref();
            ArrayList<String> daysPreferred = getDaysPrefered();
			this.preferences = new Preferences(daysPreferred, startPreferences, startTime.getValue(), endTime.getValue());
			confirmClicked = true;
			dialogStage.close();
		}
	}

    /**
     * Returns a string depending on the users preference for starting or finishing early.
     * @return returns a String representing preference for starting or finishing early.
     */
	private String setStartFinishPriorityPref() {
        if (startEarly.isSelected()) {
            return "startEarly";
        } else {
            return "finishEarly";
        }
    }

    /**
     * Adds days selected to an ArrayList of Strings and returns it.
     * @return returns an ArrayList of Strings representing the days the user selected.
     */
    private ArrayList<String> getDaysPrefered() {
        ArrayList<String> daysPreferred = new ArrayList<>();
        if (MWF.isSelected()){
            daysPreferred.add("MWF");
        }
        if (MW.isSelected()){
            daysPreferred.add("MW");
        }
        if (TR.isSelected()){
            daysPreferred.add("TR");
        }
        if (oneDay.isSelected()){
            daysPreferred.add("OneDay");
        }
        return daysPreferred;
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

		if (!startEarly.isSelected() && !finishEarly.isSelected()) {
			errorMessage += "No finish preference selected!\n"; 
		}
		if (startTime.getValue() == null || endTime.getValue() == null) {
			errorMessage += "No valid start time or end time!"; 
		}
		if (!(MWF.isSelected() || TR.isSelected() || MW.isSelected() || oneDay.isSelected())) {
			errorMessage += "No day preference selected!";
		}
		if (errorMessage.length() == 0) {
			return true;
		} else {
		    //Inform user of errors.
            invalidInputAlert.informInvalidFields(dialogStage, errorMessage);
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
