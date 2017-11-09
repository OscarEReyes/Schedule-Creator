package graphing;

import scheduleCreator.model.Course;
import scheduleCreator.model.CourseSection;
import scheduleCreator.model.Schedule;


/**
 * Created by Oscar Reyes on 7/13/2017.
 */

public class Node implements Comparable<Node> {
    private CourseSection section;
    private Course course;
    private Double significance;

    public Node(CourseSection section, Double significance) {
        this.section = section;
        this.course = section.getCourse();
        this.significance = significance;
        if (section.hasPreferredProfessor()) {
            this.significance += 0.1;
        }
    }

    public int compareTo(Node n) {
        if (significance < n.significance) {
            return -1;
        } else if (significance > n.significance) {
            return 1;
        }
        return 0;
    }

    public String toString() {
        return section + " : " + significance;
    }

    public CourseSection getSection() {
        return section;
    }

    public Course getCourse() {
        return course;
    }

    Boolean clashesWith(Node node2) {
        Schedule scheduleOfNode2 = node2.getNodeSectionSchedule();
        Schedule schedule = section.getSchedule();
        return schedule.scheduleIntervenes(scheduleOfNode2);
    }

    Double getSignificance() {
        return significance;
    }

    String getCRN() {
        return section.getCrn();
    }

    String getLink() {
        return section.getConnection().getCrn();
    }

    Boolean hasLink() {
        return section.getConnection() != null;
    }

    private Schedule getNodeSectionSchedule() {
        return section.getSchedule();
    }
}