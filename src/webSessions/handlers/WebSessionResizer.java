package webSessions.handlers;

import org.openqa.selenium.*;
import webSessions.model.WebSession;

import java.util.List;

/**
 * Created by Oscar Reyes on 7/9/2017.
 */

public class WebSessionResizer {
    private static final String CHANGE_FONT_SCRIPT = "arguments[0].style.fontSize='25px'";

    private JavascriptExecutor js;

    public WebSessionResizer(WebSession session) {
        this.js = session.getJs();
    }

    public void resizePage(WebDriver driver) {
        Dimension dimension = new Dimension(2500, 1000);
        driver.manage().window().setSize(dimension);
    }

    private void resizePageText(List<WebElement> webElements) {
        for (WebElement element : webElements) {
            js.executeScript(CHANGE_FONT_SCRIPT, element);
            resizeDivs(element);
        }
    }

    void resizeTableRows( WebElement table) {
        List<WebElement> tableRows = table.findElements(By.tagName("tr"));
        resizePageText(tableRows);
    }

    private void resizeDivs(WebElement element) {
        List<WebElement> divs = element.findElements(By.tagName("div"));
        for (WebElement div : divs) {
            js.executeScript(CHANGE_FONT_SCRIPT, div);
        }
    }
}
