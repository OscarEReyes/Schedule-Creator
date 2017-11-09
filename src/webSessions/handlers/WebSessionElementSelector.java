package webSessions.handlers;

import exceptions.SelectionFailure;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.support.ui.Select;
import webSessions.model.Selection;
import webSessions.model.WebSession;

/**
 * Created by Oscar Reyes on 7/9/2017.
 */

public class WebSessionElementSelector {
    private WebSessionClickHandler inputHandler;

    public WebSessionElementSelector(WebSession session) {
        inputHandler = new WebSessionClickHandler(session);
    }

    void select(Selection selection, String buttonSelector) throws SelectionFailure {
        try {
            tryToSelect(selection, buttonSelector);
        } catch (NoSuchElementException e) {
            throw new SelectionFailure();
        }
    }

    private void tryToSelect(Selection selection, String buttonSelector)
            throws NoSuchElementException {
        Select selectElement = new Select(selection.selector);
        selectElement.selectByValue(selection.value);
        inputHandler.tryToClickButton(buttonSelector);
    }
}
