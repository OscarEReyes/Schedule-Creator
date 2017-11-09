package webSessions.handlers;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import webSessions.model.WebSession;

import java.io.File;
import java.io.IOException;

/**
 * Created by Oscar Reyes on 7/9/2017.
 */

class ScreenshotHandler {
    private WebSessionElementSearcher searcher;
    private WebSessionResizer resizer;
    private WebSession session;

    private static final String IMAGE_DIRECTORY = "tempPic/screenshot.png";


    ScreenshotHandler(WebSession session) {
        this.session = session;
        searcher = session.getElementSearcher();
        resizer = session.getResizer();
    }

    File takeScreenshot() throws IOException {
        try {
            prepareForScreenshot();
            return tryToTakeScreenshot();
        } catch(NoSuchElementException e) {
            throw new IOException("Error taking screenshot", e);
        }
    }

    private void prepareForScreenshot() {
        WebElement table = searcher.searchByClassName("datadisplaytable");
        WebElement tableBody = searcher.searchByTagName(table, "tbody");
        resizer.resizeTableRows(tableBody);
    }

    private File tryToTakeScreenshot() throws IOException {
        TakesScreenshot s = (TakesScreenshot) session.getDriver();
        File screenshot = s.getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(screenshot, new File(IMAGE_DIRECTORY));
        return new File(IMAGE_DIRECTORY);
    }
}
