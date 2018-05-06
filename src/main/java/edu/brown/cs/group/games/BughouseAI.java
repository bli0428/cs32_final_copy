package edu.brown.cs.group.games;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import edu.brown.cs.group.components.Board;
import edu.brown.cs.group.components.InvalidMoveException;
import edu.brown.cs.group.components.Piece;
import edu.brown.cs.group.components.Queen;
import edu.brown.cs.group.positions.Position;

/**
 * Represents a Minimax ai using alpha-beta cutoff.
 *
 * @author Brian
 *
 */
public class BughouseAI implements Player {

  private Set<Piece> bank;
  private Board board;
  private int color;
  private int cutoff;
  private Heuristic internalHeur;

  private Map<Board, TranspositionMove> TT;
  private int TT_MAX_SIZE = 100000;

  private int iterDepth;

  private int nodesSearched = 0;
  private long startTime;
  private long endTime;
  private int numRepeat = 0;
  private int trimmed = 0;

  /**
   * Instantiates a new ABCutoffAI with a bank.
   */
  public BughouseAI(int cutoff) {
    this.cutoff = cutoff;
    bank = Collections.synchronizedSet(new HashSet<Piece>());
    TT = new HashMap<Board, TranspositionMove>();

  }

  public void requestPiece(String type) {

  }

  private void startBench() {
    nodesSearched = 0;
    startTime = System.nanoTime();

  }

