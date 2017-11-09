package webScraping;

import siteClasses.Semester;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Oscar Reyes on 4/21/2017.
 * Class for methods which return values which will be used for finding elements on a page.
 */
public class ValueGetter {
    public static String getSemesterValue(Semester semester) {
        Map<String, String> codeMap;
        codeMap = getSeasonCodeMap();
        String season = semester.getSeason();
        String elementCode = getSeasonCode(season, semester);

        return elementCode + codeMap.get(season);
    }

    private static String getSeasonCode(String season, Semester semester) {
        String seasonYear;
        seasonYear = semester.getYear();
        if (season.equals("Fall") || season.equals("Winter")) {
            int seasonCode = Integer.parseInt(seasonYear) + 1;
            return String.valueOf(seasonCode);
        } else {
            return seasonYear;
        }
    }

    private static Map<String, String> getSeasonCodeMap() {
        Map<String, String> codeMap = new HashMap<>();
        codeMap.put("Fall", "10");
        codeMap.put("Winter Intersession", "15");
        codeMap.put("Spring Intersession", "25");
        codeMap.put("Summer Intersession", "35");
        codeMap.put("Summer", "30");
        codeMap.put("Spring", "20");
        return codeMap;
    }
}
