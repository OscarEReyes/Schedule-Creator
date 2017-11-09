package scheduleCreator.view;

import alertMessages.invalidInputAlert;
import fieldSetup.comboBoxSetUp;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class PreferenceDialogController {
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

	@FXML
	private void initialize() {
		ToggleGroup startToggle = new ToggleGroup();
		initializeStartTimeComboBox();
        initializeEndTimeComboBox();
	}

	private void initializeStartTimeComboBox() {
        String[] startArr = {"8:00 am", "09:00 am", "10:00 am", "11:00 am", "12:00 pm",
                             "01:00 pm", "02:00 pm","03:00 pm", "04:00 pm", "05:00 pm"};
		comboBoxSetUp.setUpComboBox(startArr, startTime);
	}

    private void initializeEndTimeComboBox() {
        String[] endArr = {"12:00 pm", "01:00 pm", "02:00 pm", "03:00 pm", "04:00 pm",
                           "05:00 pm", "06:00 pm", "07:00 pm", "08:00 pm", "09:00 pm"};
        comboBoxSetUp.setUpComboBox(endArr, endTime);
    }

	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}

	public boolean isConfirmClicked() {
		return confirmClicked;
	}

	@FXML
	private void handleConfirm() {
		if (isInputValid()) {
            ArrayList<String> daysPreferred = getDaysPrefered();
			this.preferences = new Preferences(daysPreferred, startTime.getValue(), endTime.getValue());
			confirmClicked = true;
			dialogStage.close();
		}
	}

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

	@FXML
	private void handleCancel() {
		dialogStage.close();
	}

	private boolean isInputValid() {
	    Boolean validTimeInputs = startTime.getValue() != null && endTime.getValue() != null;
	    Boolean preferencesSelected = (MWF.isSelected() || TR.isSelected() ||
                MW.isSelected() || oneDay.isSelected());

	    if (validTimeInputs && preferencesSelected) {
	        return true;
        }
        invalidInputAlert.informInvalidFields(dialogStage, "Please fill out missing fields");
        return false;
	}
	
	public Preferences getPreferences() {
		return this.preferences;
	}

	public class Preferences {
		private final List<String> daysPreferred;
		private final String startTime;
		private final String endTime;

		public Preferences(final List<String> daysPreferred, 
				final String startTime,
				final String endTime){
			this.daysPreferred = daysPreferred;
			this.startTime = startTime;
			this.endTime = endTime;
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
