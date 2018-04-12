package edu.brown.cs.gajith.bacon;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

/**
 * Test every public method in the Actor class.
 *
 * @author gokulajith
 *
 */
public class ActorTest {

  /**
   * Test Construction of Actor and test both setters and getters for name and
   * id.
   */
  @Test
  public void testActorConstruction() {

    Actor actor = new Actor("id1", "name1");

    assertNotNull(actor);
    assertEquals(actor.getId(), "id1");
    assertEquals(actor.getName(), "name1");

    // Setters
    actor.setName("name2");
    actor.setId("id2");

    assertEquals(actor.getId(), "id2");
    assertEquals(actor.getName(), "name2");

  }

  /**
   * Test the equals overriden method of actor to ensure two actors are being
   * compared correctly.
   */
  @Test
  public void testActorEquals() {

    Actor actor = new Actor("id1", "name1");
    Actor sameActor = new Actor("id1", "name1");

    // third actor that has same id but different name
    Actor diffActor = new Actor("id1", "name2");

    // fourth actor that has same name but different id
    Actor diffActor2 = new Actor("id2", "name1");

    assertEquals(actor, sameActor);
    assertNotEquals(actor, diffActor);
    assertNotEquals(actor, diffActor2);

  }

}
