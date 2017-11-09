package graphing;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Oscar Reyes on 7/13/2017.
 */

public class NodeClusterer {
    private List<Node> inputNodes;
    private List<Cluster> clusters = new ArrayList<>();
    private Map<String, Cluster> clusterOfCrn = new HashMap<>();

    public NodeClusterer(List<Node> inputNodes) {
        this.inputNodes = inputNodes;
    }

    public Cluster[] createClusters() {
        while (!inputNodes.isEmpty()) {
            createClusterWithFirstNode();
        }
        return clusters.toArray(new Cluster[clusters.size()]);
    }

    private void createClusterWithFirstNode() {
        Node node = inputNodes.remove(0);
        List<Node> clusteredNodes = getClusteredNodes(node);
        createCluster(clusteredNodes);
        inputNodes.removeAll(clusteredNodes);
    }

    private List<Node> getClusteredNodes(Node seedNode) {
        List<Node> clusteredNodes = getConnections(seedNode);
        clusteredNodes.add(seedNode);
        return clusteredNodes;
    }

    private void createCluster(List<Node> clusteredNodes) {
        Cluster cluster = instantiateCluster(clusteredNodes);
        addLinkedNodes(cluster);
        clusters.add(cluster);
    }

    private List<Node> getConnections(Node node) {
        return inputNodes.stream()
                .filter(node::clashesWith)
                .collect(Collectors.toList());
    }

    private Cluster instantiateCluster(List<Node> nodes) {
        List<Double> significances = nodes.stream()
                .map(Node::getSignificance)
                .collect(Collectors.toList());
        Double importance = Collections.max(significances);

        return new Cluster(nodes, importance);
    }

    private void addLinkedNodes(Cluster cluster) {
        Set<String> allCRN = cluster.getAllCRNs();
        allCRN.forEach(crn -> clusterOfCrn.put(crn, cluster));
    }
}