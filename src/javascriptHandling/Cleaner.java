package javascriptHandling;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

import static javascriptHandling.Searcher.*;

/**
 * Created by Oscar Reyes on 4/20/2017.
 * <p>
 * This class takes care of cleaning up the scraped page.
 * It handles the removal of elements such as rows and columns.
 */

public class Cleaner {
    /**
     * Removes rows lacking information relevant to a class.
     * These include:
     * title,
     * headers,
     * and department/instructor/syllabus information.
     *
     * @param js    JavascriptExecutor Instance
     * @param table A WebElement Object representing Class Information display table
     */

    public static void cleanRows(JavascriptExecutor js, WebElement table) {
        removeDepartmentTitle(js, table);
        removeHeaders(js, table);
        removeExtraInformation(js, table);
    }

    /**
     * Removes unwanted columns from table.
     * A column is unwanted if its text:
     * Is already known.
     * Isn't required to determine if a class is available.
     * Isn't required to determine if a class is a good choice.
     *
     * @param js    JavascriptExecutor Instance
     * @param table A WebElement Object representing Class Information display table
     */

    public static void cleanColumns(JavascriptExecutor js, WebElement table) {
        List<WebElement> rows;
        rows = getClassRows(table);
        removeUnwantedColumns(js, rows);
    }

    /**
     * Deletes extra text from the page
     * Example: header and footer text.
     * This deletes anything not related to Course Classes.
     *
     * @param driver WebDriver Object Instance, the interface for the browsing.
     * @param js     JavascriptExecutor Instance
     * @param table  A WebElement Object representing Class Information display table
     */

    public static void cleanText(WebDriver driver, JavascriptExecutor js, WebElement table) {
        String removeScript = "arguments[0].parentNode.removeChild(arguments[0])";
        removeCaptionText(driver, js, removeScript);
        removeTableTitle(js, removeScript, table);
        removeSearchButton(js, removeScript, table);
    }

    /**
     * Gets rows containing class information.
     * Rows that do have the class "dddefault"
     * and have no specified "colspan" attribute.
     *
     * @param table A WebElement Object representing Class Information display table
     * @return returns a List of WebElements, the Rows containing Class information
     */

    private static List<WebElement> getClassRows(WebElement table) {
        List<WebElement> rows;
        rows = searchAllOfClass(table, "dddefault");
        rows.removeIf(e -> searchAllWithAttribute(e, "td", "colspan", "9").size() > 0);
        rows.removeIf(e -> searchAllWithAttribute(e, "td", "colspan", "14").size() > 0);
        rows.removeIf(e -> searchAllWithAttribute(e, "td", "colspan", "23").size() > 0);
        return rows;
    }

    /**
     * Removes specified columns from rows in a given List of Rows.
     *
     * @param js   JavascriptExecutor Instance
     * @param rows List of WebElements containing the rows in the Class Information display teble.
     */

    private static void removeUnwantedColumns(JavascriptExecutor js, List<WebElement> rows) {
        // Due to lack of attributes or class names,
        // there is no simpler way to remove unwanted columns.
        // The header order is constant.
        // And so does the column order.

        int[] columnsToDelete = {2, 3, 5, 6, 7, 8, 12, 13, 15, 16, 17, 19};
        String script = "arguments[0].parentNode.removeChild(arguments[0])";

        for (WebElement row : rows) {
            List<WebElement> columns;
            columns = searchAllOfTag(row, "td");
            for (int i : columnsToDelete) {
                js.executeScript(script, columns.get(i));
            }
        }
    }

    /**
     * Removes the Department Title from the Class Information Display Table
     *
     * @param js    JavascriptExecutor Instance
     * @param table A WebElement Object representing Class Information display table
     */

    private static void removeDepartmentTitle(JavascriptExecutor js, WebElement table) {
        WebElement titleCaption;
        titleCaption = searchByClassName(table, "ddtitle");
        removeElement(js, titleCaption);
    }

