package scheduleCreator.model;

public class CollegeCourse {
	private final CourseIdentity courseInfo;
	private final int creditHours;
	
	public CollegeCourse(final CourseIdentity identity){
		this.courseInfo = identity;
		this.creditHours = identity.getCourseNumber().charAt(1);
	}
	
}
