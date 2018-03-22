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
   * @param starList
   *          the list of stars to convert into a tree
   * @param dimensions
   *          the number of dimensions
   */
  public TreeBuilder(List<T> starList, int dimensions) {
    tree = newTree(starList, dimensions);
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
   * @param starList
   *          the list of stars to convert into a tree
   * @param dims
   *          the number of dimensions
   * @return the new tree
   */
  public KDTree<T> newTree(List<T> starList, int dims) {
    dimensions = dims;
    tree = new KDTree<>();
    tree.setRoot(buildTree(tree.getRoot(), null, starList, 0));
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
   * @param starList
   *          the list of stars to be converted into a tree
   * @param currDimension
   *          the current dimension that is being compared
   * @return a branch of the tree
   */
  public TreeNode<T> buildTree(TreeNode<T> n, TreeNode<T> parent,
      List<T> starList, int currDimension) {
    n = new TreeNode<T>();
    n.setParent(parent);
    int size = starList.size();
    if (size == 0) {
      n = null;
      return n;
    }

    // find middle
    int middle = size / 2;
    // sort the list
    starList.sort(Node.compareStars(currDimension));
    T median = starList.get(middle);
    // split into two lists
    List<T> lower = starList.subList(0, middle);
    List<T> upper = starList.subList(middle + 1, size);

    int nextDimension = nextDim(currDimension, dimensions);

    // item is now the median
    n.setItem(median);
    n.setLeft(buildTree(n.getLeft(), n, lower, nextDimension));
    n.setRight(buildTree(n.getRight(), n, upper, nextDimension));

    return n;
  }
}
