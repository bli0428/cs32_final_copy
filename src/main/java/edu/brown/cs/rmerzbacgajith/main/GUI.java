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

import edu.brown.cs.rmerzbacgajith.handling.Handling;
import edu.brown.cs.rmerzbacgajith.maps.Way;
import edu.brown.cs.rmerzbacgajith.tree.Node;
import edu.brown.cs.rmerzbacgajith.tree.Point;
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
    Spark.post("/nearest", new NearestHandler());
    Spark.post("/route", new RouteHandler());
    Spark.post("/routeCoords", new RouteCoordsHandler());
    Spark.post("/mapsac", new ACHandler());
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

      //Get coords for ways Bounding box
      double[] coords1 = { Double.parseDouble(qm.value("top")),
          Double.parseDouble(qm.value("left")) };
      double[] coords2 = { Double.parseDouble(qm.value("bottom")),
          Double.parseDouble(qm.value("right")) };
      
      //Call ways command
      List<String> wayId = repl.getMapCommand().waysCommand(coords1, coords2);
      List<List<double[]>> coords = new ArrayList<>();
      for (String way : wayId) {
        coords.add(repl.getMapCommand().getDBHelper().getWayLocation(way));
      }

      //Send List of coords to JS
      Map<String, Object> variables = ImmutableMap.of("ways", coords);
      return GSON.toJson(variables);
    }

  }

  /**
   * Handles user input from the GUI and searches for nearest Node.
   *
   *
   */
  private class NearestHandler implements Route {
    @Override
    public String handle(Request req, Response res) {
      QueryParamsMap qm = req.queryMap();

      //Get coords
      Double lat = Double.parseDouble(qm.value("lat"));
      Double lon = Double.parseDouble(qm.value("lon"));

      double[] coords = { lat, lon };

      //Call nearest command
      Point node = repl.getMapCommand().nearestCommand(coords);

      Map<String, Object> variables = ImmutableMap.of("node", node.getCoords());
      return GSON.toJson(variables);
    }
  }

  /**
   * Routes between Intersections for GUI
   * @author gokulajith
   *
   */
  public static class RouteHandler implements Route {
    @Override
    public String handle(Request req, Response res) {
      QueryParamsMap qm = req.queryMap();

      //Get all Streets
      String start1 = qm.value("start1");
      String start2 = qm.value("start2");
      String end1 = qm.value("end1");
      String end2 = qm.value("end2");

      List<List<double[]>> coords = new ArrayList<>();

      //Check for both intersections
      Node n1 = repl.getMapCommand().getIntersection(start1, start2);
      if (n1 == null) {
        Handling.error("first intersection not found");
      }
      Node n2 = repl.getMapCommand().getIntersection(end1, end2);
      if (n2 == null) {
        Handling.error("second intersection not found");
      } else {
        
        //Route between two nearest nodes
        List<Way> ways = repl.getMapCommand().route(n1, n2);
        for (Way way : ways) {
          coords.add(
              repl.getMapCommand().getDBHelper().getWayLocation(way.getId()));
        }
      }

      Map<String, Object> variables = ImmutableMap.of("ways", coords);
      return GSON.toJson(variables);
    }
  }

  /**
   * 
   * Route with coordinates
   *
   */
  public static class RouteCoordsHandler implements Route {
    @Override
    public String handle(Request req, Response res) {
      QueryParamsMap qm = req.queryMap();

      // neighbors command with coordinates
      Double start1 = Double.parseDouble(qm.value("start1"));
      Double start2 = Double.parseDouble(qm.value("start2"));
      Double end1 = Double.parseDouble(qm.value("end1"));
      Double end2 = Double.parseDouble(qm.value("end2"));

      double[] coords1 = { start1, start2 };
      double[] coords2 = { end1, end2 };

      List<List<double[]>> coords = new ArrayList<>();

      //Find nearest node to both coordinates
      Node n1 = (Node) repl.getMapCommand().nearestCommand(coords1);
      if (n1 == null) {
        Handling.error("first intersection not found");
      }
      Node n2 = (Node) repl.getMapCommand().nearestCommand(coords2);
      if (n2 == null) {
        Handling.error("second intersection not found");
      } else {
        
        //Route between the two nearest nodes.
        List<Way> ways = repl.getMapCommand().route(n1, n2);
        for (Way way : ways) {
          coords.add(
              repl.getMapCommand().getDBHelper().getWayLocation(way.getId()));
        }
      }

      Map<String, Object> variables = ImmutableMap.of("ways", coords);
      return GSON.toJson(variables);
    }
  }
  
  /**
   * Handler for autocorrecting street names.
   * @author gokulajith
   *
   */
  public static class ACHandler implements Route {
    @Override
    public String handle(Request req, Response res) {
      QueryParamsMap qm = req.queryMap();

      String ac = qm.value("ac");
      List<String> finalAnswer = new ArrayList<String>();
      
      //Use suggest command as before for suggestions.
      if (ac != null && ac.length() > 0) {
        finalAnswer = repl.getMapCommand().suggest(ac);
      }

      Map<String, Object> variables = ImmutableMap.of("suggestions", finalAnswer);
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
