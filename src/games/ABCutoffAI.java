package games;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import components.Board;
import components.InvalidMoveException;
import components.Piece;
import components.Queen;
import positions.Position;

/**
 * Represents a Minimax ai using alpha-beta cutoff.
 *
 * @author Brian
 *
 */
public class ABCutoffAI implements Player {

  private Set<Piece> bank;
  private Board board;
  private int color;

  /**
   * Instantiates a new ABCutoffAI with a bank.
   */
  public ABCutoffAI() {
    bank = Collections.synchronizedSet(new HashSet<Piece>());
  }

  /**
   * Determines up to a cutoff Depth what the best move is using alpha-beta
   * pruning minimax.
   *
   * @param board
   *          The current board.
   * @param cutoff
   *          The cutoff depth. Must be >= 1.
   * @param heur
   *          The heuristic used to evaluate the board.
   * @return The best Move.
   */
  private Move alphaBetaCutoff(int cutoff, Heuristic heur) {

    Move bestMove = null;
    double a = Double.NEGATIVE_INFINITY;
    double b = Double.POSITIVE_INFINITY;
    double v = Double.NEGATIVE_INFINITY;

    Map<Position, Set<Position>> validMoves = board.getValidMoves(color);

    for (Position start : validMoves.keySet()) {
      for (Position end : validMoves.get(start)) {
        Board newBoard = new Board(board);
        double temp = alphaBetaCutoffMin(newBoard, cutoff - 1, a, b, heur,
            color);
        if (temp > v) {
          bestMove = new Move(start, end);
          v = temp;
        }
        if (v >= b) {
          new Move(start, end);
        }
        a = Math.max(a, temp);
      }
    }

    return bestMove;
  }

  private double alphaBetaCutoffMax(Board tempBoard, int ply, double a,
      double b, Heuristic heur, int currColor) {

    double v = Double.NEGATIVE_INFINITY;

    // checks for stalemate.
    if (tempBoard.stalemate(color)) {
      return -1000.0;
    }

    // checks that the calling player is in checkmate.
    if (tempBoard.checkmate(color)) {
      return -10000.0;
    }

    // checks that the enemy player is in checkmate.
    if (tempBoard.checkmate(Math.abs(color - 1))) {
      return 10000.0;
    }

    // checks that the max depth has been reached.
    if (ply == 0) {
      return heur.evalBoard(tempBoard).get(color);
    }

    Map<Position, Set<Position>> validMoves = tempBoard
        .getValidMoves(currColor);
    for (Position start : validMoves.keySet()) {
      for (Position end : validMoves.get(start)) {
        Board newBoard = new Board(tempBoard);
        try {
          newBoard.processMove(start, end, false);
        } catch (InvalidMoveException e) {
          e.printStackTrace();
        }
        v = Math.max(v, alphaBetaCutoffMin(newBoard, ply - 1, a, b, heur,
            Math.abs(currColor - 1)));
        if (v >= b) {
          return v;
        }
        a = Math.max(a, v);
      }
    }

    return v;
  }

  private double alphaBetaCutoffMin(Board tempBoard, int ply, double a,
      double b, Heuristic heur, int currColor) {
    double v = Double.POSITIVE_INFINITY;

    // checks for stalemate.
    if (tempBoard.stalemate(color)) {
      return -1000.0;
    }

    // checks that the calling player is in checkmate.
    if (tempBoard.checkmate(color)) {
      return -10000.0;
    }

    // checks that the enepy player is in checkmate.
    if (tempBoard.checkmate(Math.abs(color - 1))) {
      return 10000.0;
    }

    // checks that the max depth has been reached.
    if (ply == 0) {
      return heur.evalBoard(tempBoard).get(color);
    }

    Map<Position, Set<Position>> validMoves = tempBoard
        .getValidMoves(currColor);
    for (Position start : validMoves.keySet()) {
      for (Position end : validMoves.get(start)) {
        Board newBoard = new Board(tempBoard);
        try {
          newBoard.processMove(start, end, false);
        } catch (InvalidMoveException e) {
          e.printStackTrace();
        }
        v = Math.min(v, alphaBetaCutoffMax(newBoard, ply - 1, a, b, heur,
            Math.abs(currColor - 1)));
        if (v <= a) {
          return v;
        }
        b = Math.min(b, v);
      }
    }

    return v;

  }

  @Override
  public Set<Piece> bank() {
    return bank;
  }

  @Override
  public Move move() {
    return alphaBetaCutoff(3, new MaterialHeuristic());
  }

  @Override
  public void setBoard(Board board) {
    this.board = board;
  }

  @Override
  public Piece promote(Position p) {
    return new Queen(p, color);
  }

  @Override
  public void acceptPiece(Piece p) {
    bank.add(p);

  }

  @Override
  public void setColor(int color) {
    this.color = color;
  }

}
