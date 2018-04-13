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
 
}
