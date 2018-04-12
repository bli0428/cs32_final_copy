package edu.brown.cs.gajith.bacon;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

/**
 * Test every public method in the movie class.
 *
 * @author gokulajith
 *
 */
public class MovieTest {

  /**
   * Test Construction of Movie and test both setters and getters for name and
   * id.
   */
  @Test
  public void testActorConstruction() {

    Movie movie = new Movie("id1", "name1");

    assertNotNull(movie);
    assertEquals(movie.getId(), "id1");
    assertEquals(movie.getName(), "name1");

    // Setters
    movie.setName("name2");
    movie.setId("id2");

    assertEquals(movie.getId(), "id2");
    assertEquals(movie.getName(), "name2");

  }

}
