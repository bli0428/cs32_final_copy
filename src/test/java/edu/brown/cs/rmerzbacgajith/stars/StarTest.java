package edu.brown.cs.gajith.stars;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import edu.brown.cs.gajith.commonstructures.KDtree;

/**
 * Tests every method in Star class.
 *
 * @author gokulajith
 *
 */
public class StarTest {

  /**
   * Test construction of any star to make sure it is not null.
   */
  @Test
  public void testRandomConstruction() {

    List<Double> loc = new ArrayList<Double>();
    loc.add(0.0);
    loc.add(0.0);
    loc.add(0.0);

    Star star = new Star(1, "Star", loc);
    assertNotNull(star);
  }

  /**
   * Test construction of any star with id, name, location and distance and make
   * sure values are properly stored.
   */
  @Test
  public void testProperties() {

    List<Double> loc = new ArrayList<Double>();
    loc.add(0.0);
    loc.add(0.0);
    loc.add(0.0);
    double dis = 5.0;

    Star star = new Star(1, "Star", loc);

    star.setDistance(dis);

    assertEquals(star.getName(), "Star");
    assertEquals(star.getID(), 1);
    assertEquals(star.getLocation(), loc);
    assertEquals(star.getDistance(), dis, 0);
  }

  /**
   * Test that setters and getters and hasLeft, hasRight works to access
   * children of all stars.
   */
  @Test
  public void testChildrenMethods() {

    List<Double> loc = new ArrayList<Double>();
    loc.add(0.0);
    loc.add(0.0);
    loc.add(0.0);

    Star star = new Star(1, "Star", loc);

    List<KDnode> left = new ArrayList<KDnode>();
    Star leftChild = new Star(2, "LeftChild", loc);
    left.add(leftChild);

    List<KDnode> right = new ArrayList<KDnode>();
    Star rightChild = new Star(3, "RightChild", loc);
    right.add(rightChild);

    KDtree tree1 = new KDtree(left, 0);
    star.setLeftChild(tree1);
    KDtree tree2 = new KDtree(right, 0);
    star.setRightChild(tree2);

    assertEquals(tree1, star.getLeftChild());
    assertEquals(tree2, star.getRightChild());
    assertEquals(star.hasLeft(), true);
    assertEquals(star.hasRight(), true);
  }

}
