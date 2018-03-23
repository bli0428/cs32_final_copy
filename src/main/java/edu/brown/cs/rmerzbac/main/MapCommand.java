package edu.brown.cs.rmerzbac.main;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import edu.brown.cs.rmerzbac.handling.Handling;

/**
 * BaconCommand Class that handles all of the bacon specific commands and
 * functionality. It is responsible for mdb and connect commands, as well as
 * querying for various information from the database.
 *
 * @author gokulajith
 *
 */
public class MapCommand {

  private static Connection conn = null;
  private AutocorrectCommand acCommandHelper = new AutocorrectCommand();

  /**
   * Method that deals with the mdb command to connect to a database, as well as
   * creating a Trie with all actorNames on connection to know exactly what
   * names are in the database, as well as for autocorrecting.
   *
   * @param dbname
   *          filepath to database.
   */
  public void mdbCommand(String dbname) {

    // Close conn if previous connection exists.
    if (conn != null) {
      try {
        conn.close();
      } catch (SQLException e) {
        Handling.error("Issue when closing connection to database.");
        return;
      }
    }

    File file = new File(dbname);

    if (!file.exists()) {
      Handling.error("Database could not be found or connected to.");
      return;
    }

    try {
      Class.forName("org.sqlite.JDBC");
    } catch (ClassNotFoundException e) {
      System.out.println("ERROR: Database could not be found or connected to.");
      return;
    }

    // Connect to db
    String urlToDB = new StringBuilder("jdbc:sqlite:" + dbname).toString();

    try {
      conn = DriverManager.getConnection(urlToDB);
      Statement stat = conn.createStatement();
      stat.executeUpdate("PRAGMA foreign_keys = ON;");
    } catch (SQLException ex) {
      System.out.println("ERROR: Database could not be found or connected to.");
      return;
    }

    System.out.println(new StringBuilder("map set to " + dbname).toString());

    // Create Trie for autocorrect.
    Trie trie = new Trie();

    // Query every actor in db
    PreparedStatement prep;
    ResultSet rs;
    try {
      prep = conn.prepareStatement("SELECT name FROM way");
      rs = prep.executeQuery();

      while (rs.next()) {
        String name = rs.getString(1);

        // Put all actors name and id mapping in cache and
        // insert the names into trie.
        // (Since you have to insert every name into the Trie, it is better to
        // also save every name and id so this query never
        // needs to happen again.)
        if (name.length() > 0) {
          trie.insert(name);
        }
      }
      prep.close();
      rs.close();
    } catch (SQLException e) {
      System.out.println("ERROR: Database does not contain proper tables.");
      return;
    }

    // Set trie to AutocorrectCommand Helper for autocorrect
    acCommandHelper.setTrie(trie);

    return;
  }

  public List<String> suggest(String ac) {

    // Turn prefix on if not on.
    if (!acCommandHelper.getPrefixStatus().equals("on")) {
      String[] prefixOn = new String[2];
      prefixOn[0] = "prefix";
      prefixOn[1] = "on";
      acCommandHelper.setSettings(prefixOn);
    }

    // Use acCommand method to find suggestions.
    String[] input = new String[1];
    input[0] = ac;

    List<String> suggestions = acCommandHelper.acCommand(input);
    return suggestions;

  }
}
