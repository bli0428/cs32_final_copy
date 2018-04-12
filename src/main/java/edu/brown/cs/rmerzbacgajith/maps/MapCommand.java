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

import edu.brown.cs.rmerzbacgajith.autocorrect.AutocorrectCommand;
import edu.brown.cs.rmerzbacgajith.autocorrect.Trie;
import edu.brown.cs.rmerzbacgajith.graph.Graph;
import edu.brown.cs.rmerzbacgajith.graph.GraphEdge;
import edu.brown.cs.rmerzbacgajith.graph.GraphNode;
import edu.brown.cs.rmerzbacgajith.handling.Handling;
import edu.brown.cs.rmerzbacgajith.tree.KDTree;
import edu.brown.cs.rmerzbacgajith.tree.Node;
import edu.brown.cs.rmerzbacgajith.tree.Point;
import edu.brown.cs.rmerzbacgajith.tree.TreeBuilder;

/**
 * MapCommand Class that handles all of the map specific commands and
 * functionality.
 *
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
   * Method that deals with the map command to connect to a database, as well as
   * creating a Trie with all way Names on connection to know exactly what
   * names are in the database, as well as for autocorrecting. This also populates a KDTree with
   * all the traversable nodes for the nearest command, as well as a MapsGraphBuilder and MapsDatabaseHelper for 
   * route commands and any queries that need to be made to the database.
   *
   * @param dbname
   *          filepath to database.
   */
  public void mapCommand(String dbname) {

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
    
    //Structures to hold query results
    HashSet<String> ids = new HashSet<>();
    List<Point> nodeList = new ArrayList<>();

    PreparedStatement prep;
    ResultSet rs;
    try {
      prep = conn.prepareStatement("SELECT name, start, end, type FROM way;");
      PreparedStatement nodeQuery = conn
          .prepareStatement("SELECT * FROM node WHERE id=? OR id=?");
      ResultSet nodeRs = null;
      rs = prep.executeQuery();

      // Query every Way in db
      while (rs.next()) {
        String name = rs.getString(1);
        String startId = rs.getString(2);
        String endId = rs.getString(3);
        String type = rs.getString(4);

        //If Way has a name, add it to the Trie.
        if (name.length() > 0) {
          trie.insert(name);
        }

        //If the way is traversable
        if (!type.equals("") && !type.equals("unclassified")) {
          nodeQuery.setString(1, startId);
          nodeQuery.setString(2, endId);
          nodeRs = nodeQuery.executeQuery();

          //Find the start and end nodes of the way
          while (nodeRs.next()) {
            String id = nodeRs.getString(1);
            double lat = nodeRs.getDouble(2);
            double lon = nodeRs.getDouble(3);
            
            //Store the lats and lons and create a Node Object
            double[] coords = { lat, lon };
            Node n = new Node(id, coords);
            
            //Add it to the structures.
            if (!ids.contains(id)) {
              nodeList.add(n);
              ids.add(id);
            }
          }
        }
      }
      nodeQuery.close();
      nodeRs.close();
      prep.close();
      rs.close();
    } catch (SQLException e) {
      System.out.println("ERROR: Database does not contain proper tables.");
      return;
    }

    // Set trie to AutocorrectCommand Helper for autocorrecting Way names.
    acCommandHelper.setTrie(trie);
    
    //Build KDTree of Traversable nodes using TreeBuilder.
    builder = new TreeBuilder<Point>(nodeList, DIMENSIONS);
    
    //Create dbHelper for queries that will be needed.
    dbHelper = new MapsDatabaseHelper(conn);

    //Create graphBuilder that helps create the graph that will be used for route command.
    graphBuilder = new MapsGraphBuilder<GraphNode<Node>, GraphEdge<Way>>(
        dbHelper);

    System.out.println(new StringBuilder("map set to " + dbname).toString());
    return;
  }

  /**
   * Method that handles nearest command.
   * @param coords Latitude and Longitude specified by user.
   * @return Traverable node that is nearest to the coords.
   */
  public Point nearestCommand(double[] coords) {
    
    if(conn == null) {
      Handling.error("No connection to database.");
    }
    
    //Get KDTree from TreeBuilder
    KDTree<Point> tree = builder.getTree();
    
    //Use Static Nearest Class to find the nearest Node and return (note that Node extends Point).
    Point nearest = Nearest.handleNeighborsCommandWithCoords(coords, tree);
    return nearest;
  }

  /**
   * Method that handles suggest command using autocorrect.
   * @param ac String to be autocorrected
   * @return List of suggestions as to what String should be.
   */
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

  /**
   * Method that handles route command between 2 nodes.
   * @param n1 start node
   * @param n2 destination node
   * @return List of Ways that can be taken to go between the two nodes as fast as possible.
   */
  public List<Way> route(Node n1, Node n2) {

    Map<GraphNode<Node>, GraphEdge<Way>> finalAnswer = new LinkedHashMap<GraphNode<Node>, GraphEdge<Way>>();

    //Create new graph using GraphBuilder
    graph = new Graph<GraphNode<Node>, GraphEdge<Way>>(graphBuilder);

    //Create 2 GraphNodes storing the start and dest nodes.
    GraphNode<Node> node1 = new GraphNode<Node>(n1.getId(), n1);
    GraphNode<Node> node2 = new GraphNode<Node>(n2.getId(), n2);

    // The finalPath is found as a result of calling the djisktras method
    // between the 2 nodes.
    finalAnswer = graph.djikstras(node1, node2);

    List<Way> finalWays = new ArrayList<Way>();

    //If no path is found
    if (finalAnswer.size() == 0) {
      System.out.println(
          new StringBuilder(n1.getId() + " -/- " + n2.getId()).toString());
    } else {

      Iterator<GraphNode<Node>> it = finalAnswer.keySet().iterator();

      // Iterate through all the nodes, printing accordingly with the way from
      // the edge connecting the nodes.
      GraphNode<Node> curr = it.next();
      while (it.hasNext()) {

        GraphNode<Node> next = it.next();

        //Add Way to the final Ways arraylist.
        Way currWay = finalAnswer.get(curr).getValue();
        finalWays.add(currWay);

        System.out.println(new StringBuilder(curr.getValue().getId() + " -> "
            + next.getValue().getId() + " : " + currWay.getId()).toString());

        curr = next;

      }

      // Print final line to destination node.
      System.out.println(
          new StringBuilder(curr.getValue().getId() + " -> " + n2.getId()
              + " : " + finalAnswer.get(curr).getValue().getId()).toString());

      finalWays.add(finalAnswer.get(curr).getValue());

    }

    return finalWays;
  }

  /**
   * Use dbHelper to see if there is an intersection at the corner of the two input Ways.
   * @param way1Name Name of Way 1
   * @param way2Name Name of Way 2
   * @return Node that is at the intersection between them, or null if there is no intersection. 
   */
  public Node getIntersection(String way1Name, String way2Name) {
    return dbHelper.getIntersection(way1Name, way2Name);
  }

  /**
   * Use coords to find all the ways in the bounding box created by them.
   * @param coords1 Northwest corner coords
   * @param coords2 Southeast corner coords
   * @return List of all wayIDs that are inside the box.
   *
   */
  public List<String> waysCommand(double[] coords1, double[] coords2) {
    return dbHelper.getWays(coords1, coords2);
  }

  /**
   * Getter for the dbHelper
   * @return dbHelper used in MapCommand.
   */
  public MapsDatabaseHelper getDBHelper() {
    return dbHelper;
  }
}
