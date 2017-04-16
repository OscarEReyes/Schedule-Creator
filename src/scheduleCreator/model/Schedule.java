package scheduleCreator.model;

public class Schedule {
	private final String classDays;
	private final String classHours;
	private final String classLocation;
	private int startTime;
	private int endTime;
	
	public Schedule(final String days, final String hours, final String location) {
		this.classDays = days;
		this.classHours = hours;
		this.classLocation = location;
		
		this.startTime = (int) Integer.parseInt(this.classHours.substring(0, 2)) * 60 + 
				(int) Integer.parseInt(this.classHours.substring(3, 5));
		
		this.endTime = (int) Integer.parseInt(this.classHours.substring(9, 11)) * 60 + 
				(int) Integer.parseInt(this.classHours.substring(12,14));
		
		if (this.classHours.substring(6,8).equals("pm")) {
			this.startTime += 720;
		}
		
		if (this.classHours.substring(15).equals("pm")) {
			this.endTime += 720;
		}
		
	}
	
	String getClassDays() {
		return this.classDays;
	}
	
	public String getClassHours() {
		return this.classHours;
	}
	
	public String getClassLocation() {
		return this.classLocation;
	}
	
	int getStartTime() {
		return this.startTime;
	}
	
	int getEndTime() {
		return this.endTime;
	}
	
	public String toString() {
		return "Days: " + classDays + 
					 " Hours:" + classHours +
					 " Location: " + classLocation;
	}
	
}
