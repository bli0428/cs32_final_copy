package games;

import positions.Position;

/**
 * Class that holds both positions of a Move.
 *
 * @author charliecutting
 *
 */
public class Move {
  private Position start;
  private Position end;

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
