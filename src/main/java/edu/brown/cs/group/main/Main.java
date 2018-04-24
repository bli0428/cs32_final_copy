package edu.brown.cs.group.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import joptsimple.OptionParser;
import joptsimple.OptionSet;

/**
 * The Main class of our project. This is where execution begins.
 *
 * @author klee50
 */
public final class Main {

  private static final int DEFAULT_PORT = 4567;

  /**
   * The initial method called when execution begins.
   *
   * @param args
   *          An array of command line arguments
   */
  public static void main(String[] args) {
    new Main(args).run();
  }

  private String[] args;

  private Main(String[] args) {
    this.args = args;
  }

  /**
   * Run executes the program.
   *
   */
  private void run() {

    OptionParser parser = new OptionParser();
    parser.accepts("gui");
    parser.accepts("port").withRequiredArg().ofType(Integer.class)
        .defaultsTo(DEFAULT_PORT);
    OptionSet options = parser.parse(args);
    
    GUI.runSparkServer((int) options.valueOf("port")); //TODO: DELETE THIS
    if (options.has("gui")) {
      GUI.runSparkServer((int) options.valueOf("port"));
    }

    try (BufferedReader br = new BufferedReader(
        new InputStreamReader(System.in))) {
      String k;
      while ((k = br.readLine()) != null) {
        // TODO
      }
    } catch (IOException e) {
      System.out.println("ERROR: Please enter a valid command.");
    }
  }

}
