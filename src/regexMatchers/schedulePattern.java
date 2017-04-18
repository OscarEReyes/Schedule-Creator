package regexMatchers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Oscar Reyes on 4/18/2017.
 */
public class schedulePattern {
    public static Matcher getHoursMatcher(String classInfo) {
        String hour = "(\\d{2}.\\d{2}\\s\\p{Lower}{2})";
        String hoursPattern = hour + "-" + hour;
        return Pattern.compile(hoursPattern).matcher(classInfo);
    }
    public static Matcher getDaysMatcher(String classInfo) {
        String legalDaysRepr = "(\\p{Upper}{1,4})";
        String precededBySpace = "(?<=\\s)";
        String followedByHour = "(?=\\s\\d{2}.\\d{2}\\s\\p{Lower}{2})";

        String daysPattern = precededBySpace + legalDaysRepr + followedByHour;
        return Pattern.compile(daysPattern).matcher(classInfo);
    }
}
