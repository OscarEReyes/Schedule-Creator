package scheduleCreator.model;

import exceptions.ScrapingFailure;
import graphing.Cluster;
import graphing.Node;
import graphing.NodeClusterer;
import webScraping.CourseScraper;
import webScraping.RegistrationData;

import java.util.HashMap;
import java.util.List;


public class ScheduleGenerator {
    private RegistrationData data;

    ScheduleGenerator(RegistrationData data) {
        this.data = data;
    }

    public void getCoursesAndSections() {
        try {
        CourseScraper scraper = new CourseScraper(data);
        HashMap<Course, String[]> coursesAndSections = scraper.scrape(data.courses);
        } catch (ScrapingFailure e) {
            e.printStackTrace();
        }
    }

    public void generateSchedule(HashMap<Course, String[]> coursesAndSections) {
        SectionToNodeConverter converter = new SectionToNodeConverter(coursesAndSections);
        List<Node> nodes = converter.getNodes();
        NodeClusterer clusterer = new NodeClusterer(nodes);
        Cluster[] cluster = clusterer.createClusters();
    }


    //private List<CourseSection> getOptimalClasses(ObservableList<Course> courseList) {

    //}
}
