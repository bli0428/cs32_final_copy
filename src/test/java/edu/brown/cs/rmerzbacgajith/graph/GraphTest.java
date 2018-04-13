package edu.brown.cs.rmerzbacgajith.graph;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import edu.brown.cs.rmerzbacgajith.maps.MapCommand;
import edu.brown.cs.rmerzbacgajith.maps.MapsGraphBuilder;
import edu.brown.cs.rmerzbacgajith.maps.Node;
import edu.brown.cs.rmerzbacgajith.maps.Way;

/**
 * Test Graph and Djikstra.
 *
 * @author gokulajith
 *
 */
public class GraphTest {

  /**
   * Test Djikstra.
   */
  @Test
  public void testDjikstra() {

    MapCommand bc = new MapCommand();

    // correct command where connection is created
    bc.mapCommand("data/maps/maps.sqlite3");

    // Create graph builder
    MapsGraphBuilder<GraphNode<Node>, GraphEdge<Way>> gb;
    gb = new MapsGraphBuilder<GraphNode<Node>, GraphEdge<Way>>(
        bc.getDBHelper());

    // Create graph
    Graph<GraphNode<Node>, GraphEdge<Way>> graph;
    graph = new Graph<GraphNode<Node>, GraphEdge<Way>>(gb);

    // Say input is route Thayer Street, Waterman Street, Thayer Street, Angell Street
    
    //Create start node and GraphNode
    Node start = bc.getIntersection("Thayer Street", "Waterman Street");
    GraphNode<Node> startNode = new GraphNode<Node>(start.getId(), start);

    //Create final node and GraphNode
    Node end = bc.getIntersection("Thayer Street", "Angell Street");
    GraphNode<Node> endNode = new GraphNode<Node>(end.getId(), end);

    // Just run djikstra to find final path
    Map<GraphNode<Node>, GraphEdge<Way>> shortestPath = graph
        .djikstras(startNode, endNode);

    // Ensure nodes are correct
    List<GraphNode<Node>> nodes = new ArrayList<GraphNode<Node>>(
        shortestPath.keySet());

    assertEquals(nodes.get(0).getValue().getId(), "/n/4182.7140.1955940297");
    assertEquals(nodes.get(1).getValue().getId(), "/n/4182.7139.1957915164");
    assertEquals(nodes.get(2).getValue().getId(), "/n/4182.7139.1957915187");
    assertEquals(nodes.get(3).getValue().getId(), "/n/4182.7139.201365753");
    assertEquals(nodes.get(4).getValue().getId(), "/n/4182.7139.201365755");
    assertEquals(nodes.get(5).getValue().getId(), "/n/4182.7139.201280067");
    
    // Ensure ways are correct
    List<GraphEdge<Way>> ways = new ArrayList<GraphEdge<Way>>(
        shortestPath.values());
    assertEquals(ways.get(0).getValue().getId(),
        "/w/4182.7140.19402129.26.1");
    assertEquals(ways.get(1).getValue().getId(), "/w/4182.7139.19402129.27.1");
    assertEquals(ways.get(2).getValue().getId(), "/w/4182.7139.19402129.28.1");
    assertEquals(ways.get(3).getValue().getId(),
        "/w/4182.7139.132173987.14.1");
    assertEquals(ways.get(4).getValue().getId(), "/w/4182.7139.132173987.15.1");
    assertEquals(ways.get(5).getValue().getId(), "/w/4182.7139.19362221.4.1");

  }
}
