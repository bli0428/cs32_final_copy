package components;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import positions.BankPosition;
import positions.Position;
import positions.PositionException;

/**
 * Class that represents a chess board.
 *
 * @author charliecutting
 *
 */
public class Board {

  private Map<Position, Piece> places;

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
   *
   * @throws PositionException
   *           if there's an internal issue with Position
   */
  public Board() throws PositionException {
    this.places = new HashMap<Position, Piece>();
    Position a1 = new Position(1, 1);
    Position b1 = new Position(2, 1);
    Position c1 = new Position(3, 1);
    Position d1 = new Position(4, 1);
    Position e1 = new Position(5, 1);
    Position f1 = new Position(6, 1);
    Position g1 = new Position(7, 1);
    Position h1 = new Position(8, 1);
    Position a2 = new Position(1, 2);
    Position b2 = new Position(2, 2);
    Position c2 = new Position(3, 2);
    Position d2 = new Position(4, 2);
    Position e2 = new Position(5, 2);
    Position f2 = new Position(6, 2);
    Position g2 = new Position(7, 2);
    Position h2 = new Position(8, 2);
    Position a8 = new Position(1, 8);
    Position b8 = new Position(2, 8);
    Position c8 = new Position(3, 8);
    Position d8 = new Position(4, 8);
    Position e8 = new Position(5, 8);
    Position f8 = new Position(6, 8);
    Position g8 = new Position(7, 8);
    Position h8 = new Position(8, 8);
    Position a7 = new Position(1, 7);
    Position b7 = new Position(2, 7);
    Position c7 = new Position(3, 7);
    Position d7 = new Position(4, 7);
    Position e7 = new Position(5, 7);
    Position f7 = new Position(6, 7);
    Position g7 = new Position(7, 7);
    Position h7 = new Position(8, 7);
    places.put(a1, new Rook(a1, 0));
    places.put(b1, new Knight(b1, 0));
    places.put(c1, new Bishop(c1, 0));
    places.put(h1, new Rook(h1, 0));
    places.put(g1, new Knight(g1, 0));
    places.put(f1, new Bishop(f1, 0));
    places.put(e1, new King(e1, 0));
    places.put(d1, new Queen(d1, 0));
    places.put(a2, new Pawn(a2, 0));
    places.put(b2, new Pawn(b2, 0));
    places.put(c2, new Pawn(c2, 0));
    places.put(d2, new Pawn(d2, 0));
    places.put(e2, new Pawn(e2, 0));
    places.put(f2, new Pawn(f2, 0));
    places.put(g2, new Pawn(g2, 0));
    places.put(h2, new Pawn(h2, 0));
    places.put(a8, new Rook(a8, 1));
    places.put(b8, new Knight(b8, 1));
    places.put(c8, new Bishop(c8, 1));
    places.put(h8, new Rook(h8, 1));
    places.put(g8, new Knight(g8, 1));
    places.put(f8, new Bishop(f8, 1));
    places.put(e8, new King(e8, 1));
    places.put(d8, new Queen(d8, 1));
    places.put(a7, new Pawn(a7, 1));
    places.put(b7, new Pawn(b7, 1));
    places.put(c7, new Pawn(c7, 1));
    places.put(d7, new Pawn(d7, 1));
    places.put(e7, new Pawn(e7, 1));
    places.put(f7, new Pawn(f7, 1));
    places.put(g7, new Pawn(g7, 1));
    places.put(h7, new Pawn(h7, 1));
  }

  /**
   * A copy constructor that creates a new instance of Board that is a shallow
   * copy of the old board.
   *
   * @param oldBoard
   *          The board to be copied.
   */
  public Board(Board oldBoard) {
    this.places = new HashMap<Position, Piece>();
    for (Position key : oldBoard.places().keySet()) {
      this.places.put(key, oldBoard.places().get(key).copyOf());
    }
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
    if (!p.getValidMoves(this).contains(dest)) {
      throw new InvalidMoveException(dest);
    }

    // If there's a piece at the destination, it will get taken. Send it to a
    // bank.
    if (places.containsKey(dest)) {
      Piece endPiece = places.get(dest);
      if (endPiece.type().equals("pp")) {
        out = new Pawn(new BankPosition(), endPiece.color(), true);
      } else {
        out = places.get(dest);
        endPiece.move(new BankPosition());
      }
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
      if (places.get(key).color() == color) {
        results.put(key, places.get(key).getValidMoves(this));
      }
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
          // e.printStackTrace();
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
    return places;
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
        out.addAll(places.get(p).threatens(this));
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
        out.addAll(boardPlaces.get(p).threatens(this));
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

  /**
   * Is the king of the given color in checkmate.
   *
   * @param color
   *          0 for white, 1 for black
   * @return true if the king is in checkmate, false otherwise
   */
  public boolean checkmate(int color) {
    return check(color) && getValidMoves(color).isEmpty();
  }

  /**
   * Is the game at a stalemate.
   *
   * @param color
   *          0 for white's turn, 1 for black's turn
   * @return true if the game is at a stalemate, false otherwise
   */
  public boolean stalemate(int color) {
    return !check(color) && getValidMoves(color).isEmpty();
  }
}
