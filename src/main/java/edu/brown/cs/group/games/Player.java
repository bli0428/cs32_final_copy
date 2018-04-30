package main.java.edu.brown.cs.group.games;

import java.util.Set;

import main.java.edu.brown.cs.group.components.Board;
import main.java.edu.brown.cs.group.components.Piece;
import main.java.edu.brown.cs.group.positions.Position;

/**
 * Interface that represents a Player of chess or bughouse.
 *
 * @author charliecutting
 *
 */
public interface Player {

  /**
   * Return a set containing all pieces in this player's bank.
   *
   * @return a set of pieces in this player's bank
   */
  Set<Piece> bank();

  /**
   * Return a Move, determined by the implementation.
   *
   * @return a Move object that stores the start and end positions of this
   *         player's move
   */
  Move move();

  /**
   * Sets this player's Board to board.
   *
   * @param board
   *          the board this player is on
   */
  void setBoard(Board board);

  /**
   * Gets a promotion preference from the player.
   *
   * @return a piece to promote a pawn to
   *
   * @param p
   *          the position where the promotion occurs
   */
  Piece promote(Position p);

  /**
   * Adds a piece to the bank.
   *
   * @param p
   *          the piece to add
   */
  void acceptPiece(Piece p);

  /**
   * Setter for color.
   *
   * @param color
   *          the color to set this player to.
   */
  void setColor(int color);
}
