package games;

import components.Board;
import components.InvalidMoveException;
import positions.PositionException;

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
    this.board = new Board();
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
      if (board.checkmate(turn)) {
        String t;
        if (turn == 0) {
          t = "Black";
        } else {
          t = "White";
        }
        System.out.println("Game over, " + t + " wins!");
        break;
      }
      if (board.stalemate(turn)) {
        System.out.println("Game over, it's a draw!");
      }
      Move m;
      if (turn == 0) {
        m = p1.move();
      } else {
        m = p2.move();
      }
      System.out.println("Here");
      try {
        board.processMove(m.start(), m.end());
        turn = Math.abs(turn - 1);
        System.out.println("Moved from " + m.start().col() + ","
            + m.start().row() + " to " + m.end().col() + "," + m.end().row());
      } catch (InvalidMoveException e) {
        System.out.println("That's not a valid move!");
      }
    }
  }
}
