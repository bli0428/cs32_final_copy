package edu.brown.cs.group.components;

import java.util.HashSet;
import java.util.Set;

import edu.brown.cs.group.positions.Position;
import edu.brown.cs.group.positions.PositionException;

/**
 * Class that represents a king.
 *
 * @author charliecutting
 *
 */
public class King implements Piece {

  private Position position;
  private int color;
  private boolean hasMoved;

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
  public Piece copyOf() {
    return new King(position, color);
  }

  @Override
  public Position position() {
    return position;
  }
  
  private boolean validCastle(Position rook, Board board) throws PositionException{
    Set<Position> threats = board.threatened(Math.abs(color - 1));
    int start;
    int end;
    if (rook.col() == 1) {
      start = 3;
      end = 4;
    } else {
      //rook.col() == 8
      start = 6;
      end = 7;
    }

    if (!board.places().containsKey(rook) || 
        !board.places().get(rook).type().equals("r") ||
        ((Rook)board.places().get(rook)).moveStatus()) {
      return false;
    }
    for (int i = start; i <= end; i++) {
      Position pos = new Position(i, position.row());
      if (threats.contains(pos)
          || board.places().containsKey(pos)) {
        return false;
      }
    }
    return true;
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

    // Check castling
    if (!hasMoved && !threats.contains(position)) {
      try {
        Position rRook = new Position(8, position.row());
        Position lRook = new Position(1, position.row());
        if (validCastle(rRook, board)) {
            out.add(new Position(7, position.row()));
        }
        if (validCastle(lRook, board)) {
            out.add(new Position(3, position.row()));
        }
      } catch (PositionException pe) {
        
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
    hasMoved = true;
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
  
  @Override
  public int hashCode() {
    return type().hashCode();
  }
  
  @Override
  public boolean equals(Object o) {
    if (o instanceof King) {
      return ((King) o).type().equals(type());
    }
    if (o instanceof PromotedPawn) {
      return ((PromotedPawn) o).innerType().equals(type());
    }
    return false;
  }

}
