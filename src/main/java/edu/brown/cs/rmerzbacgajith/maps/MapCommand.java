package edu.brown.cs.rmerzbacgajith.maps;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import edu.brown.cs.rmerzbacgajith.autocorrect.Trie;
import edu.brown.cs.rmerzbacgajith.graph.Graph;
import edu.brown.cs.rmerzbacgajith.graph.GraphEdge;
import edu.brown.cs.rmerzbacgajith.graph.GraphNode;
import edu.brown.cs.rmerzbacgajith.handling.Handling;
import edu.brown.cs.rmerzbacgajith.main.AutocorrectCommand;
import edu.brown.cs.rmerzbacgajith.tree.KDTree;
import edu.brown.cs.rmerzbacgajith.tree.Node;
import edu.brown.cs.rmerzbacgajith.tree.Point;
import edu.brown.cs.rmerzbacgajith.tree.TreeBuilder;

/**
 * BaconCommand Class that handles all of the bacon specific commands and
 * functionality. It is responsible for mdb and connect commands, as well as
 * querying for various information from the database.
 *
 * @author gokulajith
 *
 */
public class MapCommand {
  private static final int DIMENSIONS = 2;

  private static Connection conn = null;
  private AutocorrectCommand acCommandHelper = new AutocorrectCommand();
  private TreeBuilder<Point> builder;
  private MapsGraphBuilder<GraphNode<Node>, GraphEdge<Way>> graphBuilder;
  private Graph<GraphNode<Node>, GraphEdge<Way>> graph = null;
  private MapsDatabaseHelper dbHelper;

  /**
   * Method that deals with the mdb command to connect to a database, as well as
   * creating a Trie with all actorNames on connection to know exactly what
   * names are in the database, as well as for autocorrecting.
   *
   * @param dbname
   *          filepath to database.
   */
  public void mdbCommand(String dbname) {

    // Close conn if previous connection exists.
    if (conn != null) {
      try {
        conn.close();
      } catch (SQLException e) {
        Handling.error("Issue when closing connection to database.");
        return;
      }
    }

    File file = new File(dbname);

    if (!file.exists()) {
      Handling.error("Database could not be found or connected to.");
      return;
    }

    try {
      Class.forName("org.sqlite.JDBC");
    } catch (ClassNotFoundException e) {
      System.out.println("ERROR: Database could not be found or connected to.");
      return;
    }

    // Connect to db
    String urlToDB = new StringBuilder("jdbc:sqlite:" + dbname).toString();

    try {
      conn = DriverManager.getConnection(urlToDB);
      Statement stat = conn.createStatement();
      stat.executeUpdate("PRAGMA foreign_keys = ON;");
    } catch (SQLException ex) {
      System.out.println("ERROR: Database could not be found or connected to.");
      return;
    }

    // Create Trie for autocorrect.
    Trie trie = new Trie();
    HashSet<String> ids = new HashSet<>();
    List<Point> nodeList = new ArrayList<>();

    // Query every actor in db
    PreparedStatement prep;
    ResultSet rs;
    try {
      prep = conn.prepareStatement("SELECT name FROM way;");
      rs = prep.executeQuery();

      while (rs.next()) {
        String name = rs.getString(1);

        // Put all actors name and id mapping in cache and
        // insert the names into trie.
        // (Since you have to insert every name into the Trie, it is better to
        // also save every name and id so this query never
        // needs to happen again.)
        if (name.length() > 0) {
          trie.insert(name);
        }
      }

      prep = conn
          .prepareStatement("SELECT start,end FROM way WHERE type != \"\" AND "
              + "type != \"undefined\";");
      PreparedStatement nodeQuery = conn
          .prepareStatement("SELECT * FROM node WHERE id=? OR id=?");
      ResultSet nodeRs;
      rs = prep.executeQuery();

      while (rs.next()) {
        String startId = rs.getString(1);
        String endId = rs.getString(2);

        nodeQuery.setString(1, startId);
        nodeQuery.setString(2, endId);
        nodeRs = nodeQuery.executeQuery();

        while (nodeRs.next()) {
          String id = nodeRs.getString(1);
          double lat = nodeRs.getDouble(2);
          double lon = nodeRs.getDouble(3);
          double[] coords = { lat, lon };
          Node n = new Node(id, coords);
          if (!ids.contains(id)) {
            nodeList.add(n);
            ids.add(id);
          }
        }
      }
      prep.close();
      rs.close();
    } catch (SQLException e) {
      System.out.println("ERROR: Database does not contain proper tables.");
      return;
    }

    // Set trie to AutocorrectCommand Helper for autocorrect
    acCommandHelper.setTrie(trie);
    builder = new TreeBuilder<Point>(nodeList, DIMENSIONS);
    dbHelper = new MapsDatabaseHelper(conn);

    graphBuilder = new MapsGraphBuilder<GraphNode<Node>, GraphEdge<Way>>(
        dbHelper);

    System.out.println(new StringBuilder("map set to " + dbname).toString());
    return;
  }

  public Point nearestCommand(double[] coords) {
    KDTree<Point> tree = builder.getTree();
    Point nearest = Nearest.handleNeighborsCommandWithCoords(coords, tree);
    return nearest;
  }

  public List<String> suggest(String ac) {
    // Turn prefix on if not on.
    if (!acCommandHelper.getPrefixStatus().equals("on")) {
      String[] prefixOn = new String[2];
      prefixOn[0] = "prefix";
      prefixOn[1] = "on";
      acCommandHelper.setSettings(prefixOn);
    }

    // Use acCommand method to find suggestions.
    String[] input = new String[1];
    input[0] = ac;

    List<String> suggestions = acCommandHelper.acCommand(input);
    return suggestions;
  }

  public void route(Node n1, Node n2) {
    // TODO Auto-generated method stub

    Map<GraphNode<Node>, GraphEdge<Way>> finalAnswer = new LinkedHashMap<GraphNode<Node>, GraphEdge<Way>>();

    graph = new Graph<GraphNode<Node>, GraphEdge<Way>>(graphBuilder);

    GraphNode<Node> node1 = new GraphNode<Node>(n1.getId(), n1);
    GraphNode<Node> node2 = new GraphNode<Node>(n2.getId(), n2);

    // The finalPath is found as a result of calling the djisktras method
    // between the 2 nodes.
    finalAnswer = graph.djikstras(node1, node2);

    if (finalAnswer.size() == 0) {
      System.out.println(
          new StringBuilder(n1.getId() + " -/- " + n2.getId()).toString());
    } else {

      Iterator<GraphNode<Node>> it = finalAnswer.keySet().iterator();

      // Iterate through all the nodes, printing accordingly with the movie from
      // the edge connecting the nodes.
      GraphNode<Node> curr = it.next();
      while (it.hasNext()) {

        GraphNode<Node> next = it.next();

        Way movie = finalAnswer.get(curr).getValue();

        System.out.println(new StringBuilder(curr.getValue().getId() + " -> "
            + next.getValue().getId() + " : " + movie.getId()).toString());

        curr = next;

      }

      // Print final line to target node.
      System.out.println(
          new StringBuilder(curr.getValue().getId() + " -> " + n2.getId()
              + " : " + finalAnswer.get(curr).getValue().getId()).toString());

    }

  }

  public Node getIntersection(String way1Name, String way2Name) {
    return dbHelper.getIntersection(way1Name, way2Name);
  }

  public List<Way> waysCommand(double[] coords1, double[] coords2) {
    return dbHelper.getWays(coords1, coords2);
  }
}
