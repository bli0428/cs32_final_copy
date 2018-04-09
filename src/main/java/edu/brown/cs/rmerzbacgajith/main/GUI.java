package edu.brown.cs.rmerzbacgajith.main;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;

import freemarker.template.Configuration;
import spark.ExceptionHandler;
import spark.ModelAndView;
import spark.QueryParamsMap;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;
import spark.TemplateViewRoute;
import spark.template.freemarker.FreeMarkerEngine;

/**
 * A class that runs the GUI.
 *
 * @author rmerzbac and jj
 */
public final class GUI {
  private static final Gson GSON = new Gson();
  private static REPL repl;

  public GUI(REPL repl) {
    this.repl = repl;
  }

  /**
   * Creates a new FreeMarkerEngine, which allows the SparkServer to be run.
   *
   * @return the FreeMarkerEngine
   */
  private FreeMarkerEngine createEngine() {
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

  /**
   * Runs the SparkServer on a specified port.
   *
   * @param port
   *          the port number
   */
  public void runSparkServer(int port) {
    Spark.port(port);
    Spark.externalStaticFileLocation("src/main/resources/static");
    Spark.exception(Exception.class, new ExceptionPrinter());

    FreeMarkerEngine freeMarker = createEngine();

    // Setup Spark Routes
    Spark.get("/maps", new MapsFrontHandler(), freeMarker);
    Spark.post("/results", new MapsResultsHandler());
  }

  /**
   * Handle requests to the front page of our Autocorrect website.
   *
   * @author Reid
   */
  private static class MapsFrontHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {

      Map<String, Object> variables = ImmutableMap.of("title", "Maps");
      return new ModelAndView(variables, "maps.ftl");
    }
  }

  /**
   * Handles user input from the GUI and searches for suggestions.
   *
   * @author Reid
   *
   */
  private class MapsResultsHandler implements Route {
    @Override
    public String handle(Request req, Response res) {
      QueryParamsMap qm = req.queryMap();

      double[] coords1 = { Double.parseDouble(qm.value("top")),
          Double.parseDouble(qm.value("left")) };
      double[] coords2 = { Double.parseDouble(qm.value("bottom")),
          Double.parseDouble(qm.value("right")) };
      List<String> wayId = repl.getMapCommand().waysCommand(coords1, coords2,
          false);
      List<List<double[]>> coords = new ArrayList<>();
      for (String way : wayId) {
        coords.add(repl.getMapCommand().getDBHelper().getWayLocation(way));
      }

      Map<String, Object> variables = ImmutableMap.of("ways", coords);
      return GSON.toJson(variables);
    }

  }

  /**
   * Display an error page when an exception occurs in the server.
   *
   * @author jj
   */
  private class ExceptionPrinter implements ExceptionHandler {
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

  /**
   * Display an error message.
   *
   * @param message
   *          the message to be displayed
   * @return the message to be displayed, with HTML added
   */
  private static String errorMessage(String message) {
    String s = "<h1>Error</h1><p>" + message + "</p>";
    return s;
  }
}
