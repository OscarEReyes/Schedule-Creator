package regexMatchers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Oscar Reyes on 4/18/2017.
 */
public class ScheduleMatchers {
    private static final String HOUR_PATTERN = "(\\d{2}.\\d{2}\\s\\p{Lower}{2})";
    private static final String VALID_DAY_CODE = "(\\p{Upper}{1,4})";                        // Ex: "M", "MW", "MWF"
    private static final String PRECEDED_BY_SPACE = "(?<=\\s)";
    private static final String FOLLOWED_BY_HOUR = "(?=\\s\\d{2}.\\d{2}\\s\\p{Lower}{2})";

    public static Matcher getHoursMatcher(String classInfo) {
        String hoursPattern = HOUR_PATTERN + "-" + HOUR_PATTERN;
        return Pattern.compile(hoursPattern).matcher(classInfo);
    }
    public static Matcher getDaysMatcher(String classInfo) {
        String daysPattern = PRECEDED_BY_SPACE + VALID_DAY_CODE + FOLLOWED_BY_HOUR;
        return Pattern.compile(daysPattern).matcher(classInfo);
    }
}
