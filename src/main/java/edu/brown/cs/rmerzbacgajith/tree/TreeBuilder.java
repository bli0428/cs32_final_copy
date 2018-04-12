package edu.brown.cs.rmerzbacgajith.tree;

import java.util.List;

/**
 * A class that builds the K-D tree.
 *
 * @author rmerzbac
 *
 * @param <T>
 *          a generic type, allowing the tree to consist of anything that
 *          contains coordinates
 */
public class TreeBuilder<T extends Point> {
  private KDTree<T> tree;
  private int dimensions;

  /**
   * Constructs a new TreeBuilder.
   *
   * @param objectList
   *          the list of objects to convert into a tree
   * @param dimensions
   *          the number of dimensions
   */
  public TreeBuilder(List<T> objectList, int dimensions) {
    tree = newTree(objectList, dimensions);
  }

  /**
   * Returns the K-D tree.
   *
   * @return tree
   */
  public KDTree<T> getTree() {
    return tree;
  }

  /**
   * Constructs a new tree.
   *
   * @param objectList
   *          the list of objects to convert into a tree
   * @param dims
   *          the number of dimensions
   * @return the new tree
   */
  public KDTree<T> newTree(List<T> objectList, int dims) {
    dimensions = dims;
    tree = new KDTree<>();
    tree.setRoot(buildTree(tree.getRoot(), null, objectList, 0));
    return tree;
  }

  /**
   * Returns the next dimension. (ie. 0 to 1, 1 to 2, 2 to 0 in 3-d).
   *
   * @param currDim
   *          the current dimension
   * @param dims
   *          the number of dimensions
   * @return the next dimension
   */
  public static int nextDim(int currDim, int dims) {
    int nextDimension = currDim + 1;
    if (nextDimension >= dims) {
      nextDimension = 0;
    }
    return nextDimension;
  }

  /**
   * Recursively builds the tree, using the algorithm for constructing a K-D
   * tree.
   *
   * @param n
   *          the current node
   * @param parent
   *          the parent node
   * @param objectList
   *          the list of objects to be converted into a tree
   * @param currDimension
   *          the current dimension that is being compared
   * @return a branch of the tree
   */
  public TreeNode<T> buildTree(TreeNode<T> n, TreeNode<T> parent,
      List<T> objectList, int currDimension) {
    n = new TreeNode<T>();
    n.setParent(parent);
    int size = objectList.size();
    if (size == 0) {
      n = null;
      return n;
    }

    // find middle
    int middle = size / 2;
    // sort the list
    objectList.sort(Node.compareStars(currDimension));
    T median = objectList.get(middle);
    // split into two lists
    List<T> lower = objectList.subList(0, middle);
    List<T> upper = objectList.subList(middle + 1, size);

    int nextDimension = nextDim(currDimension, dimensions);

    // item is now the median
    n.setItem(median);
    n.setLeft(buildTree(n.getLeft(), n, lower, nextDimension));
    n.setRight(buildTree(n.getRight(), n, upper, nextDimension));

    return n;
  }
}
