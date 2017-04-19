package fieldSetup;

import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;

/**
 * Created by Oscar Reyes on 4/19/2017.
 */
public class comboBoxSetUp {
    /** Sets Up ComboBox
     *
     * @param choiceArray String array containing options wanted in ComboBox
     * @param comboBox ComboBox being set up.
     */
    public static void setUpComboBox(String[] choiceArray, ComboBox<String> comboBox) {
        // Create an observable list which will have strings in choiceArray
        ObservableList<String> observableTimeList = observableListSetUp.setUpObservableList(choiceArray);
        // Clear comboBox and set items
        comboBox.getItems().clear();
        comboBox.setItems(observableTimeList);
    }

}

