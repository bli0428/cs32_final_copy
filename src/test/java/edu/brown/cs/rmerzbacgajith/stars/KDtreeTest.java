package edu.brown.cs.gajith.stars;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import edu.brown.cs.gajith.commonstructures.KDtree;

/**
 * Test all public methods in KDtree. NOTE: All of the private methods,
 * including findNeighbors and findRadius, were tested as a result of the tests
 * in Repl class.
 *
 * @author gokulajith
 *
 */
public class KDtreeTest {

  /**
   * Test Construction and getNode method.
   */
  @Test
  public void testConstructionAndSortDimension() {

    List<Double> loc = new ArrayList<Double>();
    loc.add(0.0);
    loc.add(2.0);
    loc.add(0.0);

    List<Double> loc1 = new ArrayList<Double>();
    loc1.add(1.0);
    loc1.add(1.0);
    loc1.add(0.0);

    List<Double> loc2 = new ArrayList<Double>();
    loc2.add(2.0);
    loc2.add(0.0);
    loc2.add(0.0);

    List<Double> loc3 = new ArrayList<Double>();
    loc3.add(3.0);
    loc3.add(5.0);
    loc3.add(0.0);

    List<Double> loc4 = new ArrayList<Double>();
    loc4.add(4.0);
    loc4.add(6.0);
    loc4.add(0.0);

    Star star = new Star(1, "Star", loc1);
    Star star1 = new Star(2, "Star", loc3);
    Star star2 = new Star(3, "Star", loc);
    Star star3 = new Star(4, "Star", loc4);
    Star star4 = new Star(5, "Star", loc2);

    List<KDnode> list = new ArrayList<KDnode>();
    list.add(star);
    list.add(star1);
    list.add(star2);
    list.add(star3);
    list.add(star4);

    KDtree tree = new KDtree(list, 0);

    assertEquals(tree.getSize(), 5);
    assertEquals(tree.getNode().getID(), 5);
    assertEquals(star4.getLeftChild().getNode(), star2);
    assertEquals(star4.getRightChild().getNode(), star3);
    assertEquals(star2.getLeftChild().getNode(), star);
    assertEquals(star3.getLeftChild().getNode(), star1);
  }

  /**
   * Test findNode and getFoundNode method.
   */
  @Test
  public void testFindNodeMethods() {
    List<Double> loc = new ArrayList<Double>();
    loc.add(0.0);
    loc.add(0.0);
    loc.add(0.0);

    Star star = new Star(1, "Star", loc);
    Star star1 = new Star(2, "Star1", loc);
    Star star2 = new Star(3, "findMe", loc);

    List<KDnode> list = new ArrayList<KDnode>();
    list.add(star);
    list.add(star1);
    list.add(star2);

    KDtree tree = new KDtree(list, 0);
    tree.findNode("findMe", tree.getNode());

    assertEquals(tree.getFoundNode(), star2);

  }

}
