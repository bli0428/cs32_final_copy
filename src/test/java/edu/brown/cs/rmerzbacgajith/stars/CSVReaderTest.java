package edu.brown.cs.gajith.stars;

import static org.junit.Assert.assertEquals;

import java.io.FileNotFoundException;
import java.util.Scanner;

import org.junit.Test;

import edu.brown.cs.gajith.filereaders.CSVReader;

/**
 * Test every method in CSVReader by reading stars from String.
 *
 * @author Gokul Ajith
 *
 */
public class CSVReaderTest {

  /**
   * Test case where no star is in CSV File.
   */
  @Test
  public void testNoStarConstruction() {

    String file = "StarID,ProperName,X,Y,Z\n";

    Scanner scan = new Scanner(file);
    try {

      CSVReader reader = new CSVReader(scan);
      assertEquals(reader.getList().size(), 0);

    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }

  }

  /**
   * Test case where one star is in CSV File.
   */
  @Test
  public void testSingleStarConstruction() {

    String file = "StarID,ProperName,X,Y,Z\n1,Lonely,5,5,5\n";

    Scanner scan = new Scanner(file);
    try {

      CSVReader reader = new CSVReader(scan);
      assertEquals(reader.getList().size(), 1);
      assertEquals(reader.getList().get(0).getName(), "Lonely");

    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }

  }

  /**
   * Test case where multiple stars are in CSV File.
   */
  @Test
  public void testMultipleStarConstruction() {

    String file = "StarID,ProperName,X,Y,Z\n1,hello,5,5,5\n2,hi,5,5,5\n3,bye,"
        + "5,5,5\n4,hola,5,5,5\n";

    Scanner scan = new Scanner(file);
    try {

      CSVReader reader = new CSVReader(scan);
      assertEquals(reader.getList().size(), 4);
      assertEquals(reader.getList().get(0).getName(), "hello");
      assertEquals(reader.getList().get(1).getName(), "hi");
      assertEquals(reader.getList().get(2).getName(), "bye");
      assertEquals(reader.getList().get(3).getName(), "hola");

    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }

  }

}
