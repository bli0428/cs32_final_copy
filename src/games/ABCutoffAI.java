package games;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import components.Board;
import components.Piece;

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
  private Move alphaBetaCutoff(Board board, int cutoff, Heuristic heur) {
    Move bestMove = new Move(Double.NEGATIVE_INFINITY);
    double a = Double.NEGATIVE_INFINITY;
    double b = Double.POSITIVE_INFINITY;

    int currentColor = color;

    return null;
  }

  private double alphaBetaCutoffMax(Board board, int ply, double a, double b,
      Heuristic heur) {
    double v = Double.NEGATIVE_INFINITY;

    // checks for stalemate.
    if (board.stalemate(color)) {
      return -1000.0;
    }

    // checks that the calling player is in checkmate.
    if (board.checkmate(color)) {
      return -10000.0;
    }

    // checks that the enepy player is in checkmate.
    if (board.checkmate(Math.abs(color - 1))) {
      return 10000.0;
    }

    // checks that the max depth has been reached.
    if (ply == 0) {
      return heur.evalBoard(board).get(color);
    }

    return v;
  }

  @Override
  public Set<Piece> bank() {
    return bank;
  }

  @Override
  public Move move() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void setBoard(Board board) {
    this.board = board;
  }

  @Override
  public Piece promote() {
    // TODO Auto-generated method stub
    return null;
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
