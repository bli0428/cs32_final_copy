package edu.brown.cs.rmerzbacgajith.maps;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.List;

import org.junit.Test;

/**
 * Test all public methods in MapsCommandClass (EXCEPT FOR getIntersection() and waysCommand() as these are tested in MapsDatabaseHelper). 
 * Since MapsCommandClass relies heavily on getNeighbors() which is the only public method in
 * MapsGraphBuilder, this class is also tested here.
 *
 * @author gokulajith
 *
 */
public class MapsCommandAndGraphBuilderTest {
  
  /**
   * Test mdb command, which has the goal of connecting to a database and
   * creating a BaconDatabaseManager. Ensure that on valid connection it is
   * created and on invalid it is not. THis also tests the getter for the
   * DatabaseHelper.
   */
  @Test
  public void testMapCommandandGetDatabaseHelper() {

    MapCommand bc = new MapCommand();

    // incorrect command where connection is created
    bc.mapCommand("randomPath");
    assertNull(bc.getDBHelper());

    // correct command where connection is created
    bc.mapCommand("data/maps/smallMaps.sqlite3");
    assertNotNull(bc.getDBHelper());

  }
  
  /**
   * Test nearest command to ensure nearest node to coords is being found.
   */
  @Test
  public void testNearest() {
    
    MapCommand bc = new MapCommand();
    bc.mapCommand("data/maps/maps.sqlite3");
    
    double coords[] = {41.8340165626703, -71.4036959703903};
    Node nearest = (Node) bc.nearestCommand(coords);
    
    String id = nearest.getId();
    assertEquals(id, "/n/4183.7140.201515111");
  }
  
  /**
   * Test suggest command to ensure suggestions to street names are being found.
   */
  @Test
  public void testSuggest() {
    
    MapCommand bc = new MapCommand();
    bc.mapCommand("data/maps/maps.sqlite3");
    
    assertNotNull(bc.getDBHelper());
    
    List<String> ac = bc.suggest("Br");
    assertEquals(ac.get(0), "Browns Corner Road");
    assertEquals(ac.get(1), "Broad Street");
    assertEquals(ac.get(2), "Broadway");
    assertEquals(ac.get(3), "Brown Avenue");
    assertEquals(ac.get(4), "Brentwood Drive");
    
    List<String> ac1 = bc.suggest("Brown S");
    assertEquals(ac1.get(0), "Brown Street");
    assertEquals(ac1.get(1), "Brown School Road");
    assertEquals(ac1.get(2), "Brown Stadium");
    
  }
  
  /**
   * Test route command to ensure the correct route of ways is being found.
   */
  @Test
  public void testRoute() {
    
    MapCommand bc = new MapCommand();
    bc.mapCommand("data/maps/maps.sqlite3");
    
    assertNotNull(bc.getDBHelper());
    
    //NOTE that there is no need to test for bad input as this is checked before route command.
    
    //Test no path command
    Node n1 = bc.getIntersection("Thayer Street", "Waterman Street");
    Node n2 = bc.getIntersection("Thayer Street", "Waterman Street");
    List<Way> path = bc.route(n1, n2);
    assertEquals(path.size(), 0);
    
    //Test normal route accuracy
    Node start = bc.getIntersection("Thayer Street", "Waterman Street");
    Node end = bc.getIntersection("Thayer Street", "Charlesfield Street");
    List<Way> finalPath = bc.route(start, end);
    assertEquals(finalPath.size(), 10);
    assertEquals(finalPath.get(0).getId(), "/w/4182.7140.90091982.0.1");
    assertEquals(finalPath.get(1).getId(), "/w/4182.7140.90091982.1.1");
    assertEquals(finalPath.get(2).getId(), "/w/4182.7140.90091982.2.1");
    assertEquals(finalPath.get(3).getId(), "/w/4182.7140.90091982.3.1");
    assertEquals(finalPath.get(4).getId(), "/w/4182.7140.90091982.4.1");
    assertEquals(finalPath.get(5).getId(), "/w/4182.7140.90091982.5.1");
    assertEquals(finalPath.get(6).getId(), "/w/4182.7140.153984675.0.1");
    assertEquals(finalPath.get(7).getId(), "/w/4182.7140.153984675.1.1");
    assertEquals(finalPath.get(8).getId(), "/w/4182.7140.153984675.2.1");
    assertEquals(finalPath.get(9).getId(), "/w/4182.7140.153984675.3.1");
    
  }
 
}
