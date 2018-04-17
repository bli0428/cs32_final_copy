package components;

import java.util.Set;

import positions.Position;

/**
 * Class that represents a knight.
 *
 * @author charliecutting
 *
 */
public class Knight implements Piece {

  private Position position;
  private int color;
  private Board board;

  /**
   * Public constructor to be called at board construction.
   *
   * @param start
   *          the starting position for this Knight
   * @param color
   *          the color of this Knight (0 = white 1 = black)
   * @param board
   *          the board this Knight is on (for calculating moves)
   *
   * @param position
   * @param color
   * @param board
   */
  public Knight(Position start, int color, Board board) {
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
    return "k";
  }

  @Override
  public int value() {
    return 3;
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
