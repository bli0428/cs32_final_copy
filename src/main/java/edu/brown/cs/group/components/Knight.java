package edu.brown.cs.group.components;

import java.util.HashSet;
import java.util.Set;

import edu.brown.cs.group.positions.Position;
import edu.brown.cs.group.positions.PositionException;

/**
 * Class that represents a knight.
 *
 * @author charliecutting
 *
 */
public class Knight implements Piece {

  private Position position;
  private int color;

  /**
   * Public constructor to be called at board construction.
   *
   * @param start
   *          the starting position for this Knight
   * @param color
   *          the color of this Knight (0 = white 1 = black)
   * @param position
   * @param color
   * @param board
   */
  public Knight(Position start, int color) {
    this.color = color;
    this.position = start;
  }

  @Override
  public Piece copyOf() {
    return new Knight(position, color);
  }

  @Override
  public Position position() {
    return position;
  }

  @Override
  public Set<Position> getValidMoves(Board board) {
    Set<Position> out = new HashSet<Position>();
    for (int i = position.col() - 2; i <= position.col() + 2; i++) {
      for (int j = position.row() - 2; j <= position.row() + 2; j++) {
        try {
          if (Math.abs(position.col() - i) == 2
              && Math.abs(position.row() - j) == 1) {
            Position m = new Position(i, j);
            if (!board.places().containsKey(m)
                || board.places().get(m).color() != color) {
              out.add(m);
            }
          }
          if (Math.abs(position.col() - i) == 1
              && Math.abs(position.row() - j) == 2) {
            Position m = new Position(i, j);
            if (!board.places().containsKey(m)
                || board.places().get(m).color() != color) {
              out.add(m);
            }
          }
        } catch (PositionException pe) {
          // System.out.println("Here");
        }
      }
    }
    return out;
  }

  @Override
  public String type() {
    return "k";
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
    for (int i = position.col() - 2; i <= position.col() + 2; i++) {
      for (int j = position.row() - 2; j <= position.row() + 2; j++) {
        try {
          if (Math.abs(position.col() - i) == 2
              && Math.abs(position.row() - j) == 1) {
            Position m = new Position(i, j);
            out.add(m);
          }
          if (Math.abs(position.col() - i) == 1
              && Math.abs(position.row() - j) == 2) {
            Position m = new Position(i, j);
            out.add(m);
          }
        } catch (PositionException pe) {

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
    if (o instanceof Knight) {
      return ((Knight) o).type().equals(type());
    }
    if (o instanceof PromotedPawn) {
      return ((PromotedPawn) o).innerType().equals(type());
    }
    return false;
  }

}
