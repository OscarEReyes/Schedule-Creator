package scheduleCreator.model;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

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

	public SchedulePlanner(SchedulePlannerBuilder builder){
		this.user = builder.user;
		this.semester = builder.semester;
		this.preferences = builder.preferences;
	}

	/**
	 * 
	 * @param courseList
	 * @return A list of CourseClass objects which represent the optimal classes for the schedule.
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public List<CourseClass> genSchedule(ObservableList<Course> courseList) 
			throws IOException, InterruptedException {
		for (Course course: courseList) {
			List<CourseClass> classList = new ArrayList<CourseClass>();
			String[] classes = getCourseClasses(course);
			addOpenClasses(classes, classList, course);
			course.setCourseClasses(classList);
		}
		List<CourseClass> chosenClasses = getOptimalClasses(courseList);
		return chosenClasses;
	}
	
	
	/**
	 * Checks if a college class is open based on its string of information,
	 * if it is open, it creates a CourseClass instance and adds it to the passed
	 * classList.
	 * @param classes
	 * @param classList
	 * @param course
	 */
	public void addOpenClasses(String[] classes, List<CourseClass> classList, Course course) {
		for (String c : classes) {
			String firstLetter = c.substring(0, 1);
			if (c.length() > 1 && !firstLetter.equals("C") && Character.isDigit(c.charAt(9))) {
				CourseClass courseClass = createCourseInstance(c, course);		
				classList.add(courseClass);
			}
		}
	}

	/**
	 * Creates a CourseClass objects and returns it.
	 * @param classInfoString
	 * @param course
	 * @return CourseClass object that gets its information from the string passed.
	 */
	public CourseClass createCourseInstance(String classInfoString, Course course) {
		ClassInformation cInfo = new ClassInformation(classInfoString);
		Schedule schedule = new Schedule(cInfo.getDays() ,cInfo.getHours(), cInfo.getLocation());
		
		CourseClass courseClass = new CourseClass.
				CourseInstanceBuilder(schedule)
				.classCrn(cInfo.getCRN())
				.placesLeft(Integer.parseInt(cInfo.getSpacesLeft()))
				.classSection(cInfo.getSection())
				.classProf(cInfo.getProf())
				.courseInfo(course).build();
		
		return courseClass;
	}

	
	/**
	 * Gets optimal classes
	 * @param collegeCourseData
	 * @return a List of CourseClass objects
	 */
	public List<CourseClass> getOptimalClasses(ObservableList<Course> courseList) {
		Stack<Course> priorityStack = new Stack<Course>();
		Stack<Course> nPriorityStack = new Stack<Course>();
		List<CourseClass> chosenClasses = new ArrayList<CourseClass>();
		List<ScheduledClass> takenTimes = new ArrayList<ScheduledClass>();
		
		// Sort list of Course Objects based on the size of their classes lists. 
		// From least to greatest.
		CourseSorter CourseSorter = new CourseSorter();
		CourseSorter.sort(courseList);

		for (Course course: courseList) {
			if (course.getCourseClasses().size() == 1 || course.getPrefProfAvailable()) {
				priorityStack.push(course);
			} else{
				nPriorityStack.push(course);
			}
		}
		
		//Iterate through stacks and as a class is chosen it is added to a list of CourseClasses
		iterPStack(priorityStack, takenTimes, chosenClasses);
		iterStack(nPriorityStack, takenTimes, chosenClasses);
	
		return chosenClasses;
	}
	
	
	/**
	 * Iterates over stack. Choosing a class for each course.
	 * @param stack
	 * @param takenTimes
	 * @param chosenClasses
	 */
	private void iterPStack(Stack<Course> stack, List<ScheduledClass> takenTimes,
			List<CourseClass> chosenClasses){
		while (!stack.isEmpty()) {
			CourseClass chosenClass;
			Course tmp = stack.pop(); 
			
			if (tmp.getPrefProfAvailable()) {
				chosenClass = getClassWithPrefProf(tmp);
			} else {
				chosenClass = getPrefClass(tmp, takenTimes);
			}
			
			chooseClass(chosenClasses, takenTimes, chosenClass);
		}
	}
	
	
	/**
	 * Iterates over the stack. Choosing classes for each course
	 * @param stack
	 * @param takenTimes
	 * @param chosenClasses
	 */
	private void iterStack(Stack<Course> stack, 
			List<ScheduledClass> takenTimes, List<CourseClass> chosenClasses){
		while (!stack.isEmpty()) {
			CourseClass chosenClass;
			Course tmp = stack.pop();
			chosenClass = getPrefClass(tmp, takenTimes);
			chooseClass(chosenClasses, takenTimes, chosenClass);
		}
	}
	
	
	/**
	 * Takes a CourseInstance and adds it to a list of CourseInstance objects 
	 * that represent the chosen classes. Also adds an ScheduledClass objects
	 * created with the passed CourseInstance used as its parameter.
	 * 
	 * @param chosenClasses
	 * @param takenTimes
	 * @param chosenClass
	 */
	private void chooseClass(List<CourseClass> chosenClasses, 
			List<ScheduledClass> takenTimes, CourseClass chosenClass) {
		chosenClasses.add(chosenClass);
		ScheduledClass time = new ScheduledClass(chosenClass);
		takenTimes.add(time);
	}
	
	
	/**
	 * Gets a class that fits in the schedule (Not in taken times)
	 * @param course
	 * @param takenTimes
	 * @return CourseClass Object with schedule that does not conflict.
	 */
	public CourseClass getPrefClass(Course course, List<ScheduledClass> takenTimes ) {
		List<CourseClass> classes = course.getCourseClasses();
		for (CourseClass courseClass: classes) {
			if (takenTimes.size() == 0) {
				return courseClass;
			} else if (!timeConflict(takenTimes, courseClass)) {
				return courseClass;
			}
		}
		return null;
	}

	
	/**
	 * Gets a class with the preferred professor as its professor.
	 * @param course
	 * @return CourseClass Object with a professor attribute equal to that of the preferred professor.
	 */
	public CourseClass getClassWithPrefProf(Course course) {
		List<CourseClass> classes = course.getCourseClasses();
		
		for (CourseClass courseClass: classes) {
			String prefProf = course.getPrefProf();
			String classProf = courseClass.getClassProf();
			
			if (classProf.equals(prefProf)) {
				return courseClass;
			}
		}
		return null;
	}


	/**
	 * Checks for time conflicts a given courseInstance could have with the times already taken.
	 * 
	 * @param takenTimes - list of ScheduledClass objects representing times taken
	 * @param courseClass - CourseInstance to check for time conflicts.
	 * 
	 * @return Boolean that tells whether there is a time conflict between the passed time 
	 *         and an already chosen class
	 */
	public boolean timeConflict(List<ScheduledClass> takenTimes, CourseClass courseClass) {
		int startTime = courseClass.getStartTime();
		int endTime = courseClass.getEndTime();
		String days = courseClass.getSchedule().getClassDays();

		for (ScheduledClass sDay: takenTimes) {
			int sDaySTime = sDay.getStartTime();
			int sDayETime = sDay.getEndTime();
			String sDays = sDay.getDays();

			if ((sDaySTime >= startTime && sDayETime <= endTime) && sDays.equals(days)) {
				return true;
			}
		}

		return false;
	}
	
	
	/**
	 * Performs OCR on an image displaying classes of a certain subject.
	 * 
	 * @param course
	 * @return String array, each array being the information of a class in a predictable order.
	 * 
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public String[] getCourseClasses(Course course) throws IOException, InterruptedException {
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
	
	
	/**
	 *  Builder for the SchedulePlanner class.
	 *  
	 * @author Oscar Reyes
	 *
	 */
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
	
	
	/**
	 * Class to store class' information.
	 * 
	 * Constructor takes in a string and since it is expected
	 * to follow a pattern it gets the information making some assumptions.
	 * 
	 * In order to prevent errors, some things are checked on the string before accepting 
	 * it as valid and preventing from a instance of this class being created with
	 * an invalid string that could not work with these assumptions.
	 * 
	 * @author Oscar Reyes
	 *
	 */
	private class ClassInformation {
		private String crn;
		private String section;
		private String days;
		private String hours;
		private String prof;
		private String location;
		private String spacesLeft;

		public ClassInformation(String classInfo) {
			this.crn = classInfo.substring(3, 8);
			this.section = classInfo.substring(9, 12);
			this.days = classInfo.substring(13,16);
			this.hours = classInfo.substring(classInfo.indexOf(" ", 13) + 1 ,
					classInfo.indexOf("m", 25) + 1);
			this.prof = classInfo.substring(classInfo.indexOf(" ", 36) + 1 ,
					classInfo.indexOf("(", 40) - 1);
			this.location = classInfo.substring(classInfo.indexOf(")") + 2);
			this.spacesLeft = classInfo.substring(classInfo.indexOf("m", 25) + 2,
					classInfo.indexOf(" ", 35));
		}		

		public String getCRN() {
			return this.crn;
		}

		public String getSection() {
			return this.section;
		}

		public String getDays() {
			return this.days;
		}

		public String getHours() {
			return this.hours;
		}

		public String getProf() {
			return this.prof;
		}

		public String getLocation() {
			return this.location;
		}

		public String getSpacesLeft() {
			return this.spacesLeft;
		}
	}
	
	
	/**
	 * A class to sort a List of CollegeCourse objects based on their
	 * CourseClasses list size.
	 * @author Oscar Reyes
	 *
	 */
	public class CourseSorter {

		private List<Course> array;
		private int length;

		public void sort(List <Course> courseList) {

			if (courseList == null || courseList.size() == 0) {
				return;
			}
			this.array = courseList;
			length = courseList.size();
			quickSort(0, length - 1);
		}

		private void quickSort(int lowerIndex, int higherIndex) {

			int i = lowerIndex;
			int j = higherIndex;
			
			int pivot = array.get(lowerIndex+(higherIndex-lowerIndex)/2).getCourseClasses().size();
			
			while (i <= j) {
				while (array.get(i).getCourseClasses().size() < pivot) {
					i++;
				}
				
				while (array.get(j).getCourseClasses().size() > pivot) {
					j--;
				}
				
				if (i <= j) {
					exchangeCourses(i, j);
					i++;
					j--;
				}
			}
			
			if (lowerIndex < j)
				quickSort(lowerIndex, j);
			if (i < higherIndex)
				quickSort(i, higherIndex);
		}

		private void exchangeCourses(int i, int j) {
			Course temp = array.get(i);
			array.set(i, array.get(j));
			array.set(j, temp);
		}


	}
	
	
	/**
	 * This Class is meant to represent a scheduled class.
	 * Takes a CourseInstance as a parameter and keeps track of 
	 * its Start-Time, End-Time, and the days these times are scheduled for.
	 * @author Oscar Reyes
	 *
	 */
	private class ScheduledClass {
		private final String days;
		private final int startTime;
		private final int endTime;

		public ScheduledClass(CourseClass courseClass) {
			this.startTime = courseClass.getStartTime();
			this.endTime = courseClass.getEndTime();
			this.days = courseClass.getSchedule().getClassDays();
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
