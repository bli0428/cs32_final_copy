package positions;

/**
 * Exception to throw when something tries to access a position off the board.
 *
 * @author charliecutting
 *
 */
public class PositionException extends Exception {

  private final Position p;

  /**
   * Constructor that takes the the invalid coordinates that were accessed.
   *
   * @param p
   *          the invalid Position that something tried to access.
   */
  public PositionException(Position p) {
    this.p = p;
  }

  /**
   * Getter for the position of the exception.
   *
   * @return p
   */
  public Position position() {
    return p;
  }
}
