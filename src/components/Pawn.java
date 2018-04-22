package components;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import positions.Position;
import positions.PositionException;

/**
 * Class that represents a pawn.
 *
 * @author charliecutting
 *
 */
public class Pawn implements Piece {

  private Position pos;
  private int color;
  private boolean hasMoved;

  /**
   * Public constructor to be called at board construction.
   *
   * @param start
   *          the starting position for this Pawn
   * @param color
   *          the color of this pawn (0 = white 1 = black)
   */
  public Pawn(Position start, int color) {
    this.pos = start;
    this.color = color;
    this.hasMoved = false;
  }

  /**
   * Public constructor to be called at placement from a bank.
   *
   * @param start
   *          the starting position for this Pawn
   * @param color
   *          the color of this pawn (0 = white 1 = black)
   * @param hasMoved
   *          can this pawn move forward twice?
   */
  public Pawn(Position start, int color, Boolean hasMoved) {
    this.pos = start;
    this.color = color;
    this.hasMoved = hasMoved;
  }

  @Override
  public Position position() {
    return pos;
  }

  @Override
  public Set<Position> getValidMoves(Board board) {
    // TODO: En-pessant, promotion

    Map<Position, Piece> m = board.places();

    Set<Position> out = new HashSet<Position>();

    // For pawns (and only pawns), white and black move differently (one goes
    // forward in the + direction, one goes in the - direction), so we have to
    // check the color
    if (color == 0) {

      // Check one threatened side
      try {
        Position p = new Position(pos.row() + 1, pos.col() + 1);
        if (m.get(p).color() == 1) {
          out.add(p);
        }
      } catch (PositionException pe) {
      } // On PositionException, the new Position is off the board and should
        // not be added

      // Check the other threatened side
      try {
        Position p = new Position(pos.row() + 1, pos.col() - 1);
        if (m.get(p).color() == 1) {
          out.add(p);
        }
      } catch (PositionException pe) {
      }

      // Check the forward move
      try {
        Position p = new Position(pos.row() + 1, pos.col());
        if (!m.containsKey(p)) {
          out.add(p);
          if (!hasMoved) {
            try {
              Position p2 = new Position(pos.row() + 2, pos.col());
              if (!m.containsKey(p2)) {
                out.add(p2);
              }
            } catch (PositionException e) {
            }
          }
        }
      } catch (PositionException e) {
      }
    } else {

      // Check one threatened side
      try {
        Position p = new Position(pos.row() - 1, pos.col() + 1);
        if (m.get(p).color() == 0) {
          out.add(p);
        }
      } catch (PositionException pe) {
      }

      // Check the other threatened side
      try {
        Position p = new Position(pos.row() - 1, pos.col() - 1);
        if (m.get(p).color() == 0) {
          out.add(p);
        }
      } catch (PositionException pe) {
      }

      // Check the forward move
      try {
        Position p = new Position(pos.row() - 1, pos.col());
        if (!m.containsKey(p)) {
          out.add(p);
          if (!hasMoved) {
            try {
              Position p2 = new Position(pos.row() - 2, pos.col());
              if (!m.containsKey(p2)) {
                out.add(p2);
              }
            } catch (PositionException e) {
            }
          }
        }
      } catch (PositionException e) {
      }

    }

    for (Position move : out) {

    }

    return out;
  }

  @Override
  public String type() {
    return "p";
  }

  @Override
  public int value() {
    return 1;
  }

  @Override
  public void move(Position dest) {
    pos = dest;
  }

  @Override
  public int color() {
    return color;
  }

  @Override
  public Set<Position> threatens(Board board) {
    Set<Position> out = new HashSet<Position>();

    if (color == 0) {
      // Check one threatened side
      try {
        Position p = new Position(pos.row() + 1, pos.col() + 1);
        out.add(p);
      } catch (PositionException pe) {

      }

      // Check the other threatened side
      try {
        Position p = new Position(pos.row() + 1, pos.col() - 1);
        out.add(p);
      } catch (PositionException pe) {

      }
    } else {
      // Check one threatened side
      try {
        Position p = new Position(pos.row() - 1, pos.col() + 1);
        out.add(p);
      } catch (PositionException pe) {

      }

      // Check the other threatened side
      try {
        Position p = new Position(pos.row() - 1, pos.col() - 1);
        out.add(p);
      } catch (PositionException pe) {

      }
    }

    return out;
  }

}
