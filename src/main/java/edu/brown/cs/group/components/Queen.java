package edu.brown.cs.group.components;

import java.util.HashSet;
import java.util.Set;

import edu.brown.cs.group.positions.Position;
import edu.brown.cs.group.positions.PositionException;

/**
 * Class that represents a Queen.
 *
 * @author charliecutting
 *
 */
public class Queen implements Piece {

  private static final int VALUE = 900;

  private Position position;
  private int color;
  

  /**
   * Public constructor to be called at board construction.
   *
   * @param start
   *          the starting position for this Queen
   * @param color
   *          the color of this Queen (0 = white 1 = black)
   */
  public Queen(Position start, int color) {
    this.color = color;
    this.position = start;
  }

  @Override
  public Piece copyOf() {
    return new Queen(position, color);
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
    return "q";
  }

  @Override
  public int value() {
    return VALUE;
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
          out.add(m);
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
          out.add(m);
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
          out.add(m);
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
          out.add(m);
          break;
        }
      } catch (PositionException pe) {
        break;
      }
    }

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

    for (int i = position.col() - 1, j = position.row() + 1; i > 0
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
        - 1; i <= Position.BOARD_SIZE && j > 0; i++, j--) {
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

    for (int i = position.col() - 1, j = position.row() - 1; i > 0
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
  
  @Override
  public int hashCode() {
    return type().hashCode();
  }
  
  @Override
  public boolean equals(Object o) {
    if (o instanceof Queen) {
      return ((Queen) o).type().equals(type());
    }
    if (o instanceof PromotedPawn) {
      return ((PromotedPawn) o).innerType().equals(type());
    }
    return false;
  }

}
