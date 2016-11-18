package scheduleCreator.model;

public class CourseInstance extends CollegeCourse {
	private final Schedule schedule; 
	private final int placesLeft;
	private final String classCrn;
	private final String classSection;
	private final String classProf;
	private final CourseIdentity courseInfo;
	
	//optional
	private CourseInstance lab;
	
	public CourseInstance(CourseInstanceBuilder builder){
		super(builder.courseInfo);
		this.schedule = builder.schedule;
		this.placesLeft = builder.placesLeft;
		this.classCrn = builder.classCrn;
		this.classSection = builder.classSection;
		this.classProf = builder.classProf;
		this.lab = builder.lab;
		this.courseInfo = builder.courseInfo;
	}
	
	public static class CourseInstanceBuilder{
			private final Schedule schedule;
			private final int placesLeft;
			private final String classCrn;
			private final String classSection;
			private final String classProf;
			private CourseInstance lab;
			private final CourseIdentity courseInfo;
			
			public CourseInstanceBuilder(
					final Schedule schedule,
					final int placesLeft,
					final String classCrn,
					final String classSection,
					final String classProf,
					final CourseIdentity courseInfo){	
			this.schedule = schedule;
			this.placesLeft = placesLeft;
			this.classCrn = classCrn;
			this.classSection = classSection;
			this.classProf = classProf;
			this.courseInfo = courseInfo;
	}
			
	
		public CourseInstanceBuilder lab(CourseInstance lab){
			this.lab = lab;
			return this;
		}
		
		public CourseInstance build(){
			return new CourseInstance(this);
		}
	}
}
