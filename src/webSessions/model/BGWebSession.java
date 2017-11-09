package webSessions.model;

import org.openqa.selenium.WebDriver;
import scheduleCreator.model.Course;
import siteClasses.Semester;
import siteClasses.User;


/**
 * Created by Oscar Reyes on 7/8/2017.
 */

public class BGWebSession extends WebSession {
    private Semester semester;
    private Course[] courses;

    BGWebSession(BlueGoldWebSessionBuilder builder) {
        super(builder.driver);
        this.semester = builder.semester;
        this.courses = builder.courses;
    }

    public Course[] getCourses() {
        return courses;
    }

    public Semester getSemester() {
        return semester;
    }

    public static class BlueGoldWebSessionBuilder {
        private WebDriver driver;
        private User user;
        private Semester semester;
        private Course[] courses;

        public BlueGoldWebSessionBuilder(WebDriver driver) {
            this.driver = driver;
        }

        public BlueGoldWebSessionBuilder user(User user) {
            this.user = user;
            return this;
        }

        public BlueGoldWebSessionBuilder semester(Semester semester) {
            this.semester = semester;
            return this;
        }

        public BlueGoldWebSessionBuilder courses(Course[] courses) {
            this.courses = courses;
            return this;
        }

        public BGWebSession build() {
            return new BGWebSession(this);
        }
    }

}
