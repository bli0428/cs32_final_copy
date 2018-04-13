package edu.brown.cs.rmerzbacgajith.maps;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

/**
 * Test every public method in the Way class.
 *
 * @author gokulajith
 *
 */
public class WayTest {

  /**
   * Test Construction of Way and test both setters and getters for name and
   * id.
   */
  @Test
  public void testActorConstruction() {

    Way way = new Way("id1", "name1");

    assertNotNull(way);
    assertEquals(way.getId(), "id1");
    assertEquals(way.getName(), "name1");

    // Setters
    way.setName("name2");
    way.setId("id2");

    assertEquals(way.getId(), "id2");
    assertEquals(way.getName(), "name2");

  }

}
