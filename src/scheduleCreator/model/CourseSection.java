package scheduleCreator.model;


public class CourseSection {
    private final Course course;
    private final String crn;
    private final String section;
    private final String professor;
    private final int placesLeft;
    private final Schedule schedule;
    private CourseSection connection = null;

    CourseSection(CourseSectionBuilder builder) {
        this.schedule = builder.schedule;
        this.placesLeft = builder.placesLeft;
        this.crn = builder.classCrn;
        this.section = builder.classSection;
        this.professor = builder.classProfessor;
        this.course = builder.courseInfo;
        this.connection = builder.connection;
    }

    public String toString() {
        return course.getCourseName();
    }

    public Boolean hasPreferredProfessor() {
        String preferredProfessor = course.getPreferredProf();
        return professor.equals(preferredProfessor);
    }

    public CourseSection getConnection() {
        return this.connection;
    }

    public Course getCourse() {
        return this.course;
    }

    public String getCrn() {
        return this.crn;
    }

    public String getSection() {
        return this.section;
    }

    public int getPlacesLeft() {
        return this.placesLeft;
    }

    public Schedule getSchedule() {
        return this.schedule;
    }

    public String getProfessor() {
        return this.professor;
    }


    public static class CourseSectionBuilder {
        private final Schedule schedule;
        private int placesLeft;
        private String classCrn;
        private String classSection;
        private String classProfessor;
        private Course courseInfo;
        private CourseSection connection = null;

        public CourseSectionBuilder(final Schedule schedule) {
            this.schedule = schedule;
        }

        public CourseSectionBuilder placesLeft(int placesLeft) {
            this.placesLeft = placesLeft;
            return this;
        }

        public CourseSectionBuilder crn(String classCrn) {
            if (classCrn != null) {
                this.classCrn = classCrn;
            } else {
                this.classCrn = "Not Available";
            }
            return this;
        }

        public CourseSectionBuilder section(String classSection) {
            this.classSection = classSection;
            return this;
        }

        public CourseSectionBuilder professor(String professor) {
            if (!professor.equals("")) {
                this.classProfessor = professor;
            } else {
                this.classProfessor = "Not Available";
            }
            return this;
        }

        public CourseSectionBuilder course(Course course) {
            this.courseInfo = course;
            return this;
        }

        public CourseSectionBuilder connection(CourseSection section) {
            this.connection = connection;
            return this;
        }

        public CourseSection build() {
            return new CourseSection(this);
        }
    }
}
