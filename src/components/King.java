package components;

import java.util.Set;

import positions.Position;

/**
 * Class that represents a king.
 *
 * @author charliecutting
 *
 */
public class King implements Piece {

  private Position position;
  private int color;
  private Board board;

  /**
   * Public constructor to be called at board construction.
   *
   * @param start
   *          the starting position for this King
   * @param color
   *          the color of this King (0 = white 1 = black)
   * @param board
   *          the board this King is on (for calculating moves)
   *
   * @param position
   * @param color
   * @param board
   */
  public King(Position start, int color, Board board) {
    this.board = board;
    this.color = color;
    this.position = start;
  }

  @Override
  public Position position() {
    return position;
  }

  @Override
  public Set<Position> getValidMoves() {
    // TODO: Auto-generated method stub
    return null;
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
    // TODO: Auto-generated method stub
  }

  @Override
  public int color() {
    return color;
  }

  @Override
  public Set<Position> threatens() {
    // TODO: Auto-generated method stub
    return null;
  }

}
