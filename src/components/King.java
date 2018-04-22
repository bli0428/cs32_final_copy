package components;

import java.util.HashSet;
import java.util.Set;

import positions.Position;
import positions.PositionException;

/**
 * Class that represents a king.
 *
 * @author charliecutting
 *
 */
public class King implements Piece {

  private Position position;
  private int color;

  /**
   * Public constructor to be called at board construction.
   *
   * @param start
   *          the starting position for this King
   * @param color
   *          the color of this King (0 = white 1 = black)
   */
  public King(Position start, int color) {
    this.color = color;
    this.position = start;
  }

  @Override
  public Position position() {
    return position;
  }

  @Override
  public Set<Position> getValidMoves(Board board) {
    // TODO: Castling
    Set<Position> out = new HashSet<Position>();
    Set<Position> threats = board.threatened(Math.abs(color - 1));
    for (int i = position.col() - 1; i <= position.col() + 1; i++) {
      for (int j = position.row() - 1; j <= position.row() + 1; j++) {
        // Can't move to the current space
        if (!(i == position.col() && j == position.row())) {
          try {
            Position m = new Position(i, j);
            // Can't move to a threatened space or a space occupied by one of
            // our own pieces
            if (!threats.contains(m)) {
              if (!board.places().containsKey(m)
                  || board.places().get(m).color() != color) {
                out.add(m);
              }
            }
          } catch (PositionException pe) {

          }
        }
      }
    }
    return out;
  }

  @Override
  public String type() {
    return "K";
  }

  @Override
  public int value() {
    return 0;
  }

  @Override
  public void move(Position dest) {
    position = dest;
  }

  @Override
  public int color() {
    return color;
  }

  @Override
  public Set<Position> threatens(Board board) {
    Set<Position> out = new HashSet<Position>();
    for (int i = position.col() - 1; i <= position.col() + 1; i++) {
      for (int j = position.row() - 1; j <= position.row() + 1; j++) {
        // Can't move to the current space
        if (!(i == position.col() && j == position.row())) {
          try {
            Position m = new Position(i, j);
            // Can't move to a threatened space or a space occupied by one of
            // our own pieces
            out.add(m);
          } catch (PositionException pe) {

          }
        }
      }
    }
    return out;
  }

}
