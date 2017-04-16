package scheduleCreator.model;


public class CourseClass {
	private final Schedule schedule; 
	private final int placesLeft;
	private final String classCrn;
	private final String classSection;
	private final String classProf;
	private final Course courseInfo;
	
	//optional
	private CourseClass lab = null;
	
	// Getters
	public Schedule getSchedule() {
		return this.schedule;
	}
	
	public int getPlacesLeft() {
		return this.placesLeft;
	}
	
	public String getClassCrn() {
		return this.classCrn;
	}
	
	public String getClassSection() {
		return this.classSection;
	}
	
	public String getClassProf() {
		return this.classProf;
	}
	
	int getStartTime() {
		return this.schedule.getStartTime();
	}
	
	int getEndTime() {
		return this.schedule.getEndTime();
	}
	
	String getDays() {
		return this.getSchedule().getClassDays();
	}

	public String toString() {
	    return courseInfo.toString() + " " + schedule.toString();
    }
	public Course getCourseInfo() {
		return this.courseInfo;
	}
	
	public CourseClass getLab() {
		return this.lab;
	}


	
	CourseClass(CourseInstanceBuilder builder) {
		this.schedule = builder.schedule;
		this.placesLeft = builder.placesLeft;
		this.classCrn = builder.classCrn;
		this.classSection = builder.classSection;
		this.classProf = builder.classProf;
		this.courseInfo = builder.courseInfo;
		this.lab = builder.lab;
	}
	
	public static class CourseInstanceBuilder {
			private Schedule schedule;
			private int placesLeft;
			private String classCrn;
			private String classSection;
			private String classProf;
			private Course courseInfo;
			private CourseClass lab;
			
			CourseInstanceBuilder(final Schedule schedule) {
				this.schedule = schedule;
			}	
			
			CourseInstanceBuilder placesLeft(int placesLeft) {
                this.placesLeft = placesLeft;
                return this;
			}
			
			CourseInstanceBuilder classCrn(String classCrn) {
				if (classCrn != null) {
					this.classCrn = classCrn;
				} else { 
					this.classCrn = "Not Available";
				}
				return this;
			}
	
			CourseInstanceBuilder classSection(String classSection) {
				this.classSection = classSection;
				return this;
			}
			
			CourseInstanceBuilder classProf(String classProf) {
				if (classProf != null) {
                    this.classProf = classProf;
                } else {
				    this.classProf = "Not Available";
                }
				return this;
			}
			
			CourseInstanceBuilder courseInfo(Course courseInfo) {
				this.courseInfo = courseInfo;
				return this;
			}

			public CourseInstanceBuilder lab(CourseClass lab) {
				this.lab = lab;
				return this;
			}
			
			CourseClass build() {
				return new CourseClass(this);
			}
	}
}
