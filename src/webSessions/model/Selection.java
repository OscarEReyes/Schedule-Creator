package webSessions.model;

import org.openqa.selenium.WebElement;

/**
 * Created by Oscar Reyes on 7/10/2017.
 */

public class Selection {
    public WebElement selector;
    public String value;

    public Selection(WebElement selector, String value) {
        this.selector = selector;
        this.value = value;
    }
}
