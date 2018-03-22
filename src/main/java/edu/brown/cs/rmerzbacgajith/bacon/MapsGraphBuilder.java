package edu.brown.cs.rmerzbacgajith.bacon;

import java.util.Map;

import edu.brown.cs.rmerzbacgajith.graph.GraphBuilder;
import edu.brown.cs.rmerzbacgajith.graph.GraphEdge;
import edu.brown.cs.rmerzbacgajith.graph.GraphNode;
import edu.brown.cs.rmerzbacgajith.tree.Node;

/**
 * Bacon specific GraphBuilder that implements generic GraphBuilder in order to
 * create and map GraphNodes with their neighboring edges and nodes.
 *
 * @author gokulajith
 *
 * @param <Node>
 *          Node that goes in graph with a value storing an Actor
 * @param <Edge>
 *          Edge that goes in graph with a value storing a Movie
 */
public class MapsGraphBuilder<N, E>
    implements GraphBuilder<GraphNode<Node>, GraphEdge<Way>> {

  // private Map<String, Map<GraphNode<MapNode>, GraphEdge<Way>>> neighCache;
  private MapsDatabaseHelper dbHelper;

  /**
   * Constructor simply stores BaconDatabaseHelper and instantiates
   * neighborCache.
   *
   * @param dbHelper
   *          BaconDatabaseHelper that takes care of all db queries and caching.
   */
  public MapsGraphBuilder(MapsDatabaseHelper dbHelper) {

    // Cache that stores a nodes id to a map that holds its neighboring nodes
    // and the edges that connect them.
    // neighCache = new HashMap<String, Map<GraphNode<MapNode>,
    // GraphEdge<Way>>>();
    this.dbHelper = dbHelper;
  }

  // @Override
  // public Map<GraphNode<MapNode>, GraphEdge<Way>> getNeighbors(
  // GraphNode<MapNode> sourceNode) {
  //
  // Map<GraphNode<MapNode>, GraphEdge<Way>> neighbors;
  // neighbors = new HashMap<GraphNode<MapNode>, GraphEdge<Way>>();
  //
  // // Check cache for neighbors
  // if (neighCache.containsKey(sourceNode.getId())) {
  // neighbors = neighCache.get(sourceNode.getId());
  // } else {
  //
  // // Get actor
  // MapNode sourceActor = sourceNode.getValue();
  // String name = sourceActor.getName();
  // String[] words = name.split(" ");
  //
  // // Find first character of their last name
  // Character lastNameFirstLetter;
  // if (words.length == 1) {
  // lastNameFirstLetter = words[0].charAt(0);
  // } else {
  // lastNameFirstLetter = words[words.length - 1].charAt(0);
  // }
  //
  // // Call helper to find the neighboring nodes and edges
  // neighbors = this.djikstraHelper(sourceActor.getId(), lastNameFirstLetter);
  //
  // // Cache it for next time.
  // neighCache.put(sourceNode.getId(), neighbors);
  // }
  // return neighbors;
  // }
  //
  // /**
  // * Djikstra helper to find neighbors of a node. Finds all movies the actor
  // has
  // * been in, finds all actors in those that fit the letter matching criteria,
  // * and stores that actor in a neighboring node that is connected with an
  // edge
  // * that contains the shared movie between the two actors.
  // *
  // * @param actorId
  // * id of the actor neighbors are being found of
  // * @param lastNameFirstLetter
  // * The first letter of the lastName of the "bacon giver".
  // * @return A map that maps every neighboring node to an edge connecting the
  // * two nodes.
  // */
  // private Map<GraphNode<MapNode>, GraphEdge<Way>> djikstraHelper(String
  // actorId,
  // Character lastNameFirstLetter) {
  //
  // Map<GraphNode<MapNode>, GraphEdge<Way>> nodeToEdgeMap;
  // nodeToEdgeMap = new HashMap<GraphNode<MapNode>, GraphEdge<Way>>();
  //
  // // Use dbhelper to get all movies actor has been in
  // List<Way> movies = dbHelper.getMoviesFromActor(actorId);
  //
  // for (Way movie : movies) {
  //
  // // Use dbhelper to get all actors in the movie
  // List<MapNode> actors = dbHelper.getActorsFromMovie(movie.getId());
  //
  // // Caclulate weight as 1/(#actors in movie)
  // double weight = 1.0 / Double.valueOf(actors.size());
  //
  // // Create edge storing movie
  // GraphEdge<Way> edge = new GraphEdge<Way>(movie.getId(), movie, weight);
  //
  // for (MapNode actor : actors) {
  // Character firstNameFirstLetter = null;
  //
  // if (actor.getName().length() > 0) {
  // firstNameFirstLetter = actor.getName().charAt(0);
  // }
  //
  // // If letter matching condition is satisfied
  // if (lastNameFirstLetter.equals(firstNameFirstLetter)) {
  //
  // // Create node
  // GraphNode<MapNode> node = new GraphNode<MapNode>(actor.getId(),
  // actor);
  //
  // // Make sure the movie with least weight is always being kept in the
  // // Map
  // if (nodeToEdgeMap.containsKey(node)) {
  // GraphEdge<Way> checkMovie = nodeToEdgeMap.get(node);
  // if (checkMovie.getWeight() > edge.getWeight()) {
  //
  // nodeToEdgeMap.put(node, edge);
  // }
  // } else {
  //
  // // if node has not been entered, enter it.
  // nodeToEdgeMap.put(node, edge);
  // }
  // }
  // }
  //
  // }
  //
  // return nodeToEdgeMap;
  // }

  @Override
  public Map<GraphNode<Node>, GraphEdge<Way>> getNeighbors(
      GraphNode<Node> sourceNode) {
    // TODO Auto-generated method stub

    return null;
  }

  @Override
  public Map<GraphNode<Node>, GraphEdge<Way>> getNodeNeighborsCache(
      GraphNode<Node> node) {
    // TODO Auto-generated method stub
    return null;
  }
}
