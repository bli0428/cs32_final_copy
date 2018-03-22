package edu.brown.cs.rmerzbacgajith.graph;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

/**
 * Generic Graph class made of GraphNodes and GraphEdges that each can store
 * values depending on what the graph is being used for. Graph also utilizes a
 * generic GraphBuilder interface in order to create and map the Nodes and
 * Edges.
 *
 * @author gokulajith
 *
 * @param <Node>
 *          GraphNodes in the Graph.
 * @param <Edge>
 *          GraphEdges in the Graph.
 */
public class Graph<Node extends GraphNode<?>, Edge extends GraphEdge<?>> {

  private GraphBuilder<Node, Edge> builder;
  private Map<String, Double> distances;

  /**
   * Empty Constructor.
   */
  public Graph() {

  }

  /**
   * Constructor stores GraphBuilder, which has all the information about the
   * nodes and edges in the graph.
   *
   * @param builder
   *          Generic GraphBuilder used to create and use the Graph.
   */
  public Graph(GraphBuilder<Node, Edge> builder) {

    this.builder = builder;
  }

  /**
   * Djikstra algorithm to find the shortest path based on GraphEdge weights
   * between 2 GraphNodes in the graph.
   *
   * @param start
   *          Node to start path.
   * @param end
   *          Node to end path/target node.
   * @return LinkedHashMap that maps the Nodes and Edges in order from start to
   *         end node.
   *
   */
  public Map<Node, Edge> djikstras(Node start, Node end) {

    // PriorityQueue implementation with weightComparator that ranks Nodes based
    // on current distance value.
    Queue<Node> q = new PriorityQueue<Node>(weightComparator);

    // HashMap that maps the id of a node to a double representing its current
    // distance in the search.
    distances = new HashMap<String, Double>();

    // HashMap to map the id of a Node to the Node that came before it in the
    // path.
    Map<String, Node> prev = new HashMap<String, Node>();

    // Set of ids of Nodes that have already been visited in the search.
    Set<String> visited = new HashSet<String>();

    // LinkedHashMap<Node, Edge> finalPath = new LinkedHashMap<Node, Edge>();

    // Place first node in queue and set its distance to 0 and visited to true.
    q.add(start);
    distances.put(start.getId(), 0.0);
    visited.add(start.getId());

    while (!q.isEmpty()) {

      // Get node from queue that has lowest distance in distances HashMap.
      Node curr = q.poll();

      // Use builder to find each Node that is currs neighbor, as well as each
      // edge that attaches the two nodes.
      Map<Node, Edge> neighbors = builder.getNeighbors(curr);

      // If the target node is found.
      if (curr.getId().equals(end.getId())) {
        return this.createPath(curr, prev);
      }

      // Otherwise, loop through all neighboring nodes
      Set<Node> actorNeighbors = neighbors.keySet();

      for (Node neighbor : actorNeighbors) {

        String neighborId = neighbor.getId();
        Edge edge = neighbors.get(neighbor);

        // Create new distance value
        double newDist = distances.get(curr.getId()) + edge.getWeight();

        // If the neighbor has never been visited
        if (!visited.contains(neighborId)) {

          // Update its status in all of the data structures.
          distances.put(neighborId, newDist);
          prev.put(neighborId, curr);
          q.add(neighbor);
          visited.add(neighborId);

          // If a better distance has been found
        } else if (newDist < distances.get(neighborId)) {

          // Update the neighbors distance in the queue and distances map
          distances.remove(neighborId);
          q.remove(neighbor);
          distances.put(neighborId, newDist);
          q.add(neighbor);

          // Map this new previous node.
          prev.put(neighborId, curr);
        }
      }
    }

    return Collections.emptyMap();
  }

  /**
   * Creates Path from start to end node if Djikstra is successful.
   *
   * @param curr
   *          target node
   * @param prev
   *          Map of Node ids to the Nodes that came before them.
   * @return Map that maps path in order of nodes with the edges that connect
   *         them.
   */
  private Map<Node, Edge> createPath(Node curr, Map<String, Node> prev) {

    Map<Node, Edge> finalPath = new LinkedHashMap<Node, Edge>();
    Stack<Node> s = new Stack<Node>();

    // Loop backwards, adding nodes to stack based on previous pointers.
    while (prev.containsKey(curr.getId())) {

      s.push(curr);

      Node temp = curr;

      curr = prev.get(curr.getId());
      prev.remove(temp.getId());
    }

    s.push(curr);

    // Then, pop every node from the stack in the correct final order, mapping
    // it to the edge that
    // connects it to the next node in the path.
    while (s.size() > 1) {

      Node n = s.pop();

      finalPath.put(n, builder.getNodeNeighborsCache(n).get(s.peek()));
    }

    return finalPath;

  }

  /**
   * Comparator that ranks Nodes based on distances in the distances HashMap for
   * each search.
   */
  private Comparator<Node> weightComparator = new Comparator<Node>() {
    @Override
    public int compare(Node n1, Node n2) {
      if (distances.get(n1.getId()) < distances.get(n2.getId())) {
        return -1;
      } else {
        return 1;
      }
    }
  };
}
