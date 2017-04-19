package fieldSetup;

import javafx.collections.ObservableList;
import javafx.scene.control.ChoiceBox;

/**
 * Created by Oscar Reyes on 4/19/2017.
 */
public class choiceBoxSetUp {
    /**
     * @param choiceArray String array containing options wanted in comboBox
     * @param choiceBox    ComboBox being set up.
     */
    public static void setUpChoiceBox(String[] choiceArray, ChoiceBox<String> choiceBox) {
        // Create an observable list which will have strings in choiceArray
        ObservableList<String> observableTimeList = observableListSetUp.setUpObservableList(choiceArray);
        // Clear choiceBox and set items
        choiceBox.getItems().clear();
        choiceBox.setItems(observableTimeList);
    }

}
