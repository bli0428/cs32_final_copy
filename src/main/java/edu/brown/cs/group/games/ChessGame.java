package edu.brown.cs.group.games;

import edu.brown.cs.group.components.Board;
import edu.brown.cs.group.components.InvalidMoveException;
import edu.brown.cs.group.positions.Position;
import edu.brown.cs.group.positions.PositionException;

/**
 * Class for running a chess game. Handles turn order, checkmate, etc.
 *
 * @author charliecutting
 *
 */
public class ChessGame {

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
  public ChessGame(Player p1, Player p2) throws PositionException {
    this.p1 = p1;
    this.p2 = p2;
    this.board = new Board(p1, p2);
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
      if (board.checkmate(turn)) {
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
        break;
      }
      if (board.stalemate(turn)) {
        System.out.println("Game over, it's a draw!");
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
      } catch (InvalidMoveException e) {
        System.out.println("That's not a valid move!");
      }
    }
  }
}
