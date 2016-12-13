package scheduleCreator.model;

public class CourseClass {
	private final Schedule schedule; 
	private final int placesLeft;
	private final String classCrn;
	private final String classSection;
	private final String classProf;
	private final Course courseInfo;
	
	//optional
	private CourseClass lab;
	
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
	
	public int getStartTime() {
		return this.schedule.getStartTime();
	}
	
	public int getEndTime() {
		return this.schedule.getEndTime();
	}
	
	public Course getCourseInfo() {
		return this.courseInfo;
	}
	
	public CourseClass(CourseInstanceBuilder builder) {
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
			
			public CourseInstanceBuilder(final Schedule schedule) {	
				this.schedule = schedule;
			}	
			
			public CourseInstanceBuilder placesLeft(int placesLeft) {
				this.placesLeft = placesLeft;
				return this;
			}
			
			public CourseInstanceBuilder classCrn(String classCrn) {
				this.classCrn = classCrn;
				return this;
			}
	
			public CourseInstanceBuilder classSection(String classSection) {
				this.classSection = classSection;
				return this;
			}
			
			public CourseInstanceBuilder classProf(String classProf) {
				this.classProf = classProf;
				return this;
			}
			
			public CourseInstanceBuilder courseInfo(Course courseInfo) {
				this.courseInfo = courseInfo;
				return this;
			}
	
		
			public CourseInstanceBuilder lab(CourseClass lab) {
				this.lab = lab;
				return this;
			}
			
			public CourseClass build() {
				return new CourseClass(this);
			}
	}
}
