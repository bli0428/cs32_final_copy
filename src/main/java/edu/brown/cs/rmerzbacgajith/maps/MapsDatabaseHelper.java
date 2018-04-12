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
 * Class that handles all the queries and caching for Maps.
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
   * Find all the Traversable ways that starts at a node.
   *
   * @param nodeId
   *          id of node being searched
   * @return Set of all the traversable ways that start with input node.
   */
  public List<Way> getWaysFromNode(String nodeId) {

    // Check cache
    if (wayCache.containsKey(nodeId)) {
      return wayCache.get(nodeId);
    }

    PreparedStatement prep;
    ResultSet rs;
    List<Way> ways = new ArrayList<Way>();

    // Find the id and name of a traversable way given node id
    try {
      prep = conn.prepareStatement("SELECT id, name FROM way WHERE start = ?"
          + " AND type != \"\" AND type != \"unclassified\";");
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
   * Find endNode of a way.
   *
   * @param wayId
   *          id of the way
   * @return End Node of the Way
   */
  public Node getEndNodeFromWay(String wayId) {

    // check cache
    if (nodeCache.containsKey(wayId)) {
      return nodeCache.get(wayId);
    }

    PreparedStatement prep;
    ResultSet rs;
    Node endNode = null;

    // Find end node and its lat, lon with given wayid.
    try {
      prep = conn
          .prepareStatement("SELECT way.end, node.latitude, node.longitude "
              + "FROM way, node WHERE node.id = way.end AND way.id = ?;");

      prep.setString(1, wayId);
      rs = prep.executeQuery();

      while (rs.next()) {
        String id = rs.getString(1);
        Double lat = rs.getDouble(2);
        Double lon = rs.getDouble(3);

        double[] coords = { lat, lon };

        // Create new Node with id and coords.
        endNode = new Node(id, coords);
      }
      prep.close();
      rs.close();
    } catch (SQLException e) {
      Handling.error("Database does not contain proper tables.");
    }

    // cache and return the endNode.

    if (endNode != null) {
      nodeCache.put(wayId, endNode);
    }

    return endNode;
  }

  /**
   * Return an intersection node at the two Ways if one exists.
   *
   * @param way1Name
   *          Way 1 name
   * @param way2Name
   *          Way 2 name
   * @return Node at intersection of both ways.
   */
  public Node getIntersection(String way1Name, String way2Name) {
    PreparedStatement prep;
    ResultSet rs;
    ResultSet innerRs;
    ResultSet nodeRs;
    try {

      // Query first Way
      prep = conn
          .prepareStatement("SELECT start, end FROM way WHERE name = ? AND "
              + "type != \"\" AND type != \"unclassified\";");
      prep.setString(1, way1Name);
      rs = prep.executeQuery();
      while (rs.next()) {
        String currStart1 = rs.getString(1);
        String currEnd1 = rs.getString(2);

        // Query Second way
        prep = conn
            .prepareStatement("SELECT start, end FROM way WHERE name = ? "
                + "AND type != \"\" AND type != \"unclassified\";");
        prep.setString(1, way2Name);
        innerRs = prep.executeQuery();
        while (innerRs.next()) {
          String currStart2 = innerRs.getString(1);
          String currEnd2 = innerRs.getString(2);
          String comparison = "";

          // Check that there is an intersection, and set the comparison to be
          // that node.
          if (currStart1.equals(currEnd2) || currStart1.equals(currEnd2)) {
            comparison = currStart1;
          } else if (currEnd1.equals(currStart2) || currEnd1.equals(currEnd2)) {
            comparison = currEnd1;
          }

          // If a node is found
          if (comparison != "") {

            // Query for node id and coords
            prep = conn.prepareStatement(
                "SELECT id, latitude, longitude FROM node WHERE id = ?;");
            prep.setString(1, comparison);
            nodeRs = prep.executeQuery();
            while (nodeRs.next()) {
              String id = nodeRs.getString(1);
              Double lat = nodeRs.getDouble(2);
              Double lon = nodeRs.getDouble(3);

              // Create node and return
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

  /**
   * Query that handles way command by query for nodes based on the input
   * coords.
   *
   * @param coords1
   *          Northwest point of box
   * @param coords2
   *          Southeast point of box
   * @return List of wayIds that are inside the box.
   */
  public List<String> getWays(double[] coords1, double[] coords2) {
    PreparedStatement prep;
    ResultSet rs;
    List<String> ids = new ArrayList<>();
    try {

      // Query for all nodes that are within the box by setting conditionals for
      // the latitude and longitude.
      prep = conn
          .prepareStatement("SELECT way.id FROM way, node AS n1, node AS n2 "
              + "WHERE n1.id = way.start AND n2.id = way.end "
              + "AND ((n1.latitude > ? AND n1.latitude < ? "
              + "AND n1.longitude > ? "
              + "AND n1.longitude < ?) OR (n2.latitude > ? AND n2.latitude < ? "
              + "AND n2.longitude > ? AND n2.longitude < ?)) ORDER BY way.id;");

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
        String id = rs.getString(1);

        // Print wayId
        System.out.println(id);
        ids.add(id);
      }
      return ids;
    } catch (SQLException e) {
      Handling.error("Database does not contain proper tables.");
    }
    return null;
  }

  /**
   * Gets the location of a way based on its start and end nodes.
   *
   * @param wayId
   *          Way that location must be found of
   * @return List of doubles[] where first index is startNode coords and second
   *         index is endNode coords.
   */
  public List<double[]> getWayLocation(String wayId) {
    PreparedStatement prep;
    ResultSet rs;
    try {

      // Find coords of the start node of the Way
      double[] coords1 = new double[2];
      prep = conn.prepareStatement("SELECT latitude, longitude FROM node, way "
          + "WHERE way.id = ? AND node.id = way.start;");
      prep.setString(1, wayId);

      rs = prep.executeQuery();
      while (rs.next()) {
        coords1[0] = rs.getDouble(1);
        coords1[1] = rs.getDouble(2);
      }

      // Find coords of the end node of the Way
      double[] coords2 = new double[2];
      prep = conn.prepareStatement("SELECT latitude, longitude FROM node, way "
          + "WHERE way.id = ? AND node.id = way.end;");
      prep.setString(1, wayId);

      rs = prep.executeQuery();
      while (rs.next()) {
        coords2[0] = rs.getDouble(1);
        coords2[1] = rs.getDouble(2);
      }

      // Add both coords to the list and return.
      List<double[]> coords = new ArrayList<>();
      coords.add(coords1);
      coords.add(coords2);
      return coords;
    } catch (SQLException e) {
      Handling.error("Database does not contain proper tables.");
    }
    return null;
  }

}
