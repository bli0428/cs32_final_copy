package edu.brown.cs.group.handling;

/**
 * A class that processes Strings and makes sure that they can be converted into
 * the desired format.
 *
 * @author rmerzbac
 *
 */
public final class TypeFormatCheck {
  private TypeFormatCheck() {
  }

  /**
   * Checks if a command-line string field can be converted to an int.
   *
   * @param str
   *          the string to be converted to an int
   * @param field
   *          the name of the string's field
   * @param min
   *          the lowest number that the int can be (to check for negativity)
   * @return the new int, or Integer.MIN_VALUE if an error was encountered
   */
  public static int checkInt(String str, String field, int min) {
    int result;
    try {
      result = Integer.parseInt(str);
      if (result < min) {
        Handling.error(field + " cannot be less than " + min);
        return Integer.MIN_VALUE;
      }
      return result;
    } catch (NumberFormatException e) {
      Handling.improperType(field, "integer");
      return Integer.MIN_VALUE;
    }
  }

  /**
   * Checks if a command-line string field can be converted to a double.
   *
   * @param str
   *          the string to be converted to a double
   * @param field
   *          the name of the string's field
   * @param min
   *          the lowest number that the double can be (to check for negativity)
   * @return the new double, or Double.MIN_VALUE if an error was encountered
   */
  public static double checkDouble(String str, String field, double min) {
    double result;
    try {
      result = Double.parseDouble(str);
      if (min != Double.MIN_VALUE && result < min) {
        Handling.error(field + " cannot be less than " + min);
        return Double.MIN_VALUE;
      }
      return result;
    } catch (NumberFormatException e) {
      Handling.improperType(field, "double");
      return Double.MIN_VALUE;
    }
  }

  /**
   * Removes quotation marks around a string, if they exist. If no marks exist,
   * returns null.
   *
   * @param str
   *          the string to be checked and modified
   * @param field
   *          the name of the string's field
   * @return the modified string or null on error
   */
  public static String checkAndRemoveQuotes(String str, String field) {
    int len = str.length();
    if (len > 2 && str.charAt(0) == '"' && str.charAt(len - 1) == '"') {
      return str.substring(1, len - 1);
    } else {
      Handling.improperCommand(field + " must be wrapped in quotation marks");
      return null;
    }
  }
}
