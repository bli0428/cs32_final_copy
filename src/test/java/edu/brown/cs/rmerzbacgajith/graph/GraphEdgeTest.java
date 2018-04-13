package edu.brown.cs.rmerzbacgajith.graph;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

/**
 * Test all public methods in GraphEdge construction and getters for properties.
 *
 * @author gokulajith
 *
 */
public class GraphEdgeTest {
  /**
   * Test Construction of Edge and test getters for name, value, weight.
   */
  @Test
  public void testEdgeConstruction() {

    double weight = 5.5;
    GraphEdge<String> edge = new GraphEdge<String>("1", "hi", weight);

    assertNotNull(edge);

    // Test getters
    assertEquals(edge.getId(), "1");
    assertEquals(edge.getValue(), "hi");
    assertEquals(edge.getWeight(), weight, 0.01);
  }

  /**
   * Test the equals overriden method of edges to ensure two edges are being
   * compared correctly.
   */
  @Test
  public void testNodeEquals() {

    GraphEdge<String> edge = new GraphEdge<String>("1", "hi", 5.5);

    GraphEdge<String> sameEdge = new GraphEdge<String>("1", "hi", 5.5);
    GraphEdge<String> diffedge1 = new GraphEdge<String>("2", "hi", 5.5);
    GraphEdge<String> diffedge2 = new GraphEdge<String>("1", "hello", 5.5);

    assertEquals(edge, sameEdge);
    assertNotEquals(edge, diffedge1);
    assertNotEquals(edge, diffedge2);

  }

}
