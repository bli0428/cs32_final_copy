package main.java.edu.brown.cs.group.games;

import java.util.List;

import main.java.edu.brown.cs.group.components.Board;

/**
 * Represents a Heuristic function that takes in a board and evaluates the
 * state.
 *
 * @author Brian
 *
 */
@FunctionalInterface
public interface Heuristic {
  /**
   * Gives the evaluation of the current board. Should output List in form of
   * [value of player 1, value of player 2, value of player i, ...].
   *
   * @param board
   *          The board to evaluate.
   * @return The value vector.
   */
  List<Double> evalBoard(Board board);
}
