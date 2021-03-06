package edu.brown.cs.group.games;

import java.io.IOException;
import java.util.Set;

import org.eclipse.jetty.websocket.api.Session;

import com.google.gson.JsonObject;

import edu.brown.cs.group.components.Board;
import edu.brown.cs.group.components.InvalidMoveException;
import edu.brown.cs.group.main.ChessWebSocket;
import edu.brown.cs.group.positions.Position;
import edu.brown.cs.group.positions.PositionException;

/**
 * Class for running a chess game. Handles turn order, checkmate, etc.
 *
 * @author charliecutting
 *
 */
public class ChessGame implements Game {

  private Player p1;
  private Player p2;
  private Board board;
  private int turn = 0;

  /**
   * Public constructor.
   *
   * @param p1
   *          the first player
   * @param p2
   *          the second player
   * @throws PositionException
   *           if something is wrong with the Board constructor
   */
  public ChessGame(Player p1, Player p2) {
    this.p1 = p1;
    this.p2 = p2;
    try {
      this.board = new Board(p1, p2);
    } catch (PositionException e) {
      // No
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    p2.setBoard(board);
    p1.setBoard(board);
    p1.setColor(0);
    p2.setColor(1);
  }

  public ChessGame(Player p1, Player p2, Board board) throws PositionException {
    this.p1 = p1;
    this.p2 = p2;
    this.board = board;
    p2.setBoard(board);
    p1.setBoard(board);
    p1.setColor(0);
    p2.setColor(1);
  }

  /**
   * Plays a chess game.
   */
  public void play() {
    while (true) {
      board.print();
      int gameOver = board.gameOver(turn);
      if (gameOver == 1) {
        String t;
        if (turn == 0) {
          t = "Black";
        } else {
          t = "White";
        }
        for (Position p : board.threatened(turn)) {
          System.out.println(p.col() + "," + p.row());
        }
        System.out.println("Game over, " + t + " wins!");
        for (Session session : ChessWebSocket.games.keySet()) {
          if (ChessWebSocket.games.get(session) == this) {
            JsonObject message = new JsonObject();
            message.addProperty("type",
                ChessWebSocket.MESSAGE_TYPE.GAMEOVER.ordinal());
            JsonObject payload = new JsonObject();
            payload.addProperty("winner", t);
            message.add("payload", payload);
            try {
              session.getRemote()
                  .sendString(ChessWebSocket.GSON.toJson(message));
            } catch (IOException e) {
              // TODO Auto-generated catch block
              e.printStackTrace();
            }
          }
        }
        break;
      }
      if (gameOver == 2) {
        System.out.println("Game over, it's a draw!");
        for (Session session : ChessWebSocket.games.keySet()) {
          if (ChessWebSocket.games.get(session) == this) {
            JsonObject message = new JsonObject();
            message.addProperty("type",
                ChessWebSocket.MESSAGE_TYPE.GAMEOVER.ordinal());
            JsonObject payload = new JsonObject();
            payload.addProperty("winner", "draw");
            message.add("payload", payload);
            try {
              session.getRemote()
                  .sendString(ChessWebSocket.GSON.toJson(message));
            } catch (IOException e) {
              // TODO Auto-generated catch block
              e.printStackTrace();
            }
          }
        }
        break;
      }
      Move m;
      if (turn == 0) {
        m = p1.move();
      } else {
        m = p2.move();
      }
      try {
        board.processMove(m.start(), m.end(), false);
        turn = Math.abs(turn - 1);
        System.out.println("Moved from " + m.start().col() + ","
            + m.start().row() + " to " + m.end().col() + "," + m.end().row());
        for (Session session : ChessWebSocket.games.keySet()) {
          if (ChessWebSocket.games.get(session) == this) {
            if (ChessWebSocket.playerNum
                .get(ChessWebSocket.playerSession.get(session)) == turn) {
              JsonObject message = new JsonObject();
              message.addProperty("type",
                  ChessWebSocket.MESSAGE_TYPE.UPDATE.ordinal());
              JsonObject payload = new JsonObject();
              payload.addProperty("moveFrom", m.start().numString());
              payload.addProperty("moveTo", m.end().numString());
              message.add("payload", payload);
              session.getRemote()
                  .sendString(ChessWebSocket.GSON.toJson(message));
            }
          }
        }
      } catch (InvalidMoveException e) {
        System.out.println("That's not a valid move!");
      } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      System.out.println("here!");

      // Extra gameover check to deal with the 1 turn delay for stalemate
      // gameOver = board.gameOver(turn);
      // if (gameOver == 2) {
      // System.out.println("Game over, it's a draw!");
      // break;
      // }
    }
  }

  @Override
  public Set<Position> moves(int player, Position pos) {
    System.out.println(player + " " + pos.numString());
    return board.getValidMoves(player).get(pos);
  }
}
