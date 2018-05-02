package edu.brown.cs.group.games;

import java.util.Set;

import edu.brown.cs.group.components.Board;
import edu.brown.cs.group.components.InvalidMoveException;
import edu.brown.cs.group.components.Piece;
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
      board1 = new Board(p0, p2);
      board2 = new Board(p3, p1);
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
    Player[] t2 = { p2, p3 };
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

    /**
     * Public constructor.
     */
    SubGame(int b) {
      turn = 0;
      this.b = b;
    }

    @Override
    public void run() {
      while (!gameOver) {
        if (boards[b].checkmate(turn)) {
          endGame();
          System.out.println("Game over!");
          break;
        }
        Move m = teams[turn][b].move();
        try {
          Piece p;
          if (m.start() instanceof BankPosition) {
            p = boards[b].processPlace(m.start(), m.end(), m.getPiece());
          } else {
            p = boards[b].processMove(m.start(), m.end(), false);
          }
          turn = Math.abs(turn - 1);
          if (p != null) {
            teams[turn][Math.abs(b - 1)].acceptPiece(p);
          }
          System.out.println("Moved from " + m.start().col() + ","
              + m.start().row() + " to " + m.end().col() + "," + m.end().row());
        } catch (InvalidMoveException e) {
          System.out.println("That's not a valid move!");
        }
        turn = Math.abs(turn - 1);
      }

    }
  }

  private synchronized void endGame() {
    gameOver = true;
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
      return boards[0].getValidMoves(player / 2).get(pos);
    } else {
      return boards[1].getValidMoves(player % 3).get(pos);
    }
  }

}
