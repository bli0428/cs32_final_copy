package edu.brown.cs.group.positions;

/**
 * Class that represents a Position() not on the board (which is in a piece
 * bank).
 *
 * @author charliecutting
 *
 */
public class BankPosition extends Position {
  private static BankPosition singleton = new BankPosition();

  /**
   * Defualt Constructor.
   */
  public BankPosition() {
    super();
  }
  
  public static BankPosition getInstance() {
    return singleton;
  }

}
