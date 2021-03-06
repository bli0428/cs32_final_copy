package edu.brown.cs.group.components;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import edu.brown.cs.group.positions.Position;
import edu.brown.cs.group.positions.PositionException;

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

  @Override
  public Piece copyOf() {
    return new Pawn(pos, color, hasMoved);
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
    // TODO: promotion

    Map<Position, Piece> m = board.places();

    Set<Position> out = new HashSet<Position>();

    // For pawns (and only pawns), white and black move differently (one goes
    // forward in the + direction, one goes in the - direction), so we have to
    // check the color
    if (color == 0) {
      // Check for en passant
      try {
        Position p = new Position(pos.col() + 1, pos.row());
        if (board.getPassant() != null && board.getPassant().color() == 1 && 
            board.getPassant().position().row() == p.row() &&
            board.getPassant().position().col() == p.col()) {
          out.add(new Position(pos.col() + 1, pos.row() + 1));
        }
      } catch (PositionException pe) {
        // Do nothing 
      }
      
      try {
        Position p = new Position(pos.col() - 1, pos.row());
        if (board.getPassant() != null && board.getPassant().color() == 1 && 
            board.getPassant().position().row() == p.row() &&
            board.getPassant().position().col() == p.col()) {
          out.add(new Position(pos.col() - 1, pos.row() + 1));
        }
      } catch (PositionException pe) {
        // Do nothing
      }
      
      // Check one threatened side
      try {
        Position p = new Position(pos.col() + 1, pos.row() + 1);
        if (m.containsKey(p) && m.get(p).color() == 1) {
          out.add(p);
        }
      } catch (PositionException pe) {
        // System.out.println("Here");
      } // On PositionException, the new Position is off the board and should
        // not be added

      // Check the other threatened side
      try {
        Position p = new Position(pos.col() - 1, pos.row() + 1);
        if (m.containsKey(p) && m.get(p).color() == 1) {
          out.add(p);
        }
      } catch (PositionException pe) {
        // System.out.println("Here");
      }

      // Check the forward move
      try {
        Position p = new Position(pos.col(), pos.row() + 1);
        if (!m.containsKey(p)) {
          out.add(p);
          if (!hasMoved) {
            try {
              Position p2 = new Position(pos.col(), pos.row() + 2);
              if (!m.containsKey(p2)) {
                out.add(p2);
              }
            } catch (PositionException e) {
            }
          }
        }
      } catch (PositionException e) {
        // System.out.println("Here");
      }
    } else {
      
      // Check for en passant
      try {
        Position p = new Position(pos.col() + 1, pos.row());
        if (board.getPassant() != null && board.getPassant().color() == 0 && 
            board.getPassant().position().row() == p.row() &&
            board.getPassant().position().col() == p.col()) {
          out.add(new Position(pos.col() + 1, pos.row() - 1));
        }
      } catch (PositionException pe) {
        // Do nothing 
      }
      
      try {
        Position p = new Position(pos.col() - 1, pos.row());
        if (board.getPassant() != null && board.getPassant().color() == 0 && 
            board.getPassant().position().row() == p.row() &&
            board.getPassant().position().col() == p.col()) {
          out.add(new Position(pos.col() - 1, pos.row() - 1));
        }
      } catch (PositionException pe) {
        // Do nothing
      }

      // Check one threatened side
      try {
        Position p = new Position(pos.col() - 1, pos.row() - 1);
        if (m.containsKey(p) && m.get(p).color() == 0) {
          out.add(p);
        }
      } catch (PositionException pe) {
        // System.out.println("Here");
      }

      // Check the other threatened side
      try {
        Position p = new Position(pos.col() + 1, pos.row() - 1);
        if (m.containsKey(p) && m.get(p).color() == 0) {
          out.add(p);
        }
      } catch (PositionException pe) {
        // System.out.println("Here");
      }

      // Check the forward move
      try {
        Position p = new Position(pos.col(), pos.row() - 1);
        if (!m.containsKey(p)) {
          out.add(p);
          if (!hasMoved) {
            try {
              Position p2 = new Position(pos.col(), pos.row() - 2);
              if (!m.containsKey(p2)) {
                out.add(p2);
              }
            } catch (PositionException e) {
              // System.out.println("Here");
            }
          }
        }
      } catch (PositionException e) {
        // System.out.println("Here");
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
    return 100;
  }

  @Override
  public void move(Position dest) {
    pos = dest;
    hasMoved = true;
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
        Position p = new Position(pos.col() + 1, pos.row() + 1);
        out.add(p);
      } catch (PositionException pe) {

      }

      // Check the other threatened side
      try {
        Position p = new Position(pos.col() - 1, pos.row() + 1);
        out.add(p);
      } catch (PositionException pe) {

      }
    } else {
      // Check one threatened side
      try {
        Position p = new Position(pos.col() - 1, pos.row() - 1);
        out.add(p);
      } catch (PositionException pe) {

      }

      // Check the other threatened side
      try {
        Position p = new Position(pos.col() + 1, pos.row() - 1);
        out.add(p);
      } catch (PositionException pe) {

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
    if (o instanceof Pawn) {
      return ((Pawn) o).type().equals(type());
    }
    if (o instanceof PromotedPawn) {
      return ((PromotedPawn) o).innerType().equals(type());
    }
    return false;
  }

}
