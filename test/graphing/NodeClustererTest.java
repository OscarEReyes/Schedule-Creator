package graphing;

import org.junit.Test;
import scheduleCreator.model.Course;
import scheduleCreator.model.CourseSection;
import scheduleCreator.model.Schedule;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Oscar Reyes on 7/13/2017.
 */

public class NodeClustererTest {
    @Test
    public void createClusters() throws Exception {
        Course calculusTwoCourse = new Course();
        Schedule s1 = new Schedule("MWF", "10:00 am-10:50 am", "testLocation");
        Schedule s2 = new Schedule("MWF", "01:00 pm-01:50 pm", "testLocation");
        Schedule s3 = new Schedule("TR", "08:00 am-09:15 am", "testLocation");
        Schedule s4 = new Schedule("TR", "09:30 am-10:45 am", "testLocation");
        Schedule s5 = new Schedule("TR", "11:00 am-12:15 pm", "testLocation");

        CourseSection section1 = new CourseSection.CourseSectionBuilder(s1)
                .crn("12345")
                .professor("Dr. Ahmed")
                .section("001")
                .course(calculusTwoCourse)
                .placesLeft(20)
                .build();

        CourseSection section2 = new CourseSection.CourseSectionBuilder(s1)
                .crn("23456")
                .professor("Dr. Ahmed")
                .section("002")
                .course(calculusTwoCourse)
                .placesLeft(20)
                .build();

        CourseSection section3 = new CourseSection.CourseSectionBuilder(s2)
                .crn("34567")
                .professor("Dr. Wang")
                .section("003")
                .course(calculusTwoCourse)
                .placesLeft(20)
                .build();

        CourseSection section4 = new CourseSection.CourseSectionBuilder(s2)
                .crn("45678")
                .professor("Dr. Wang")
                .section("004")
                .course(calculusTwoCourse)
                .placesLeft(20)
                .build();

        CourseSection section5 = new CourseSection.CourseSectionBuilder(s5)
                .crn("56789")
                .professor("Dr. Wang")
                .section("005")
                .course(calculusTwoCourse)
                .placesLeft(20)
                .build();

        Node node1 = new Node(section1, 1.0);
        Node node2 = new Node(section2, 1.0);
        Node node3 = new Node(section3, 0.6);
        Node node4 = new Node(section4, 0.6);
        Node node5 = new Node(section5, 0.5);

        List<Node> nodes = new ArrayList<>();
        nodes.add(node1);
        nodes.add(node2);
        nodes.add(node3);
        nodes.add(node4);
        nodes.add(node5);

        NodeClusterer clusterer = new NodeClusterer(nodes);
        Cluster[] clusters = clusterer.createClusters();
        for (Cluster c: clusters) {
            System.out.print(c);
            System.out.print("\n\n");
        }
    }


}