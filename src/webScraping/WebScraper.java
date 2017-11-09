package webScraping;

import webSessions.handlers.BGWebSessionHandler;

import java.io.File;
import java.io.IOException;

import static ocr.ImageAnalyzer.analyzeImage;

public class WebScraper {
    public WebScraper() {}

    private final String[] NO_CLASSES = {};

    // TODO make sure to click return to menu on screen to return to the registration menu. So algorithm will not break.

    String[] scrapeClassesInCourse(BGWebSessionHandler handler) {
        try {
            File screenshot = handler.takeScreenshot();
            return getClassesInScreenshot(screenshot);
        } catch (IOException e) {
            return  NO_CLASSES;
        }
    }

    private String[] getClassesInScreenshot(File screenshot) {
        String results = analyzeImage(screenshot);
        return results.split("\\r?\\n");
    }
}
