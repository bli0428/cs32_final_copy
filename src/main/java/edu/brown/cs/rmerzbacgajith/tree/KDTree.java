package edu.brown.cs.rmerzbacgajith.tree;

import edu.brown.cs.rmerzbacgajith.handling.Handling;
import edu.brown.cs.rmerzbacgajith.maps.Node;

/**
 * A class representing the K-D tree.
 *
 * @author rmerzbac
 *
 * @param <T>
 *          a generic type, allowing the tree to consist of anything that
 *          contains coordinates
 */
public class KDTree<T extends Point> {
  private TreeNode<T> root;

  /**
   * Constructs a new KDTree, with all of its node's fields set to null.
   */
  public KDTree() {
    root = new TreeNode<T>(null, null, null, null);
  }

  /**
   * Constructs a new KDTree, using a specific node as the root.
   *
   * @param firstNode
   *          the desired root
   */
  public KDTree(TreeNode<T> firstNode) {
    root = firstNode;
  }

  /**
   * Returns the root of the tree.
   *
   * @return the root
   */
  public TreeNode<T> getRoot() {
    return root;
  }

  /**
   * Sets the root of the tree.
   *
   * @param r
   *          the desired root
   */
  public void setRoot(TreeNode<T> r) {
    root = r;
  }

  /**
   * Prints the tree, by calling the toString method of the root.
   */
  @Override
  public String toString() {
    return root.toString();
  }

  /**
   * Checks the tree, making sure all of the nodes are in their correct
   * position.
   *
   * @return true if the tree was valid, false otherwise
   */
  public boolean checkTree() {
    return checkTreeRecur(root, null, 0);
  }

  /**
   * Checks a specific tree, making sure all of the nodes are in their correct
   * position.
   *
   * @param t
   *          the specific tree
   * @return true if the tree was valid, false otherwise
   */
  public boolean checkTree(KDTree<T> t) {
    return checkTreeRecur(t.getRoot(), null, 0);
  }

  /**
   * Recursively checks the tree, making sure the nodes are in their correct
   * position.
   *
   * @param n
   *          the current node
   * @param parent
   *          the parent node
   * @param currDim
   *          the current dimension
   * @return true if the branch of the tree was valid, false otherwise
   */
  public boolean checkTreeRecur(TreeNode<T> n, TreeNode<T> parent,
      int currDim) {
    if (n == null) {
      return true;
    }
    if (n.getParent() != parent) {
      return false;
    }
    if (n.getLeft() != null
        && n.getCoord(currDim) < n.getChildCoord(true, currDim)) {
      return false;
    }
    if (n.getRight() != null
        && n.getCoord(currDim) > n.getChildCoord(false, currDim)) {
      return false;
    }
    int nextDim = TreeBuilder.nextDim(currDim, n.getItem().getCoords().length);
    return checkTreeRecur(n.getLeft(), n, nextDim)
        && checkTreeRecur(n.getRight(), n, nextDim);
  }

  /**
   * Finds a node based on its star's name.
   *
   * @param name
   *          the name of the node to be found
   * @return the found node, or null if the node did not exist
   */
  public TreeNode<Node> findNode(String name) {
    if (root.getItem().getClass() != Node.class) {
      Handling.error("findNode can only be called on Node classes");
      return null;
    }
    @SuppressWarnings("unchecked")
    TreeNode<Node> starRoot = (TreeNode<Node>) root;
    return findNodeRecurs(name, starRoot);
  }

  /**
   * Recursively finds the node. (Helper function for findNode.)
   *
   * @param id
   *          the id of the node to be found
   * @param n
   *          the current node
   * @return the found node, or null if the node did not exist
   */
  public TreeNode<Node> findNodeRecurs(String id, TreeNode<Node> n) {
    if (n == null) {
      return null;
    } else if (n.getItem().getId().equals(id)) {
      return n;
    } else {
      TreeNode<Node> l = findNodeRecurs(id, n.getLeft());
      if (l != null) {
        return l;
      }
      // Must be in the right side
      return findNodeRecurs(id, n.getRight());
    }
  }
}
