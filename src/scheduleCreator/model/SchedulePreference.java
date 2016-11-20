package scheduleCreator.model;

import javafx.beans.property.StringProperty;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class SchedulePreference {
	private final StringProperty hoursPreference;
	private final StringProperty daysPreference;
	private final BooleanProperty breaksPreference;
	private final StringProperty beginEndPreference;
	
	// Default Constructor
	
	 public SchedulePreference() {
	        this(null, null, null, null);
	 }
	
	 public SchedulePreference(String hoursPreference, String daysPreference,
			 Boolean breaksPreference, String beginEndPreference) {
	        this.hoursPreference = new SimpleStringProperty(hoursPreference);
	        this.daysPreference = new SimpleStringProperty(daysPreference);
	        this.breaksPreference = new SimpleBooleanProperty(breaksPreference);
	        this.beginEndPreference = new SimpleStringProperty(beginEndPreference);
	    }
}
