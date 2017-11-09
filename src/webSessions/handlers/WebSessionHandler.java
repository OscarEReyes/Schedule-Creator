package webSessions.handlers;

import exceptions.DriverFailure;
import exceptions.SelectionFailure;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import webSessions.model.Selection;
import webSessions.model.WebSession;

import java.io.File;
import java.io.IOException;

import static webSessions.PhantomJsSetter.getWebDriver;

/**
 * Created by Oscar Reyes on 7/9/2017.
 */

public class WebSessionHandler {
    WebSession session;
    WebSessionElementSearcher searcher;
    WebDriver driver;

    WebSessionHandler() {
        driver = getWebDriver();
        searcher = new WebSessionElementSearcher(driver);
    }

    void getUrlWithDriver(String url) {
        driver.get(url);
    }

    public WebElement getSelectorByClassName(String className) {
        return searcher.searchByClassName(className);
    }

    public WebElement getSelectorByCssSelector(String cssSelector) {
        return searcher.searchByCssSelector(cssSelector);
    }

    void tryToSelect(Selection selection, String buttonCssSelector)
            throws DriverFailure {
        try {
            WebSessionElementSelector selector = new WebSessionElementSelector(session);
            selector.select(selection, buttonCssSelector);
        } catch (SelectionFailure e ) {
            throw new DriverFailure();
        }
    }

    public File takeScreenshot() throws IOException {
        ScreenshotHandler taker = new ScreenshotHandler(session);
        return taker.takeScreenshot();
    }

    public WebElement executeScript(String script, WebElement element) {
        JavascriptExecutor js = session.getJs();
        return (WebElement) js.executeScript(script, element);
    }

    public void quitSession() {
        WebDriver driver = session.getDriver();
        driver.quit();
    }

    public WebSessionElementSearcher getSearcher() {
        return searcher;
    }

    public WebSessionClickHandler getClickHandler() {
        return new WebSessionClickHandler(session);
    }
}
