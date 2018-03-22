package edu.brown.cs.rmerzbacgajith.bacon;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableMap;

import edu.brown.cs.rmerzbacgajith.autocorrect.Trie;
import edu.brown.cs.rmerzbacgajith.graph.Graph;
import edu.brown.cs.rmerzbacgajith.graph.GraphEdge;
import edu.brown.cs.rmerzbacgajith.graph.GraphNode;
import edu.brown.cs.rmerzbacgajith.main.AutocorrectCommand;

/**
 * BaconCommand Class that handles all of the bacon specific commands and
 * functionality. It is responsible for mdb and connect commands, as well as
 * querying for various information from the database.
 *
 * @author gokulajith
 *
 */
public class BaconCommand {

  private static Connection conn = null;
  private Graph<GraphNode<MapNode>, GraphEdge<Way>> graph = null;
  private MapsGraphBuilder<GraphNode<MapNode>, GraphEdge<Way>> builder;
  private Map<GraphNode<MapNode>, GraphEdge<Way>> finalAnswer;
  private MapsDatabaseHelper dbHelper;
  private AutocorrectCommand acCommandHelper = new AutocorrectCommand();

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
        System.out.println("ERROR: Issue when closing connection to database.");
        return;
      }
    }

    File file = new File(dbname);

    if (!file.exists()) {
      System.out.println("ERROR: Database could not be found or connected to.");
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

    System.out.println(new StringBuilder("db set to " + dbname).toString());

    // Create new ctorName to ID mapping cache, and Trie for
    // autocorrect.

    Map<String, String> actorNameToIdCache = new HashMap<String, String>();
    Trie trie = new Trie();

    // Query every actor in db
    PreparedStatement prep;
    ResultSet rs;
    try {
      prep = conn.prepareStatement("SELECT * from actor");
      rs = prep.executeQuery();

      while (rs.next()) {
        String id = rs.getString(1);
        String name = rs.getString(2);

        // Put all actors name and id mapping in cache and
        // insert the names into trie.
        // (Since you have to insert every name into the Trie, it is better to
        // also save every name and id so this query never
        // needs to happen again.)
        if (name.length() > 0) {
          actorNameToIdCache.put(name, id);
          trie.insert(name);
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

    // Create a new DatabaseHelper to handle all queries and caching to this new
    // database.
    dbHelper = new MapsDatabaseHelper(conn, actorNameToIdCache);

    // Use the database helper to create a new graph builder.
    builder = new MapsGraphBuilder<GraphNode<MapNode>, GraphEdge<Way>>(
        dbHelper);

    return;

  }

  /**
   * Method that handles connecting two actor names.
   *
   * @param name1
   *          String actor name 1
   * @param name2
   *          String actor name 2
   */
  public void connectCommand(String name1, String name2) {

    finalAnswer = new LinkedHashMap<GraphNode<MapNode>, GraphEdge<Way>>();

    // If no db, error.
    if (conn == null) {
      System.out.println("ERROR: No database has been connected to.");
      return;
    }

    String id1 = dbHelper.getActorID(name1);
    String id2 = dbHelper.getActorID(name2);

    // Ensure both names are in db or error.
    if (id1 == null) {
      System.out
          .println("ERROR: First input actor could not be found in database");
      return;
    }

    if (id2 == null) {
      System.out
          .println("ERROR: Second input actor could not be found in database");
      return;
    }

    // Create both actors, and GraphNodes that contain the actor values.
    MapNode actor1 = new MapNode(id1, name1);
    GraphNode<MapNode> node1 = new GraphNode<MapNode>(id1, actor1);

    MapNode actor2 = new MapNode(id2, name2);
    GraphNode<MapNode> node2 = new GraphNode<MapNode>(id2, actor2);

    // Create a new Graph using the passed in BaconGraphBuilder
    graph = new Graph<GraphNode<MapNode>, GraphEdge<Way>>(builder);

    // The finalPath is found as a result of calling the djisktras method
    // between the 2 nodes.
    finalAnswer = graph.djikstras(node1, node2);

    // If it is empty, no path.
    if (finalAnswer.size() == 0) {
      System.out.println(new StringBuilder(name1 + " -/- " + name2).toString());
    } else {

      Iterator<GraphNode<MapNode>> it = finalAnswer.keySet().iterator();

      // Iterate through all the nodes, printing accordingly with the movie from
      // the edge connecting the nodes.
      GraphNode<MapNode> curr = it.next();
      while (it.hasNext()) {

        GraphNode<MapNode> next = it.next();

        Way movie = finalAnswer.get(curr).getValue();

        System.out.println(new StringBuilder(curr.getValue().getName() + " -> "
            + next.getValue().getName() + " : " + movie.getName()).toString());

        curr = next;

      }

      // Print final line to target node.
      System.out
          .println(new StringBuilder(curr.getValue().getName() + " -> " + name2
              + " : " + finalAnswer.get(curr).getValue().getName()).toString());

    }
  }

  /**
   * Method that uses AutocorrectCommand class to autocorrect user's typing
   * actor names.
   *
   * @param ac
   *          String user input
   * @return List of ranked suggestions
   */

  public List<String> autocorrect(String ac) {

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

  /**
   * Return immutable Map of the finalPath that Djikstra found between the 2
   * nodes.
   *
   * @return Map that maps the Nodes and Edges in order from start to end node.
   */
  public Map<GraphNode<MapNode>, GraphEdge<Way>> getAnswer() {
    return new ImmutableMap.Builder<GraphNode<MapNode>, GraphEdge<Way>>()
        .putAll(this.finalAnswer).build();
  }

  /**
   * Getter for BaconDatabaseHelper that has many queries to db with caching.
   *
   * @return BaconDatabaseHelper
   */
  public MapsDatabaseHelper getdbHelper() {
    return dbHelper;
  }
}
