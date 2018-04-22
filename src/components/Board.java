package components;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import positions.BankPosition;
import positions.Position;

/**
 * Class that represents a chess board.
 *
 * @author charliecutting
 *
 */
public class Board {

  private Map<Position, Piece> places;

  private final Map<Position, Piece> DEFAULT_START = //
      new HashMap<Position, Piece>(); // TODO: Initialize this map with the
                                      // default starting values for the board

  /**
   * Constructor that takes a map of starting positions (allows arbitrary game
   * initialization).
   *
   * @param startingPlaces
   *          a map of starting positions
   */
  public Board(Map<Position, Piece> startingPlaces) {
    this.places = startingPlaces;
  }

  /**
   * Default constructor that sets up the board in a standard chess
   * configuration.
   */
  public Board() {
    this.places = DEFAULT_START;
  }

  /**
   * A copy constructor that creates a new instance of Board that is a
   * shallow copy of the old board.
   *
   * @param oldBoard
   *          The board to be copied.
   */
  public Board(Board oldBoard) {
    this.places = new HashMap<Position, Piece>(oldBoard.places());
  }

  /**
   * Processes a move from start to dest.
   *
   * @param start
   *          the start position
   * @param dest
   *          the end position
   * @throws InvalidMoveException
   *           if the start position or end position are invalid.
   * @return a reference to the piece that was at dest, or null if there was
   *         nothing there
   */
  public Piece processMove(Position start, Position dest)
      throws InvalidMoveException {

    Piece out = null;

    // If there's no piece at start or the piece at start can't move to end,
    // throw an exception
    if (!places.containsKey(start)) {
      throw new InvalidMoveException(dest);
    }
    Piece p = places.get(start);
    if (!p.getValidMoves().contains(dest)) {
      throw new InvalidMoveException(dest);
    }

    // If there's a piece at the destination, it will get taken. Send it to a
    // bank.
    if (places.containsKey(dest)) {
      out = places.get(dest);
      places.get(dest).move(new BankPosition());
      places.remove(dest);
    }

    // Process the move by updating the piece's internal position and the
    // positions map.
    p.move(dest);
    places.put(dest, p);
    places.remove(start);
    return out;
  }

  /**
   * Returns the valid moves of this board for a given color.
   *
   * @param color
   *          The color of the current player.
   * @return The valid moves represented as a Map.
   */
  public Map<Position, Set<Position>> getValidMoves(int color) {
    Map<Position, Set<Position>> results = new HashMap<>();

    // Creates all valid moves for this board.
    for (Position key : places.keySet()) {
      results.put(key, places.get(key).getValidMoves());
    }

    // Filters all valid moves for moves that would leave King in check.
    for (Position key : results.keySet()) {

      Position start = key;
      Set<Position> validMoves = results.get(key);

      for (Iterator<Position> i = validMoves.iterator(); i.hasNext();) {
        Board tempBoard = new Board(this);
        Position end = i.next();
        try {
          tempBoard.processMove(start, end);
        } catch (InvalidMoveException e) {
          e.printStackTrace();
        }
        if (tempBoard.check(color)) {
          i.remove();
        }
      }

    }
    return results;
  }

  /**
   * Getter for places.
   *
   * @return an ImmutableMap copy of places.
   */
  public Map<Position, Piece> places() {
    return places; // TODO: Figure out why my Guava import isn't working and
                   // make this an ImmutableMap
  }

  /**
   * Build the set of all positions threatened by pieces of a particular color.
   *
   * @param color
   *          0 for white, 1 for black
   * @return the set of threatened spaces
   */
  public Set<Position> threatened(int color) {
    Set<Position> out = new HashSet<Position>();
    for (Position p : places.keySet()) {
      if (places.get(p).color() == color) {
        out.addAll(places.get(p).threatens());
      }
    }
    return out;
  }

  /**
   * Build the set of all positions threatened by pieces of a particular color
   * for a particular board.
   *
   * @param color
   *          0 for white, 1 for black
   * @param board
   *          The board to check on.
   * @return the set of threatened spaces
   */
  public Set<Position> threatened(int color, Board board) {
    Set<Position> out = new HashSet<Position>();
    Map<Position, Piece> boardPlaces = board.places();
    for (Position p : boardPlaces.keySet()) {
      if (boardPlaces.get(p).color() == color) {
        out.addAll(boardPlaces.get(p).threatens());
      }
    }

    return out;
  }

  /**
   * Is the king of the given color in check.
   *
   * @param color
   *          0 for white, 1 for black
   * @return true if the king is in check, false otherwise
   */
  public boolean check(int color) {
    Set<Position> threats = threatened(Math.abs(color - 1));
    for (Position p : threats) {
      Piece k = places.get(p);
      if (k != null && k.type().equals("K") && k.color() == color) {
        return true;
      }
    }
    return false;
  }
}
