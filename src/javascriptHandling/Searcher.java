package javascriptHandling;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.Collections;
import java.util.List;

/**
 * Created by Oscar Reyes on 4/20/2017.
 *
 * This class was made to handle Searching objects.
 * Returning null or an Empty List when appropriate.
 * It is also written to understand its usage easily.
 */

public class Searcher {
    /**
     *
     * @param driver WebDriver Object Instance, the interface for the browsing.
     * @param text The String Object representing the text the element looked for contains.
     * @return WebElement Objects retrieved by search
     */

    public static WebElement searchByText(WebDriver driver, String text) {
        try {
            return driver.findElement(By.linkText(text));
        } catch (NoSuchElementException e) {
            System.out.println("Element with Link Text: " + text + " Not Found");
            return null;
        }
    }

    /**
     *
     * @param driver WebDriver Object Instance, the interface for the browsing.
     * @param tagName The Tag Name of the Element being looked for.
     * @param attrib The attribute being used for the search.
     * @param value The expected value of the attribute being used for the search
     * @return WebElement Objects retrieved by search
     */

    public static WebElement searchByAttribute(WebDriver driver, String tagName, String attrib, String value) {
        try {
            return driver.findElement(By.cssSelector(tagName + "[" + attrib + "='" + value + "']"));
        } catch (NoSuchElementException e) {
            System.out.println(tagName + " Element with Value: " + value + "for Attrib: " + attrib + " Not Found");
            return null;
        }
    }

    /**
     *
     * @param elem Element containing the element being looked for.
     * @param tagName The Tag Name of the Element being looked for.
     * @param attrib The attribute being used for the search.
     * @param value The expected value of the attribute being used for the search
     * @return WebElement Objects retrieved by search
     */

    public static WebElement searchByAttribute(WebElement elem, String tagName, String attrib, String value) {
        try {
            return elem.findElement(By.cssSelector(tagName + "[" + attrib + "='" + value + "']"));
        } catch (NoSuchElementException e) {
            System.out.println(tagName + " Element with Value: " + value + "for Attrib: " + attrib + " Not Found");
            return null;
        }
    }

    /**
     *
     * @param elem Element containing the elements being looked for.
     * @param tagName The Tag Name of the elements being looked for.
     * @param attrib The attribute being used for the search.
     * @param value The expected value of the attribute being used for the search
     * @return WebElement Objects retrieved by search
     */

    static List<WebElement> searchAllWithAttribute(WebElement elem, String tagName, String attrib, String value) {
        try {
            return elem.findElements(By.cssSelector(tagName + "[" + attrib + "='" + value + "']"));
        } catch (NoSuchElementException e) {
            System.out.println(tagName + " Element with Value: " + value + "for Attrib: " + attrib + " Not Found");
            return Collections.emptyList();
        }
    }

    /**
     *
     * @param driver WebDriver Object Instance, the interface for the browsing.
     * @param className The Expected class name of the element being searched.
     * @return WebElement Objects retrieved by search
     */

    public static WebElement searchByClassName(WebDriver driver, String className) {
        try {
            return driver.findElement(By.className(className));
        } catch (NoSuchElementException e) {
            System.out.println("Element with Class Name: " + className + " Not Found");
            return null;
        }
    }

    /**
     *
     * @param elem Element containing the element being looked for.
     * @param className The Expected class name of the element being searched.
     * @return WebElement Objects retrieved by search
     */

    public static WebElement searchByClassName(WebElement elem, String className) {
        try {
            return elem.findElement(By.className(className));
        } catch (NoSuchElementException e) {
            System.out.println("Element with Class Name: " + className + " Not Found");
            return null;
        }
    }

    /**
     *
     * @param elem Element containing the element being looked for.
     * @param className The Expected class name of the elements being searched.
     * @return WebElement Objects retrieved by search
     */

    static List<WebElement> searchAllOfClass(WebElement elem, String className) {
        try {
            return elem.findElements(By.className(className));
        } catch (NoSuchElementException e) {
            return handleEmptyList("Class Name", className);
        }
    }

    /**
     *
     * @param elem Element containing the element being looked for.
     * @param tagName The Tag Name of the elements being looked for.
     * @return WebElement Objects retrieved by search
     */

    static List<WebElement> searchAllOfTag(WebElement elem, String tagName) {
        try {
            return elem.findElements(By.tagName("input"));
        } catch (NoSuchElementException e) {
            return handleEmptyList("Tag Name", tagName);
        }
    }

    /**
     * Handling for searches expected to yield a List of WebElements.
     * Returns an emptyList to prevent errors.
     *
     * @param searchParam The Parameter used for the search.
     * @param paramValue The eppected value for the parameter used in the search.
     * @return An empty list of WebElements, for whenever nothing is found in a search.
     */

    private static List<WebElement> handleEmptyList(String searchParam, String paramValue) {
        System.out.println("Elements with" + searchParam + ":" + paramValue + " Not Found");
        return Collections.emptyList();
    }

}
