package edu.brown.cs.rmerzbacgajith.graph;

import java.util.Objects;

/**
 * GraphEdge class that represents the edges that connect GraphNodes in the
 * Graph. The generic GraphEdge can store a value T.
 *
 * @author gokulajith
 *
 * @param <T>
 *          The generic value that the edge holds (in the example of bacon,
 *          movies).
 */
public class GraphEdge<T> {

  private String id;
  private T value;
  private Double weight;

  /**
   * Constructor that stores key properties of the edge.
   *
   * @param id
   *          of the edge.
   * @param value
   *          of generic type T that can be accessed.
   * @param weight
   *          of the edge as a double
   */
  public GraphEdge(String id, T value, double weight) {
    this.id = id;
    this.value = value;
    this.weight = weight;
  }

  /**
   * Getter for the edge's ID.
   *
   * @return String id
   */
  public String getId() {
    return this.id;
  }

  /**
   * Getter for the edge's value.
   *
   * @return Value that the edge is storing.
   */
  public T getValue() {
    return this.value;
  }

  /**
   * Getter for the weight of the edge.
   *
   * @return weight as a double.
   */
  public double getWeight() {
    return weight;
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

    GraphEdge<?> edge = (GraphEdge<?>) o;
    return ((value == edge.value || (value != null && value.equals(edge.value)))
        && (id == edge.id || (id != null && id.equals(edge.id))));
  }

}
