package webScraping;


import exceptions.DriverFailure;
import exceptions.LoginFail;
import exceptions.NavigatorFailure;
import exceptions.SelectionFailure;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import webSessions.handlers.BGWebSessionHandler;
import webSessions.handlers.WebSessionClickHandler;
import webSessions.handlers.WebSessionElementSearcher;
import webSessions.model.Selection;

/**
 * Created by Oscar Reyes on 7/10/2017.
 */
public class BlueGoldPageNavigator {
    private BGWebSessionHandler sessionHandler;
    private WebSessionElementSearcher searcher;
    private WebSessionClickHandler clickHandler;

    private static final String REGISTRATION_SELECTOR = "a[href='/PROD/twbkwbis.P_GenMenu?name=bmenu.P_RegMnu']";
    private static final String SEARCH_LINK_SELECTOR = "a[href='/PROD/bwskfcls.p_sel_crse_search']";
    private static final String SELECT_SUBJECT_SELECTOR = "select[id='subj_id']";
    private static final String TERM_SELECT_BUTTON_SELECTOR = "input[value='p_term']";
    private static final String COURSE_SEARCH_BUTTON_SELECTOR = "input[value='Course Search']";
    private static final String VIEW_SECTIONS_BUTTON_SELECTOR = "input[value='ViewSections']";
    private static final String GET_PARENT_SCRIPT = "return arguments[0].parentNode;";

    public BlueGoldPageNavigator(BGWebSessionHandler handler) {
        sessionHandler = handler;
        searcher = handler.getSearcher();
        clickHandler = handler.getClickHandler();
    }

    void getToRegistration() throws NavigatorFailure {
        try {
            sessionHandler.logIn();
            clickHandler.tryToClickLink(REGISTRATION_SELECTOR);
        } catch(LoginFail | NoSuchElementException e) {
            throw new NavigatorFailure();
        }
    }

    void goToCourseSearch () throws DriverFailure {
        clickHandler.tryToClickLink(SEARCH_LINK_SELECTOR);
    }

    void selectTheSemester() throws SelectionFailure {
        String semesterValue = sessionHandler.getValueForSemester();
        WebElement termSelector = sessionHandler.getSelectorByClassName("p_term");
        Selection selectTerm = new Selection(termSelector, semesterValue);
        sessionHandler.select(selectTerm, TERM_SELECT_BUTTON_SELECTOR);
    }

    void selectTheSubject(String subject) throws NavigatorFailure {
        try {
            WebElement subjectSelector = sessionHandler.getSelectorByCssSelector(SELECT_SUBJECT_SELECTOR);
            Selection subjectSelection = new Selection(subjectSelector, subject);
            sessionHandler.select(subjectSelection, COURSE_SEARCH_BUTTON_SELECTOR);
        } catch (NoSuchElementException | SelectionFailure e) {
            throw new NavigatorFailure();
        }

    }

    void viewClassSections(String className) throws NavigatorFailure {
        try {
            String courseTdTextPath = "//td[contains(text(),'" + className + "')]";
            WebElement courseTD = searcher.searchByXPath(courseTdTextPath);
            WebElement courseRow = getParent(courseTD);
            clickHandler.tryToClickButton(courseRow, VIEW_SECTIONS_BUTTON_SELECTOR);
        } catch (NoSuchElementException e){
            throw new NavigatorFailure();
        }
    }

    void returnToRegistration() throws NavigatorFailure {
        try {
            clickHandler.tryToClickLink(REGISTRATION_SELECTOR);
        } catch (NoSuchElementException e) {
            throw new NavigatorFailure();
        }
    }

    private WebElement getParent(WebElement element) throws NavigatorFailure {
        try {
            return sessionHandler.executeScript(GET_PARENT_SCRIPT, element);
        } catch (NoSuchElementException e){
            throw new NavigatorFailure();
        }
    }
}
