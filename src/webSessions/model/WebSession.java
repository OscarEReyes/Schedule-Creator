package webSessions.model;

import org.openqa.selenium.*;
import webSessions.handlers.WebSessionClickHandler;
import webSessions.handlers.WebSessionElementSearcher;
import webSessions.handlers.WebSessionElementSelector;
import webSessions.handlers.WebSessionResizer;


/**
 * Created by Oscar Reyes on 7/7/2017.
 */

public class WebSession {
    private final WebDriver driver;
    private final JavascriptExecutor js;

    private final WebSessionResizer resizer;
    private final WebSessionElementSearcher searcher;
    private final WebSessionElementSelector selector;
    private final WebSessionClickHandler inputHandler;

    WebSession(WebDriver driver) {
        this.driver = driver;
        this.js = (JavascriptExecutor) driver;
        resizer = new WebSessionResizer(this);
        selector = new WebSessionElementSelector(this);
        searcher = new WebSessionElementSearcher(driver);
        inputHandler = new WebSessionClickHandler(this);
        resizer.resizePage(this.driver);
    }

    public WebDriver getDriver() {
        return driver;
    }

    public JavascriptExecutor getJs() {
        return js;
    }

    public WebSessionResizer getResizer() {
        return resizer;
    }

    public WebSessionElementSearcher getElementSearcher() {
        return searcher;
    }

    public WebSessionElementSelector getElementSelector() {
        return selector;
    }

    public WebSessionClickHandler getInputHandler() {
        return inputHandler;
    }

}
