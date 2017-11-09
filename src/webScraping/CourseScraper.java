package webScraping;

import exceptions.NavigatorFailure;
import exceptions.ScrapingFailure;
import scheduleCreator.model.Course;
import webSessions.handlers.BGWebSessionHandler;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by Oscar Reyes on 7/13/2017.
 */
public class CourseScraper {
    BlueGoldPageNavigator navigator;
    BGWebSessionHandler handler;

    public CourseScraper(RegistrationData data) {
        this.handler = new BGWebSessionHandler(data);
        this.navigator = new BlueGoldPageNavigator(handler);
    }

    public HashMap<Course, String[]> scrape(Course[] courses)
            throws ScrapingFailure {
        return scrapeCourses(courses);
    }

    private HashMap<Course, String[]> scrapeCourses(Course[] courses) throws ScrapingFailure {
        prepareForScraping();
        HashMap<Course, String[]> sectionsOfCourse = new HashMap<>();
        ClassSectionsGetter sectionGetter = new ClassSectionsGetter(navigator, handler);
        for (Course course : courses) {
            String[] sections = sectionGetter.getSections(course);
            sectionsOfCourse.put(course, sections);
            prepareForNextCourse();
        }
        return sectionsOfCourse;
    }

    private void prepareForScraping() throws ScrapingFailure {
        try {
            navigator.getToRegistration();
        } catch (NavigatorFailure e) {
            throw new ScrapingFailure();
        }
    }

    private void prepareForNextCourse() throws ScrapingFailure {
        try {
            navigator.returnToRegistration();
        } catch (NavigatorFailure e) {
            throw new ScrapingFailure();
        }
    }
}
