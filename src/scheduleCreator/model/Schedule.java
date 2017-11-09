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
		
		this.startTime = Integer.parseInt(hours.substring(0, 2).concat(hours.substring(3,5)));
		
		this.endTime = Integer.parseInt(hours.substring(9, 11).concat(hours.substring(12,14)));

		if (startTime != 1200 && hours.substring(6,8).equals("pm")) {
			this.startTime += 1200;
		}
		
		if (startTime != 1200 && hours.substring(15).equals("pm")) {
			this.endTime += 1200;
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

	public Boolean scheduleIntervenes(Schedule s2) {
		int s2StartTime = s2.getStartTime();
		int s2EndTime = s2.getEndTime();

		if (haveDaysInCommon(s2)) {
            if (startTime <= s2StartTime && endTime >= s2EndTime) {
                return true;
            } else if (endTime > s2StartTime && endTime < s2EndTime) {
                return true;
            } else if (startTime > s2StartTime && startTime < s2EndTime) {
                return true;
            }
        }
		return false;
	}

	private Boolean haveDaysInCommon(Schedule s2) {
	    for(char c: classDays.toCharArray()) {
	        String s2Days = s2.getClassDays();
	        if (s2Days.contains(String.valueOf(c))) {
	            return true;
            }
        }
        return false;
    }
	
}
