package scheduleCreator.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Course {
	private final StringProperty creditHours;
	private final StringProperty department;
	private final StringProperty courseNumber;
	private final StringProperty courseName;
	// optional
	private final StringProperty preferredProf;
	
	private double sectionCount;
	private boolean preferredProfAvailable = false;
	private CourseSection chosenClass;
	
	public Course(CourseBuilder builder) {
		this.courseName = builder.name;
		this.department = builder.department;
		this.courseNumber = builder.courseNumber;
		this.creditHours = new SimpleStringProperty(
		        Character.toString(this.courseNumber.get().charAt(1)));
        this.preferredProf = builder.preferredProfessor;
	}

	// This is a constructor just for testing.
	public Course() {
	    this.department = new SimpleStringProperty("MATH");
        this.courseName = new SimpleStringProperty("Calculus II");
	    this.courseNumber = new SimpleStringProperty("2414");
	    this.preferredProf = new SimpleStringProperty("");
	    this.creditHours = new SimpleStringProperty("4");
    }

	void setSectionCount(int count) {
		this.sectionCount = count;
	}

	public void setPreferredProfAvailable(Boolean avail) {
		this.preferredProfAvailable = avail;
	}
	
	void setChosenClass(CourseSection c) {
		this.chosenClass = c;
	}

	public String getDepartment()       {
		   return department.get();
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
	
	public String getPreferredProf() {
		return preferredProf.get();
	}
	
	Boolean getPreferredProfAvailable() {
		return preferredProfAvailable;
	}
	
	public CourseSection getChosenClass() {
		return chosenClass;
	}

    double getSectionCount() {
        return sectionCount;
    }
	
    public StringProperty courseNameProperty() {
		return courseName;
	}

    public String toString() {
	    return this.department.get() + " " +
				courseNumber.get() + " " +
                courseName.get();
    }

    /**
     * CollegeCourse Builder class.
     */
	public static class CourseBuilder {
		private StringProperty department;
		private StringProperty courseNumber;
		private final StringProperty name;
		private StringProperty preferredProfessor;

		public CourseBuilder(String name){
			this.name = new SimpleStringProperty(name);
		}

		public CourseBuilder courseDepartment(String department){
			this.department = new SimpleStringProperty(department);
			return this;
		}

		public CourseBuilder courseNumber(String courseNumber){
			this.courseNumber = new SimpleStringProperty(courseNumber);
			return this;
		}

		public CourseBuilder preferredProfessor(String professor){
			this.preferredProfessor = new SimpleStringProperty(professor);
			return this;
		}

		public Course build(){
			return new Course(this);
		}
	}

	
}
