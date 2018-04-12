package edu.brown.cs.rmerzbacgajith.maps;

import java.util.ArrayList;

import edu.brown.cs.rmerzbacgajith.handling.Handling;
import edu.brown.cs.rmerzbacgajith.tree.KDTree;
import edu.brown.cs.rmerzbacgajith.tree.Node;
import edu.brown.cs.rmerzbacgajith.tree.Point;
import edu.brown.cs.rmerzbacgajith.tree.TreeBuilder;
import edu.brown.cs.rmerzbacgajith.tree.TreeNode;

/**
 * A class that handles the "nearest" command.
 *
 */
public class Nearest {
  protected Nearest() {
  }

  /**
   * Finds the closest node to the coordinates.
   *
   * @param coords
   *          the desired coordinates
   * @param tree
   *          the current K-D tree
   * @return the nearest Node to coordinates.
   */
  public static Point handleNeighborsCommandWithCoords(double[] coords,
      KDTree<Point> tree) {
    if (tree == null || tree.getRoot() == null) {
      Handling.error("no input data");
      return null;
    }
    return getNeighbors(coords, tree);
  }

  /**
   * Gets the closest neighbor.
   *
   * @param coords
   *          the desired coordinates
   * @param tree
   *          the current K-D tree
   * @return Closest Point to the coords.
   */
  public static Point getNeighbors(double[] coords, KDTree<Point> tree) {

    TreeNode<Point> first = findFirstLeaf(coords, tree.getRoot(), 0,
        coords.length);
    return findClosestPoint(coords, tree.getRoot(), first, 0, coords.length)
        .getItem();
  }

  /**
   * Finds the closest node to the coordinates.
   *
   * @param coords
   *          the desired coordinates
   * @param currNode
   *          the current node
   * @param currBest
   *          the best node at the current point in the search
   * @param currDim
   *          the current dimension that is being compared
   * @param dimensions
   *          the number of dimensions
   * @return the closest node.
   */
  public static TreeNode<Point> findClosestPoint(double[] coords,
      TreeNode<Point> currNode, TreeNode<Point> currBest, int currDim,
      int dimensions) {
    if (currNode == null) {
      return currBest;
    }
    double currBestDistance = getDistance(coords,
        currBest.getItem().getCoords());
    if (getDistance(coords, currNode.getCoords()) < currBestDistance) {
      currBest = currNode;
    }

    boolean onLeftSide = coords[currDim] < currNode.getCoord(currDim);

    // Check current side
    if (onLeftSide) {
      currBest = findClosestPoint(coords, currNode.getLeft(), currBest,
          TreeBuilder.nextDim(currDim, dimensions), dimensions);
    } else {
      currBest = findClosestPoint(coords, currNode.getRight(), currBest,
          TreeBuilder.nextDim(currDim, dimensions), dimensions);
    }

    // Distance along the current axis (vector projection)
    double projectedCurrDistance = Math
        .abs(currNode.getCoord(currDim) - coords[currDim]);
    if (projectedCurrDistance < currBestDistance) {
      // Check opposite side (opposite of above)
      if (onLeftSide) {
        currBest = findClosestPoint(coords, currNode.getRight(), currBest,
            TreeBuilder.nextDim(currDim, dimensions), dimensions);
      } else {
        currBest = findClosestPoint(coords, currNode.getLeft(), currBest,
            TreeBuilder.nextDim(currDim, dimensions), dimensions);
      }
    }
    return currBest;
  }

  /**
   * Recursively finds the first leaf node, by going down the tree and comparing
   * each node to the coordinates. This nodes serves as a first guess for the
   * closest node.
   *
   * @param coords
   *          the desired coordinates
   * @param currNode
   *          the current node
   * @param currDim
   *          the current dimension that is being compared
   * @param dimensions
   *          the number of dimensions
   * @return the first leaf node
   */
  public static TreeNode<Point> findFirstLeaf(double[] coords,
      TreeNode<Point> currNode, int currDim, int dimensions) {
    if (currNode.getLeft() == null && currNode.getRight() == null) {
      // no children to search
      return currNode;
    }
    int nextDim = TreeBuilder.nextDim(currDim, dimensions);
    if (coords[currDim] <= currNode.getCoord(currDim)) {
      // go to left side
      if (currNode.getLeft() == null) {
        return currNode;
      }
      return findFirstLeaf(coords, currNode.getLeft(), nextDim, dimensions);
    } else {
      // go to right side
      if (currNode.getRight() == null) {
        return currNode;
      }
      return findFirstLeaf(coords, currNode.getRight(), nextDim, dimensions);
    }
  }

  /**
   * A trivial algorithm for finding neighbors, used for testing.
   *
   * @param numNeighbors
   *          the number of neighbors to find
   * @param coords
   *          the desired coordinates
   * @param tree
   *          the current K-D tree
   * @return the list of results
   */
  public static ArrayList<Point> findNumNeighborsTrivial(int numNeighbors,
      double[] coords, KDTree<Point> tree) {
    ArrayList<Point> results = new ArrayList<Point>(numNeighbors);
    for (int i = 0; i < numNeighbors; i++) {
      results.add(
          findNearestRecurs(coords, tree.getRoot(), tree.getRoot(), results)
              .getItem());
    }
    return results;
  }

  /**
   * A helper function for findNumNeighborsTrivial - also only used for testing.
   * Recursively finds the closest node.
   *
   * @param coords
   *          the desired coordinates
   * @param currNode
   *          the current node
   * @param currBest
   *          the best node at the current point in the search
   * @param results
   *          the list of results
   * @return the closest node that has not already been found
   */
  public static TreeNode<Point> findNearestRecurs(double[] coords,
      TreeNode<Point> currNode, TreeNode<Point> currBest,
      ArrayList<Point> results) {
    if (currNode == null) {
      return currBest;
    }
    if (arrayContains(results, currBest.getItem())
        && !arrayContains(results, currNode.getItem())) {
      // Handles case where root is closest node
      currBest = currNode;
    } else if (!arrayContains(results, currNode.getItem())
        && getDistance(coords, currNode.getCoords()) < getDistance(coords,
            currBest.getCoords())) {
      currBest = currNode;
    }
    // Checks both children for a closer node
    currBest = findNearestRecurs(coords, currNode.getLeft(), currBest, results);
    currBest = findNearestRecurs(coords, currNode.getRight(), currBest,
        results);
    return currBest;
  }

  /**
   * Finds the distance between two sets of coordinates.
   *
   * @param coords1
   *          the first coordinates
   * @param coords2
   *          the second coordinates
   * @return the distance
   */
  public static double getDistance(double[] coords1, double[] coords2) {
    assert coords1.length == coords2.length;
    double sum = 0;
    for (int i = 0; i < coords1.length; i++) {
      sum += Math.pow(coords1[i] - coords2[i], 2);
    }
    return Math.sqrt(sum);
  }

  /**
   * Determines whether an ArrayList of Nodes contains a specific Node.
   *
   * @param list
   *          the ArrayList that is being checked
   * @param item
   *          the Node that is being searched for
   * @return true or false, whether or not the Node is found
   */
  public static boolean arrayContains(ArrayList<Point> list, Point item) {
    if (list == null) {
      return false;
    }
    for (int i = 0; i < list.size(); i++) {
      if (list.get(i) == null) {
        return false;
      }
      if (list.get(i).equals(item)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Prints the results in the correct format (by ID).
   *
   * @param results
   *          the list of results
   */
  public static void printResults(ArrayList<Node> results) {
    for (int i = 0; i < results.size(); i++) {
      System.out.println(results.get(i).getId());
    }
  }
}
