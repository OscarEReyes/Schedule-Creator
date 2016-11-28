package scheduleCreator.view;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;

public class SemesterDialogController {
	@FXML
	private ChoiceBox<String> seasonChoiceBox;
	@FXML
	private ChoiceBox<String> yearChoiceBox;
	
	private Stage dialogStage;
	private Boolean confirmClicked = false;

	
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
    	seasonList.add("2016");
    	seasonList.add("2017");
        seasonList.add("2018");
        seasonList.add("2019");
        seasonList.add("2020");
        
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
}
