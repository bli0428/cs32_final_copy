package components;

import java.util.HashSet;
import java.util.Set;

import positions.Position;
import positions.PositionException;

/**
 * Class that represents a Bishop.
 *
 * @author charliecutting
 *
 */
public class Bishop implements Piece {

  private Position position;
  private int color;

  /**
   * Public constructor to be called at board construction.
   *
   * @param start
   *          the starting position for this Bishop
   * @param color
   *          the color of this Bishop (0 = white 1 = black)
   * @param position
   * @param color
   */
  public Bishop(Position start, int color) {
    this.color = color;
    this.position = start;
  }

  @Override
  public Piece copyOf() {
    return new Bishop(position, color);
  }

  @Override
  public Position position() {
    return position;
  }

  @Override
  public Set<Position> getValidMoves(Board board) {
    Set<Position> out = new HashSet<Position>();

    for (int i = position.col() + 1, j = position.row()
        + 1; i <= Position.BOARD_SIZE && j <= Position.BOARD_SIZE; i++, j++) {
      try {
        Position m = new Position(i, j);
        if (!board.places().containsKey(m)) {
          out.add(m);
        } else {
          if (board.places().get(m).color() != color) {
            out.add(m);
          }
          break;
        }
      } catch (PositionException pe) {
        break;
      }
    }

    for (int i = position.col() - 1, j = position.row() + 1; i > 0
        && j <= Position.BOARD_SIZE; i--, j++) {
      try {
        Position m = new Position(i, j);
        if (!board.places().containsKey(m)) {
          out.add(m);
        } else {
          if (board.places().get(m).color() != color) {
            out.add(m);
          }
          break;
        }
      } catch (PositionException pe) {
        break;
      }
    }

    for (int i = position.col() + 1, j = position.row()
        - 1; i <= Position.BOARD_SIZE && j > 0; i++, j--) {
      try {
        Position m = new Position(i, j);
        if (!board.places().containsKey(m)) {
          out.add(m);
        } else {
          if (board.places().get(m).color() != color) {
            out.add(m);
          }
          break;
        }
      } catch (PositionException pe) {
        break;
      }
    }

    for (int i = position.col() - 1, j = position.row() - 1; i > 0
        && j > 0; i--, j--) {
      try {
        Position m = new Position(i, j);
        if (!board.places().containsKey(m)) {
          out.add(m);
        } else {
          if (board.places().get(m).color() != color) {
            out.add(m);
          }
          break;
        }
      } catch (PositionException pe) {
        break;
      }
    }

    return out;
  }

  @Override
  public String type() {
    return "b";
  }

  @Override
  public int value() {
    return 3;
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

    for (int i = position.col() + 1, j = position.row()
        + 1; i <= Position.BOARD_SIZE && j <= Position.BOARD_SIZE; i++, j++) {
      try {
        Position m = new Position(i, j);
        if (!board.places().containsKey(m)) {
          out.add(m);
        } else {
          out.add(m);
          break;
        }
      } catch (PositionException pe) {
        break;
      }
    }

    for (int i = position.col() + 1, j = position.row() + 1; i > 0
        && j <= Position.BOARD_SIZE; i--, j++) {
      try {
        Position m = new Position(i, j);
        if (!board.places().containsKey(m)) {
          out.add(m);
        } else {
          out.add(m);
          break;
        }
      } catch (PositionException pe) {
        break;
      }
    }

    for (int i = position.col() + 1, j = position.row()
        + 1; i <= Position.BOARD_SIZE && j > 0; i++, j--) {
      try {
        Position m = new Position(i, j);
        if (!board.places().containsKey(m)) {
          out.add(m);
        } else {
          out.add(m);
          break;
        }
      } catch (PositionException pe) {
        break;
      }
    }

    for (int i = position.col() + 1, j = position.row() + 1; i > 0
        && j > 0; i--, j--) {
      try {
        Position m = new Position(i, j);
        if (!board.places().containsKey(m)) {
          out.add(m);
        } else {
          out.add(m);
          break;
        }
      } catch (PositionException pe) {
        break;
      }
    }

    return out;
  }

}
