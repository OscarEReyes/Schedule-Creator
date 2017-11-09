package webSessions.handlers;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Created by Oscar Reyes on 7/9/2017.
 */

public class WebSessionElementSearcher {
    private WebDriver driver;

    public WebSessionElementSearcher(WebDriver driver) {
        this.driver = driver;
    }

    public WebElement searchByXPath(String textPath) throws NoSuchElementException {
        return driver.findElement(By.xpath(textPath));
    }

    WebElement searchByCssSelector(WebElement element, String selector)
            throws NoSuchElementException {
        return element.findElement(By.cssSelector(selector));
    }

    WebElement searchByCssSelector(String selector) throws NoSuchElementException {
        return driver.findElement(By.cssSelector(selector));
    }

    WebElement searchByName(String name) throws NoSuchElementException {
        return driver.findElement(By.name(name));
    }

    WebElement searchByTagName(WebElement element, String tagName) throws NoSuchElementException {
        return element.findElement(By.tagName(tagName));
    }

    WebElement searchByClassName(String className) {
        return driver.findElement(By.className(className));
    }
}
