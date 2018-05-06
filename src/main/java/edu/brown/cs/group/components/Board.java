package edu.brown.cs.group.components;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import edu.brown.cs.group.games.Player;
import edu.brown.cs.group.games.Tables;
import edu.brown.cs.group.positions.BankPosition;
import edu.brown.cs.group.positions.Position;
import edu.brown.cs.group.positions.PositionException;

/**
 * Class that represents a chess board.
 *
 * @author charliecutting
 *
 */
public class Board {

  private static final String[] WB = { "W", "B" };

  private Map<Position, Piece> places;

  private Player[] players;

  private Piece passant;

  private int fiftyMove = 0;
  // private Boolean threefold;

  /**
   * Constructor that takes a map of starting positions (allows arbitrary game
   * initialization).
   *
   * @param startingPlaces
   *          a map of starting positions
   */
  public Board(Map<Position, Piece> startingPlaces, Player white,
      Player black) {
    this.places = startingPlaces;
    players = new Player[2];
    players[0] = white;
    players[1] = black;
  }

  /**
   * Default constructor that sets up the board in a standard chess
   * configuration.
   *
   * @throws PositionException
   *           if there's an internal issue with Position
   */
  public Board(Player white, Player black) throws PositionException {
    this.places = new HashMap<Position, Piece>();
    players = new Player[2];
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

    players[0] = white;
    players[1] = black;
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

    players = new Player[2];
    players[0] = oldBoard.players[0];
    players[1] = oldBoard.players[1];
  }

  /**
   * Processes a move from start to dest, taking in a promoted Piece.
   * 
   * @param start
   * @param dest
   * @param prmtPiece
   *          A piece in the form of something like Knight or Queen. Not the
   *          actual PromotedPawn class.
   * @return
   * @throws InvalidMoveException
   */
  public Piece processMove(Position start, Position dest, Piece prmtPiece)
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

    // Castling
    if (p.type().equals("K") && Math.abs(dest.col() - start.col()) == 2) {
      Piece rook;
      Position rookDest;
      Position rookStart;
      try {
        if (dest.col() - start.col() == 2) {
          rookStart = new Position(8, dest.row());
          rook = places.get(rookStart);
          rookDest = new Position(6, dest.row());
        } else {
          // dest.col() - start.col() == -2
          rookStart = new Position(1, dest.row());
          rook = places.get(rookStart);
          rookDest = new Position(4, dest.row());
        }
        rook.move(rookDest);
        places.put(rookDest, rook);
        places.remove(rookStart);
      } catch (PositionException pe) {
        // Something wrong with the rook
        System.out.println("ERROR: Invalid Castle!");
      }
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

    // If the piece is a pawn, and en-passant is an option, and the pawn is
    // moving to the left or right column,
    // this indicates that the player, in fact, does want to perform en-passant
    if (passant != null && dest.col() != start.col() && p.type().equals("p")
        && !places.containsKey(dest)) {
      out = new Pawn(new BankPosition(), passant.color(), true);
      places.remove(passant.position());
    }

    if (p.type().equals("p")
        && (start.row() == dest.row() - 2 || start.row() == dest.row() + 2)) {
      passant = p;
    } else {
      passant = null;
    }

    // Process the move by updating the piece's internal position and the
    // positions map.
    p.move(dest);
    places.put(dest, p);
    places.remove(start);

    // Promotions
    if (p.type().equals("p") && (dest.row() == 8 || dest.row() == 1)) {
      p = new PromotedPawn(prmtPiece);
      places.put(dest, p);
    }

    return out;
  }

  /**
   * Processes a move from start to dest.
   *
   * @param start
   *          the start position
   * @param dest
   *          the end position
   * @param usrQuery
   *          is this move a temporary process
   * @throws InvalidMoveException
   *           if the start position or end position are invalid.
   * @return a reference to the piece that was at dest, or null if there was
   *         nothing there
   */
  public Piece processMove(Position start, Position dest, boolean usrQuery)
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

    // Castling
    if (p.type().equals("K") && Math.abs(dest.col() - start.col()) == 2) {
      Piece rook;
      Position rookDest;
      Position rookStart;
      try {
        if (dest.col() - start.col() == 2) {
          rookStart = new Position(8, dest.row());
          rook = places.get(rookStart);
          rookDest = new Position(6, dest.row());
        } else {
          // dest.col() - start.col() == -2
          rookStart = new Position(1, dest.row());
          rook = places.get(rookStart);
          rookDest = new Position(4, dest.row());
        }
        rook.move(rookDest);
        places.put(rookDest, rook);
        places.remove(rookStart);
      } catch (PositionException pe) {
        // Something wrong with the rook
        System.out.println("ERROR: Invalid Castle!");
      }
    }

    // 50 move stalemate Rule
    if (p.type().equals("p")) {
      fiftyMove = 0;
    } else {
      fiftyMove += 1;
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
      fiftyMove = 0;
    }

