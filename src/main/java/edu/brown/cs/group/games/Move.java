package edu.brown.cs.group.games;

import edu.brown.cs.group.components.Piece;
import edu.brown.cs.group.positions.BankPosition;
import edu.brown.cs.group.positions.Position;

/**
 * Class that holds both positions of a Move.
 *
 * @author charliecutting
 *
 */
public class Move {
  private Position start;
  private Position end;
  private double value;
  private Piece piece;

  /**
   * Public constructor.
   *
   * @param start
   *          the start position
   * @param end
   *          the end position
   */
  public Move(Position start, Position end) {
    this.start = start;
    this.end = end;
  }

  public Move(Position end, Piece piece) {
    this.start = new BankPosition();
    this.end = end;
    this.piece = piece;
  }

  public Move(Position start, Position end, double value) {
    this.start = start;
    this.end = end;
    this.value = value;
  }

  public Move(double value) {
    this.start = null;
    this.end = null;
    this.value = value;
  }

  public double value() {
    return value;
  }

  public void setValue(double value) {
    this.value = value;
  }

  @Override
  public String toString() {
    return String.format("start: %s, end: %s", start.numString(),
        end.numString());
  }

  /**
   * Getter for start.
   *
   * @return start
   */
  public Position start() {
    return start;
  }

  /**
   * Getter for end.
   *
   * @return end
   */
  public Position end() {
    return end;
  }

}
