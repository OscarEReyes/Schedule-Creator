package scheduleCreator.model;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.regex.Matcher;

import javafx.collections.ObservableList;

import ocr.ImageAnalyzer;
import ocr.WebScraper;
import regexMatchers.classInfoPatterns;
import regexMatchers.schedulePattern;
import siteClasses.Semester;
import siteClasses.User;

import static alertMessages.unavailableClassAlert.alertClassesUnavailable;

public class SchedulePlanner {
	private User user;
	private Semester semester;

	SchedulePlanner(SchedulePlannerBuilder builder){
		this.user = builder.user;
		this.semester = builder.semester;
	}

	/**
	 * 
	 * @param courseList ObservableList of Course Objects
	 * @return A list of CourseClass objects which represent the optimal classes for the schedule.
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public List<CourseClass> genSchedule(ObservableList<Course> courseList) 
			throws IOException, 
			InterruptedException {
		for (Course course: courseList) {
			List<CourseClass> classList = new ArrayList<>();
			String[] classes = getCourseClasses(course);
			addOpenClasses(classes, classList, course);
			course.setCourseClasses(classList);
		}
		return getOptimalClasses(courseList);
	}

    /**
     * Performs OCR on an image displaying classes of a certain subject.
     *
     * @param course Course Object
     * @return String array, each array being the information of a class in a predictable order.
     *
     * @throws IOException
     * @throws InterruptedException
     */
    private String[] getCourseClasses(Course course) throws IOException, InterruptedException {
        File imageFile;
        String results;
        WebScraper scraper = new WebScraper();
        ImageAnalyzer imAn = new ImageAnalyzer();
        imageFile = scraper.scrapeCoursePage(user, course, semester);

        if (imageFile != null) {
            results = imAn.analyzeImage(imageFile);
            return results.split("\\r?\\n");
        } else {
            return null;
        }
    }

	/**
	 * Handles adding classes that are open.
	 * if it is a valid class it will execute the isOpenClass method which takes care of adding it to classList
     * if open.
     *
     * Informs the user if there were classes that were unavailable. (Closed, full, or dual-enrollment classes)
	 * @param classes String representing classes
	 * @param classList List of CourseClass Objects
	 * @param course Course Object
	 */
	private void addOpenClasses(String[] classes, List<CourseClass> classList, Course course) {
        int unavailableClassCount = 0;
        for (String classString : classes) {
            // If String length is greater than 1 and not some empty string or space
            if (classString.length() > 1) {
                if (!isOpenCourse(classString, classList, course)) {
                        unavailableClassCount += 1;
                }
            }
        }
        if (unavailableClassCount > 0) {
            alertClassesUnavailable(unavailableClassCount);
        }
    }

    /**
     * Returns true if classString represents an open Course available for registration and adds it to classList.
     *
     * @param classString String representation of a class in TAMUK
     * @param classList List of CourseClass objects to which a generated CourseClass object is added if open.
     * @param course A Course object from which a CourseClass is built.
     * @return Boolean to inform if the course was Open or not.
     */
    private boolean isOpenCourse(String classString, List<CourseClass> classList, Course course) {
        // Check that selection status is SR (Selective Registration)
        if (classString.substring(0, 2).equals("SR")) {
            CourseClass courseClass = createCourseInstance(classString, course);
            boolean noSpaceLeft = courseClass.getPlacesLeft() == 0;
            boolean invalidSection = courseClass.getClassSection().equals("invalid");

            if (!noSpaceLeft && !invalidSection) {
                classList.add(courseClass);
                return true;
            }
        }
        return false;
    }

	/**
	 * Creates a CourseClass objects and returns it.
	 * @param info String representing info about a class
	 * @param course Course Object
	 * @return CourseClass object that gets its information from the string passed.

	 */
	private CourseClass createCourseInstance(String info, Course course) {
		ClassInformation cInfo = new ClassInformation(info);
		Schedule schedule = new Schedule(cInfo.getDays(), cInfo.getHours(), cInfo.getLocation());

		return new CourseClass.
				CourseInstanceBuilder(schedule)
				.classCrn(cInfo.getCRN())
				.placesLeft(cInfo.getSpacesLeft())
				.classSection(cInfo.getSection())
				.classProf(cInfo.getProf())
				.courseInfo(course).build();
	}

