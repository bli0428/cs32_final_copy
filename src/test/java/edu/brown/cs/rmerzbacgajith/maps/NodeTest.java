package edu.brown.cs.rmerzbacgajith.maps;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import edu.brown.cs.rmerzbacgajith.maps.Node;

/**
 * Test every public method in the Node class.
 *
 * @author gokulajith
 *
 */
public class NodeTest {

  /**
   * Test Construction of Node and test both setters and getters for 
   * id and coords.
   */
  @Test
  public void testNodeConstruction() {

    double[] coords = {1, 2};
    Node node = new Node("id1", coords);

    assertNotNull(node);
    assertEquals(node.getId(), "id1");
    assertEquals(node.getCoords(), coords);

    // Setter for id
    node.setID("id2");

    assertEquals(node.getId(), "id2");

  }

  /**
   * Test the equals overriden method of Node to ensure two Nodes are being
   * compared correctly.
   */
  @Test
  public void testNodesEquals() {

    double[] coords = {1, 2};
    
    Node node = new Node("id1", coords);
    
    //Node that has same ID
    Node sameNode = new Node("id1", coords);

    //Node that has different ID
    Node diffNode = new Node("id2", coords);

    assertEquals(node, sameNode);
    assertNotEquals(node, diffNode);

  }

}
