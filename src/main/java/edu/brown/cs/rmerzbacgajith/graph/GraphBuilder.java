package edu.brown.cs.rmerzbacgajith.graph;

import java.util.Map;

/**
 * Generic Interface that is passed into generic graph in order to build it.
 *
 * @author gokulajith
 *
 * @param <Node>
 *          Nodes in the Graph
 * @param <Edge>
 *          Edges in the Graph
 */
public interface GraphBuilder<Node, Edge> {

  /**
   * Qeury and find each node that borders the sourceNode, and map it to the
   * edge that connects them.
   *
   * @param sourceNode
   *          Node that neighbors are being looked for.
   * @return Map that maps each neighboring node to the edge that connects the
   *         node to the sourceNode.
   */
  Map<Node, Edge> getNeighbors(Node sourceNode);

  /**
   * For a node already visited, return cached Neighbors Map.
   *
   * @param node
   *          already visited node that neighbors need to be found.
   * @return Map that maps each neighboring node to the edge that connects the
   *         node to the sourceNode.
   */
  Map<Node, Edge> getNodeNeighborsCache(Node node);

}
