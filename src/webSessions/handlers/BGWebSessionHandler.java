package webSessions.handlers;

import exceptions.DriverFailure;
import exceptions.LoginFail;
import exceptions.SelectionFailure;
import scheduleCreator.model.Course;
import siteClasses.Semester;
import siteClasses.User;
import webScraping.RegistrationData;
import webSessions.model.BGWebSession;
import webSessions.model.Selection;

import static webScraping.ValueGetter.getSemesterValue;


/**
 * Created by Oscar Reyes on 7/9/2017.
 */

public class BGWebSessionHandler extends WebSessionHandler {
    private static final String LOGIN_URL = "https://as2.tamuk.edu:9203/PROD/twbkwbis.P_WWWLogin";

    private User user;
    private Semester semester;
    private BGWebSession session;

    public BGWebSessionHandler(RegistrationData data) {
        super();
        session = createWebSession(data);
        semester = data.semester;
        user = data.user;
        searcher = new WebSessionElementSearcher(session.getDriver());
    }

    private BGWebSession createWebSession(RegistrationData data) {
        return new BGWebSession.BlueGoldWebSessionBuilder(driver)
                .user(data.user)
                .semester(data.semester)
                .courses(data.courses)
                .build();
    }

    public void select(Selection selection, String buttonCssSelector) throws SelectionFailure {
        try {
            tryToSelect(selection, buttonCssSelector);
        } catch (DriverFailure e) {
            throw new SelectionFailure();
        }
    }

    public void logIn() throws LoginFail {
        try {
            tryLoggingInToBlueGold();
        } catch (DriverFailure e) {
            throw new LoginFail();
        }
    }

    private void tryLoggingInToBlueGold() throws DriverFailure {
        getUrlWithDriver(LOGIN_URL);
        WebSessionFormHandler handler = new WebSessionFormHandler(session);
        handler.fillOutLoginForm("sid", "pin", user);
        handler.submitForm("loginform");
    }

    public Course[] getCourses() {
        return session.getCourses();
    }

    public String getValueForSemester() {
        return getSemesterValue(semester);
    }
}
