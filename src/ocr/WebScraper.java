package ocr;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.Select;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import scheduleCreator.model.Course;
import scheduleCreator.view.LoginDialogController.User;
import scheduleCreator.view.SemesterDialogController.Semester;



public class WebScraper {
	
	private Stage dialogStage;
	/**
	 * Sets this dialog's stage.
	 * 
	 * @param dialogStage Stage Instance
	 */
	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}
	
  /**
   * A method that will scrape the Blue & Gold website for the list of classes of a course.
   * @param user User Object Instance
   * @param course Course Object Instance
   * @param semester Semester Object instance, the Semester the user is interested in.
   * @return File - an image that will be used to perform OCR and get the classes for a course
   * @throws IOException
   */
	public File scrapeCoursePage(User user, Course course, Semester semester) throws IOException{
	// Set-up PhantomJS
			Capabilities caps = new DesiredCapabilities();
			((DesiredCapabilities) caps).setJavascriptEnabled(true);                
			((DesiredCapabilities) caps).setCapability("takesScreenshot", true);  
			((DesiredCapabilities) caps).setCapability(
					PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY,
					"C:\\PhantomJs\\bin\\phantomjs.exe"
					);

			// Initialize WebDriver 
			WebDriver driver = new  PhantomJSDriver(caps);
			JavascriptExecutor js;

			js = (JavascriptExecutor)driver;

			// Set size of window (Affects Screenshot size)
			Dimension dimension = new Dimension(1800, 1000);
			driver.manage().window().setSize(dimension);

			// Access blue gold website
			driver.get("https://as2.tamuk.edu:9203/PROD/twbkwbis.P_WWWLogin");

			// Find the login input elements by their name
			WebElement element = driver.findElement(By.name("sid"));
			WebElement pin = driver.findElement(By.name("PIN"));

			// Enter login information
			element.sendKeys(user.getUsername());
			pin.sendKeys(user.getPassword());

			// Submit Login information
			element.submit();
			System.out.println("Page title is: " + driver.getTitle());

			driver.findElement(By.linkText("Registration")).click();  	
			driver.findElement(By.cssSelector("a[href*='.p_sel_crse_search']")).click();

			HashMap<String, String> codeMap = new HashMap<>();
			codeMap.put("Fall", "10");
			codeMap.put("Winter Intersession", "15");
			codeMap.put("Spring Intersession", "25");
			codeMap.put("Summer Intersession", "35");
			codeMap.put("Summer", "30");
			codeMap.put("Spring", "20");

			String season = semester.getSeason();
			String elementCode;

			if (season.equals("Fall") || season.equals("Winter")) {
				int code = Integer.parseInt(semester.getYear()) + 1;
				elementCode = String.valueOf(code);
			} else {
				elementCode = semester.getYear();
			}
			// Select Semester term
			new Select(driver.findElement(By.name("p_term"))).selectByValue(elementCode + codeMap.get(season));

			driver.findElement(By.xpath("//input[3]")).click(); 

			File imageFile = getImageFromPage(driver, course, js);
			
			driver.quit();
			return imageFile;
		}

	private File getImageFromPage(WebDriver driver, Course course, JavascriptExecutor js)
			throws IOException{
		
		List<WebElement> subject;
		subject = driver.findElements(By.name("sel_subj"));
		
		try { 
			// Retrieves the element that is displayed
			for (WebElement elem: subject){
				if (elem.isDisplayed()) {
					new Select(elem).selectByValue(course.getCourseDep());
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
			resizeText(js, l);
			return getImage(driver);
		} catch (org.openqa.selenium.NoSuchElementException e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.initOwner(dialogStage);
			alert.setTitle("Department or Course Not Found");
			alert.setContentText("ERROR");

			// Show error message window and wait for a response.
			alert.showAndWait();
			return null;
		}
}
	
	/**
	 * Deletes extra text from the page
	 * This ranges from the user information on top of the page to the footer text.
	 * This deletes anything not related to Course Classes.
	 * @param js JavascriptExecutor Instance
	 * @param driver WebDriver Instance
	 * @param table WebElement object, expected to be a table
	 */
	private void delExtraText(JavascriptExecutor js, WebDriver driver, WebElement table) {
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
			if (elem.isDisplayed()) {
				js.executeScript(removeScript, elem);
			}
		}
	}

	/**
	 * Changes the font from the page.
	 * Useful so Tess4j can do a good job at recognizing text
	 * @param js JavascriptExecutor instance
	 * @param list A list of WebElement objects
	 */
	private void resizeText(JavascriptExecutor js, List<WebElement> list) {
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
	 * @param table A WebElement object, expected to be a table
	 * @param js JavascriptExecutor Instance
	 */
	private void cleanRows(WebElement table, JavascriptExecutor js) {
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
   * The JavaScriptExecutor is tied to the driver and so the changes it does
   * affect the driver that is passed. Removes unwanted columns from table.
   * @param table WebElement table
   * @param js JavascriptExecutor Instance
   */
	private void cleanColumns(WebElement table, JavascriptExecutor js){
		// Create a list of "tr" elements
		List<WebElement> rows;
		rows = table.findElements(By.tagName("tr"));

		// Remove those elements that have less than 10 "td" elements, the rows that will be changed
		// The rows that don't have to be changed have less than "10" td elements
		rows.removeIf(e -> (e.findElements(By.tagName("td")).size() < 10));

		String script = "arguments[0].parentNode.removeChild(arguments[0])";
		int[] columnsToDelete = {2,3,5,6,7,8,12,13,15,16,17,19};

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
	 * @param driver driver used
	 * @return File - Screenshot of driver
	 * @throws IOException
	 */
	private File getImage(WebDriver driver) throws IOException{
		File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		String imgDir = "\\tempPic\\screenshot.png";

		FileUtils.copyFile(scrFile, new File(imgDir));
		return new File(imgDir);
	}
	
	}
