package scheduleCreator.model;

import regexMatchers.ScheduleMatchers;
import regexMatchers.SectionMatchers;

import java.util.regex.Matcher;

/**
 * Created by Oscar Reyes on 7/14/2017.
 */

class SectionExtractor {
        private String sectionString;
        private String crn;
        private String professor;
        private Matcher sectionMatcher;
        private Course course;

        SectionExtractor(Course course, String sectionString) {
            this.course = course;
            this.sectionString = sectionString;

            Matcher crnMatcher = SectionMatchers.getCRNMatcher(sectionString);
            Matcher professorMatcher = SectionMatchers.getProfMatcher(sectionString);

            crn = findTextWithMatcher(crnMatcher);
            professor = findTextWithMatcher(professorMatcher);
            sectionMatcher = SectionMatchers.getSectionMatcher(sectionString);
        }

        CourseSection createCourseSection() {
            Schedule sectionSchedule = getSectionSchedule();
            return new CourseSection.CourseSectionBuilder(sectionSchedule)
                    .section(getSection())
                    .crn(crn)
                    .professor(professor)
                    .course(course)
                    .placesLeft(getPlacesLeft())
                    .build();
        }

        private Schedule getSectionSchedule() {
            Matcher daysMatcher = ScheduleMatchers.getDaysMatcher(sectionString);
            Matcher hoursMatcher = ScheduleMatchers.getHoursMatcher(sectionString);
            Matcher locationMatcher = SectionMatchers.getLocationMatcher(sectionString);

            String days = findTextWithMatcher(daysMatcher);
            String hours = findTextWithMatcher(hoursMatcher);
            String location = findTextWithMatcher(locationMatcher);
            return new Schedule(days, hours, location);
        }

        private String getSection() {
            String section = findTextWithMatcher(sectionMatcher);
            char firstCharacter = section.charAt(1);
            if (Character.isLetter(firstCharacter)) {
                return "invalid";
            } else {
                return section;
            }
        }

        private int getPlacesLeft() {
            Matcher placesLeftMatcher = SectionMatchers.getSpacesLeftMatcher(sectionString);

            if (placesLeftMatcher.find()) {
                return findPlacesLeft(placesLeftMatcher);
            }
            return -1;
        }

        private int findPlacesLeft(Matcher placesLeftMatcher) {
            try {
                return Integer.parseInt(placesLeftMatcher.group());
            } catch (NumberFormatException e) {
                return -1;
            }
        }

    private String findTextWithMatcher(Matcher matcher) {
            if (matcher.find()) {
                return matcher.group();
            }
            return "";
        }
}