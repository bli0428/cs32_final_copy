package edu.brown.cs.rmerzbacgajith.tree;

import java.util.Objects;

/**
 * A class representing a node, which adds ID and name fields to the existing
 * Point class.
 *
 * @author rmerzbac
 */
public class Node extends Point {
  private String id;

  /**
   * Constructs a new node.
   *
   * @param paramID
   *          the desired ID
   * @param paramCoords
   *          the desired coordinates
   */
  public Node(String paramID, double[] paramCoords) {
    super(paramCoords);
    id = paramID;
  }

  /**
   * Gets the node's ID.
   *
   * @return id
   */
  public String getID() {
    return id;
  }

  /**
   * Sets the node's ID.
   *
   * @param pID
   *          the desired ID
   */
  public void setID(String pID) {
    id = pID;
  }

  /**
   * Prints a visual representation of the node.
   */
  @Override
  public String toString() {
    return "[" + id + ", " + printCoords() + "]";
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }

    if (o == null || this.getClass() != o.getClass()) {
      return false;
    }

    Node person = (Node) o;
    return ((id == person.id || (id != null && id.equals(person.id))));
  }

}
