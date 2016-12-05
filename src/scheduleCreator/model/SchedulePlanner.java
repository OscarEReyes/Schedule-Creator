package scheduleCreator.model;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javafx.collections.ObservableList;
import ocr.ImageAnalyzer;
import ocr.WebScraper;
import scheduleCreator.view.LoginDialogController.User;
import scheduleCreator.view.PreferenceDialogController.Preferences;
import scheduleCreator.view.SemesterDialogController.Semester;

public class SchedulePlanner {
	private User user;
	private Semester semester;
	private Preferences preferences;
	private HashMap<String, Integer> hourMap = new HashMap<String, Integer>(); 


	public SchedulePlanner(SchedulePlannerBuilder builder){
		this.user = builder.user;
		this.semester = builder.semester;
		this.preferences = builder.preferences;
		hourMap.put("08:00 am", 8);
		hourMap.put("08:50 am", 8);
		hourMap.put("09:00 am", 9);
		hourMap.put("09:15 am", 9);
		hourMap.put("09:30 am", 9);
		hourMap.put("09:50 am", 9);
		hourMap.put("10:00 am", 10);
		hourMap.put("10:15 am", 10);
		hourMap.put("10:30 am", 10);
		hourMap.put("10:50 am", 10);
		hourMap.put("11:00 am", 11);
		hourMap.put("11:15 am", 11);
		hourMap.put("11:30 am", 11);
		hourMap.put("11:50 am", 11);
		hourMap.put("12:00 pm", 12);
		hourMap.put("12:15 pm", 12);
		hourMap.put("12:30 pm", 12);
		hourMap.put("12:50 pm", 12);
		hourMap.put("01:00 pm", 13);
		hourMap.put("01:15 pm", 13);
		hourMap.put("01:30 pm", 13);
		hourMap.put("01:50 pm", 13);
		hourMap.put("02:30 pm", 14);
		hourMap.put("02:00 pm", 14);
		hourMap.put("02:50 pm", 14);
		hourMap.put("03:00 pm", 15);
		hourMap.put("03:30 pm", 15);
		hourMap.put("03:50 pm", 15);
		hourMap.put("04:00 pm", 16);
		hourMap.put("04:45 pm", 16);
		hourMap.put("04:50 pm", 16);
		hourMap.put("05:00 pm", 17);
		hourMap.put("05:50 pm", 17);
		hourMap.put("06:00 pm", 18);
		hourMap.put("08:50 pm", 20);
		hourMap.put("09:00 pm", 21);
	}

	public void generateSchedule(ObservableList<CollegeCourse> collegeCourseData) throws IOException, InterruptedException {
		for (CollegeCourse course: collegeCourseData) {
			// Find classes for course and set them
			String[] classes = getCourseClasses(course);
			List<CourseInstance> courseClasses = new ArrayList<CourseInstance>();
			for (String c : classes) {
				System.out.println(c);
				if (c.length() > 1 && !c.substring(0,1).equals("C") && Character.isDigit(c.charAt(9))) {
					CourseInstance courseClass = createCourseInstance(c, course);
					String startTime;
					startTime = courseClass.getSchedule().getClassHours().substring(0,8);
					String endTime;
					endTime = courseClass.getSchedule().getClassHours().substring(9);				
					
					if (hourMap.get(startTime) >= hourMap.get(preferences.getStartTime()) &&
							hourMap.get(endTime) >= hourMap.get(preferences.getEndTime())) {
						
						if (courseClass.getClassProf().equals(course.getPrefProf())) {
							course.setPrefProfAvailable(true);
						}
						courseClasses.add(courseClass);
					}
				}
			}

			course.setCourseClasses(courseClasses);
		}
		makeSchedule(collegeCourseData);

	}
	public void makeSchedule(ObservableList<CollegeCourse> collegeCourseData) {
		List<CourseInstance> scheduledClasses = new ArrayList<CourseInstance>();
		List<CollegeCourse> priorityCourses = new ArrayList<CollegeCourse>();
		List<CollegeCourse> highPriorityCourses = new ArrayList<CollegeCourse>();
		List<CollegeCourse> medPriorityCourses = new ArrayList<CollegeCourse>();
		List<CollegeCourse> lowPriorityCourses = new ArrayList<CollegeCourse>();
		
		List<ScheduleDay> takenTimes = new ArrayList<ScheduleDay>();
		for (CollegeCourse course: collegeCourseData) {
			int courseClassesSize = course.getCourseClasses().size();
			if (course.getPrefProfAvailable() == true) {
				priorityCourses.add(course);
			} else if (courseClassesSize == 1){
				highPriorityCourses.add(course);
			}else if (courseClassesSize >= 2 && courseClassesSize <= 4) {
				medPriorityCourses.add(course);
			} else {
				lowPriorityCourses.add(course);
			}
		}
		
		scheduledClasses = lookForClass(highPriorityCourses, takenTimes, scheduledClasses);

		for (CollegeCourse course: priorityCourses) {
			for (CourseInstance courseClass: course.getCourseClasses()) {
				String startTime = courseClass.getSchedule().getClassHours().substring(0,8);
				String endTime = courseClass.getSchedule().getClassHours().substring(9);
				
				int sTime = hourMap.get(startTime);
				int eTime = hourMap.get(endTime);
				if (courseClass.getClassProf().equals(course.getPrefProf()) &&
						takenTimes.size() == 0 || timeConflictExists(takenTimes, sTime, eTime,
								courseClass.getSchedule().getClassDays()) == false) {
						scheduledClasses.add(courseClass);
						takenTimes.add(new ScheduleDay(courseClass.getSchedule().getClassDays(), sTime, eTime));

				}
				scheduledClasses = lookForClass(medPriorityCourses, takenTimes, scheduledClasses);
				scheduledClasses = lookForClass(lowPriorityCourses, takenTimes, scheduledClasses);

			}
		}
		
		for (CourseInstance courseClass: scheduledClasses) {
			System.out.println(courseClass.getClassCrn());
		}
	}
	