  private void printBench() {
    endTime = System.nanoTime();
    System.out.println(String.format(
        "%d nodes searched in depth %d in %f seconds with %d boards repeated, %d boards trimmed",
        nodesSearched, iterDepth, (endTime - startTime) / 1000000000.0,
        numRepeat, trimmed));

    startTime = System.nanoTime();
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
  private Move alphaBetaCutoff(int cutoff, Heuristic heur, double alpha,
      double beta) {
    startBench();

    TranspositionMove potentialBest = null;

    nodesSearched = 0;
    trimmed = 0;

    Move bestMove = null;
    double v = Double.NEGATIVE_INFINITY;
    double a = alpha;
    double b = beta;

    Map<Position, Set<Position>> validMoves = board.getValidMoves(color);
    // System.out.println(validMoves.toString());

    // checks transposition table to try out the best move
    if (TT.containsKey(board)) {
      potentialBest = TT.get(board);
      if (validMoves.containsKey(potentialBest.getStart()) && validMoves
          .get(potentialBest.getStart()).contains(potentialBest.getEnd())) {
        // assert validMoves.containsKey(potentialBest.getStart());
        // assert
        // validMoves.get(potentialBest.getStart()).contains(potentialBest.getEnd());
        Board newBoard = new Board(board);

        try {
          newBoard.processMove(potentialBest.getStart(), potentialBest.getEnd(),
              promote(potentialBest.getEnd()));

        } catch (InvalidMoveException e) {

          e.printStackTrace();
        }
        double temp = alphaBetaCutoffMin(newBoard, cutoff - 1, a, b, heur,
            color);
        if (temp > v) {

          bestMove = new Move(potentialBest.getStart(), potentialBest.getEnd());
          v = temp;
          if (TT.containsKey(board)) {
            if (TT.get(board).getDepth() <= iterDepth) {
              TT.put(new Board(board), new TranspositionMove(
                  potentialBest.getStart(), potentialBest.getEnd(), iterDepth));
            }
          } else {
            TT.put(new Board(board), new TranspositionMove(
                potentialBest.getStart(), potentialBest.getEnd(), iterDepth));
          }

        }
        if (v >= b) {
          trimmed++;
        }
        bestMove.setValue(v);
        a = Math.max(a, temp);
      }
    }

    for (Position start : validMoves.keySet()) {
      for (Position end : validMoves.get(start)) {
        if (potentialBest != null) {
          if (start.equals(potentialBest.getStart())
              && end.equals(potentialBest.getEnd())) {
            continue;
          }
        }

        Board newBoard = new Board(board);

        try {
          newBoard.processMove(start, end, promote(end));

        } catch (InvalidMoveException e) {

          e.printStackTrace();
        }
        double temp = alphaBetaCutoffMin(newBoard, cutoff - 1, a, b, heur,
            color);
        if (temp > v) {

          bestMove = new Move(start, end);
          bestMove.setValue(temp);
          v = temp;
          if (TT.containsKey(board)) {
            if (TT.get(board).getDepth() <= iterDepth) {
              TT.put(new Board(board),
                  new TranspositionMove(start, end, iterDepth));
            }
          } else {
            TT.put(new Board(board),
                new TranspositionMove(start, end, iterDepth));
          }

        }
        if (v >= b) {
          trimmed++;
        }
        a = Math.max(a, temp);
      }
    }

    printBench();
    System.out.println(bestMove.toString());
    System.out.println(v);
    return bestMove;
  }

  private double alphaBetaCutoffMax(Board tempBoard, int ply, double a,
      double b, Heuristic heur, int currColor) {

    TranspositionMove potentialBest = null;
    nodesSearched++;

    if (TT.containsKey(tempBoard)) {
      numRepeat++;
    }

    double v = Double.NEGATIVE_INFINITY;
    int gameOver = tempBoard.gameOver(color);

    // checks for stalemate.
    if (gameOver == 2) {
      return 0;
    }

    // checks that the calling player is in checkmate.
    if (gameOver == 1) {
      return -200000.0;
    }

    // checks that the enemy player is in checkmate.
    if (tempBoard.gameOver(Math.abs(color - 1)) == 1) {
      return 200000.0;
    }

    // checks that the max depth has been reached.
    if (ply == 0) {
      return heur.evalBoard(tempBoard).get(color);
    }

    Map<Position, Set<Position>> validMoves = tempBoard
        .getValidMoves(currColor);

    // checks transposition table
    if (TT.containsKey(tempBoard)) {
      potentialBest = TT.get(tempBoard);

      if (validMoves.containsKey(potentialBest.getStart()) && validMoves
          .get(potentialBest.getStart()).contains(potentialBest.getEnd())) {
        Board newBoard = new Board(tempBoard);

        try {
          newBoard.processMove(potentialBest.getStart(), potentialBest.getEnd(),
              new Queen(potentialBest.getEnd(), currColor));
        } catch (InvalidMoveException e) {
          e.printStackTrace();
        }
        double temp = alphaBetaCutoffMin(newBoard, ply - 1, a, b, heur,
            Math.abs(currColor - 1));
        if (temp > v) {
          v = temp;
          if (TT.containsKey(tempBoard)) {
            if (TT.get(tempBoard).getDepth() <= iterDepth) {
              TT.put(new Board(tempBoard), new TranspositionMove(
                  potentialBest.getStart(), potentialBest.getEnd(), iterDepth));
            }
          } else {
            TT.put(new Board(tempBoard), new TranspositionMove(
                potentialBest.getStart(), potentialBest.getEnd(), iterDepth));
          }
        }

        if (v >= b) {
          // System.out.println("returning from cutoffmax early" + v);
          trimmed++;
          return v;
        }
        a = Math.max(a, v);
      }
    }

    for (Position start : validMoves.keySet()) {
      for (Position end : validMoves.get(start)) {

        if (potentialBest != null) {
          if (start.equals(potentialBest.getStart())
              && end.equals(potentialBest.getEnd())) {
            continue;
          }
        }

        Board newBoard = new Board(tempBoard);

        try {
          newBoard.processMove(start, end, new Queen(end, currColor));
        } catch (InvalidMoveException e) {
          // continue;
          e.printStackTrace();
        }
        double temp = alphaBetaCutoffMin(newBoard, ply - 1, a, b, heur,
            Math.abs(currColor - 1));
        if (temp > v) {
          v = temp;
          if (TT.containsKey(tempBoard)) {
            if (TT.get(tempBoard).getDepth() <= iterDepth) {
              TT.put(new Board(tempBoard),
                  new TranspositionMove(start, end, iterDepth));
            }
          } else {
            TT.put(new Board(tempBoard),
                new TranspositionMove(start, end, iterDepth));
          }
        }

        // v = Math.max(v, alphaBetaCutoffMin(newBoard, ply - 1, a, b, heur,
        // Math.abs(currColor - 1)));

        if (v >= b) {
          // System.out.println("returning from cutoffmax early" + v);
          trimmed++;
          return v;
        }
        a = Math.max(a, v);
      }
    }
    // System.out.println("returning from cutoffmax" + v);
    return v;
  }

  private double alphaBetaCutoffMin(Board tempBoard, int ply, double a,
      double b, Heuristic heur, int currColor) {
    TranspositionMove potentialBest = null;

    nodesSearched++;

    if (TT.containsKey(tempBoard)) {
      numRepeat++;
    }

    double v = Double.POSITIVE_INFINITY;

    int gameOver = tempBoard.gameOver(color);

    // checks for stalemate.
    if (gameOver == 2) {
      return -1000.0;
    }

    // checks that the calling player is in checkmate.
    if (gameOver == 1) {
      return -10000.0;
    }

    // checks that the enemy player is in checkmate.
    if (tempBoard.gameOver(Math.abs(color - 1)) == 1) {
      return 10000.0;
    }

    // checks that the max depth has been reached.
    if (ply == 0) {
      // System.out.println("returning from cutoffmin" + v);
      // System.out.println(color);
      // System.out.println(heur.evalBoard(tempBoard));
      return heur.evalBoard(tempBoard).get(color);
    }

    Map<Position, Set<Position>> validMoves = tempBoard
        .getValidMoves(currColor);

    // checks transposition table
    if (TT.containsKey(tempBoard)) {
      potentialBest = TT.get(tempBoard);

      if (validMoves.containsKey(potentialBest.getStart()) && validMoves
          .get(potentialBest.getStart()).contains(potentialBest.getEnd())) {
        Board newBoard = new Board(tempBoard);

        try {
          newBoard.processMove(potentialBest.getStart(), potentialBest.getEnd(),
              new Queen(potentialBest.getEnd(), currColor));
        } catch (InvalidMoveException e) {
          e.printStackTrace();
        }

        double temp = alphaBetaCutoffMax(newBoard, ply - 1, a, b, heur,
            Math.abs(currColor - 1));

        if (temp < v) {
          v = temp;
          if (TT.containsKey(tempBoard)) {
            if (TT.get(tempBoard).getDepth() <= iterDepth) {
              TT.put(new Board(tempBoard), new TranspositionMove(
                  potentialBest.getStart(), potentialBest.getEnd(), iterDepth));
            }
          } else {
            TT.put(new Board(tempBoard), new TranspositionMove(
                potentialBest.getStart(), potentialBest.getEnd(), iterDepth));
          }
        }

        // v = Math.min(v, alphaBetaCutoffMax(newBoard, ply - 1, a, b, heur,
        // Math.abs(currColor - 1)));

        if (v <= a) {
          // System.out.println("returning from cutoffmin early" + v);
          // System.out.println("trimmed 3");
          trimmed++;
          return v;
        }
        b = Math.min(b, v);
      }
    }

    for (Position start : validMoves.keySet()) {
      for (Position end : validMoves.get(start)) {

        if (potentialBest != null) {
          if (start.equals(potentialBest.getStart())
              && end.equals(potentialBest.getEnd())) {
            continue;
          }
        }

        Board newBoard = new Board(tempBoard);

        try {
          newBoard.processMove(start, end, new Queen(end, currColor));
        } catch (InvalidMoveException e) {
          e.printStackTrace();
        }

        double temp = alphaBetaCutoffMax(newBoard, ply - 1, a, b, heur,
            Math.abs(currColor - 1));

        if (temp < v) {
          v = temp;
          if (TT.containsKey(tempBoard)) {
            if (TT.get(tempBoard).getDepth() <= iterDepth) {
              TT.put(new Board(tempBoard),
                  new TranspositionMove(start, end, iterDepth));
            }
          } else {
            TT.put(new Board(tempBoard),
                new TranspositionMove(start, end, iterDepth));
          }
        }

        // v = Math.min(v, alphaBetaCutoffMax(newBoard, ply - 1, a, b, heur,
        // Math.abs(currColor - 1)));

        if (v <= a) {
          // System.out.println("returning from cutoffmin early" + v);
          // System.out.println("trimmed 3");
          trimmed++;
          return v;
        }
        b = Math.min(b, v);
      }
    }
    // System.out.println("returning from cutoffmin" + v);
    return v;

  }

  @Override
  public Set<Piece> bank() {
    return bank;
  }

  @Override
  public Move move() {

    Move result = null;
    double alpha = Double.NEGATIVE_INFINITY;
    double beta = Double.POSITIVE_INFINITY;
    for (iterDepth = 1; iterDepth < cutoff + 1;) {

      result = alphaBetaCutoff(iterDepth, internalHeur, alpha, beta);

      if (Double.compare(result.value(), alpha) <= 0
          || Double.compare(result.value(), beta) >= 0) {
        alpha = Double.NEGATIVE_INFINITY;
        beta = Double.POSITIVE_INFINITY;
        System.out.println("repeating because outside window size");
      } else {
        iterDepth++;
        alpha = result.value() - 21.0;
        beta = result.value() + 21.0;
        // alpha = Double.NEGATIVE_INFINITY;
        // beta = Double.POSITIVE_INFINITY;
      }

    }

    return result;
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

  @Override
  public int getColor() {
    return color;
  }
}
