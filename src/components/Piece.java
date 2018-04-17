package components;

import java.util.Set;

import positions.Position;

/**
 * Interface that represents a chess piece.
 *
 * @author charliecutting
 *
 */
public interface Piece {

  /**
   * Getter for this position.
   *
   * @return a Position representing this piece's place on the board
   */
  Position position();

  /**
   * Calculates all valid moves for this piece.
   *
   * @return a set containing the end positions for all of this piece's valid
   *         moves.
   */
  Set<Position> getValidMoves();

  /**
   * Returns a String corresponding to this piece's type (K = King, q = Queen, r
   * = Rook, k = Knight, b = Bishop, p = Pawn).
   *
   * @return a String matching the above key
   */
  String type();

  /**
   * Returns an int representing this piece's value (Pawn = 1, Bishop = 3,
   * Knight = 3, Rook = 5, Queen = 9, King = 0).
   *
   * @return an int matching the above key
   */
  int value();

  /**
   * Move this piece from its current position to the destination, if possible.
   *
   * @param dest
   *          the destination position for the move
   */
  void move(Position dest);

  /**
   * Getter for color (0 = white, 1 = black).
   *
   * @return an int matching the above key
   */
  int color();

  /**
   * Calculates all positions threatened by this piece.
   *
   * @return a Set<Position> with all the threatened squares
   */
  Set<Position> threatens();
}
