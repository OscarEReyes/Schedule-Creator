package ocr;

import java.io.File;
import java.io.IOException;

import java.util.List;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import org.openqa.selenium.support.ui.Select;

import scheduleCreator.model.CollegeCourse;


public class WebScraper {

  /**
   * A method that will scrape the Blue & Gold website for the list of classes of a course.
   * @param driver
   * @param course
   * @param js
   * @return File - an image that will be used to perform OCR and get the classes for a course
   * @throws IOException
   */
	public File getImageFromPage(WebDriver driver, CollegeCourse course, JavascriptExecutor js)
			throws IOException{
		
		List<WebElement> subject;
		subject = driver.findElements(By.name("sel_subj"));
		
		// Retrieves the element that is displayed
		for (WebElement elem: subject){
			if (elem.isDisplayed() == true) {
				new Select(elem).selectByValue(course.getCourseDepartment());	
			}
		}

		driver.findElement(By.xpath("//input[18]")).click();

		// Look for table display with the text equal to the course name
		// Use it to get its parent and then select the right submit button  
		String textPath = "//td[contains(text(),'" + course.getCourseName() +  "')]";
		String returnScript = "return arguments[0].parentNode;";
		WebElement elem = driver.findElement(By.xpath(textPath));

		WebElement parentElement = (WebElement)js.executeScript(returnScript, elem);

		parentElement.findElement(By.name("SUB_BTN")).click();

		// Get the table of classes
		elem = driver.findElement(By.className("datadisplaytable"));
		WebElement table = elem.findElement(By.tagName("tbody"));
		
		// Clean up page by deleting unnecessary rows, columns, and trivial text
		cleanRows(table, js);
		cleanColumns(table, js);
		delExtraText(js, driver, elem);
		List<WebElement> l = table.findElements(By.tagName("tr"));

		// Resize page text
		resizeText(js, driver, l);
		File imageFile = getImage(driver);
		return imageFile;
}
	
	/**
	 * Deletes extra text from the page
	 * This ranges from the user information on top of the page to the footer text.
	 * This deletes anything not related to Course Classes.
	 * @param js
	 * @param driver
	 * @param table
	 */
	public void delExtraText(JavascriptExecutor js, WebDriver driver, WebElement table) {
		WebElement p = table.findElement(By.xpath("./.."));
		List <WebElement> inputs = p.findElements(By.tagName("input"));
		String removeScript = "arguments[0].parentNode.removeChild(arguments[0])";

		String[] cToDelete = {
				"banner_copyright", 
				"pagefooterdiv", 
				"headerwrapperdiv", 
				"infotextdiv", 
				"footerlinksdiv" ,
				"pagetitlediv" 
		};

		js.executeScript(removeScript, table.findElement(By.className("captiontext")));

		for (String s: cToDelete) {
			js.executeScript(removeScript, driver.findElement(By.className(s)));
		}

		for (WebElement elem: inputs){
			if (elem.isDisplayed() == true) {
				js.executeScript(removeScript, elem);
			}
		}
	}

	/**
	 * Changes the font from the page.
	 * Useful so Tess4j can do a good job at recognizing text
	 * @param js
	 * @param driver
	 * @param list
	 */
	public void resizeText(JavascriptExecutor js, WebDriver driver, List<WebElement> list) {
		for (WebElement webElem: list) {
			String changeFontScript = "arguments[0].style.fontSize='25px'";
			js.executeScript(changeFontScript, webElem);
			
			for(WebElement div: webElem.findElements(By.tagName("div"))) {
				js.executeScript(changeFontScript, div);
			}
			
		}
	}

	/** 
	 * Removes rows that do not contain class information
	 * @param table
	 * @param js
	 */
	public void cleanRows(WebElement table, JavascriptExecutor js) {
		// Create a list of "tr" elements
		List<WebElement> unwantedRows;
		unwantedRows = table.findElements(By.tagName("tr"));
		String removeScript = "arguments[0].parentNode.removeChild(arguments[0])";

		// Remove those "tr" elements with more than ten "td" tags, these are the wanted rows
		unwantedRows.removeIf(e -> (e.findElements(By.tagName("td")).size() > 10));

		// Iterate over each row that is unwanted and remove that element from the page
		for (WebElement e: unwantedRows) {
			js.executeScript(removeScript, e);
		}
	}    

  /**
   * Takes in a table element and an JavascriptExecutor.
   * The JavaScriptExectuor is tied to the driver and so the changes it does
   * affect the driver that is passed. Removes unwanted columns from table.
   * @param table
   * @param js
   */
	public void cleanColumns(WebElement table, JavascriptExecutor js){
		// Create a list of "tr" elements
		List<WebElement> rows;
		rows = table.findElements(By.tagName("tr"));

		// Remove those elements that have less than 10 "td" elements, the rows that will be changed
		// The rows that don't have to be changed have less than "10" td elements
		rows.removeIf(e -> (e.findElements(By.tagName("td")).size() < 10));

		String script = "arguments[0].parentNode.removeChild(arguments[0])";
		int[] columnsToDelete = {2,5,6,7,8,12,13,15,16,17};

		// Iterate over each row and delete the unnecessary columns
		for (WebElement e: rows) {
			List<WebElement> columns = e.findElements(By.tagName("td"));
			for (int i : columnsToDelete) {
				js.executeScript(script, columns.get(i));
			}
		}
	}
	
	/**
	 * Gets a screenshot from the driver
	 * @param driver
	 * @return File - Screenshot of driver
	 * @throws IOException
	 */
	public File getImage(WebDriver driver) throws IOException{
		File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		String imgDir = "C:\\Users\\oscarjr\\Documents\\College Work" + 
				"\\Honors Contract\\Fall 2016\\tempPic";

		FileUtils.copyFile(scrFile, new File(imgDir));
		File imageFile = new File(imgDir);
		return imageFile;
	
}
	
	}
