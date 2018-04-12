package edu.brown.cs.rmerzbacgajith.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import edu.brown.cs.rmerzbacgajith.handling.Handling;
import edu.brown.cs.rmerzbacgajith.maps.MapCommand;
import edu.brown.cs.rmerzbacgajith.tree.Node;

/**
 * REPL class for maps that handles and parses the user input and calls the
 * appropriate methods in mapCommand to return accurate output.
 *
 */
public class REPL {
  private MapCommand mc;
  private static final int NUM_INTERSECTIONS = 2;

  /**
   * Constructor simply instantiates a new MapCommand that handles all the
   * commands related to maps.
   */
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

    // Check first word to see the type of the command

    // If first word is map, connect to db using method in MapCommand.
    if (parsed[0].equals("map")) {
      if (parsed.length == 2) {
        mc.mapCommand(parsed[1]);
      } else {
        Handling.improperCommandUse("map <path/to/database>");
      }

      // For a ways command
    } else if (parsed[0].equals("ways")) {

      // Ensure user has entered coordinates properly
      if (parsed.length == 5) {

        // Parse the coordinates and call ways method in MapCommand.
        try {
          double[] coords1 = { Double.parseDouble(parsed[1]),
              Double.parseDouble(parsed[2]) };
          double[] coords2 = { Double.parseDouble(parsed[3]),
              Double.parseDouble(parsed[4]) };
          mc.waysCommand(coords1, coords2);
        } catch (Exception ex) {
          Handling.improperCommandUse("ways <lat1> <lon1> <lat2> <lon2>");
        }
      } else {
        Handling.improperCommandUse("ways <lat1> <lon1> <lat2> <lon2>");
      }

      // For a nearest command
    } else if (parsed[0].equals("nearest")) {

      // Ensure coordinates have been entered properly
      if (parsed.length == 3) {

        // Create array of coordinates and use method in MapCommand to find the
        // nearest traversable node to this point.
        try {
          double[] coords = { Double.parseDouble(parsed[1]),
              Double.parseDouble(parsed[2]) };
          Node nearest = (Node) mc.nearestCommand(coords);
          System.out.println(nearest.getId());
        } catch (Exception ex) {
          Handling.improperCommandUse("nearest <latitude> <longitude>");
        }
      } else {
        Handling.improperCommandUse("nearest <latitude> <longitude>");
      }

      // For a suggest command
    } else if (parsed[0].equals("suggest")) {

      if (parsed.length > 1) {
        // Parse what user has typed after the word suggest and use method in
        // MapCommand to autocorrect
        // for suggestions.
        String words = line.substring(line.indexOf(" ") + 1);
        mc.suggest(words);
      } else {
        Handling.improperCommandUse("suggest <words>");
      }

      // For a route command, first determine whether it is a route with
      // intersections or coordinates command.
    } else if (parsed[0].equals("route")) {

      // Check number of quotes in the String
      int numQuotes = line.length() - line.replace("\"", "").length();

      // Route with Intersection
      if (numQuotes == NUM_INTERSECTIONS * 4) {

        // Ensure Intersection 1 is in the database using MapCommand method.
        parsed = line.split("\"");
        Node n1 = mc.getIntersection(parsed[1], parsed[3]);
        if (n1 == null) {
          Handling.error("First intersection not found");
          return;
        }

        // Ensure Intersection 2 is in the database using MapCommand method.
        Node n2 = mc.getIntersection(parsed[5], parsed[7]);
        if (n2 == null) {
          Handling.error("Second intersection not found");
          return;
        } else {

          // If both intersections are not null, call route command passing in
          // the two nodes.
          mc.route(n1, n2);
        }

        // Route with coordinates
      } else {
        if (parsed.length == 5 && numQuotes == 0) {

          // Parse both sets of coordinates and use nearest method to find the
          // nearest traversable nodes to these coordinates.
          try {
            double[] coords1 = { Double.parseDouble(parsed[1]),
                Double.parseDouble(parsed[2]) };
            double[] coords2 = { Double.parseDouble(parsed[3]),
                Double.parseDouble(parsed[4]) };

            Node n1 = (Node) mc.nearestCommand(coords1);
            Node n2 = (Node) mc.nearestCommand(coords2);

            // Route between the two nodes.
            mc.route(n1, n2);
          } catch (Exception ex) {
            Handling.improperCommandUse("route <lat1> <lon1> <lat2> <lon2>");
          }
        } else {
          Handling.improperCommandUse("route <lat1> <lon1> <lat2> <lon2>");
        }
      }

      // If first word of command is not recognized, error.
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

  /**
   * Getter for the MapCommand used in this repl.
   *
   * @return MapCommand used in this repl.
   */
  public MapCommand getMapCommand() {
    return mc;
  }
}
