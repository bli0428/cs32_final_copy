package edu.brown.cs.gajith.main;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;


import freemarker.template.Configuration;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import spark.ExceptionHandler;
import spark.Request;
import spark.Response;
import spark.Spark;
import spark.template.freemarker.FreeMarkerEngine;

/**
 * The Main class of our project. This is where execution begins.
 *
 * @author gajith
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

  //private static Repl repl;
  private String[] args;

  private Main(String[] args) {
    this.args = args;
  }

  private void run() {
    // Parse command line arguments
    OptionParser parser = new OptionParser();
    parser.accepts("gui");
    parser.accepts("port").withRequiredArg().ofType(Integer.class)
        .defaultsTo(DEFAULT_PORT);

    OptionSet options = parser.parse(args);

    // create REPL and run sparkserver and repl
   // repl = new Repl();
    if (options.has("gui")) {
      runSparkServer((int) options.valueOf("port"));
    }

    //repl.run(null);
  }

  private static FreeMarkerEngine createEngine() {
    Configuration config = new Configuration();
    File templates = new File("src/main/resources/spark/template/freemarker");
    try {
      config.setDirectoryForTemplateLoading(templates);
    } catch (IOException ioe) {
      System.out.printf("ERROR: Unable use %s for template loading.%n",
          templates);
      System.exit(1);
    }
    return new FreeMarkerEngine(config);
  }

  private void runSparkServer(int port) {
    Spark.port(port);
    Spark.externalStaticFileLocation("src/main/resources/static");
    Spark.exception(Exception.class, new ExceptionPrinter());

    FreeMarkerEngine freeMarker = createEngine();

    // Setup Spark Routes
//    Spark.get("/home", new HomeFrontHandler(), freeMarker);
//    Spark.get("/stars", new StarsFrontHandler(), freeMarker);
//    Spark.post("/results", new StarsSubmitHandler(repl), freeMarker);
//    Spark.get("/autocorrect", new ACFrontHandler(), freeMarker);
//    Spark.post("/validate", new ACValidateHandler(repl));
//    Spark.get("/bacon", new BaconFrontHandler(), freeMarker);
//    Spark.post("/baconac", new BaconAutocorrect(repl));
//    Spark.get("/bacon/actor/:actorid", new ActorHandler(repl), freeMarker);
//    Spark.get("/bacon/movie/:movieid", new MovieHandler(repl), freeMarker);
//    Spark.post("/bacon", new BaconValidateHandler(repl));

  }

  /**
   * Display an error page when an exception occurs in the server.
   *
   * @author jj
   */
  private static class ExceptionPrinter implements ExceptionHandler {
    @Override
    public void handle(Exception e, Request req, Response res) {
      res.status(500);
      StringWriter stacktrace = new StringWriter();
      try (PrintWriter pw = new PrintWriter(stacktrace)) {
        pw.println("<pre>");
        e.printStackTrace(pw);
        pw.println("</pre>");
      }
      res.body(stacktrace.toString());
    }
  }

}
