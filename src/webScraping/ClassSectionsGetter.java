package webScraping;

import exceptions.DriverFailure;
import exceptions.NavigatorFailure;
import exceptions.SelectionFailure;
import scheduleCreator.model.Course;
import webSessions.handlers.BGWebSessionHandler;

/**
 * Created by Oscar Reyes on 7/13/2017.
 */

class ClassSectionsGetter {
    private static final String[] NO_SECTIONS = {};
    private BlueGoldPageNavigator navigator;
    private BGWebSessionHandler handler;

    ClassSectionsGetter(BlueGoldPageNavigator navigator, BGWebSessionHandler handler) {
        this.navigator = navigator;
        this.handler = handler;
    }

    String[] getSections(Course course) {
        try {
            return tryToGetSections(course);
        } catch (NavigatorFailure e) {
            return NO_SECTIONS;
        }
    }

    private String[] tryToGetSections(Course course) throws NavigatorFailure {
        getToClassSections(course);
        WebScraper scraper = new WebScraper();
        String[] sections = scraper.scrapeClassesInCourse(handler);
        navigator.returnToRegistration();
        return sections;
    }

    private void getToClassSections(Course course) throws NavigatorFailure {
        try {
            navigator.selectTheSemester();
            navigator.selectTheSubject(course.getDepartment());
            navigator.goToCourseSearch();
            navigator.viewClassSections(course.getCourseName());
        } catch (SelectionFailure | DriverFailure e) {
            throw new NavigatorFailure();
        }
    }
}
