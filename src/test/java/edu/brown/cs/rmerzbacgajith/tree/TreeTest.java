package edu.brown.cs.rmerzbacgajith.tree;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Test;

import edu.brown.cs.rmerzbacgajith.maps.MapCommand;

public class TreeTest {

  @Test
  public void testBigTree() throws IOException {
    MapCommand mc = new MapCommand();
    mc.mapCommand("data/maps/maps.sqlite3");
    KDTree<Point> tree = mc.getBuilder().getTree();
    assertTrue(tree.checkTree());
  }

  @Test
  public void testCheckerOnBadTreeWrongOrder() {
    double[] rootCoords = { 0, 2, 3 };
    double[] lCoords = { 1, 2, 3 };
    double[] rCoords = { 2, 2, 3 };
    TreeNode<Point> left = new TreeNode<Point>(new Point(lCoords), null, null,
        null);
    TreeNode<Point> right = new TreeNode<Point>(new Point(rCoords), null, null,
        null);
    TreeNode<Point> root = new TreeNode<Point>(new Point(rootCoords), null,
        left, right);
    left.setParent(root);
    right.setParent(root);

    KDTree<Point> tree = new KDTree<Point>(root);
    assertFalse(tree.checkTree());

    double[] rootCoords2 = { 2, 2, 3 };
    double[] lCoords2 = { 0, 2, 3 };
    double[] rCoords2 = { 1, 2, 3 };
    TreeNode<Point> left2 = new TreeNode<Point>(new Point(lCoords2), null, null,
        null);
    TreeNode<Point> right2 = new TreeNode<Point>(new Point(rCoords2), null,
        null, null);
    TreeNode<Point> root2 = new TreeNode<Point>(new Point(rootCoords2), null,
        left2, right2);
    left.setParent(root2);
    right.setParent(root2);

    KDTree<Point> tree2 = new KDTree<Point>(root2);
    assertFalse(tree2.checkTree());
  }

  @Test
  public void testCheckerOnBadTreeWrongParent() {
    double[] rootCoords = { 0, 2, 3 };
    double[] lCoords = { -1, 2, 3 };
    double[] rCoords = { 2, 2, 3 };
    TreeNode<Point> left = new TreeNode<Point>(new Point(lCoords), null, null,
        null);
    TreeNode<Point> right = new TreeNode<Point>(new Point(rCoords), null, null,
        null);
    TreeNode<Point> root = new TreeNode<Point>(new Point(rootCoords), null,
        left, right);
    left.setParent(root);
    right.setParent(root);

    KDTree<Point> tree = new KDTree<Point>(root);
    assertTrue(tree.checkTree());

    left.setParent(right);
    assertFalse(tree.checkTree());
  }

  @Test
  public void nodeToString() {
    double[] coords = { 0, 2, 3 };
    TreeNode<Point> left = new TreeNode<Point>();
    TreeNode<Point> right = new TreeNode<Point>();
    TreeNode<Point> n = new TreeNode<Point>(new Point(coords), null, left,
        right);
    assert (n.toString()
        .equals("Item: [(0.0 2.0 3.0)]\n" + "Left:\n" + "    Item: null\n"
            + "  Left: null\n" + "  Right: null\n" + "\n" + "Right:\n"
            + "    Item: null\n" + "  Left: null\n" + "  Right: null\n"));
  }
}
