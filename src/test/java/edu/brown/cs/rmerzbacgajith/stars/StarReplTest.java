package edu.brown.cs.gajith.stars;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import edu.brown.cs.gajith.main.Repl;

/**
 * Test all methods in StarRepl class.
 *
 * @author gokulajith
 *
 */
public class StarReplTest {

  /**
   * Test stars command with one input star.
   */
  @Test
  public void testStarsCommandWithOneStar() {

    String command = "stars data/stars/one-star.csv";

    Repl repl = new Repl();
    repl.run(command);

    assertEquals(repl.getSC().getTree().getSize(), 1);
  }

  /**
   * Test stars command with multiple input stars as well as getTree method.
   */
  @Test
  public void testStarsCommandWithMultipleStars() {

    String command = "stars data/stars/ten-star.csv";

    Repl repl = new Repl();
    repl.run(command);

    assertEquals(repl.getSC().getTree().getSize(), 10);
  }

  /**
   * Test Neighbors command with location as well as getAnswer method.
   */
  @Test
  public void testNeighborsCommandWithLocation() {

    String command = "stars data/stars/ten-star.csv";

    Repl repl = new Repl();
    repl.run(command);

    command = "neighbors 5 0 0 0";
    repl.run(command);

    assertEquals(repl.getSC().getAnswer().size(), 5);
    assertEquals(repl.getSC().getAnswer().get(0).getID(), 0);
    assertEquals(repl.getSC().getAnswer().get(1).getID(), 70667);
    assertEquals(repl.getSC().getAnswer().get(2).getID(), 71454);
    assertEquals(repl.getSC().getAnswer().get(3).getID(), 71457);
    assertEquals(repl.getSC().getAnswer().get(4).getID(), 87666);
  }

  /**
   * Test Neighbors command with name as well as getAnswer method.
   */
  @Test
  public void testNeighborsCommandWithName() {

    String command = "stars data/stars/ten-star.csv";

    Repl repl = new Repl();
    repl.run(command);

    command = "neighbors 4 \"Sol\"";
    repl.run(command);

    assertEquals(repl.getSC().getAnswer().size(), 4);
    assertEquals(repl.getSC().getAnswer().get(0).getID(), 70667);
    assertEquals(repl.getSC().getAnswer().get(1).getID(), 71454);
    assertEquals(repl.getSC().getAnswer().get(2).getID(), 71457);
    assertEquals(repl.getSC().getAnswer().get(3).getID(), 87666);
  }

  /**
   * Test radius command with location as well as getAnswer method.
   */
  @Test
  public void testRadiusCommandWithLocation() {

    String command = "stars data/stars/ten-star.csv";

    Repl repl = new Repl();
    repl.run(command);

    command = "radius 5 0 0 0";
    repl.run(command);

    assertEquals(repl.getSC().getAnswer().size(), 6);
    assertEquals(repl.getSC().getAnswer().get(0).getID(), 0);
    assertEquals(repl.getSC().getAnswer().get(1).getID(), 70667);
    assertEquals(repl.getSC().getAnswer().get(2).getID(), 71454);
    assertEquals(repl.getSC().getAnswer().get(3).getID(), 71457);
    assertEquals(repl.getSC().getAnswer().get(4).getID(), 87666);
    assertEquals(repl.getSC().getAnswer().get(5).getID(), 118721);
  }

  /**
   * Test radius command with name as well as getAnswer method.
   */
  @Test
  public void testRadiusCommandWithName() {

    String command = "stars data/stars/ten-star.csv";

    Repl repl = new Repl();
    repl.run(command);

    command = "radius 5 \"Sol\"";
    repl.run(command);

    assertEquals(repl.getSC().getAnswer().size(), 5);
    assertEquals(repl.getSC().getAnswer().get(0).getID(), 70667);
    assertEquals(repl.getSC().getAnswer().get(1).getID(), 71454);
    assertEquals(repl.getSC().getAnswer().get(2).getID(), 71457);
    assertEquals(repl.getSC().getAnswer().get(3).getID(), 87666);
    assertEquals(repl.getSC().getAnswer().get(4).getID(), 118721);
  }

}
