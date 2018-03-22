package edu.brown.cs.rmerzbacgajith.bacon;

import java.util.Objects;

/**
 * Actor class that is created for every Actor that will be stored as the value
 * of a GraphNode in the graph.
 *
 * @author gokulajith
 *
 */
public class MapNode {

  private String id;
  private String name;

  /**
   * Constructor that simply sets properties of the Actor.
   *
   * @param id
   *          String id of the actor
   * @param name
   *          String name of the actor
   */
  public MapNode(String id, String name) {
    this.id = id;
    this.name = name;
  }

  /**
   * Setter for the id.
   *
   * @param id
   *          new String id to be set
   */
  public void setId(String id) {

    this.id = id;

  }

  /**
   * Getter for the ID of the actor.
   *
   * @return String actor ID.
   */
  public String getId() {
    return id;
  }

  /**
   * Setter for the name of the Actor.
   *
   * @param name
   *          new String name to be set
   */
  public void setName(String name) {

    this.name = name;

  }

  /**
   * Getter for the name of the actor.
   *
   * @return String actor name.
   */
  public String getName() {
    return name;
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, id);
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }

    if (o == null || this.getClass() != o.getClass()) {
      return false;
    }

    MapNode person = (MapNode) o;
    return ((name == person.name || (name != null && name.equals(person.name)))
        && (id == person.id || (id != null && id.equals(person.id))));
  }
}