	/**
	 * Gets optimal classes
	 * @param courseList ObservableList of Course Objects
	 * @return a List of CourseClass objects
	 */
	private List<CourseClass> getOptimalClasses(ObservableList<Course> courseList) {
		Stack<Course> priorityStack = new Stack<>();
		Stack<Course> nPriorityStack = new Stack<>();
		List<CourseClass> chosenClasses = new ArrayList<>();
		List<ScheduledClass> takenTimes = new ArrayList<>();

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
	 * @param stack A stack of Course Objects
	 * @param takenTimes List of ScheduledClass
	 * @param chosenClasses List of CourseClass representing classes that have been chosen
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
			tmp.setChosenClass(chosenClass);
			chooseClass(chosenClasses, takenTimes, chosenClass);
		}
	}


	/**
	 * Iterates over the stack. Choosing classes for each course
	 * @param stack Stack of Course Objects
	 * @param takenTimes List of ScheduledClass Objects representing times taken
	 * @param chosenClasses List of CourseClass Objects representing chosenClasses
	 */
	private void iterStack(Stack<Course> stack, 
			List<ScheduledClass> takenTimes, List<CourseClass> chosenClasses) {
		while (!stack.isEmpty()) {
			CourseClass chosenClass;
			Course tmp = stack.pop();
			chosenClass = getPrefClass(tmp, takenTimes);
			tmp.setChosenClass(chosenClass);
			chooseClass(chosenClasses, takenTimes, chosenClass);
		}
	}


	/**
	 * Takes a CourseInstance and adds it to a list of CourseInstance objects 
	 * that represent the chosen classes. Also adds an ScheduledClass objects
	 * created with the passed CourseInstance used as its parameter.
	 * 
	 * @param chosenClasses List of CourseClass Objects representing chosenClasses
	 * @param takenTimes List of ScheduledClass Objects representing times taken
	 * @param chosenClass CourseClass Object representing a chosenClass
	 */
	private void chooseClass(List<CourseClass> chosenClasses, 
			List<ScheduledClass> takenTimes, CourseClass chosenClass) {
		chosenClasses.add(chosenClass);
		ScheduledClass time = new ScheduledClass(chosenClass);
		takenTimes.add(time);
	}


	/**
	 * Gets a class that fits in the schedule (Not in taken times)
	 * @param course Course Instance
	 * @param takenTimes List of ScheduledClass Objects
	 * @return CourseClass Object with schedule that does not conflict.
	 */
	private CourseClass getPrefClass(Course course, List<ScheduledClass> takenTimes) {
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
	 * @param course Course Instance
	 * @return CourseClass Object with a professor attribute equal to that of the preferred professor.
	 */
	private CourseClass getClassWithPrefProf(Course course) {
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
	private boolean timeConflict(List<ScheduledClass> takenTimes, CourseClass courseClass) {
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
		private String crn;
		private String section;
		private String days;
		private String hours;
		private String prof;
		private String location;
		private int spacesLeft;

		ClassInformation(String classInfo) {
			final Matcher crnMatcher = classInfoPatterns.getCRNMatcher(classInfo);
            final Matcher sectionMatcher = classInfoPatterns.getSectionMatcher(classInfo);
			final Matcher daysMatcher = schedulePattern.getDaysMatcher(classInfo);
			final Matcher hoursMatcher = schedulePattern.getHoursMatcher(classInfo);
            final Matcher profMatcher = classInfoPatterns.getProfMatcher(classInfo);
			final Matcher spacesLeftMatcher = classInfoPatterns.getSpacesLeftMatcher(classInfo);
			final Matcher locationMatcher = classInfoPatterns.getLocationMatcher(classInfo);

			if(crnMatcher.find()) {
				this.crn = crnMatcher.group(); 
			}
			
			if(sectionMatcher.find()) {
                String section = sectionMatcher.group();
                if (Character.isLetter(section.charAt(1))) {
                    this.section = "invalid";
                } else {
                    this.section = section;
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

		String getCRN() {
			return this.crn;
		}

		String getSection() {
			return this.section;
		}

		String getDays() {
			return this.days;
		}

		String getHours() {
			return this.hours;
		}

		String getProf() {
			return this.prof;
		}

		String getLocation() {
			return this.location;
		}

		int getSpacesLeft() {
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

		void sort(List<Course> courseList) {

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

		ScheduledClass(CourseClass courseClass) {
			this.startTime = courseClass.getStartTime();
			this.endTime = courseClass.getEndTime();
			this.days = courseClass.getDays();
		}
		
		Boolean checkConflict(int start, int end, String d) {
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
