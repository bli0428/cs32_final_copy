package edu.brown.cs.group.components;

import java.util.Set;

import edu.brown.cs.group.positions.Position;

/**
 * Class that represents a PromotedPawn. Its type is "pp". Acts as a proxy for
 * another piece.
 *
 * @author Brian
 *
 */
public class PromotedPawn implements Piece {

  private Piece promotedPiece;

  /**
   * Constructor that creates a new PromotedPawn with the given promotedPiece.
   *
   * @param promotedPiece
   *          The Piece that this acts as a proxy for.
   */
  public PromotedPawn(Piece promotedPiece) {
    assert !(promotedPiece instanceof King);
    this.promotedPiece = promotedPiece;
  }

  @Override
  public Position position() {
    return promotedPiece.position();
  }

  @Override
  public Piece copyOf() {
    return new PromotedPawn(promotedPiece.copyOf());
  }

  @Override
  public Set<Position> getValidMoves(Board board) {
    return promotedPiece.getValidMoves(board);
  }

  @Override
  public String type() {
    return "pp";
    // return promotedPiece.type() + " p";
  }

  public String innerType() {
    return promotedPiece.type();
  }

  @Override
  public int value() {
    return promotedPiece.value();
  }

  @Override
  public void move(Position dest) {
    promotedPiece.move(dest);
  }

  @Override
  public int color() {
    return promotedPiece.color();
  }

  @Override
  public Set<Position> threatens(Board board) {
    return promotedPiece.threatens(board);
  }

  @Override
  public int hashCode() {
    return promotedPiece.type().hashCode();
  }

  @Override
  public boolean equals(Object o) {
    if (o instanceof PromotedPawn) {
      return ((PromotedPawn) o).innerType().equals(innerType());
    }
    if (o instanceof Piece) {
      return ((Piece) o).type().equals(innerType());
    }
    return false;
  }

}
