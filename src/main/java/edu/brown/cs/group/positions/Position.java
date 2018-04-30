package main.java.edu.brown.cs.group.positions;

import java.util.Objects;

/**
 * Class that stores a position on the chessboard.
 *
 * @author charliecutting
 *
 */
public class Position {

  private final int col; // int representing the the column (a-h) of the
                         // position with a=1, b=2... e=8

  private final int row; // int representing the row (1-8) of the position

  public static final int BOARD_SIZE = 8;

  /**
   * Constructor that takes a row and column.
   *
   * @param col
   *          the column (a=1, b=2... h=8) of the position
   * @param row
   *          the row (1-8) of the position
   * @throws PositionException
   *           if this constructor is called with invalid coordinates
   */
  public Position(int col, int row) throws PositionException {
    // try {
    this.col = col;
    this.row = row;
    if (!(col >= 1 && col <= BOARD_SIZE)) {
      throw new PositionException(this);
    }
    if (!(row >= 1 && row <= BOARD_SIZE)) {
      throw new PositionException(this);
    }
    // assert col >= 1 && col <= BOARD_SIZE;
    // assert row >= 1 && row <= BOARD_SIZE;
    // } catch (AssertionError err) {
    // throw new PositionException(this);
    // }
  }

  /**
   * Protected constructor ONLY to be called by BankPosition.
   */
  protected Position() {
    this.col = 0;
    this.row = 0;
  }

  /**
   * Getter for row.
   *
   * @return row
   */
  public int row() {
    return row;
  }

  /**
   * Getter for column.
   *
   * @return col
   */
  public int col() {
    return col;
  }

  /**
   * Creates a String in the form (a-h)(1-8) that represents the position on the
   * chess board.
   *
   * Used to make printable strings for the CLI.
   *
   * @return a String representing this position on a chess board.
   */
  @Override
  public String toString() {
    if (col == 0) {
      return "Bank";
    }
    String abcdefgh = "abcdefgh";
    return abcdefgh.charAt(col - 1) + row + "";
  }

  /**
   * Creates a String in the form (1-8),(1-8) that represents the position on
   * the chess board.
   *
   * Used to make nice Strings to pass to the frontend code.
   *
   * @return a String representing this position on a chess board.
   */
  public String numString() {
    return col + "," + row;
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof Position)) {
      return false;
    }
    return col == ((Position) o).col && row == ((Position) o).row;
  }

  @Override
  public int hashCode() {
    return Objects.hash(row, col);
  }

}
