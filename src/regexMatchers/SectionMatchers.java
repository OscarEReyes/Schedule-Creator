package regexMatchers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Oscar Reyes on 4/18/2017.
 * Contains all methods needed to get Matcher objects to find information relevant to a class inside of a classInformation
 * String.
 *
 */

public class SectionMatchers {
    private static String NO_PRECEDING_NUMBERS = "(?<!\\d)";
    private static String NO_FOLLOWING_NUMBERS = "(?!\\d)";

    public static Matcher getCRNMatcher(String classInfo) {
        String VALID_CRN = "\\d{5}";

        String crnPattern = NO_PRECEDING_NUMBERS + VALID_CRN + NO_FOLLOWING_NUMBERS;
        return Pattern.compile(crnPattern).matcher(classInfo);
    }

    public static Matcher getSectionMatcher(String classInfo) {
        String VALID_SECTION_STRING = "\\p{Alnum}{3}";

        String sectionPattern = NO_PRECEDING_NUMBERS + VALID_SECTION_STRING + NO_FOLLOWING_NUMBERS;
        return Pattern.compile(sectionPattern).matcher(classInfo);
    }

    public static Matcher getSpacesLeftMatcher(String classInfo) {
        String PRECEDED_BY_HOUR = "(?<=m\\s)";
        String VALID_SPACE_NUMBER = "(\\d{1,2})";
        String FOLLOWED_BY_HOUR = "(?<=m\\s)";
        String spacesLeftPattern = PRECEDED_BY_HOUR + VALID_SPACE_NUMBER + FOLLOWED_BY_HOUR;
        return Pattern.compile(spacesLeftPattern).matcher(classInfo);
    }

    public static Matcher getProfMatcher(String classInfo) {
        String anything = ".+?";
        String PRECEDED_BY_SPACES_LEFT = "(?<=\\s\\d{1,2}?\\s)";
        String FOLLOWED_BY_PROFESSOR_MARK = "(?=\\s\\p{Punct}P\\p{Punct})";
        String profPattern = PRECEDED_BY_SPACES_LEFT + anything + FOLLOWED_BY_PROFESSOR_MARK;

        return Pattern.compile(profPattern).matcher(classInfo);
    }

    public static Matcher getLocationMatcher(String classInfo) {
        String PRECEDED_BY_PROFESSOR_MARK = "(?<=\\s\\p{Punct}P\\p{Punct})";
        String locationPattern = PRECEDED_BY_PROFESSOR_MARK + ".+";
        return Pattern.compile(locationPattern).matcher(classInfo);
    }
}
