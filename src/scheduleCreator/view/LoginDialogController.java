package scheduleCreator.view;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import scheduleCreator.model.CollegeCourse;

public class LoginDialogController {
	@FXML
	private TextField userName;
	@FXML
	private TextField password;
	
	private Stage dialogStage;
	private Boolean loginClicked = false;
	
	public class User{
		private final String username;
		private final String password;
		
		public User(final String username, final String password){
			this.username = username;
			this.password = password;
		}
		
		public String getUsername(){
			return this.username;
		}
		
		public String getPassword(){
			return this.password;
		}
	}
}