    // If the piece is a pawn, and en-passant is an option, and the pawn is
    // moving to the left or right column,
    // this indicates that the player, in fact, does want to perform en-passant
    if (passant != null && dest.col() != start.col() && p.type().equals("p")
        && !places.containsKey(dest)) {
      out = new Pawn(new BankPosition(), passant.color(), true);
      places.remove(passant.position());
    }

    if (p.type().equals("p")
        && (start.row() == dest.row() - 2 || start.row() == dest.row() + 2)) {
      passant = p;
    } else {
      passant = null;
    }

    // Process the move by updating the piece's internal position and the
    // positions map.
    p.move(dest);
    places.put(dest, p);
    places.remove(start);
    // Promotions
    if (!usrQuery && p.type().equals("p")
        && (dest.row() == 8 || dest.row() == 1)) {
      p = new PromotedPawn(players[p.color()].promote(dest));
      places.put(dest, p);
    }

    return out;

  }

  /**
   * Processes a place at dest.
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
  public Piece processPlace(Position start, Position dest, Piece p)
      throws InvalidMoveException {

    assert start instanceof BankPosition;
    assert placable().contains(dest);

    Piece out = null;

    // Process the move by updating the piece's internal position and the
    // positions map.
    p.move(dest);
    places.put(dest, p);
    places.remove(start);
    System.out.println(toString());
    return out;
  }

  public Piece getPassant() {
    return passant;
  }

  public Set<Position> placable() {
    Set<Position> out = new HashSet<Position>();
    for (int i = 1; i <= 8; i++) {
      for (int j = 1; j <= 8; j++) {
        Position p;
        try {
          p = new Position(i, j);
          if (!places.containsKey(p))
            out.add(p);
        } catch (PositionException e) {
          // shouldn't get here
          e.printStackTrace();
        }
      }
    }
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
    Map<Position, Set<Position>> results = new HashMap<Position, Set<Position>>();

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
          tempBoard.processMove(start, end, true);
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
        out.addAll(boardPlaces.get(p).threatens(board)); // Potential bug -
                                                         // should this be
                                                         // "this" or "board"?
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
    for (Position p : places.keySet()) {
      if (places.get(p).type().equals("K") && places.get(p).color() == color) {
        return isAttacked(Math.abs(color - 1), p);
      }
    }
    return false;
  }

  // public boolean check(int color) {
  // Set<Position> threats = threatened(Math.abs(color - 1));
  // for (Position p : threats) {
  // Piece k = places.get(p);
  // if (k != null && k.type().equals("K") && k.color() == color) {
  // // System.out.println(p.col() + "," + p.row());
  // return true;
  // }
  // }
  // return false;
  // }

  /**
   * Checks whether a given position is attacked by the given color
   * 
   * @param color
   *          The attacking color
   * @param pos
   *          The Position that is attacked
   * @return
   */
  private boolean isAttacked(int color, Position pos) {
    int posCol = pos.col();
    int posRow = pos.row();
    int attackedIndex = (posRow - 1) * 16 + (posCol - 1);

    // finds pieces of the right color.
    for (Position attackerPos : places.keySet()) {
      Piece attacker = places.get(attackerPos);
      if (attacker.color() == color) {
        String type = attacker.type().equals("pp")
            ? ((PromotedPawn) attacker).innerType()
            : attacker.type();
        int attackerIndex = (attackerPos.row() - 1) * 16
            + (attackerPos.col() - 1);
        int canAttack = Tables.ATTACK_ARRAY[attackedIndex - attackerIndex
            + 128];
        int attackDelta = Tables.DELTA_ARRAY[attackedIndex - attackerIndex
            + 128];
        switch (type) {
        case "b":
          if (canAttack == Tables.ATTACK_KQBbP
              || canAttack == Tables.ATTACK_KQBwP
              || canAttack == Tables.ATTACK_QB) {
            if (!isBlocked(attackerIndex, attackedIndex, attackDelta)) {
              return true;
            }
          }
          break;
        case "K":
          if (canAttack == Tables.ATTACK_KQR || canAttack == Tables.ATTACK_KQBbP
              || canAttack == Tables.ATTACK_KQBwP) {
            if (!isBlocked(attackerIndex, attackedIndex, attackDelta)) {
              return true;
            }
          }
          break;
        case "q":
          if (canAttack >= Tables.ATTACK_KQR && canAttack <= Tables.ATTACK_QB) {
            if (!isBlocked(attackerIndex, attackedIndex, attackDelta)) {
              return true;
            }
          }
          break;
        case "r":
          if (canAttack == Tables.ATTACK_KQR || canAttack == Tables.ATTACK_QR) {
            if (!isBlocked(attackerIndex, attackedIndex, attackDelta)) {
              return true;
            }
          }
          break;
        case "k":
          if (canAttack == Tables.ATTACK_N) {
            if (!isBlocked(attackerIndex, attackedIndex, attackDelta)) {
              return true;
            }
          }
          break;
        case "p":
          if ((canAttack == Tables.ATTACK_KQBbP && color == 1)
              || (canAttack == Tables.ATTACK_KQBwP && color == 0)) {
            if (!isBlocked(attackerIndex, attackedIndex, attackDelta)) {
              return true;
            }
          }

        }
      }
    }
    return false;

  }

  private boolean isBlocked(int start, int end, int delta) {
    for (int i = start + delta; i != end; i += delta) {
      int posCol = (i % 16) + 1;
      int posRow = (i / 16) + 1;
      try {
        if (places.containsKey(new Position(posCol, posRow))) {
          return true;
        }
      } catch (PositionException e) {
        System.err.println("probably indices are not right");
        e.printStackTrace();
      }

    }
    return false;
  }

  /**
   * Checks whether the game is over.
   * 
   * @param color
   *          0 for white, 1 for black
   * @return 1 if in checkmate, 2 if in stalemate, 0 otherwise
   */
  public int gameOver(int color) {
    boolean inCheck = check(color);
    boolean hasMoves = false;
    Map<Position, Set<Position>> map = getValidMoves(color);

    for (Position p : map.keySet()) {
      if (!map.get(p).isEmpty()) {
        hasMoves = true;

        break;
      }
    }

    if (!hasMoves) {
      return inCheck ? 1 : 2;
    }

    if (fiftyMove >= 50) {

      return 2;
    }

    Collection<Piece> pieces = places.values();
    ArrayList<String> pieceTypes = new ArrayList<String>();
    for (Piece piece : pieces) {
      pieceTypes.add(piece.type());
    }
    if (pieces.size() <= 3) {

      if (pieces.size() == 2 || pieceTypes.contains("b")
          || pieceTypes.contains("k")) {
        System.out.println("hi");
        return 2;
      }

    }

    return 0;

  }

  /**
   * Is the king of the given color in checkmate.
   *
   * @param color
   *          0 for white, 1 for black
   * @return true if the king is in checkmate, false otherwise
   */
  public boolean checkmate(int color) {
    Map<Position, Set<Position>> map = getValidMoves(color);

    if (!check(color)) {
      return false;
    }

    for (Position p : map.keySet()) {
      if (!map.get(p).isEmpty()) {
        return false;
      }
    }

    return true;
  }

  /**
   * Is the game at a stalemate.
   *
   * @param color
   *          0 for white's turn, 1 for black's turn
   * @return true if the game is at a stalemate, false otherwise
   */
  public boolean stalemate(int color) {
    Map<Position, Set<Position>> map = getValidMoves(color);

    if (check(color)) {
      return false;
    }

    Collection<Piece> pieces = places.values();
    // Stalemate by insufficient checkmate material

    // K vs K
    if (checkStale(pieces, (ArrayList<String>) Arrays.asList("K", "K"))) {
      return true;
    }
    // K + k vs K
    if (checkStale(pieces, (ArrayList<String>) Arrays.asList("K", "K", "k"))) {
      return true;
    }
    // K + b vs K
    if (checkStale(pieces, (ArrayList<String>) Arrays.asList("K", "b", "K"))) {
      return true;
    }

    for (Position p : map.keySet()) {
      if (!map.get(p).isEmpty()) {
        return false;
      }
    }

    return true;
  }

  private boolean checkStale(Collection<Piece> piecesLeft,
      ArrayList<String> staleCond) {
    if (staleCond.size() != piecesLeft.size()) {
      return false;
    }
    for (Piece piece : piecesLeft) {
      System.out.println("Piece= " + piece.type());
      System.out.println(staleCond.contains(piece.type()));
      if (!staleCond.contains(piece.type())) {
        return false;
      }
    }
    return true;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    for (int i = 8; i > 0; i--) {
      sb.append(i + ". ");
      for (int j = 1; j <= 8; j++) {
        Position p;
        try {
          p = new Position(j, i);
          if (places.containsKey(p)) {
            sb.append(WB[places.get(p).color()]);
            sb.append(places.get(p).type());
          } else {
            sb.append("__");
          }
          sb.append(" ");
        } catch (PositionException e) {
          // Should never be reached
          e.printStackTrace();
        }
      }
      sb.append("\n");
    }

    sb.append("   ");
    for (int i = 1; i <= 8; i++) {
      sb.append(i + "  ");
    }
    sb.append("\n");
    return sb.toString();
  }

  public void print() {
    System.out.println(toString());
  }

  @Override
  public boolean equals(Object o) {
    if (o instanceof Board) {
      return ((Board) o).toString().equals(toString());
    }
    return false;
  }

  @Override
  public int hashCode() {
    return toString().hashCode();
  }
}
