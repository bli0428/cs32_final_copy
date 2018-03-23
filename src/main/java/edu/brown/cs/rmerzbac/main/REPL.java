package edu.brown.cs.rmerzbac.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import edu.brown.cs.rmerzbac.handling.Handling;

public class REPL {
  private MapCommand mc;
  private static final int NUM_INTERSECTIONS = 2;

  public REPL() {
    mc = new MapCommand();
  }

  /**
   * Runs the REPL, using a BufferedReader to read user input.
   */
  public void runRepl() {
    try (BufferedReader br = new BufferedReader(
        new InputStreamReader(System.in))) {
      String line;
      while ((line = br.readLine()) != null) {
        processCommand(line);
      }
    } catch (IOException ioe) {
      // Not possible. No error message can make sense of this.
      ioe.printStackTrace();
    }
  }

  /**
   * Processes user input and acts accordingly.
   *
   * @param line
   *          The command provided by the user
   */
  private void processCommand(String line) {
    // Split on whitespace and punctuation
    String[] parsed = line.split("\\s+");
    if (parsed[0].equals("map")) {
      if (parsed.length == 2) {
        mc.mdbCommand(parsed[1]);
      } else {
        Handling.improperCommandUse("map <path/to/database>");
      }
    } else if (parsed[0].equals("ways")) {
      if (parsed.length == 5) {
        System.out.println("ways");
      } else {
        Handling.improperCommandUse("ways <lat1> <lon1> <lat2> <lon2>");
      }
    } else if (parsed[0].equals("nearest")) {
      if (parsed.length == 3) {
        System.out.println("nearest");
      } else {
        Handling.improperCommandUse("nearest <latitude> <longitude>");
      }
    } else if (parsed[0].equals("suggest")) {
      if (parsed.length > 1) {
        String words = line.substring(line.indexOf(" ") + 1);
        List<String> suggestions = mc.suggest(words);
      } else {
        Handling.improperCommandUse("suggest <words>");
      }
    } else if (parsed[0].equals("route")) {
      int numQuotes = line.length() - line.replace("\"", "").length();
      if (numQuotes == NUM_INTERSECTIONS * 4) {
        System.out.println("route w/ names");
      } else {
        if (parsed.length == 5 && numQuotes == 0) {
          System.out.println("route w/ lat lon");
        } else {
          Handling.improperCommandUse("route <lat1> <lon1> <lat2> <lon2>");
        }
      }
    } else {
      Handling.error("Invalid command.");
      return;
    }
  }

  /**
   * Determines whether an array contains a String.
   *
   * @param s
   *          the string
   * @param arr
   *          the array
   * @return a boolean representing whether the String was found.
   */
  public boolean arrayContains(String s, String[] arr) {
    for (int i = 0; i < arr.length; i++) {
      if (s.equals(arr[i])) {
        return true;
      }
    }
    return false;
  }
}
