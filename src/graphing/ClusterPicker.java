package graphing;

import scheduleCreator.model.Course;

import java.util.*;

/**
 * Created by Oscar Reyes on 7/17/2017.
 */

public class ClusterPicker {
    private List<Cluster> clusters;
    private Map<String, Cluster> clusterOfCrn;

    ClusterPicker(List<Cluster> clusters, HashMap<String, Cluster> clusterOfCrn) {
        this.clusters = clusters;
        this.clusterOfCrn = clusterOfCrn;
        Collections.sort(clusters);
    }

    public List<Node> chooseSectionNodes(List<Course> courses) {
        List<Node> nodes = new ArrayList<>();
        while (!clusters.isEmpty() && (nodes.size() != courses.size())) {
            pickNode(nodes);
        }
        return nodes;
    }

    private void pickNode(List<Node> nodes) {
        Cluster cluster = clusters.remove(0);
        Node pickedNode = cluster.pickNode();
        Course course = pickedNode.getCourse();

        clusters.forEach(c -> removedLinkedCourses(c, course));
        nodes.add(pickedNode);
    }

    private void removedLinkedCourses(Cluster cluster, Course course) {
        List<String> links = cluster.removeNodesOfCourse(course);
        links.forEach(this::removeLink);
    }

    private void removeLink(String crn) {
        if (clusterOfCrn.containsKey(crn)) {
            Cluster cluster = clusterOfCrn.get(crn);
            clusterOfCrn.remove(crn);
            cluster.removeNodeOfCRN(crn);
        }
    }
}