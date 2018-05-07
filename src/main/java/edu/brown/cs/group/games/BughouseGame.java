package edu.brown.cs.group.games;

import java.io.IOException;
import java.util.Set;

import org.eclipse.jetty.websocket.api.Session;

import com.google.gson.JsonObject;

import edu.brown.cs.group.components.Board;
import edu.brown.cs.group.components.InvalidMoveException;
import edu.brown.cs.group.components.Piece;
import edu.brown.cs.group.main.ChessWebSocket;
import edu.brown.cs.group.positions.BankPosition;
import edu.brown.cs.group.positions.Position;
import edu.brown.cs.group.positions.PositionException;

/**
 * Class representing a game of Bughouse.
 *
 * @author charliecutting
 *
 */
public class BughouseGame implements Game {
  private Player p0;
  private Player p1;
  private Player p2;
  private Player p3;
  private Board board1;
  private Board board2;
  private int turn = 0;
  private Player[] team1;
  private Player[] team2;
  private Board[] boards;
  private Player[][] teams;
  private boolean gameOver;

  /**
   * Public constructor.
   *
   * @param p0
   * @param p1
   * @param p2
   * @param p3
   * @param b1
   * @param b2
   */
  public BughouseGame(Player p0, Player p1, Player p2, Player p3) {
    try {
      board1 = new Board(p0, p2, true);
      board2 = new Board(p3, p1, true);
    } catch (PositionException e) {
      // Shouldn't get here
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    this.p0 = p0;
    this.p1 = p1;
    this.p2 = p2;
    this.p3 = p3;

    p0.setBoard(board1);
    p2.setBoard(board1);
    p1.setBoard(board2);
    p3.setBoard(board2);
    Player[] t1 = { p0, p1 };
    Player[] t2 = { p3, p2 };
    team1 = t1;
    team2 = t2;
    p0.setColor(0);
    p2.setColor(1);
    p1.setColor(1);
    p3.setColor(0);
    Board[] b = { board1, board2 };
    boards = b;
    Player[][] t = { team1, team2 };
    teams = t;
    gameOver = false;
  }

  /**
   * Runnable for game logic.
   *
   * @author charliecutting
   *
   */
  private class SubGame implements Runnable {

    private int turn;
    private int b;
    private Player[] prs;

    /**
     * Public constructor.
     */
    SubGame(int b) {
      turn = 0;
      this.b = b;
      if (b == 0) {
        Player[] play = { p0, p2 };
        prs = play;
      } else {
        Player[] play = { p3, p1 };
        prs = play;
      }

    }

    @Override
    public void run() {
      while (!gameOver) {
        // System.out.println("here, player " + getPlay(turn, b));
        if (boards[b].gameOver(turn) == 1) {
          endGame(getTeam(Math.abs(turn - 1), b));
          System.out.println("Game over!");
          break;
        }
        Move m = prs[turn].move();
        try {
          Piece p;
          if (m.start() instanceof BankPosition) {
            p = boards[b].processPlace(m.start(), m.end(), m.getPiece());
          } else {
            p = boards[b].processMove(m.start(), m.end(), false);
          }
          if (p != null) {
            teams[getTeam(turn, b)][Math.abs(b - 1)].acceptPiece(p);
            System.out.println("sending " + p.type() + " to " + getTeam(turn, b)
                + " " + Math.abs(b - 1));
          }
          turn = Math.abs(turn - 1);
          // System.out.println("Moved from " + m.start().col() + ","
          // + m.start().row() + " to " + m.end().col() + "," + m.end().row());
          for (Session session : ChessWebSocket.games.keySet()) {
            // System.out
            // .println("shdijgklmgrisdjnhgjsdgjbgsbhijsndrgisgijisbfjgfsj");
            if (ChessWebSocket.games.get(session) == BughouseGame.this) {
              System.out.println(ChessWebSocket.playerNum
                  .get(ChessWebSocket.playerSession.get(session)) + " "
                  + getPlay(turn, b));
              if (ChessWebSocket.playerNum.get(
                  ChessWebSocket.playerSession.get(session)) == getPlay(turn,
                      b)) {
                // System.out.println("here " + "b=" + b + "turn=" + turn
                // + "update=" + b * 2 + turn);
                JsonObject message = new JsonObject();

                if (m.start() instanceof BankPosition) {
                  message.addProperty("type",
                      ChessWebSocket.MESSAGE_TYPE.UPDATE.ordinal());
                  JsonObject payload = new JsonObject();
                  payload.addProperty("moveFrom", m.start().numString());
                  payload.addProperty("moveTo", m.end().numString());
                  payload.addProperty("piece", m.getPiece().type());
                  payload.addProperty("color", m.getPiece().color());
                  message.add("payload", payload);
                  session.getRemote()
                      .sendString(ChessWebSocket.GSON.toJson(message));
                } else {
                  message.addProperty("type",
                      ChessWebSocket.MESSAGE_TYPE.UPDATE.ordinal());
                  JsonObject payload = new JsonObject();
                  payload.addProperty("moveFrom", m.start().numString());
                  payload.addProperty("moveTo", m.end().numString());
                  message.add("payload", payload);
                  session.getRemote()
                      .sendString(ChessWebSocket.GSON.toJson(message));
                  // System.out.println("Sent move");
                  break;
                }
              }
            }
          }
        } catch (InvalidMoveException e) {
          System.out.println("That's not a valid move!");
        } catch (IOException e) {
          // TODO Auto-generated catch block
          // Shouldn't get here
          e.printStackTrace();
        }

      }

    }
  }

  public int getPlay(int a, int b) {
    if (a == 0 && b == 0)
      return 0;
    if (a == 1 && b == 0)
      return 2;
    if (a == 0 && b == 1)
      return 3;
    if (a == 1 && b == 1)
      return 1;
    return -1;
  }

  private synchronized void endGame(int team) {
    gameOver = true;
    for (Session s : ChessWebSocket.games.keySet()) {
      if (ChessWebSocket.games.get(s).equals(this)) {
        JsonObject message = new JsonObject();
        message.addProperty("type",
            ChessWebSocket.MESSAGE_TYPE.GAMEOVER.ordinal());
        JsonObject payload = new JsonObject();
        String winner = "team " + (team + 1) + " ";
        payload.addProperty("winner", winner);
        message.add("payload", payload);
        try {
          s.getRemote().sendString(ChessWebSocket.GSON.toJson(message));
        } catch (IOException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
      }
    }
  }

  public void play() {
    Thread b0 = new Thread(new SubGame(0));
    Thread b1 = new Thread(new SubGame(1));

    b0.start();
    b1.start();
  }

  @Override
  public Set<Position> moves(int player, Position pos) {
    if (player % 2 == 0) {
      System.out.println(player);
      return boards[0].getValidMoves(player / 2).get(pos);
    } else {
      System.out.println(player);
      return boards[1].getValidMoves(player % 3).get(pos);
    }
  }

  public int getTeam(int a, int b) {
    if (a == 0 && b == 0)
      return 0;
    if (a == 1 && b == 0)
      return 1;
    if (a == 0 && b == 1)
      return 1;
    if (a == 1 && b == 1)
      return 0;
    return -1;
  }
}
