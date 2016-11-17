package scheduleCreator.model;

public class Schedule 
{
	private final String classDays;
	private final String classHours;
	private final String classLocation;
	
	public Schedule(final String days, final String hours, final String location)
	{
		this.classDays = days;
		this.classHours = hours;
		this.classLocation = location;
	}
}
