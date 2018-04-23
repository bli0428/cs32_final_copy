package edu.brown.cs.rmerzbacgajith.handling;

/**
 * A class that handles all error messages and prints them in the desired
 * format.
 *
 * @author rmerzbac
 */
public final class Handling {
  private Handling() {
  }

  /**
   * Prints out an error message.
   *
   * @param text
   *          the text to be printed
   */
  public static void error(String text) {
    System.out.println("ERROR: " + text);
  }

  /**
   * Prints an informative message if a bad command is used.
   *
   * @param text
   *          the proper form of the command
   */
  public static void improperCommand(String text) {
    error("Improper command - " + text + ".");
  }

  /**
   * Returns "a " or "an " depending on whether or not the following word begins
   * with a vowel or consonant.
   *
   * @param word
   *          the following word
   * @return "a " or "an "
   */
  public static String aOrAn(String word) {
    String vowels = "aeiouAEIOU";
    if (vowels.contains(word.charAt(0) + "")) {
      return "an ";
    } else {
      return "a ";
    }
  }

  /**
   * Prints an informative message if a bad command is used in the form "ERROR:
   * Improper command - use [cmd].".
   *
   * @param cmd
   *          the proper form of the command
   */
  public static void improperCommandUse(String cmd) {
    improperCommand("use " + cmd);
  }

  /**
   * Prints an informative message if an improper type is used in the form
   * "ERROR: [field] must be [a/an] [type].".
   *
   * @param field
   *          the incorrect command-line field
   *
   * @param type
   *          the type that the field should be
   */
  public static void improperType(String field, String type) {
    error(field + " must be " + aOrAn(type) + type);
  }

  /**
   * Prints "ERROR: file not found".
   */
  public static void fileNotFound() {
    error("file not found");
  }

  /**
   * Prints: "ERROR: SQL error".
   */
  public static void sqlError() {
    error("SQL error");
  }

  /**
   * Prints: "ERROR: you are not logged in".
   */
  public static void notLoggedIn() {
    error("you are not logged in");
  }

  /**
   * Prints: "ERROR: invalid username or password".
   */
  public static void invalidLogin() {
    error("invalid username or password");
  }
}
