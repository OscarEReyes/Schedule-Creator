package scheduleCreator.view;


import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class LoginDialogController {
	@FXML
	private TextField usernameField;
	@FXML
	private TextField passwordField;

	private Stage dialogStage;
	private User user;
	private Boolean loginClicked = false;

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
	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}

	/**
	 * Returns true if user has clicked the login button,
	 * Returns false if it is not the case.
	 * @return
	 */
	public boolean isLoginClicked() {
		return loginClicked;
	}


	/**
	 * Executed if the user clicks login.
	 */
	@FXML
	private void handleLogin() {
		if (isInputValid()) {
			this.user = new User(usernameField.getText(), passwordField.getText());
			loginClicked = true;
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

	public class User{
		private final String username;
		private final String password;

		public User(final String username, final String password){
			this.username = username;
			this.password = password;
		}

		public String getUsername() {
			return this.username;
		}

		public String getPassword() {
			return this.password;
		}


	}

}
