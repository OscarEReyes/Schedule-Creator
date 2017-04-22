package ocr;

import org.junit.Test;
import scheduleCreator.model.Course;
import siteClasses.Semester;
import siteClasses.User;
import webScraping.WebScraper;

/**
 * Created by oscarjr on 4/16/2017.
 */
public class WebScraperTest {
    @Test
    public void scrapeCoursePage() throws Exception {
        WebScraper scraper = new WebScraper();

        String username = "K00------";
        String password = "-------";
        String season = "Fall";
        String year = "2017";

        User user = new User(username, password);
        Semester semester = new Semester(season, year);
        Course course = new Course();
        scraper.getScreenshot(user, semester, course);

    }

}