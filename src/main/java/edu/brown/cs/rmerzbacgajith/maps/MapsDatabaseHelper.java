package edu.brown.cs.rmerzbacgajith.maps;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.brown.cs.rmerzbacgajith.handling.Handling;
import edu.brown.cs.rmerzbacgajith.tree.Node;

/**
 * Class that handles all the queries and caching for Bacon.
 *
 * @author gokulajith
 *
 */
public class MapsDatabaseHelper {
  private Map<String, Node> nodeCache;
  private Map<String, List<Way>> wayCache;
  private Map<String, String> wayIdtoNameCache;

  private static Connection conn = null;

  /**
   * Instantiates all the data structures for querying and caching.
   *
   * @param conn
   *          Connection
   * @param actorNameToIdCache
   *          Cache between actorNames and ids.
   */
  public MapsDatabaseHelper(Connection conn) {

    MapsDatabaseHelper.conn = conn;

    // Cache from MovieID to List of actors in the way
    nodeCache = new HashMap<String, Node>();

    // Cache from actorId to List of Movies the actor has been in.
    wayCache = new HashMap<String, List<Way>>();

    // Cache from MovieID to MovieName
    wayIdtoNameCache = new HashMap<String, String>();
  }

  /**
   * Get name of way from wayID.
   *
   * @param wayId
   *          String id
   * @return String name.
   */
  public String getWayName(String wayId) {

    // Check cache
    if (wayIdtoNameCache.containsKey(wayId)) {
      return wayIdtoNameCache.get(wayId);
    }

    PreparedStatement prep;
    ResultSet rs;
    String wayName = null;

    // Find way name when id is wayId
    try {
      prep = conn.prepareStatement("SELECT name FROM way WHERE id = ?;");
      prep.setString(1, wayId);
      rs = prep.executeQuery();

      while (rs.next()) {

        // Assign wayName
        wayName = rs.getString(1);
      }
      prep.close();
      rs.close();
    } catch (SQLException e) {
      System.out.println("ERROR: Database does not contain proper tables.");
    }

    // Cache and return
    wayIdtoNameCache.put(wayId, wayName);
    return wayName;
  }

  /**
   * Find all the ways an actor has been in.
   *
   * @param actorId
   *          id of actor being searched
   * @return Set of all the ways the actor has been in.
   */
  public List<Way> getWaysFromNode(String nodeId) {

    if (wayCache.containsKey(nodeId)) {
      return wayCache.get(nodeId);
    }

    PreparedStatement prep;
    ResultSet rs;
    List<Way> ways = new ArrayList<Way>();

    // Find the id and name of a way given actor id
    try {
      prep = conn.prepareStatement("SELECT id, name from way WHERE start = ?;");
      prep.setString(1, nodeId);
      rs = prep.executeQuery();

      while (rs.next()) {
        String id = rs.getString(1);
        String name = rs.getString(2);

        // Create way with id and name and add to list
        Way way = new Way(id, name);
        ways.add(way);
      }
      prep.close();
      rs.close();
    } catch (SQLException e) {
      System.out.println("ERROR: Database does not contain proper tables.");
    }

    // Cache and Return the list.
    wayCache.put(nodeId, ways);
    return ways;
  }

  /**
   * Find all the actors in a way.
   *
   * @param wayId
   *          id of the way
   * @return Set of actors in the way
   */
  public Node getEndNodeFromWay(String wayId) {

    // check cache
    if (nodeCache.containsKey(wayId)) {
      return nodeCache.get(wayId);
    }

    PreparedStatement prep;
    ResultSet rs;
    Node endNode = null;

    // Find all actor ids and names with given wayid.
    try {
      prep = conn.prepareStatement(
          "SELECT way.end, node.latitude, node.longitude FROM way, node WHERE node.id = way.end AND way.id = ?;");

      prep.setString(1, wayId);
      rs = prep.executeQuery();

      while (rs.next()) {
        String id = rs.getString(1);
        Double lat = rs.getDouble(2);
        Double lon = rs.getDouble(3);

        double[] coords = { lat, lon };
        // Create new actor with id and name and add to list.
        endNode = new Node(id, coords);
      }
      prep.close();
      rs.close();
    } catch (SQLException e) {
      Handling.error("Database does not contain proper tables.");
    }

    // cache and return the list.

    if (endNode != null) {
      nodeCache.put(wayId, endNode);
    }

    return endNode;
  }

  public Node getIntersection(String way1Name, String way2Name) {
    PreparedStatement prep;
    ResultSet rs;
    ResultSet innerRs;
    ResultSet nodeRs;
    try {
      prep = conn.prepareStatement("SELECT start, end FROM way WHERE name = ?");
      prep.setString(1, way1Name);
      rs = prep.executeQuery();
      while (rs.next()) {
        String currStart1 = rs.getString(1);
        String currEnd1 = rs.getString(2);
        prep = conn
            .prepareStatement("SELECT start, end FROM way WHERE name = ?");
        prep.setString(1, way2Name);
        innerRs = prep.executeQuery();
        while (innerRs.next()) {
          String currStart2 = innerRs.getString(1);
          String currEnd2 = innerRs.getString(2);
          String comparison = "";
          if (currStart1.equals(currEnd2) || currStart1.equals(currEnd2)) {
            comparison = currStart1;
          } else if (currEnd1.equals(currStart2) || currEnd1.equals(currEnd2)) {
            comparison = currEnd1;
          }
          if (comparison != "") {
            prep = conn.prepareStatement(
                "SELECT id, latitude, longitude FROM node WHERE id = ?;");
            prep.setString(1, comparison);
            nodeRs = prep.executeQuery();
            while (nodeRs.next()) {
              String id = nodeRs.getString(1);
              Double lat = nodeRs.getDouble(2);
              Double lon = nodeRs.getDouble(3);

              double[] coords = { lat, lon };
              Node n = new Node(id, coords);

              return n;
            }
          }
        }
      }
    } catch (SQLException e) {
      Handling.error("Database does not contain proper tables.");
    }
    return null;
  }

  public List<Way> getWays(double[] coords1, double[] coords2) {
    PreparedStatement prep;
    ResultSet rs;
    try {
      prep = conn.prepareStatement(
          "SELECT way.id FROM way, node AS n1, node AS n2 WHERE n1.id = "
              + "way.start AND n2.id = way.end "
              + "AND n1.latitude > ? AND n1.latitude < ? AND n1.longitude > ? "
              + "AND n1.longitude < ? AND n2.latitude > ? AND n2.latitude < ? "
              + "AND n2.longitude > ? AND n2.longitude < ? ORDER BY way.id;");
      prep.setDouble(1, coords2[0]);
      prep.setDouble(2, coords1[0]);
      prep.setDouble(3, coords1[1]);
      prep.setDouble(4, coords2[1]);
      prep.setDouble(5, coords2[0]);
      prep.setDouble(6, coords1[0]);
      prep.setDouble(7, coords1[1]);
      prep.setDouble(8, coords2[1]);

      rs = prep.executeQuery();
      while (rs.next()) {
        System.out.println(rs.getString(1));
      }
    } catch (SQLException e) {
      Handling.error("Database does not contain proper tables.");
    }
    return null;
  }

}
