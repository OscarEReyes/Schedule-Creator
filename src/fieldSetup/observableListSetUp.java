package fieldSetup;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Oscar Reyes on 4/19/2017.
 *
 */
 class observableListSetUp {
    /** Returns an Observable List made with a List of Strings taken from an String Array taken as input.
     *
     * @param choiceArray String array containing strings to be added to Observable List
     * @return ObservableList of Strings
     */
    static ObservableList<String> setUpObservableList(String[] choiceArray) {
        // Create an observable list which will have strings in choiceArray
        List<String> choiceList = new ArrayList<>();
        List<String>temp = Arrays.asList(choiceArray);
        choiceList.addAll(temp);
        return FXCollections.observableList(choiceList);
    }
}