    /**
     * Removes headers from the Class Information Display Table
     *
     * @param js    JavascriptExecutor Instance
     * @param table A WebElement Object representing Class Information display table
     */

    private static void removeHeaders(JavascriptExecutor js, WebElement table) {
        List<WebElement> headers;
        headers = searchAllOfClass(table, "ddheader");
        removeElements(js, headers);
    }

    /**
     * Removes Rows containing non-useful information.
     * The trivial links referred to here are:
     *     * Links to Syllabus,
     *     * Department Budget,
     *     * And other non-useful text,
     *     which gets in the way of OCR.
     * <p>
     * This links are more often than not, broken
     * or just text and not a link.
     *
     * @param js    JavascriptExecutor Instance
     * @param table A WebElement Object representing Class Information display table
     */

    private static void removeExtraInformation(JavascriptExecutor js, WebElement table) {
        // When blank, the column span of a "td" will be 23.
        // When it shows Syllabus, department budget, etc. Then it will be 9
        // When it shows how a course is connected to another one. Then it will be 14
        List<WebElement> trivialLinks;
        trivialLinks = searchAllWithAttribute(table, "td", "colspan", "9");
        removeElements(js, trivialLinks);
    }

    /**
     * Removes div elements containing information not relevant.
     * Removing these elements results in removing:
     * header text
     * footer text
     * copyright text
     * etc.
     *
     * @param driver       WebDriver Object Instance, the interface for the browsing.
     * @param js           JavascriptExecutor Instance
     * @param removeScript String which represents a JavaScript script which will remove an element
     */

    private static void removeCaptionText(WebDriver driver, JavascriptExecutor js, String removeScript) {
        String[] divsToDelete = {
                "headerwrapperdiv",
                "pagetitlediv",
                "infotextdiv",
                "footerlinksdiv",
                "pagefooterdiv",
                "banner_copyright"
        };

        WebElement div;
        for (String className : divsToDelete) {
            div = searchByClassName(driver, className);
            js.executeScript(removeScript, div);
        }
    }

    /**
     * Removes the Title
     *
     * @param js           JavascriptExecutor Instance
     * @param removeScript String which represents a JavaScript script which will remove an element
     * @param table        A WebElement Object representing Class Information display table
     */

    private static void removeTableTitle(JavascriptExecutor js, String removeScript, WebElement table) {
        WebElement titleCaption;
        titleCaption = searchByClassName(table, "captiontext");
        js.executeScript(removeScript, titleCaption);
    }

    /**
     * Removes Search Button
     * "New Search"
     *
     * This is so that its text is not picked up by OCR.
     *
     * @param js           JavascriptExecutor Instance
     * @param removeScript String which represents a JavaScript script which will remove an element
     * @param table        A WebElement Object representing Class Information display table
     */

    private static void removeSearchButton(JavascriptExecutor js, String removeScript, WebElement table) {
        WebElement newSearchBtn;
        newSearchBtn = searchByAttribute(table, "imput", "value", "New Search");
        js.executeScript(removeScript, newSearchBtn);
    }

    /**
     * Removes some elements from the page being scraped.
     *
     * @param js            JavascriptExecutor Instance
     * @param elemsToDelete List of WebElement objects to be deleted.
     */

    private static void removeElements(JavascriptExecutor js, List<WebElement> elemsToDelete) {
        String removeScript = "arguments[0].parentNode.removeChild(arguments[0])";
        for (WebElement elem : elemsToDelete) {
            js.executeScript(removeScript, elem);
        }
    }

    /**
     * Removes an element from the page being scraped.
     *
     * @param js                 JavascriptExecutor Instance
     * @param elementToBeDeleted Element on page to be deleted.
     */

    private static void removeElement(JavascriptExecutor js, WebElement elementToBeDeleted) {
        String removeScript = "arguments[0].parentNode.removeChild(arguments[0])";
        if (elementToBeDeleted != null) {
            js.executeScript(removeScript, elementToBeDeleted);
        }
    }
}
