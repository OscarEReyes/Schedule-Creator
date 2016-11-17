package scheduleCreator.model;

public class CourseIdentity 
{
	private final String courseDepartment;
	private final String courseNumber;
	private final String courseName;
	
	public CourseIdentity(final String department, final String number,
           final String name)
	{
		this.courseDepartment = department;
		this.courseNumber = number;
		this.courseName = name;
	}
	
	public String getCourseDepartment()
	{
		return this.courseDepartment;
	}
	
	public String getCourseNumber()
	{
		return this.courseNumber;
	}
	
	public String getCourseName()
	{
		return this.courseName;
	}
}
