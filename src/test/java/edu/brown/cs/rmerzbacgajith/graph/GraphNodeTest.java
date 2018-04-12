package edu.brown.cs.gajith.graph;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

/**
 * Tests construction and every public method in GraphNode.
 *
 * @author gokulajith
 *
 */
public class GraphNodeTest {

  /**
   * Test Construction of Node and test both getters for name and value.
   */
  @Test
  public void testNodeConstruction() {

    GraphNode<String> node = new GraphNode<String>("1", "hi");

    assertNotNull(node);

    // Test getters
    assertEquals(node.getId(), "1");
    assertEquals(node.getValue(), "hi");
  }

  /**
   * Test the equals overriden method of nodes to ensure two nodes are being
   * compared correctly.
   */
  @Test
  public void testNodeEquals() {

    GraphNode<String> node = new GraphNode<String>("1", "hi");

    GraphNode<String> sameNode = new GraphNode<String>("1", "hi");
    GraphNode<String> diffnode1 = new GraphNode<String>("2", "hi");
    GraphNode<String> diffnode2 = new GraphNode<String>("1", "hello");

    assertEquals(node, sameNode);
    assertNotEquals(node, diffnode1);
    assertNotEquals(node, diffnode2);

  }

}
