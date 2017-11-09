package webSessions.handlers;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import webSessions.model.WebSession;

/**
 * Created by Oscar Reyes on 7/9/2017.
 */

public class WebSessionClickHandler {
    private WebSessionElementSearcher searcher;

    public WebSessionClickHandler(WebSession session) {
        searcher = session.getElementSearcher();
    }

    public void tryToClickButton(WebElement element, String cssSelector)
            throws NoSuchElementException {
        WebElement button = searcher.searchByCssSelector(element, cssSelector);
        button.click();
    }

    void tryToClickButton(String cssSelector)
            throws NoSuchElementException {
        WebElement button = searcher.searchByCssSelector(cssSelector);
        button.click();
    }

    public void tryToClickLink(String cssSelector) throws NoSuchElementException {
        WebElement link = searcher.searchByCssSelector(cssSelector);
        link.click();
    }
}
