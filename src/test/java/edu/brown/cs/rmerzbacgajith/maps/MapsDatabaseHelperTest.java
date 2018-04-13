package edu.brown.cs.rmerzbacgajith.maps;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;


/**
 * Ensures every public method in MapsDatabaseHelper is accurate.
 *
 * @author gokulajith
 *
 */
public class MapsDatabaseHelperTest {
  
  /**
   * Tests two methods getWayName(), getEndNodeFromWay(), getWayLocation() which returns the 
   * name, endNode and locations of the start and end nodes of a way given wayId.
   */
  @Test
  public void testWayPropertyQueryingCaching() {
    
    MapCommand bc = new MapCommand();

    // correct command where connection is created
    bc.mapCommand("data/maps/smallMaps.sqlite3");
    assertNotNull(bc.getDBHelper());

    MapsDatabaseHelper dbHelper = bc.getDBHelper();
    
    //if wayId does not exist in db, return null for name.
    String fakeID = "fakeId";
    String fakeName = dbHelper.getWayName(fakeID);
    
    assertNull(fakeName);
    
    //Test getting name, endNode and way with ID
    String id = "/w/3";
    String name = dbHelper.getWayName(id);
    Node endNode = dbHelper.getEndNodeFromWay(id);
    List<double[]> loc = dbHelper.getWayLocation(id);
    
    double[] startLoc = loc.get(0);
    double[] endLoc = loc.get(1);
    
    assertEquals(name, "Sootball Ln");
    assertEquals(endNode.getId(), "/n/4");
    assert(startLoc[0] == 41.8203);
    assert(startLoc[1] == -71.4);
    assert(endLoc[0] == 41.8203);
    assert(endLoc[1] == -71.4003);
     
  }
  
  /**
   * Tests getWaysFromNode() method that gets all ways where input nodeId is
   * start node of the way.
   */
  @Test
  public void testEndNodeFromWay() {
    
    MapCommand bc = new MapCommand();

    // correct command where connection is created
    bc.mapCommand("data/maps/smallMaps.sqlite3");
    assertNotNull(bc.getDBHelper());

    //Note: Node that does not exist does not need to be tested as this is checked before usage of
    //this method.
    MapsDatabaseHelper dbHelper = bc.getDBHelper();
    
    //Check case where node is not start of any way
    String nodeId = "/n/5";
    List<Way> ways = dbHelper.getWaysFromNode(nodeId);
    assertEquals(ways.size(), 0);
    
    //Check multiple ways
    String nId = "/n/1";
    List<Way> ways1 = dbHelper.getWaysFromNode(nId);
    assertEquals(ways1.size(), 2);
    assertEquals(ways1.get(0).getId(), "/w/1");
    assertEquals(ways1.get(1).getId(), "/w/3");
    
     
  }
  
  
  /**
   * Tests getIntersection() method that returns node at an intersection if it exists.
   */
  @Test
  public void testGetIntersection() {
    
    MapCommand bc = new MapCommand();

    // correct command where connection is created
    bc.mapCommand("data/maps/smallMaps.sqlite3");
    assertNotNull(bc.getDBHelper());

    //Note: WayName that does not exist does not need to be tested as this is checked before usage of
    //this method.
    MapsDatabaseHelper dbHelper = bc.getDBHelper();
    
    //Check case where there is no intersection
    Node noIntersect = dbHelper.getIntersection("Sootball Ln", "Kamaji Pl");
    assertNull(noIntersect);
    
    //Check case where intersection exists
    Node intersect = dbHelper.getIntersection("Sootball Ln", "Yubaba St");
    assertEquals(intersect.getId(), "/n/4");
  }
  
  /**
   * Tests getWays() method that returns all ways inside a bounding box of coordinates.
   */
  @Test
  public void testGetWays() {
    
    MapCommand bc = new MapCommand();

    // correct command where connection is created
    bc.mapCommand("data/maps/smallMaps.sqlite3");
    assertNotNull(bc.getDBHelper());

    //Note: WayName that does not exist does not need to be tested as this is checked before usage of
    //this method.
    MapsDatabaseHelper dbHelper = bc.getDBHelper();
    
    //Ensure Expected ways are in box
    double coords1[] = {42, -71.4001};
    double coords2[] = {41.8, -71.3};
    
   List<String> ways = dbHelper.getWays(coords1, coords2);
   assertEquals(ways.size(), 5);
   assertTrue(ways.contains("/w/0"));
   assertTrue(ways.contains("/w/1"));
   assertTrue(ways.contains("/w/2"));
   assertTrue(ways.contains("/w/3"));
   assertTrue(ways.contains("/w/4"));
   
  }
}
