package edu.brown.cs.rmerzbacgajith.main;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * .txt File reader that parses words within a Text file to an arraylist.
 *
 * @author gokulajith
 *
 */
public class TextReader {

  private List<String> words;

  /**
   * Use parameter scanner and read line by line, parsing the words.
   *
   * @param scan
   *          Scanner with content of .txt file.
   */
  public TextReader(Scanner scan) {

    words = new ArrayList<String>();

    // Loop through lines of file
    while (scan.hasNextLine()) {

      // Lowercase and remove all invalid characters
      String line = scan.nextLine();
      line = line.toLowerCase();
      line = line.replaceAll("[^a-z ]", " ");

      // Add words to ArrayList
      Scanner scan2 = new Scanner(line);
      while (scan2.hasNext()) {

        words.add(scan2.next());
      }

      // Close both scanners.
      scan2.close();
    }

    scan.close();
  }

  /**
   * Return Parsed list of words.
   *
   * @return List of words as Strings that have been read from file.
   */
  public List<String> getWords() {
    return words;
  }

}
