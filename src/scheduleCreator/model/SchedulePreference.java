package scheduleCreator.model;

import javafx.beans.property.StringProperty;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class SchedulePreference {
	private final StringProperty hoursPref;
	private final StringProperty daysPref;
	private final BooleanProperty breaksPref;
	private final StringProperty beginEndPref;
		
	 public SchedulePreference() {
	        this.hoursPref = new SimpleStringProperty("any");
	        this.daysPref = new SimpleStringProperty("all");
	        this.breaksPref = new SimpleBooleanProperty(false);
	        this.beginEndPref = new SimpleStringProperty("end");
	 }
	 
	 // Getter Methods
	 
	 public String getHoursPref(){
		 return hoursPref.get();
	 }
	 
	 public String getDaysPref(){
		 return daysPref.get();
	 }
	 
	 public Boolean getBreaksPref(){
		 return breaksPref.get();
	 }
	 
	 public String getBeginEndPref(){
		 return beginEndPref.get();
	 }
	 
	 // Setter Methods
	 
	 public void setHoursPref(String hoursPref) {
	        this.hoursPref.set(hoursPref);
	 }
	 
	 public void setDaysPref(String daysPref) {
	        this.daysPref.set(daysPref);
	 }
	 
	 public void setBreaksPref(Boolean breaksPref) {
	        this.breaksPref.set(breaksPref);
	 }
	 
	 public void setBeginEndPref(String beginEndPref) {
	        this.beginEndPref.set(beginEndPref);
	 }
}
