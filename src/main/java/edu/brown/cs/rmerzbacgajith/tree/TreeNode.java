package edu.brown.cs.rmerzbacgajith.tree;

/**
 * A class representing a node in a K-D tree.
 *
 * @author rmerzbac
 *
 * @param <T>
 *          a generic type, allowing the node to consist of anything that
 *          contains coordinates
 */
public class TreeNode<T extends Point> {
  private T item; // Value that splits the Points
  private TreeNode<T> parent;
  private TreeNode<T> left;
  private TreeNode<T> right;

  /**
   * Creates a new node with specific values.
   *
   * @param pItem
   *          the desired item
   * @param pParent
   *          the desired parent
   * @param pLeft
   *          the desired left child
   * @param pRight
   *          the desired right child
   */
  public TreeNode(T pItem, TreeNode<T> pParent, TreeNode<T> pLeft,
      TreeNode<T> pRight) {
    item = pItem;
    parent = pParent;
    left = pLeft;
    right = pRight;
  }

  /**
   * Creates a node with all fields set to null.
   */
  public TreeNode() {
    item = null;
    parent = null;
    left = null;
    right = null;
  }

  /**
   * Gets the node's item.
   *
   * @return item
   */
  public T getItem() {
    return item;
  }

  /**
   * Gets the node's parent.
   *
   * @return parent
   */
  public TreeNode<T> getParent() {
    return parent;
  }

  /**
   * Gets the node's left child.
   *
   * @return left
   */
  public TreeNode<T> getLeft() {
    return left;
  }

  /**
   * Gets the node's right child.
   *
   * @return right
   */
  public TreeNode<T> getRight() {
    return right;
  }

  /**
   * Gets the node's coordinates.
   *
   * @return coords
   */
  public double[] getCoords() {
    return item.getCoords();
  }

  /**
   * Gets a specific coordinate from the node's item.
   *
   * @param coord
   *          the desired coordinate (0 = x, 1 = y, etc.)
   * @return the value of the coordinate
   */
  public double getCoord(int coord) {
    return item.getCoords()[coord];
  }

  /**
   * Gets a specific coordinate from one of the node's children.
   *
   * @param isLeft
   *          true = left child, false = right child
   * @param coord
   *          the desired coordinate
   * @return the value of the coordinate
   */
  public double getChildCoord(boolean isLeft, int coord) {
    if (isLeft) {
      return getLeft().getItem().getCoords()[coord];
    } else {
      return getRight().getItem().getCoords()[coord];
    }
  }

  /**
   * Sets the node's item.
   *
   * @param i
   *          the desired item
   */
  public void setItem(T i) {
    item = i;
  }

  /**
   * Sets the node's parent.
   *
   * @param p
   *          the desired parent
   */
  public void setParent(TreeNode<T> p) {
    parent = p;
  }

  /**
   * Sets the node's left child.
   *
   * @param l
   *          the desired left child
   */
  public void setLeft(TreeNode<T> l) {
    left = l;
  }

  /**
   * Sets the node's right child.
   *
   * @param r
   *          the desired right child
   */
  public void setRight(TreeNode<T> r) {
    right = r;
  }

  /**
   * Prints a visual representation of the node.
   */
  @Override
  public String toString() {
    String str = "Item: " + item + "\n";
    String tab = "  ";
    str += "Left:\n  " + left.toString(tab);
    str += "\nRight:\n  " + right.toString(tab);
    return str;
  }

  /**
   * Prints a visual representation of the node, with a desired indentation.
   *
   * @param tab
   *          the desired indentation
   * @return the visual representation
   */
  public String toString(String tab) {
    String str = tab + "Item: " + item + "\n";
    if (left != null) {
      str += tab + "Left:\n  " + left.toString(tab + "  ");
    } else {
      str += tab + "Left: null\n";
    }
    if (right != null) {
      str += tab + "Right:\n  " + right.toString(tab + "  ");
    } else {
      str += tab + "Right: null\n";
    }
    return str;
  }
}
