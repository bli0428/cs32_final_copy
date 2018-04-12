package edu.brown.cs.rmerzbacgajith.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import edu.brown.cs.rmerzbacgajith.handling.Handling;
import edu.brown.cs.rmerzbacgajith.maps.MapCommand;
import edu.brown.cs.rmerzbacgajith.tree.Node;

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
  public void processCommand(String line) {
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
        double[] coords1 = { Double.parseDouble(parsed[1]),
            Double.parseDouble(parsed[2]) };
        double[] coords2 = { Double.parseDouble(parsed[3]),
            Double.parseDouble(parsed[4]) };
        mc.waysCommand(coords1, coords2, true);
      } else {
        Handling.improperCommandUse("ways <lat1> <lon1> <lat2> <lon2>");
      }
    } else if (parsed[0].equals("nearest")) {
      if (parsed.length == 3) {
        double[] coords = { Double.parseDouble(parsed[1]),
            Double.parseDouble(parsed[2]) };
        Node nearest = (Node) mc.nearestCommand(coords);
        System.out.println(nearest.getId());
      } else {
        Handling.improperCommandUse("nearest <latitude> <longitude>");
      }
    } else if (parsed[0].equals("suggest")) {
      if (parsed.length > 1) {
        String words = line.substring(line.indexOf(" ") + 1);
        mc.suggest(words);
      } else {
        Handling.improperCommandUse("suggest <words>");
      }
    } else if (parsed[0].equals("route")) {
      int numQuotes = line.length() - line.replace("\"", "").length();
      if (numQuotes == NUM_INTERSECTIONS * 4) {
        parsed = line.split("\"");
        Node n1 = mc.getIntersection(parsed[1], parsed[3]);
        if (n1 == null) {
          Handling.error("first intersection not found");
          return;
        }
        Node n2 = mc.getIntersection(parsed[5], parsed[7]);
        if (n2 == null) {
          Handling.error("second intersection not found");
          return;
        } else {
          mc.route(n1, n2);
        }
      } else {
        if (parsed.length == 5 && numQuotes == 0) {
          double[] coords1 = { Double.parseDouble(parsed[1]),
              Double.parseDouble(parsed[2]) };
          Node n1 = (Node) mc.nearestCommand(coords1);
          double[] coords2 = { Double.parseDouble(parsed[3]),
              Double.parseDouble(parsed[4]) };
          Node n2 = (Node) mc.nearestCommand(coords2);
          mc.route(n1, n2);
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

  public MapCommand getMapCommand() {
    return mc;
  }
}
