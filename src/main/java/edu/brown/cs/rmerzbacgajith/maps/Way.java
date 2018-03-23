package edu.brown.cs.rmerzbacgajith.maps;

/**
 * Movie object that is created for every Movie that is a value of a GraphEdge
 * in the graph.
 *
 * @author gokulajith
 *
 */
public class Way {

  private String name;
  private String id;

  /**
   * Constructor that simply sets properties of the movie.
   *
   * @param id
   *          String id of the movie
   * @param name
   *          String name of the movie
   */
  public Way(String id, String name) {

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
   * Getter for the ID of the movie.
   *
   * @return String actor ID.
   */
  public String getId() {
    return id;
  }

  /**
   * Setter for the name of the movie.
   *
   * @param name
   *          new String name to be set
   */
  public void setName(String name) {

    this.name = name;

  }

  /**
   * Getter for the name of the movie.
   *
   * @return String movie name.
   */
  public String getName() {
    return name;
  }
}
