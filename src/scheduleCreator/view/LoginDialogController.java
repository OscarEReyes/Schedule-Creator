package scheduleCreator.view;


import alertMessages.InvalidFieldHandling;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import siteClasses.User;

public class LoginDialogController {
	@FXML
	private TextField usernameField;
	@FXML
	private TextField passwordField;

	private Stage dialogStage;
	private User user;

	public User getUser() {
		return this.user;
	}

	/**
	 * Initializes the controller class. 
	 * Automatically called once the fxml file has loaded.
	 */
	@FXML
	private void initialize() {
	}


	/**
	 * Sets this dialog's stage.
	 * 
	 * @param dialogStage
	 */
	@SuppressWarnings("JavaDoc")
	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}


	/**
	 * Executed if the user clicks login.
	 */
	@FXML
	private void handleLogin() {
		if (isInputValid()) {
			this.user = new User(usernameField.getText(), passwordField.getText());
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

		if (usernameField.getText().length() != 9) {
			errorMessage += "Not a valid username!\n"; 
		}

		if (passwordField.getText() == null || 
				passwordField.getText().length() != 6) {
			errorMessage += "Not a valid 6-character pin!\n"; 
		}

		return InvalidFieldHandling.checkErrorMessage(dialogStage, errorMessage);

	}
}
