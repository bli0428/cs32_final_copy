package edu.brown.cs.rmerzbacgajith.graph;

import java.util.Objects;

/**
 * GraphNode class represent the generic Nodes inside the Graph. Every node can
 * store a generic Value T.
 *
 * @author gokulajith
 *
 * @param <T>
 *          Value that the node stores (in example of bacon, Actor).
 */
public class GraphNode<T> {

  private String id;
  private T value;

  /**
   * Store the key properties of the node.
   *
   * @param id
   *          of the node as String.
   * @param value
   *          stored in the Node as generic T.
   */
  public GraphNode(String id, T value) {
    this.id = id;
    this.value = value;
  }

  /**
   * Getter for the Id of the node.
   *
   * @return String id of the node.
   */
  public String getId() {
    return this.id;
  }

  /**
   * Getter for the value stored in the node.
   *
   * @return T value in the node.
   */
  public T getValue() {
    return this.value;
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, value);
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }

    if (o == null || this.getClass() != o.getClass()) {
      return false;
    }

    GraphNode<?> person = (GraphNode<?>) o;
    return ((value == person.value
        || (value != null && value.equals(person.value)))
        && (id == person.id || (id != null && id.equals(person.id))));
  }

}
