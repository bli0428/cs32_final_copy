package edu.brown.cs.group.components;

import java.util.HashSet;
import java.util.Set;

import edu.brown.cs.group.positions.Position;
import edu.brown.cs.group.positions.PositionException;

/**
 * Class that represents a Rook.
 *
 * @author charliecutting
 *
 */
public class Rook implements Piece {

  private Position position;
  private int color;
  private boolean hasMoved;

  /**
   * Public constructor to be called at board construction.
   *
   * @param start
   *          the starting position for this Rook
   * @param color
   *          the color of this Rook (0 = white 1 = black)
   */
  public Rook(Position start, int color) {
    this.color = color;
    this.position = start;
  }

  @Override
  public Piece copyOf() {
    return new Rook(position, color);
  }

  @Override
  public Position position() {
    return position;
  }

  @Override
  public Set<Position> getValidMoves(Board board) {
    Set<Position> out = new HashSet<Position>();
    for (int i = position.col() + 1; i <= Position.BOARD_SIZE; i++) {
      try {
        Position m = new Position(i, position.row());
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

    for (int i = position.row() + 1; i <= Position.BOARD_SIZE; i++) {
      try {
        Position m = new Position(position.col(), i);
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

    for (int i = position.col() - 1; i > 0; i--) {
      try {
        Position m = new Position(i, position.row());
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

    for (int i = position.row() - 1; i > 0; i--) {
      try {
        Position m = new Position(position.col(), i);
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
    return "r";
  }

  @Override
  public int value() {
    return 5;
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
    for (int i = position.col() + 1; i <= Position.BOARD_SIZE; i++) {
      try {
        Position m = new Position(i, position.row());
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

    for (int i = position.row() + 1; i <= Position.BOARD_SIZE; i++) {
      try {
        Position m = new Position(position.col(), i);
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

    for (int i = position.col() - 1; i > 0; i--) {
      try {
        Position m = new Position(i, position.row());
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

    for (int i = position.row() - 1; i > 0; i--) {
      try {
        Position m = new Position(position.col(), i);
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

}
