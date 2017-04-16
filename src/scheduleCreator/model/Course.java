package scheduleCreator.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.util.List;

import javafx.beans.property.IntegerProperty;

public class Course {
	private final StringProperty creditHours;
	private final StringProperty courseDep;
	private final StringProperty courseNumber;
	private final StringProperty courseName;
	// optional
	private final StringProperty prefProf;
	
	private List<CourseClass> courseClasses;
	private boolean prefProfAvailable = false;
	private CourseClass chosenClass = null;
	
	public Course(CollegeCourseBuilder builder) {
		this.courseDep = builder.courseDep;
		this.courseNumber = builder.courseNumber;
		this.courseName = builder.courseName;
		this.creditHours = 	new SimpleStringProperty(
		        Character.toString(this.courseNumber.get().charAt(1)));
        this.prefProf = builder.prefProf;
	}
	
	// Setters
	
	public void setCourseDep(String courseDep) {
		this.courseDep.set(courseDep);
	}

	public void setCourseNumber(String courseNumber) {
		this.courseNumber.set(courseNumber);
	}

	public void setCourseName(String courseName) {
		this.courseName.set(courseName);
	}

	public void setCreditHours(String creditHours) { this.creditHours.set(creditHours);}

	public void setPrefProf(String prefProf) {
		this.prefProf.set(prefProf);
	}

	public void setCourseClasses(List<CourseClass> classes) {
		this.courseClasses = classes;
	}
	
	public void setPrefProfAvailable(Boolean avail) {
		this.prefProfAvailable = avail;
	}
	
	public void setChosenClass(CourseClass c) {
		this.chosenClass = c;
	}
		
	// Getters
	
	public String getCourseDep() {
		return courseDep.get();
	}
	
	public String getCourseNumber() {
		return courseNumber.get();
	}
	
	public String getCourseName() {
		return courseName.get();
	}
	
	public String getCreditHours() {
		return creditHours.get();
	}
	
	public String getPrefProf() {
		return prefProf.get();
	}
	
	public List<CourseClass> getCourseClasses() {
		return this.courseClasses;
	}
	
	public Boolean getPrefProfAvailable() {
		return this.prefProfAvailable;
	}
	
	public CourseClass getChosenClass() {
		return this.chosenClass;
	}
	
	// Get Property Methods
	
	public StringProperty creditHoursProperty() {
		return creditHours;
	}
	
	public StringProperty courseDepProperty() {
		return courseDep;
	}
	
	public StringProperty courseNumberProperty() {
		return courseNumber;
	}
	
	public StringProperty courseNameProperty() {
		return courseName;
	}
	
	public static class CollegeCourseBuilder {
		private StringProperty courseDep;
		private StringProperty courseNumber;
		private StringProperty courseName;
		private StringProperty prefProf;

		public CollegeCourseBuilder(String courseName){
			this.courseName = new SimpleStringProperty(courseName);
			this.prefProf = new SimpleStringProperty("");
		}

		public CollegeCourseBuilder courseDepartment(String courseDep){
			this.courseDep = new SimpleStringProperty(courseDep);
			return this;
		}

		public CollegeCourseBuilder courseNumber(String courseNumber){
			this.courseNumber = new SimpleStringProperty(courseNumber);
			return this;
		}

		public CollegeCourseBuilder prefProf(String prefProf){
			this.prefProf = new SimpleStringProperty(prefProf);
			return this;
		}

		public Course build(){
			return new Course(this);
		}
	}

	
}
