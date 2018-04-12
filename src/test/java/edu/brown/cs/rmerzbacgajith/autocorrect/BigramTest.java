package edu.brown.cs.gajith.autocorrect;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Test construction and every method in Bigram class.
 *
 * @author gokulajith
 *
 */
public class BigramTest {

  /**
   * Test Construction of Bigram.
   */
  @Test
  public void testBigramConstruction() {

    Bigram bigram = new Bigram("up", "whats");

    assertEquals(bigram.getBigram(), "whats");
    assertEquals(bigram.getWord(), "up");
    assertEquals(bigram.getFrequency(), 1);

  }

  /**
   * Test Frequency Getters and Setters of Bigram.
   */
  @Test
  public void testBigramFrequency() {

    Bigram bigram = new Bigram("up", "whats");
    bigram.setFrequency(10);

    assertEquals(bigram.getFrequency(), 10);

  }

  /**
   * Test Frequency Getters and Setters of word in a Bigram.
   */
  @Test
  public void testBigramWord() {

    Bigram bigram = new Bigram("up", "whats");
    bigram.setWord("down");

    assertEquals(bigram.getWord(), "down");

  }
}
