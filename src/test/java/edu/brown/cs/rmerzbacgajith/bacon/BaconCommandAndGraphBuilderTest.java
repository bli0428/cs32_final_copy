package edu.brown.cs.gajith.bacon;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import edu.brown.cs.gajith.graph.GraphEdge;
import edu.brown.cs.gajith.graph.GraphNode;

/**
 * Test all public methods in BaconCommandClass. Since BaconCommandClass relies
 * heavily on getNeighbors() which is the only public method in
 * BaconGraphBuilder, this class is also tested here.
 *
 * @author gokulajith
 *
 */
public class BaconCommandAndGraphBuilderTest {

  /**
   * Test mdb command, which has the goal of connecting to a database and
   * creating a BaconDatabaseManager. Ensure that on valid connection it is
   * created and on invalid it is not. THis also tests the getter for the
   * DatabaseHelper.
   */
  @Test
  public void testMDBCommandandGetDatabaseHelper() {

    BaconCommand bc = new BaconCommand();

    // incorrect command where connection is created
    bc.mdbCommand("randomPath");
    assertNull(bc.getdbHelper());

    // correct command where connection is created
    bc.mdbCommand("data/bacon/bacon.sqlite3");
    assertNotNull(bc.getdbHelper());

  }

  /**
   * Test connectCommand which takes in two names and finds the shortest path
   * between the 2 actors. This also checks the getFinalAnswer() getter that
   * stores a Map containing the path. This also ensures BaconGraphBuilder is
   * building the Graph properly with its one public method that is used to find
   * the path.
   */
  @Test
  public void testConnectCommandandGetFinalAnswer() {

    BaconCommand bc = new BaconCommand();

    bc.mdbCommand("data/bacon/smallBacon.sqlite3");

    // Try case of error connect Command since actor does not exist
    bc.connectCommand("Random Actor", "Sylvester Stallone");
    assertEquals(bc.getAnswer().size(), 0);

    // try no path command
    bc.connectCommand("David S. Howard", "Sigourney Weaver");
    assertEquals(bc.getAnswer().size(), 0);

    // try accurate command
    bc.connectCommand("Taylor Swift", "Sylvester Stallone");

    assertNotNull(bc.getAnswer());
    assertEquals(bc.getAnswer().keySet().size(), 5);

    // Ensure actors are correct
    List<GraphNode<Actor>> actors = new ArrayList<GraphNode<Actor>>(
        bc.getAnswer().keySet());

    assertEquals(actors.get(0).getValue().getName(), "Taylor Swift");
    assertEquals(actors.get(1).getValue().getName(), "Shirley McLaine");
    assertEquals(actors.get(2).getValue().getName(), "Matthew McConaughey");
    assertEquals(actors.get(3).getValue().getName(), "Mini Anden");
    assertEquals(actors.get(4).getValue().getName(), "Amy Stiller");

    // Ensure movies are correct
    List<GraphEdge<Movie>> movies = new ArrayList<GraphEdge<Movie>>(
        bc.getAnswer().values());
    assertEquals(movies.get(0).getValue().getName(), "Valentine's Day");
    assertEquals(movies.get(1).getValue().getName(), "Bernie");
    assertEquals(movies.get(2).getValue().getName(), "Tropic Thunder");
    assertEquals(movies.get(3).getValue().getName(), "Tropic Thunder");
    assertEquals(movies.get(4).getValue().getName(),
        "Lovers and Other Strangers");

  }

  /**
   * Test Autocorrecting an actors name.
   */
  @Test
  public void testAutocorrect() {
    BaconCommand bc = new BaconCommand();

    // correct command where connection is created
    bc.mdbCommand("data/bacon/bacon.sqlite3");
    assertNotNull(bc.getdbHelper());

    List<String> ac = bc.autocorrect("Bra");
    assertEquals(ac.get(0), "Brad Morris");
    assertEquals(ac.get(1), "Brandon Lee");
    assertEquals(ac.get(2), "Brandon Smith");
    assertEquals(ac.get(3), "Brad Armstrong");
    assertEquals(ac.get(4), "Brad Attitude");

    List<String> ac1 = bc.autocorrect("Taylor S");
    assertEquals(ac1.get(0), "Taylor Sanches");
    assertEquals(ac1.get(1), "Taylor Schilling");
    assertEquals(ac1.get(2), "Taylor Sheridan");
    assertEquals(ac1.get(3), "Taylor Simpson");
    assertEquals(ac1.get(4), "Taylor St. Clair");

  }

}
