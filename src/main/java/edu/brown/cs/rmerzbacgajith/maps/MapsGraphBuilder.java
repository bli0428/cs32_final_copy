package edu.brown.cs.rmerzbacgajith.maps;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableMap;

import edu.brown.cs.rmerzbacgajith.graph.GraphBuilder;
import edu.brown.cs.rmerzbacgajith.graph.GraphEdge;
import edu.brown.cs.rmerzbacgajith.graph.GraphNode;

/**
 * Maps specific GraphBuilder that implements generic GraphBuilder in order to
 * create and map GraphNodes with their neighboring edges and nodes.
 *
 * @author gokulajith
 *
 * @param <N>
 *          Node that goes in graph with a value storing an node from db
 * @param <E>
 *          Edge that goes in graph with a value storing a Way
 */
public class MapsGraphBuilder<N, E>
    implements GraphBuilder<GraphNode<Node>, GraphEdge<Way>> {

  private static final double RADIUS = 6371;

  private Map<String, Map<GraphNode<Node>, GraphEdge<Way>>> neighCache;
  private MapsDatabaseHelper dbHelper;

  /**
   * Constructor simply stores MapsDatabaseHelper.
   *
   * @param dbHelper
   *          MapsDatabaseHelper that takes care of all db queries and caching.
   */
  public MapsGraphBuilder(MapsDatabaseHelper dbHelper) {

    // Cache that stores a nodes id to a map that holds its neighboring nodes
    // and the edges that connect them.
    neighCache = new HashMap<String, Map<GraphNode<Node>, GraphEdge<Way>>>();
    this.dbHelper = dbHelper;
  }

  @Override
  public Map<GraphNode<Node>, GraphEdge<Way>> getNeighbors(
      GraphNode<Node> sourceNode) {

    Map<GraphNode<Node>, GraphEdge<Way>> neighbors;
    neighbors = new HashMap<GraphNode<Node>, GraphEdge<Way>>();

    // Check cache for neighbors
    if (neighCache.containsKey(sourceNode.getId())) {
      neighbors = neighCache.get(sourceNode.getId());
    } else {

      // Get Node from db
      Node sourceMapnode = sourceNode.getValue();

      // Call helper to find the neighboring nodes and edges
      neighbors = this.djikstraHelper(sourceMapnode);

      // Cache it for next time.
      neighCache.put(sourceNode.getId(), neighbors);
    }
    return neighbors;
  }

  /**
   * Djikstra helper to find neighbors of a node. Finds all traversable ways the
   * sourceNode is a startNode of, and then Creates a GraphNode with the endNode
   * of that way and maps it to the way itself as a neighbor to the sourceNode.
   *
   * @param sourceNode
   *          node that the neighbors are being found of
   * @return A map that maps every neighboring node of the sourceNode to an edge
   *         connecting the two nodes.
   */
  private Map<GraphNode<Node>, GraphEdge<Way>> djikstraHelper(Node sourceNode) {

    Map<GraphNode<Node>, GraphEdge<Way>> nodeToEdgeMap;
    nodeToEdgeMap = new HashMap<GraphNode<Node>, GraphEdge<Way>>();

    String nodeId = sourceNode.getId();
    double[] sourceCoords = sourceNode.getCoords();

    // Use dbhelper to get all traversable ways that the sourceNode is the
    // startNode of
    List<Way> ways = dbHelper.getWaysFromNode(nodeId);

    // Loop through all the ways
    for (Way way : ways) {

      // Use dbhelper to get the endNode of the way
      Node node = dbHelper.getEndNodeFromWay(way.getId());

      // Set the weight to be the straight line distance + Haversine distance
      // for A* heuristic.
      double[] endCoords = node.getCoords();
      double weight = this.findDistance(sourceCoords, endCoords)
          + haversineDistance(sourceCoords, endCoords);

      // Create edge storing Way
      GraphEdge<Way> edge = new GraphEdge<Way>(way.getId(), way, weight);

      // Create GraphNode storing neighbor node.
      GraphNode<Node> graphNode = new GraphNode<Node>(node.getId(), node);

      // Make sure the Edge with least weight is always being kept in the
      // Map
      if (nodeToEdgeMap.containsKey(graphNode)) {
        GraphEdge<Way> checkMovie = nodeToEdgeMap.get(graphNode);
        if (checkMovie.getWeight() > edge.getWeight()) {

          nodeToEdgeMap.put(graphNode, edge);
        }
      } else {

        // if node has not been entered, enter it.
        nodeToEdgeMap.put(graphNode, edge);
      }

    }

    return nodeToEdgeMap;
  }

  /**
   * Finds the distance between two sets of coordinates.
   *
   * @param coords1
   *          the first coordinates
   * @param coords2
   *          the second coordinates
   * @return the distance
   */
  private double findDistance(double[] coords1, double[] coords2) {
    assert coords1.length == coords2.length;
    double sum = 0;
    for (int i = 0; i < coords1.length; i++) {
      sum += Math.pow(coords1[i] - coords2[i], 2);
    }
    return Math.sqrt(sum);
  }

  /**
   * Finds the haversine distance between two sets of coordinates.
   *
   * @param coords1
   *          the first coordinates
   * @param coords2
   *          the second coordinates
   * @return the distance
   */
  private double haversineDistance(double[] coords1, double[] coords2) {
    assert coords1.length == coords2.length;
    double delPhi = Math.toRadians(coords2[0] - coords1[0]);
    double delLambda = Math.toRadians(coords2[1] - coords1[1]);
    double a = Math.pow(Math.sin(delPhi / 2.0), 2)
        + (Math.cos(Math.toRadians(coords1[0]))
            * Math.cos(Math.toRadians(coords2[0]))
            * Math.pow(Math.sin(delLambda / 2.0), 2));
    double c = Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    double d = c * RADIUS;
    return d;
  }

  @Override
  public Map<GraphNode<Node>, GraphEdge<Way>> getNodeNeighborsCache(
      GraphNode<Node> node) {
    return new ImmutableMap.Builder<GraphNode<Node>, GraphEdge<Way>>()
        .putAll(neighCache.get(node.getId())).build();
  }

}
