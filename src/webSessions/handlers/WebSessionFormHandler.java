package webSessions.handlers;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import siteClasses.User;
import webSessions.model.WebSession;

/**
 * Created by Oscar Reyes on 7/9/2017.
 */

class WebSessionFormHandler {
    private WebDriver driver;
    private WebSessionElementSearcher searcher;

    WebSessionFormHandler(WebSession session) {
        this.driver = session.getDriver();
        searcher = session.getElementSearcher();
    }

    void submitForm(String formName) throws NoSuchElementException {
        WebSessionElementSearcher searcher = new WebSessionElementSearcher(driver);
        WebElement loginForm = searcher.searchByName(formName);
        loginForm.submit();
    }

    void fillOutLoginForm(String userTag, String passwordTag, User user)
            throws NoSuchElementException {
        WebElement userField = getField(userTag);
        WebElement passwordField = getField(passwordTag);

        insertKeysToField(userField, user.getUsername());
        insertKeysToField(passwordField, user.getPassword());
    }

    private WebElement getField(String fieldName) {
        return searcher.searchByName(fieldName);
    }

    private void insertKeysToField(WebElement field, String userInput) throws NoSuchElementException {
        field.sendKeys(userInput);
    }
}
