package webScraping;

import scheduleCreator.model.Course;
import siteClasses.Semester;
import siteClasses.User;

/**
 * Created by Oscar Reyes on 7/9/2017.
 */
public class RegistrationData {
    public User user;
    public Semester semester;
    public Course[] courses;

    public RegistrationData(User user, Semester semester, Course[] courses) {
        this.user = user;
        this.semester = semester;
        this.courses = courses;
    }
}
