package edu.brown.cs.group.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

import edu.brown.cs.group.accounts.DatabaseManager;
import edu.brown.cs.group.accounts.Protection;
import edu.brown.cs.group.accounts.User;
import edu.brown.cs.group.components.Bishop;
import edu.brown.cs.group.components.Board;
import edu.brown.cs.group.components.King;
import edu.brown.cs.group.components.Piece;
import edu.brown.cs.group.games.ABCutoffAI;
import edu.brown.cs.group.games.ABCutoffAIV2;
import edu.brown.cs.group.games.ChessGame;
import edu.brown.cs.group.games.Player;
import edu.brown.cs.group.games.ReplPlayer;
import edu.brown.cs.group.handling.Handling;
import edu.brown.cs.group.positions.Position;
import edu.brown.cs.group.positions.PositionException;

public class REPL {
  private Protection prot;
  private DatabaseManager dbm;
  private User user;

  public REPL() {
    prot = new Protection();
    user = null;
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
      if (dbm != null) {
        dbm.close();
      }
    } catch (IOException ioe) {
      // Not possible. No error message can make sense of this.
      ioe.printStackTrace();
    }
  }

  /**
   * Processes user input and acts accordingly.
   *
   * @param line
   *          The command provided by the user
   */
  public void processCommand(String line) {
    // Split on whitespace and punctuation
    String[] parsed = line.split("\\s+");
    if (parsed[0].equals("mdb")) {
      if (parsed.length == 2) {
        if (dbm != null) {
          dbm.close();
        }
        dbm = new DatabaseManager(parsed[1]);
      }
    } else if (parsed[0].equals("game")) {

      try {
        Player pl1 = new ABCutoffAI();
        Player pl2 = new ABCutoffAI();
        HashMap<Position, Piece> temp = new HashMap<>();
        Position p1 = new Position(2, 8);
        Piece k1 = new King(p1, 1);
        temp.put(p1, k1);

        Position p2 = new Position(4, 2);
        Piece k2 = new King(p2, 0);
        temp.put(p2, k2);

        Position p3 = new Position(4, 5);
        Piece bishop = new Bishop(p3, 0);
        temp.put(p3, bishop);
        Board b1 = new Board(temp, pl1, pl2);

        // ChessGame game = new ChessGame(pl1, pl2, b1);
        ChessGame game = new ChessGame(new ABCutoffAIV2(5), new ReplPlayer());
        // ChessGame game = new ChessGame(new ReplPlayer(), new ReplPlayer());
        game.play();
      } catch (PositionException e) {

        e.printStackTrace();

      }
    } else if (parsed[0].equals("new")) {
      if (parsed.length == 3) {
        dbm.addUser(parsed[1], parsed[2]);
      } else {
        Handling.improperCommandUse("new <username> <password>");
      }
    } else if (parsed[0].equals("login")) {
      if (parsed.length == 3) {
        if (dbm != null) {
          user = dbm.getUser(parsed[1], parsed[2]);
          if (user != null) {
            System.out.println("logged in as " + user.getUsername(dbm));
          }
        } else {
          Handling.error("no input database");
        }
      }
    } else if (line.equals("logout")) {
      user = null;
      System.out.println("logged out");
    } else if (parsed[0].equals("username")) {
      if (line.equals("username")) {
        if (user != null) {
          System.out.println(user.getUsername(dbm));
        } else {
          Handling.notLoggedIn();
        }
      } else if (parsed.length == 4) {
        if (user != null) {
          if (dbm.changeUsername(parsed[1], parsed[2], parsed[3])) {
            System.out.println("username set to \"" + parsed[3] + "\"");
          } else {
            Handling.error("failed to update username");
          }
        } else {
          Handling.notLoggedIn();
        }
      } else {
        Handling.improperCommandUse(
            "username <old username> <password> <new username>");
      }
    } else if (parsed[0].equals("password")) {
      if (parsed.length == 4) {
        if (user != null) {
          if (dbm.changePassword(parsed[1], parsed[2], parsed[3])) {
            System.out.println("password updated");
          } else {
            Handling.error("failed to update password");
          }
        } else {
          Handling.notLoggedIn();
        }
      } else {
        Handling.improperCommandUse(
            "password <username> <old password> <new password>");
      }

    } else {
      Handling.error("Invalid command.");
      return;
    }

  }

  public User getUser() {
    return user;
  }

  public DatabaseManager getDbm() {
    return dbm;
  }
}
