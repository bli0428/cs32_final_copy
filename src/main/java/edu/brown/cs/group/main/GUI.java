package edu.brown.cs.group.main;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//import javax.security.auth.login.Configuration;
//import javax.xml.ws.Response;

import freemarker.template.Configuration;
//import freemarker.template.Version;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
//import com.sun.corba.se.impl.presentation.rmi.ExceptionHandler;


import edu.brown.cs.group.accounts.User;
import spark.ModelAndView;
import spark.QueryParamsMap;
import spark.Route;
import spark.Spark;
import spark.TemplateViewRoute;
import spark.template.freemarker.FreeMarkerEngine;

import spark.ExceptionHandler;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import spark.Request;
import spark.Response;


/**
 * A class that runs the GUI.
 *
 * @author rmerzbac and jj
 */
public final class GUI {
  private static final Gson GSON = new Gson();
  private static REPL repl;

  /**
   * Constructor for GUI.
   *
   * @param repl
   *          the REPL that is being used to call commands
   */
  public GUI(REPL repl) {
    this.repl = repl;
  }

  /**
   * Creates a new FreeMarkerEngine, which allows the SparkServer to be run.
   *
   * @return the FreeMarkerEngine
   */
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

  /**
   * Runs the SparkServer on a specified port.
   *
   * @param port
   *          the port number
   */
  public static void runSparkServer(int port) {
    Spark.port(port);
    Spark.externalStaticFileLocation("src/main/resources/static");
    Spark.exception(Exception.class, new ExceptionPrinter());

    FreeMarkerEngine freeMarker = createEngine();

    // Setup Spark Routes
    Spark.get("/login", new LoginFrontPageHandler(), freeMarker);
    Spark.post("/loginresults", new LoginResultsHandler(), freeMarker);
    Spark.get("/newaccount", new NewAccountFrontHandler(), freeMarker);
    Spark.post("/newaccountresults", new NewAccountResultsHandler(),
        freeMarker);
    Spark.get("/changepassword", new ChangePasswordFrontHandler(), freeMarker);
    Spark.post("/changepasswordresults", new ChangePasswordResultsHandler(),
        freeMarker);
    Spark.get("/changeusername", new ChangeUsernameFrontHandler(), freeMarker);
    Spark.post("/changeusernameresults", new ChangeUsernameResultsHandler(),
        freeMarker);
    Spark.post("/logout", new LogoutHandler(), freeMarker);

    Spark.get("/chess", new ChessHandler(), freeMarker);
    Spark.post("/chess", new MoveHandler());
  }

