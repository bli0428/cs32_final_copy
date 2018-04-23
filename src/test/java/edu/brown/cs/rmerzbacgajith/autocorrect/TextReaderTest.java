package edu.brown.cs.rmerzbacgajith.autocorrect;

import static org.junit.Assert.assertEquals;

import java.util.Scanner;

import org.junit.Test;


/**
 * Test every method in CSVReader by reading words from String.
 *
 * @author gokulajith
 *
 */
public class TextReaderTest {

  /**
   * Test case where there are no words in the file.
   */
  @Test
  public void testNoWordConstruction() {

    String file = "             ";
    Scanner scan = new Scanner(file);

    try {
      TextReader reader = new TextReader(scan);
      assertEquals(reader.getWords().size(), 0);

    } catch (Exception ex) {

    }
  }

  /**
   * Test case where there is one word in the file.
   */
  @Test
  public void testOneWordConstruction() {

    String file = "hello ";
    Scanner scan = new Scanner(file);

    try {
      TextReader reader = new TextReader(scan);
      assertEquals(reader.getWords().size(), 1);

    } catch (Exception ex) {

    }
  }

  /**
   * Test case where there are multiple words in the file.
   */
  @Test
  public void testMultipleWordConstruction() {

    String file = "hello my name is jj";
    Scanner scan = new Scanner(file);

    try {
      TextReader reader = new TextReader(scan);
      assertEquals(reader.getWords().size(), 5);

    } catch (Exception ex) {

    }
  }

  /**
   * Test case where there are invalid symbols in the file.
   */
  @Test
  public void testInvalidCharacterConstruction() {

    String file = "couldn't w22alk @beach t?day";
    Scanner scan = new Scanner(file);

    try {
      TextReader reader = new TextReader(scan);
      assertEquals(reader.getWords().size(), 7);

    } catch (Exception ex) {

    }
  }
}
