package edu.brown.cs.rmerzbacgajith.main;

import joptsimple.OptionParser;
import joptsimple.OptionSet;

/**
 * The Main class of our project. This is where execution begins.
 *
 * @author rmerzbac, jj
 */
public final class Main {

  // private static final int DEFAULT_PORT = 4567;
  private static final int DEFAULT_PORT = 3005;
  private static REPL repl;
  private static GUI gui;

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
    //gui = new GUI(repl);

    //gui.runSparkServer((int) options.valueOf("port")); // TODO: DELETE THIS
    if (options.has("gui")) {
      gui.runSparkServer((int) options.valueOf("port"));
    }
    repl.runRepl();
  }

}