  private static class ChessHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {
      Map<String, Object> variables = ImmutableMap.of("title", "CHESS");
      return new ModelAndView(variables, "board.ftl");
    }
  }

  private static class MoveHandler implements Route {
    @Override
    public String handle(Request req, Response res) {
      QueryParamsMap qm = req.queryMap();
      int row = Integer.parseInt(qm.value("row"));
      int col = Integer.parseInt(qm.value("col"));

      // TODO: get valid moves from row and col

      // TODO: pass in which player's turn
      // TODO: pass in list of valid moves

      // hardcoded for testing
      List<String> validMoves = new ArrayList<String>();
      validMoves.add("5-4");
      validMoves.add("4-4");

      Map<String, Object> variables = ImmutableMap.of("validMoves", validMoves);
      return GSON.toJson(variables);
    }
  }

  /**
   * Handle requests to the front page of our Autocorrect website.
   *
   * @author Reid
   */
  private static class LoginFrontPageHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {

      Map<String, Object> variables = ImmutableMap.of("title", "Login",
          "message", "");
      return new ModelAndView(variables, "login.ftl");
    }
  }

  /**
   * Handles "radius" commands via the GUI and displays the results.
   *
   * @author rmerzbac
   */
  public static class LoginResultsHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {
      QueryParamsMap qm = req.queryMap();

      String username = qm.value("username");
      String password = qm.value("password");
      String remember = qm.value("remember");
      repl.processCommand("login " + username + " " + password);
      User user = repl.getUser();

      if (user != null) {
        Map<String, Object> variables = ImmutableMap.of("title", "Home",
            "content",
            "<p>Welcome " + user.getUsername(repl.getDbm()) + ".</p>");
        return new ModelAndView(variables, "home.ftl");
      } else {
        Map<String, Object> variables = ImmutableMap.of("title", "Login",
            "message", "Invalid username or password.");
        return new ModelAndView(variables, "login.ftl");
      }
    }
  }

  /**
   * Handle requests to the front page of our Autocorrect website.
   *
   * @author Reid
   */
  private static class NewAccountFrontHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {

      Map<String, Object> variables = ImmutableMap.of("title", "Create account",
          "message", "");
      return new ModelAndView(variables, "newaccount.ftl");
    }
  }

  /**
   * Handles "radius" commands via the GUI and displays the results.
   *
   * @author rmerzbac
   */
  public static class NewAccountResultsHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {
      QueryParamsMap qm = req.queryMap();

      String username = qm.value("username");
      String password = qm.value("password");
      String password2 = qm.value("password2");
      if (!password.equals(password2)) {
        Map<String, Object> variables = ImmutableMap.of("title",
            "Create account", "message", "Passwords don't match.");
        return new ModelAndView(variables, "newaccount.ftl");
      }
      if (!repl.getDbm().addUser(username, password)) {
        Map<String, Object> variables = ImmutableMap.of("title",
            "Create account", "message", "Invalid username or password.");
        return new ModelAndView(variables, "newaccount.ftl");
      }
      Map<String, Object> variables = ImmutableMap.of("title", "Login",
          "message", "New account created.");
      return new ModelAndView(variables, "login.ftl");
    }
  }

  /**
   * Handle requests to the front page of our Autocorrect website.
   *
   * @author Reid
   */
  private static class ChangePasswordFrontHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {

      Map<String, Object> variables = ImmutableMap.of("title",
          "Change password", "message", "");
      return new ModelAndView(variables, "changepassword.ftl");
    }
  }

  public static class ChangePasswordResultsHandler
      implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {
      QueryParamsMap qm = req.queryMap();

      String username = qm.value("username");
      String currPassword = qm.value("currpassword");
      if (repl.getDbm().getUser(username, currPassword) == null) {
        Map<String, Object> variables = ImmutableMap.of("title",
            "Change password", "message", "Invalid username or password.");
        return new ModelAndView(variables, "changepassword.ftl");
      }

      String newPassword = qm.value("newpassword");
      String newPassword2 = qm.value("newpassword2");
      if (!newPassword.equals(newPassword2)) {
        Map<String, Object> variables = ImmutableMap.of("title",
            "Change password", "message", "Passwords don't match.");
        return new ModelAndView(variables, "changepassword.ftl");
      }
      if (!repl.getDbm().changePassword(username, currPassword, newPassword2)) {
        Map<String, Object> variables = ImmutableMap.of("title",
            "Change password", "message", "Failed to change password.");
        return new ModelAndView(variables, "changepassword.ftl");
      }
      Map<String, Object> variables = ImmutableMap.of("title", "Home",
          "content", "<p>Password successfully updated.</p>");
      return new ModelAndView(variables, "home.ftl");
    }
  }

  private static class ChangeUsernameFrontHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {

      Map<String, Object> variables = ImmutableMap.of("title",
          "Change password", "message", "");
      return new ModelAndView(variables, "changeusername.ftl");
    }
  }
  
  public static class ChangeUsernameResultsHandler
      implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {
      QueryParamsMap qm = req.queryMap();

      String currUsername = qm.value("currusername");
      String password = qm.value("password");
      if (repl.getDbm().getUser(currUsername, password) == null) {
        Map<String, Object> variables = ImmutableMap.of("title",
            "Change password", "message", "Invalid username or password.");
        return new ModelAndView(variables, "changeusername.ftl");
      }

      String newUsername = qm.value("newusername");
      if (!repl.getDbm().changeUsername(currUsername, password, newUsername)) {
        Map<String, Object> variables = ImmutableMap.of("title",
            "Change password", "message", "Failed to change username.");
        return new ModelAndView(variables, "changeusername.ftl");
      }
      Map<String, Object> variables = ImmutableMap.of("title", "Home",
          "content", "<p>Username successfully updated.</p>");
      return new ModelAndView(variables, "home.ftl");
    }
  }

  /**
   * Handles "radius" commands via the GUI and displays the results.
   *
   * @author rmerzbac
   */
  public static class LogoutHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {
      repl.processCommand("logout");
      Map<String, Object> variables = ImmutableMap.of("title", "Log in",
          "message", "");
      return new ModelAndView(variables, "login.ftl");
    }
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
