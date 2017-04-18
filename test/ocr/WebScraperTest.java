package ocr;

import org.junit.Test;
import scheduleCreator.model.Course;
import scheduleCreator.model.CourseClass;
import siteClasses.Semester;
import siteClasses.User;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Scanner;

import static org.junit.Assert.*;

/**
 * Created by oscarjr on 4/16/2017.
 */
public class WebScraperTest {
    @Test
    public void scrapeCoursePage() throws Exception {
        WebScraper scraper = new WebScraper();

        String username = "K00363631";
        String password = "979797";
        String season = "Fall";
        String year = "2017";

        User user = new User(username, password);
        Semester semester = new Semester(season, year);
        Course course = new Course();
        scraper.scrapeCoursePage(user, course, semester);

    }

}