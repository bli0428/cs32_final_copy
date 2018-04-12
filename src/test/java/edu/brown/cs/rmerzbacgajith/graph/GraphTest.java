package edu.brown.cs.gajith.graph;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import edu.brown.cs.gajith.bacon.Actor;
import edu.brown.cs.gajith.bacon.BaconCommand;
import edu.brown.cs.gajith.bacon.BaconGraphBuilder;
import edu.brown.cs.gajith.bacon.Movie;

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

    BaconCommand bc = new BaconCommand();

    // correct command where connection is created
    bc.mdbCommand("data/bacon/bacon.sqlite3");

    // Create graph builder
    BaconGraphBuilder<GraphNode<Actor>, GraphEdge<Movie>> gb;
    gb = new BaconGraphBuilder<GraphNode<Actor>, GraphEdge<Movie>>(
        bc.getdbHelper());

    // Create graph
    Graph<GraphNode<Actor>, GraphEdge<Movie>> graph;
    graph = new Graph<GraphNode<Actor>, GraphEdge<Movie>>(gb);

    // say actor 1 is Abigail Mason
    String id = bc.getdbHelper().getActorID("Abigail Mason");

    // Create actor and store in node
    Actor actor = new Actor(id, "Abigail Mason");
    GraphNode<Actor> startNode = new GraphNode<Actor>(id, actor);

    // Say final actor is Laurel Bryce
    String id2 = bc.getdbHelper().getActorID("Laurel Bryce");

    // Create actor and store in node
    Actor actor2 = new Actor(id2, "Laurel Bryce");
    GraphNode<Actor> endNode = new GraphNode<Actor>(id2, actor2);

    // Just run djikstra to find final path
    Map<GraphNode<Actor>, GraphEdge<Movie>> shortestPath = graph
        .djikstras(startNode, endNode);

    // Ensure actors are correct
    List<GraphNode<Actor>> actors = new ArrayList<GraphNode<Actor>>(
        shortestPath.keySet());

    assertEquals(actors.get(0).getValue().getName(), "Abigail Mason");
    assertEquals(actors.get(1).getValue().getName(), "Martin Landau");
    assertEquals(actors.get(2).getValue().getName(), "Lisa Marie");
    assertEquals(actors.get(3).getValue().getName(), "Mike Starr");
    assertEquals(actors.get(4).getValue().getName(), "Scarlett Johansson");
    assertEquals(actors.get(5).getValue().getName(), "Justin Long");

    // Ensure movies are correct
    List<GraphEdge<Movie>> movies = new ArrayList<GraphEdge<Movie>>(
        shortestPath.values());
    assertEquals(movies.get(0).getValue().getName(),
        "You'll Never Amount to Anything");
    assertEquals(movies.get(1).getValue().getName(), "Sleepy Hollow");
    assertEquals(movies.get(2).getValue().getName(), "Ed Wood");
    assertEquals(movies.get(3).getValue().getName(), "The Black Dahlia");
    assertEquals(movies.get(4).getValue().getName(),
        "He's Just Not That Into You");
    assertEquals(movies.get(5).getValue().getName(), "After.Life");

  }
}
