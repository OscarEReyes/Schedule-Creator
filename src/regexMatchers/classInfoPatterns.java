package regexMatchers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Oscar Reyes on 4/18/2017.
 * Contains all methods needed to get Matcher objects to find information relevant to a class inside of a classInfo
 * String.
 *
 */
public class classInfoPatterns {
    private static String noPrecedingNumbers = "(?<!\\d)";
    private static String noFollowingNumbers = "(?!\\d)";

    /**
     * Returns Matcher object capable of matching CRN patterns.
     * @param classInfo classInfo string representing class information.
     * @return
     */
    public static Matcher getCRNMatcher(String classInfo) {
        String noFollowingNumbers = "(?!\\d)";
        // The pattern consists of 5 digits with no preceding numbers and no numbers directly after them.
        String crnPattern = noPrecedingNumbers + "\\d{5}" + noFollowingNumbers;

        return Pattern.compile(crnPattern).matcher(classInfo);
    }

    /**
     * Returns Matcher object capable of matching section patterns.
     * @param classInfo classInfo string representing class information.
     * @return
     */
    public static Matcher getSectionMatcher(String classInfo) {
        // The pattern consists of 3 alphanumeric symbols with no preceeding numbers or numbers directly after them.
        String sectionPattern = noPrecedingNumbers + "\\p{Alnum}{3}" + noFollowingNumbers;

        return Pattern.compile(sectionPattern).matcher(classInfo);
    }

    /**
     * Returns Matcher object capable of matching the pattern of the substring that denotes the amount of spaces left.
     * @param classInfo classInfo string representing class information.
     * @return
     */
    public static Matcher getSpacesLeftMatcher(String classInfo) {
        String precededByHour = "(?<=m\\s)";
        String followedByProf = "(?=\\s\\p{Upper})";
        // The pattern consists of one or two digits, which are are surrounded by a space from both sides
        // the left space is preceeded by an hour and the space after the digits is followed by a professors name.
        // Something like " 12:00am 10 Dr. Some Professor
        String spacesLeftPattern = precededByHour + "(\\d{1,2})" + followedByProf;

        return Pattern.compile(spacesLeftPattern).matcher(classInfo);
    }

    /**
     * Returns Matcher object capable of matching professor patterns.
     * @param classInfo classInfo string representing class information.
     * @return
     */
    public static Matcher getProfMatcher(String classInfo) {
        String precededBySpacesLeft = "(?<=\\s\\d{1,2}?\\s)";
        String anything = ".+?";
        String followedByProfMark = "(?=\\s\\p{Punct}P\\p{Punct})"; // "(P)"
        // The pattern consists of the spaces left pattern followed by anythind and then the Professor mark. "(P)"
        String profPattern = precededBySpacesLeft + anything + followedByProfMark;

        return Pattern.compile(profPattern).matcher(classInfo);
    }

    /**
     * Returns Matcher object capable of matching  patterns.
     * @param classInfo classInfo string representing class information.
     * @return
     */


    public static Matcher getLocationMatcher(String classInfo) {
        String precededByProfMark = "(?<=\\s\\p{Punct}P\\p{Punct})";
        // Finds profMark and reads till EOL
        String locationPattern = precededByProfMark + ".+";

        return Pattern.compile(locationPattern).matcher(classInfo);
    }
}
