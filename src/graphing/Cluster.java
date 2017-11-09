package graphing;

import scheduleCreator.model.Course;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Oscar Reyes on 7/13/2017.
 */

public final class Cluster implements Comparable<Cluster> {
    private Map<String, Node> nodeOfCRN = new HashMap<>();
    private List<Node> nodes;
    private Double importance;

    Cluster(List<Node> nodes, Double imp) {
        Collections.sort(nodes);
        this.nodes = nodes;
        nodes.forEach(node -> nodeOfCRN.put(node.getCRN(), node));
        importance = imp;
    }

    @Override
    public String toString() {
        return nodeOfCRN.toString();
    }

    @Override
    public int compareTo(Cluster c) {
        if (importance < c.importance) {
            return -1;
        } else if (importance > c.importance) {
            return 1;
        }
        return 0;
    }

    Node pickNode() {
        return nodes.get(0);
    }

    void removeNodeOfCRN(String crn) {
        if (nodeOfCRN.containsKey(crn)) {
            removeNode(nodeOfCRN.get(crn));
            recalculateImportance();
        }
    }

    private void removeNode(Node node) {
        nodes.remove(node);
        nodeOfCRN.remove(node.getCRN());
    }

    private void recalculateImportance() {
        List<Double> significances = nodeOfCRN.values()
                .stream()
                .map(Node::getSignificance)
                .collect(Collectors.toList());
        importance = Collections.max(significances);
    }


    List<String> removeNodesOfCourse(Course course) {
        List<Node> nodesToRemove = getNodesOfCourse(course);
        removeNodes(nodesToRemove);
        return nodesToRemove.stream()
                .filter(Node::hasLink)
                .map(Node::getLink)
                .collect(Collectors.toList());
    }

    private List<Node> getNodesOfCourse(Course course) {
        return nodes.stream()
                .filter(node -> node.getCourse().equals(course))
                .collect(Collectors.toList());
    }

    private void removeNodes(List<Node> nodesToRemove) {
        nodesToRemove.forEach(this::removeNode);
        recalculateImportance();
    }

    Set<String> getAllCRNs() {
        return nodeOfCRN.keySet();
    }
}