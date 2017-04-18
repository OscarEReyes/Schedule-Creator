package siteClasses;

/**
 * Created by Oscar Reyes on 4/16/2017.
 */
public class Semester {
    private final String season;
    private final String year;

    public Semester(final String season, final String year){
        this.season = season;
        this.year = year;
    }

    public String getSeason() {
        return this.season;
    }

    public String getYear() {
        return this.year;
    }


}
