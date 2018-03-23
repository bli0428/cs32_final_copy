package edu.brown.cs.rmerzbac.main;

import joptsimple.OptionParser;
import joptsimple.OptionSet;

/**
 * The Main class of our project. This is where execution begins.
 *
 * @author rmerzbac, jj
 */
public final class Main {

  private static final int DEFAULT_PORT = 4567;
  private static REPL repl;

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
   * Runs the program.
   */
  private void run() {

    // Parse command line arguments
    OptionParser parser = new OptionParser();
    parser.accepts("gui");
    parser.accepts("port").withRequiredArg().ofType(Integer.class)
        .defaultsTo(DEFAULT_PORT);
    // Additional feature, used for testing
    parser.accepts("dimensions").withRequiredArg().ofType(Integer.class);

    OptionSet options = parser.parse(args);

    repl = new REPL();
    if (options.has("gui")) {
      System.out.println("gui not set up yet");
      // GUI.runSparkServer((int) options.valueOf("port"));
    }
    repl.runRepl();
  }

}
