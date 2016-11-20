package scheduleCreator.model;

public class CourseInstance extends CollegeCourse {
	private final Schedule schedule; 
	private final int placesLeft;
	private final String classCrn;
	private final String classSection;
	private final String classProf;
	
	//optional
	private CourseInstance lab;
	
	public CourseInstance(CourseInstanceBuilder builder){
		this.schedule = builder.schedule;
		this.placesLeft = builder.placesLeft;
		this.classCrn = builder.classCrn;
		this.classSection = builder.classSection;
		this.classProf = builder.classProf;
		this.lab = builder.lab;
	}
	
	public static class CourseInstanceBuilder{
			private Schedule schedule;
			private int placesLeft;
			private String classCrn;
			private String classSection;
			private String classProf;
			private CourseInstance lab;
			
			public CourseInstanceBuilder(final Schedule schedule){	
				this.schedule = schedule;
			}	
			
			public CourseInstanceBuilder placesLeft(int placesLeft){
				this.placesLeft = placesLeft;
				return this;
			}
			public CourseInstanceBuilder classCrn(String classCrn){
				this.classCrn = classCrn;
				return this;
			}
	
			public CourseInstanceBuilder classSection(String classSection){
				this.classSection = classSection;
				return this;
			}
			
			public CourseInstanceBuilder classProf(String classProf){
				this.classProf = classProf;
				return this;
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
