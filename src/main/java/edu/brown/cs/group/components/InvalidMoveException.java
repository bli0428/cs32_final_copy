package main.java.edu.brown.cs.group.components;

import main.java.edu.brown.cs.group.positions.Position;

/**
 * Exception thrown when something happens such that a piece is attempted to be
 * moved improperly.
 *
 * @author charliecutting
 *
 */
public class InvalidMoveException extends Exception {

  private Position dest;

  /**
   * Public constructor.
   *
   * @param dest
   *          the destination of the invalid move
   */
  public InvalidMoveException(Position dest) {
    this.dest = dest;
  }

  /**
   * Getter for the destination.
   *
   * @return dest
   */
  public Position dest() {
    return dest;
  }
}
