package edu.brown.cs.rmerzbacgajith.maps;

import java.util.Objects;

import edu.brown.cs.rmerzbacgajith.tree.Point;

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
  public String getId() {
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
