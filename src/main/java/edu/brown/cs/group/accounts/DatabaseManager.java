package edu.brown.cs.group.accounts;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;

import org.sqlite.SQLiteException;

import edu.brown.cs.group.handling.Handling;

/*
 * SQLITE3 COMMAND TO BUILD TABLE:
 * CREATE TABLE "accounts"("userid" INTEGER, "username" TEXT, "salt" TEXT, "password" TEXT, PRIMARY KEY ("userid"));
 */
/**
 * A class that builds a graph. This class handles all interaction with the
 * database.
 * 
 *
 * @author Reid
 */
public class DatabaseManager {
  private Connection conn = null;
  private PreparedStatement prep = null;
  private Protection prot;

  private HashMap<Integer, User> users;

  /**
   * Constructs a new GraphBuilder.
   *
   * @param path
   *          the path to the database
   */
  public DatabaseManager(String path) {
    // this line loads the driver manager class, and must be
    // present for everything else to work properly
    try {
      Class.forName("org.sqlite.JDBC");
    } catch (ClassNotFoundException e) {
      Handling.error("class not found");
      return;
    }
    String urlToDB = "jdbc:sqlite:" + path;
    try {
      conn = DriverManager.getConnection(urlToDB);
      prot = new Protection();
      users = new HashMap<Integer, User>();
      addAllCurrentUsers();
      System.out.println("db set to " + path);
    } catch (SQLException e) {
      Handling.sqlError();
      Handling.error("failed to access database");
    }
  }

  public String addUser(String username, String password) {
    ResultSet rs;
    try {
      prep = conn.prepareStatement("SELECT * FROM accounts WHERE username=?");
      prep.setString(1, username);
      rs = prep.executeQuery();
      if (rs.next()) {
        Handling.error("username is already taken.");
        return "Username is already taken.";
      }

      String salt = prot.generateSalt();
      prep = conn
          .prepareStatement("INSERT INTO \"accounts\" VALUES (?, ?, ?, ?);");
      prep.setNull(1, Types.INTEGER);
      prep.setString(2, username);
      prep.setString(3, salt);
      prep.setString(4, prot.hash(password, salt));
      prep.executeUpdate();

      prep = conn
          .prepareStatement("SELECT userid FROM accounts WHERE username=?");
      prep.setString(1, username);
      rs = prep.executeQuery();
      rs.next();
      int id = rs.getInt(1);

      users.put(id, new User(id, username));
      return null;
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return "Invalid username or password.";
  }

  public User getUser(String username, String password) {
    ResultSet rs;
    try {
      prep = conn.prepareStatement(
          "SELECT salt, password FROM accounts WHERE username=?");
      prep.setString(1, username);
      rs = prep.executeQuery();
      if (rs.next()) {
        String salt = rs.getString(1);
        String encryptedPass = rs.getString(2);
        if (encryptedPass.equals(prot.hash(password, salt))) {
          prep = conn
              .prepareStatement("SELECT userid FROM accounts WHERE username=?");
          prep.setString(1, username);
          rs = prep.executeQuery();
          rs.next();
          int id = rs.getInt(1);
          return users.get(id);
        } else {
          Handling.invalidLogin();
          return null;
        }
      } else {
        Handling.invalidLogin();
        return null;
      }
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Closes all open connections.
   */
  public void close() {
    try {
      if (prep != null && !prep.isClosed()) {
        prep.close();
      }
      if (conn != null && !conn.isClosed()) {
        conn.close();
      }
    } catch (SQLException e) {
      Handling.error("error closing connections");
      e.printStackTrace();
    }
  }

  private void addAllCurrentUsers() throws SQLException {
    ResultSet rs;
    prep = conn.prepareStatement("SELECT userid, username FROM accounts;");
    rs = prep.executeQuery();
    while (rs.next()) {
      int userid = rs.getInt(1);
      String username = rs.getString(2);
      users.put(userid, new User(userid, username));
    }
    rs.close();
  }

  public String getUsername(int userId) {
    try {
      prep = conn
          .prepareStatement("SELECT username FROM accounts WHERE userId=?");
      prep.setInt(1, userId);
      ResultSet rs = prep.executeQuery();
      rs.next();
      return rs.getString(1);
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }

  public String changeUsername(String currUsername, String password,
      String newUsername) {
    ResultSet rs;
    try {
      if (getUser(currUsername, password) == null) {
        return "Invalid username or password.";
      }
      prep = conn.prepareStatement("SELECT * FROM accounts WHERE username=?");
      prep.setString(1, newUsername);
      rs = prep.executeQuery();
      if (rs.next()) {
        Handling.error("username is already taken.");
        return "Username is already taken.";
      }

      prep = conn
          .prepareStatement("SELECT userid FROM accounts WHERE username=?");
      prep.setString(1, currUsername);
      rs = prep.executeQuery();
      rs.next();
      int userId = rs.getInt(1);

      String salt = prot.generateSalt();
      prep = conn.prepareStatement(
          "UPDATE accounts SET username=?, salt=?, password=? WHERE userid=?;");
      prep.setString(1, newUsername);
      prep.setString(2, salt);
      prep.setString(3, prot.hash(password, salt));
      prep.setInt(4, userId);
      prep.executeUpdate();
      users.get(userId).setUsername(newUsername);
      return null;
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return "Error changing username.";
  }

  public boolean changePassword(String username, String currPassword,
      String newPassword) {
    ResultSet rs;
    try {
      if (getUser(username, currPassword) == null) {
        return false;
      }

      prep = conn
          .prepareStatement("SELECT userid FROM accounts WHERE username=?");
      prep.setString(1, username);
      rs = prep.executeQuery();
      rs.next();
      int userId = rs.getInt(1);

      String salt = prot.generateSalt();
      prep = conn.prepareStatement(
          "UPDATE accounts SET salt=?, password=? WHERE userid=?;");
      prep.setString(1, salt);
      prep.setString(2, prot.hash(newPassword, salt));
      prep.setInt(3, userId);
      prep.executeUpdate();
      return true;
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return false;
  }
}
