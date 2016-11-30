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
		hourMap.put("8:00 am", 8);
		hourMap.put("9:00 am", 9);
		hourMap.put("10:00 am", 10);
		hourMap.put("11:00 am", 11);
		hourMap.put("12:00 pm", 12);
		hourMap.put("1:00 pm", 13);
		hourMap.put("2:00 am", 14);
		hourMap.put("3:00 am", 15);
		hourMap.put("4:00 am", 16);
		hourMap.put("5:00 am", 17);
		hourMap.put("6:00 pm", 18);
		hourMap.put("7:00 pm", 19);
		hourMap.put("8:00 pm", 20);
		hourMap.put("9:00 pm", 21);
	}

	public void generateSchedule(ObservableList<CollegeCourse> collegeCourseData) throws IOException, InterruptedException {
		for (CollegeCourse course: collegeCourseData) {
			// Find classes for course and set them
			String[] classes = getCourseClasses(course);
			List<CourseInstance> courseClasses = new ArrayList<CourseInstance>();
			for (String c : classes) {
				if (c.length() > 1 && c.charAt(0) != 'C' && Character.isDigit(c.charAt(9))) {
					CourseInstance courseClass = createCourseInstance(c, course);
					String startTime = courseClass.getSchedule().getClassHours().substring(0,8);
					String endTime = courseClass.getSchedule().getClassHours().substring(9);
					
					if (hourMap.get(startTime) >= hourMap.get(preferences.getStartTime()) &&
							hourMap.get(endTime) >= hourMap.get(preferences.getEndTime())) {
						if (courseClass.getClassProf() == course.getPrefProf()) {
							course.setPrefProfAvailable(true);
						}
						courseClasses.add(courseClass);
					}
				}
			}

			course.setCourseClasses(courseClasses);
		}

	}
	public void makeSchedule(ObservableList<CollegeCourse> collegeCourseData) {
		List<CollegeCourse> priorityCourses = new ArrayList<CollegeCourse>();
		List<CollegeCourse> medPriorityCourses = new ArrayList<CollegeCourse>();
		List<CollegeCourse> lowPriorityCourses = new ArrayList<CollegeCourse>();

		for (CollegeCourse course: collegeCourseData) {
			int courseClassesSize = course.getCourseClasses().size();
			if (course.getPrefProfAvailable() == true || courseClassesSize == 1) {
				priorityCourses.add(course);
			}else if (courseClassesSize >= 2 && courseClassesSize <= 4) {
				medPriorityCourses.add(course);
			} else {
				lowPriorityCourses.add(course);
			}
		}
	}

	public CourseInstance createCourseInstance(String c, CollegeCourse course) {
		String crn = c.substring(3, 8);
		String section = c.substring(9, 12);
		String days = c.substring(13,16);
		String hours = c.substring(17,34);
		String spacesLeft = c.substring(35, c.indexOf(" ", 36));
		String prof = c.substring(c.indexOf(" ", 36) ,c.indexOf("(", 40) - 1);
		String date = c.substring(c.indexOf("(", 40) - 1, c.indexOf("-", 40) + 6);
		String location = c.substring(c.indexOf("-", 40) + 8);

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
		results = imAn.analyzeImage(imageFile);
		String[] classes = results.split("\\r?\\n");
		return classes;
	}

	public static class SchedulePlannerBuilder {
		private User user;
		private Semester semester;
		private Preferences preferences;


		public SchedulePlannerBuilder(Preferences preferences){
			this.preferences = preferences;
		}

		public SchedulePlannerBuilder courseDepartment(User user){
			this.user = user;
			return this;
		}

		public SchedulePlannerBuilder courseNumber(Semester semester){
			this.semester = semester;
			return this;
		}


		public SchedulePlanner build(){
			return new SchedulePlanner(this);
		}
	}
}
