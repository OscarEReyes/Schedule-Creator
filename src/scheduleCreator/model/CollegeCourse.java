package scheduleCreator.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.util.List;

import javafx.beans.property.IntegerProperty;

public class CollegeCourse {
	private final IntegerProperty creditHours;
	private final StringProperty courseDepartment;
	private final StringProperty courseNumber;
	private final StringProperty courseName;
	// optional
	private final StringProperty prefProf;
	
	private List<CourseInstance> courseClasses;
	public CollegeCourse(CollegeCourseBuilder builder) {
		this.courseDepartment = builder.courseDepartment;
		this.courseNumber = builder.courseNumber;
		this.courseName = builder.courseName;
		this.creditHours = 	new SimpleIntegerProperty(
				(int) this.courseNumber.get().charAt(1));
		this.prefProf = builder.prefProf;
	}
	
	// Setters
	
	public void setCourseDepartment(String courseDepartment){
		this.courseDepartment.set(courseDepartment);
	}
	
	public void setCourseNumber(String courseNumber){
		this.courseNumber.set(courseNumber);
	}
	public void setCourseName(String courseName){
		this.courseName.set(courseName);
	}
	
	public void setPrefProf(String prefProf){
		this.prefProf.set(prefProf);
	}
		
	// Getters
	
	public String getCourseDepartment() {
		return courseDepartment.get();
	}
	
	public String getCourseNumber() {
		return courseNumber.get();
	}
	
	public String getCourseName() {
		return courseName.get();
	}
	
	public int getCreditHours() {
		return creditHours.get();
	}
	
	public String getPrefProf() {
		return prefProf.get();
	}
	
	public List<CourseInstance> getCourseClasses() {
		return this.courseClasses;
	}
	
	// Get Property Methods
	
	public IntegerProperty creditHoursProperty() {
		return creditHours;
	}
	
	public StringProperty courseDepartmentProperty() {
		return courseDepartment;
	}
	
	public StringProperty courseNumberProperty() {
		return courseNumber;
	}
	
	public StringProperty courseNameProperty() {
		return courseName;
	}
	
	public static class CollegeCourseBuilder {
		private StringProperty courseDepartment;
		private StringProperty courseNumber;
		private StringProperty courseName;
		private IntegerProperty creditHours;
		private StringProperty prefProf;
		
		public CollegeCourseBuilder(String courseName){
			this.courseName = new SimpleStringProperty(courseName);
			this.prefProf = new SimpleStringProperty("");
		}
		
		public CollegeCourseBuilder courseDepartment(String courseDepartment){
			this.courseDepartment = new SimpleStringProperty(courseDepartment);
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
		
		public CollegeCourse build(){
			return new CollegeCourse(this);
		}
	}
	
	
}
