package javascriptHandling;

import org.openqa.selenium.*;

import java.util.List;

/**
 * Created by Oscar Reyes on 4/20/2017.
 * <p>
 * This class handles Editing Web Elements.
 * Through operations such as resizing.
 */

public class ElementEditor {
    /**
     * Changes the font from the page.
     * Useful so Tess4j can do a good job at recognizing text
     *
     * @param js   JavascriptExecutor instance
     * @param list A list of WebElement objects
     */

    public static void resizeText(JavascriptExecutor js, List<WebElement> list) {
        for (WebElement webElem : list) {
            String changeFontScript = "arguments[0].style.fontSize='25px'";
            js.executeScript(changeFontScript, webElem);

            for (WebElement div : webElem.findElements(By.tagName("div"))) {
                js.executeScript(changeFontScript, div);
            }
        }
    }

    /**
     * Resizes page to a bigger size.
     * Does it so that screenshot can capture all classes.
     *
     * @param driver WebDriver Object Instance, the interface for the browsing.
     */

    public static void resizePage(WebDriver driver) {
        Dimension dimension = new Dimension(1800, 1000);
        driver.manage().window().setSize(dimension);
    }
}
