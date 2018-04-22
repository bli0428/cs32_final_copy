package games;

import java.util.Set;

import components.Piece;

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
  public Move move();

}
