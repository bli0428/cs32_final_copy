package edu.brown.cs.group.games;

import java.util.Collections;
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
public class ABCutoffAI implements Player {

  private Set<Piece> bank;
  private Board board;
  private int color;

  private final int cutoff = 4;

  private int nodesSearched = 0;
  private long startTime;
  private long endTime;
  private int depth;
  private int numRepeat = 0;

  private int trimmed = 0;

  private Set<Board> visitedBoards = new HashSet<>();

  /**
   * Instantiates a new ABCutoffAI with a bank.
   */
  public ABCutoffAI() {
    bank = Collections.synchronizedSet(new HashSet<Piece>());
  }

  private void startBench() {
    nodesSearched = 0;
    startTime = System.nanoTime();
    depth = 0;
  }

  private void printBench() {
    endTime = System.nanoTime();
    System.out.println(String.format(

        "%d nodes searched in depth %d in %f seconds with %d boards repeated, %d boards trimmed",
        nodesSearched, depth, (endTime - startTime) / 1000000000.0, numRepeat,
        trimmed));

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
  private Move alphaBetaCutoff(int cutoff, Heuristic heur) {
    startBench();
    nodesSearched = 0;
    trimmed = 0;

    Move bestMove = null;
    double a = Double.NEGATIVE_INFINITY;
    double b = Double.POSITIVE_INFINITY;
    double v = Double.NEGATIVE_INFINITY;

    Map<Position, Set<Position>> validMoves = board.getValidMoves(color);

    for (Position start : validMoves.keySet()) {
      for (Position end : validMoves.get(start)) {

        Move tempMove = new Move(start, end);

        // System.out.println(String.format("Looking at Move: %s, type %s",
        // tempMove.toString(), board.places().get(start).type()));
        Board newBoard = new Board(board);

        try {
          newBoard.processMove(start, end, promote(end));

        } catch (InvalidMoveException e) {

          e.printStackTrace();
        }
        double temp = alphaBetaCutoffMin(newBoard, cutoff - 1, a, b, heur,
            color);
        if (temp > v) {
          // System.out.println("hi");
          // System.out.println(temp);
          // System.out.println(v);
          bestMove = tempMove;
          v = temp;
          // System.out.println(String.format("new best move: %s with value %f",
          // bestMove.toString(), v));
        }
        if (v >= b) {
          trimmed++;
          new Move(start, end);
        }
        a = Math.max(a, temp);
      }
    }

    printBench();
    return bestMove;
  }

  private double alphaBetaCutoffMax(Board tempBoard, int ply, double a,
      double b, Heuristic heur, int currColor) {

    nodesSearched++;

    if (visitedBoards.contains(tempBoard)) {
      numRepeat++;
    }
    visitedBoards.add(tempBoard);

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
    for (Position start : validMoves.keySet()) {
      for (Position end : validMoves.get(start)) {
        Board newBoard = new Board(tempBoard);

        try {
          newBoard.processMove(start, end, new Queen(end, currColor));
        } catch (InvalidMoveException e) {
          e.printStackTrace();
        }
        v = Math.max(v, alphaBetaCutoffMin(newBoard, ply - 1, a, b, heur,
            Math.abs(currColor - 1)));
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

    nodesSearched++;

    if (visitedBoards.contains(tempBoard)) {
      numRepeat++;
    }
    visitedBoards.add(tempBoard);

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
    for (Position start : validMoves.keySet()) {
      for (Position end : validMoves.get(start)) {
        Board newBoard = new Board(tempBoard);

        try {
          newBoard.processMove(start, end, new Queen(end, currColor));
        } catch (InvalidMoveException e) {
          e.printStackTrace();
        }

        v = Math.min(v, alphaBetaCutoffMax(newBoard, ply - 1, a, b, heur,
            Math.abs(currColor - 1)));
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
    for (int i = 1; i < 4; i++) {
      result = alphaBetaCutoff(i, new VersionTwoHeuristic());
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

  @Override
  public void requestPiece(String type) {
    // TODO Auto-generated method stub
    
  }

}
