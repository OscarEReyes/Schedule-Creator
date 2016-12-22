package scheduleCreator.model;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import exceptions.FullClassException;
import exceptions.InvalidSectionException;
import exceptions.NullClassFieldException;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import ocr.ImageAnalyzer;
import ocr.WebScraper;
import scheduleCreator.MainApp;
import scheduleCreator.view.LoginDialogController.User;
import scheduleCreator.view.PreferenceDialogController.Preferences;
import scheduleCreator.view.SemesterDialogController.Semester;

public class SchedulePlanner {
	private User user;
	private Semester semester;

	public SchedulePlanner(SchedulePlannerBuilder builder){
		this.user = builder.user;
		this.semester = builder.semester;
	}

	/**
	 * 
	 * @param courseList
	 * @return A list of CourseClass objects which represent the optimal classes for the schedule.
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public List<CourseClass> genSchedule(ObservableList<Course> courseList) 
			throws IOException, 
			InterruptedException {
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
		int fullClassCount = 0;
		int invalidSectionCount = 0;
		
		for (String c : classes) {
			Boolean classInfoIsValid = c.length() > 1;
			Boolean classIsNotClosed = c.charAt(1) != 'C';
			
			if (classInfoIsValid && classIsNotClosed) {
				CourseClass courseClass = null;
				try {
					courseClass = createCourseInstance(c, course);
				} catch (FullClassException e) {
					fullClassCount += 1;
				} catch (InvalidSectionException e) {
					invalidSectionCount += 1;
				} catch (NullClassFieldException e) {
					e.getMessage();
				} finally {
					if (courseClass != null) {
						classList.add(courseClass);
					}
				}
			}
		}
		if (fullClassCount > 0 || invalidSectionCount > 0) {
		  Alert alert = new Alert(AlertType.ERROR);
      alert.initOwner(null);
      alert.setTitle("Full and unavailable classes");
      alert.setContentText("Full Classes: " + fullClassCount + 
      		"\nDual-Enrollment or Honor-Roll classes: " + invalidSectionCount);
      
      // Show error message window and wait for a response.
      alert.showAndWait();
		}
	}

	/**
	 * Creates a CourseClass objects and returns it.
	 * @param classInfoString
	 * @param course
	 * @return CourseClass object that gets its information from the string passed.
	 * @throws FullClassException 
	 * @throws InvalidSectionException 
	 * @throws NullClassFieldException 
	 */
	public CourseClass createCourseInstance(String info, Course c) 
			throws FullClassException, 
						 InvalidSectionException,
						 NullClassFieldException {
		ClassInformation cInfo = new ClassInformation(info);
		Schedule schedule = new Schedule(cInfo.getDays(), cInfo.getHours(), cInfo.getLocation());

		CourseClass courseClass = new CourseClass.
				CourseInstanceBuilder(schedule)
				.classCrn(cInfo.getCRN())
				.placesLeft(cInfo.getSpacesLeft())
				.classSection(cInfo.getSection())
				.classProf(cInfo.getProf())
				.courseInfo(c).build();

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
			List<ScheduledClass> takenTimes, List<CourseClass> chosenClasses) {
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
		int start = courseClass.getStartTime();
		int end = courseClass.getEndTime();
		String days = courseClass.getSchedule().getClassDays();

		for (ScheduledClass sClass: takenTimes) {
			if (sClass.checkConflict(start, end, days)) {
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


		public SchedulePlannerBuilder(){
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
	 * uses regular expressions to find wanted information
	 * and store it in appropriate variables
	 * 
	 * @author Oscar Reyes
	 *
	 */
	private class ClassInformation {
		private String crn = null;
		private String section = null;
		private String days = null;
		private String hours = null;
		private String prof = null;
		private String location = null;
		private int spacesLeft;

		public ClassInformation(String classInfo) throws InvalidSectionException {
			String noPrecedingNumbers = "(?<!\\d)";
			String noFollowingNumbers = "(?!\\d)";
			String legalDaysRepr = "(\\p{Upper}{1,4})";
			String hour = "(\\d{2}.\\d{2}\\s\\p{Lower}{2})";
			String precededBySpacesLeft = "(?<=\\s\\d{1,2}?\\s)";
			String precededBySpace = "(?<=\\s)";
			String followedByHour = "(?=\\s\\d{2}.\\d{2}\\s\\p{Lower}{2})";
			String precededByHour = "(?<=m\\s)";
			String followedByProf = "(?=\\s\\p{Upper})";
			String anything = ".+?";
			String followedByProfMark = "(?=\\s\\p{Punct}P\\p{Punct})"; // "(P)"
			String precededByProfMark = "(?<=\\s\\p{Punct}P\\p{Punct})";
			
			String crnPattern = noPrecedingNumbers + "\\d{5}" + noFollowingNumbers;
			String sectionPattern = noPrecedingNumbers + "\\p{Alnum}{3}" + noFollowingNumbers;
			String daysPattern = precededBySpace + legalDaysRepr + followedByHour;
			String hoursPattern = hour + "-" + hour;
			String profPattern = precededBySpacesLeft + anything + followedByProfMark;
			String spacesLeftPattern = precededByHour + "(\\d{1,2})" + followedByProf;
			String locationPattern = precededByProfMark + ".+"; // Finds profMark and reads till EOL

			final Matcher crnMatcher = Pattern.compile(crnPattern).matcher(classInfo);
			final Matcher sectionMatcher = Pattern.compile(sectionPattern).matcher(classInfo);
			final Matcher daysMatcher = Pattern.compile(daysPattern).matcher(classInfo);
			final Matcher hoursMatcher = Pattern.compile(hoursPattern).matcher(classInfo);
			final Matcher profMatcher = Pattern.compile(profPattern).matcher(classInfo);
			final Matcher spacesLeftMatcher = Pattern.compile(spacesLeftPattern).matcher(classInfo);
			final Matcher locationMatcher = Pattern.compile(locationPattern).matcher(classInfo);


			if(crnMatcher.find()) {
				this.crn = crnMatcher.group(); 
			}
			
			if(sectionMatcher.find()) {
				try {
					this.section = sectionMatcher.group();
					Integer.parseInt(sectionMatcher.group());
				} catch (NumberFormatException e) {
					throw new InvalidSectionException(this.section);
				}
			}
			
			if(daysMatcher.find()) {
				this.days = daysMatcher.group(); 
			}

			if(hoursMatcher.find()) {
				this.hours = hoursMatcher.group(); 
			}
			
			if(profMatcher.find()) {
				this.prof = profMatcher.group(); 
			}
			
			if(spacesLeftMatcher.find()) {
				try {
					this.spacesLeft = Integer.parseInt(spacesLeftMatcher.group()); 
				} catch (NumberFormatException e) {
					this.spacesLeft = 0;
				}
			}
			
			if(locationMatcher.find()) {
				this.location = locationMatcher.group(); 
			}

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

		public int getSpacesLeft() {
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
			this.days = courseClass.getDays();
		}
		
		public Boolean checkConflict(int start, int end, String d) { 
			Boolean sameTimes = start == startTime && end == endTime;
			Boolean startsInBetween = start <= startTime && end > endTime;
			Boolean endsHalfway = start <= endTime && end > endTime;
			Boolean sameDays = days.equals(d);
			
			if (sameTimes && sameDays) {
				return true;
			} else if (start <= startTime && end >= endTime && sameDays) {
				return true;
			} else if (start >= startTime && end <= endTime && sameDays) {
				return true;
			} else if (startsInBetween || endsHalfway && sameDays){
				return true;
			}
			return false;
		}

	}
}