	public List<CourseInstance> lookForClass(List<CollegeCourse> courses, 
			List<ScheduleDay> takenTimes, List<CourseInstance> scheduledClasses) {
		for (CollegeCourse course: courses) {
			for (CourseInstance courseClass: course.getCourseClasses()) {
				String startTime = courseClass.getSchedule().getClassHours().substring(0,8);
				String endTime = courseClass.getSchedule().getClassHours().substring(9);
				
				int sTime = hourMap.get(startTime);
				int eTime = hourMap.get(endTime);
				if (takenTimes.size() == 0 || timeConflictExists(takenTimes, sTime, eTime,
						courseClass.getSchedule().getClassDays()) == false) {
						scheduledClasses.add(courseClass);
						takenTimes.add(new ScheduleDay(courseClass.getSchedule().getClassDays(), sTime, eTime));
				}
			}
		}
		return scheduledClasses;
	}
	
	public boolean timeConflictExists(List<ScheduleDay> takenTimes, int startTime, int endTime, String days) {
		for (ScheduleDay sDay: takenTimes) {
			if ((sDay.getStartTime() >= startTime && sDay.getEndTime() <= endTime) &&
					sDay.getDays().equals(days)) {
				return true;
			}
		}
		return false;
	}


	public CourseInstance createCourseInstance(String c, CollegeCourse course) {
		String crn = c.substring(3, 8);
		String section = c.substring(9, 12);
		String days = c.substring(13,16);
		String hours = c.substring(c.indexOf(" ", 13) + 1 ,c.indexOf("m", 25) + 1);
		String prof = c.substring(c.indexOf(" ", 36) + 1 , c.indexOf("(", 40) - 1);
		String location = c.substring(c.indexOf(")") + 2);
		String spacesLeft = c.substring(c.indexOf("m", 25) + 2, c.indexOf(" ", 35));

		System.out.println("Class: " + crn + " " + section + " " + days + " " + hours + " " + prof + " " + location + " spaces left: " + spacesLeft );
		CourseInstance courseClass = new CourseInstance.
				CourseInstanceBuilder(new Schedule(days ,hours, location))
				.classCrn(crn)
				.placesLeft(Integer.parseInt(spacesLeft))
				.classSection(section)
				.classProf(prof)
				.courseInfo(course).build();
		return courseClass;
	}

	public String[] getCourseClasses(CollegeCourse course) throws IOException, InterruptedException {
		File imageFile;
		String results;
		WebScraper scraper = new WebScraper();
		ImageAnalyzer imAn = new ImageAnalyzer();
		imageFile = scraper.scrapeCoursePage(user, course, semester);
		if (imageFile != null) {
			results = imAn.analyzeImage(imageFile);
			String[] classes = results.split("\\r?\\n");
			return classes;
		} else {
			return null;
		}
	}

	public static class SchedulePlannerBuilder {
		private User user;
		private Semester semester;
		private Preferences preferences;


		public SchedulePlannerBuilder(Preferences preferences){
			this.preferences = preferences;
		}

		public SchedulePlannerBuilder user(User user){
			this.user = user;
			return this;
		}

		public SchedulePlannerBuilder semester(Semester semester){
			this.semester = semester;
			return this;
		}


		public SchedulePlanner build(){
			return new SchedulePlanner(this);
		}
	}
	
	private class ScheduleDay {
		private final String days;
		private final int startTime;
		private final int endTime;
		
		ScheduleDay(String days, int startTime, int endTime) {
			this.days = days;
			this.startTime = startTime;
			this.endTime = endTime;
		}
		
		public String getDays() {
			return days;
		}
		
		public int getStartTime() {
			return startTime;
		}
		
		public int getEndTime() {
			return endTime;
		}
		
	}
}
