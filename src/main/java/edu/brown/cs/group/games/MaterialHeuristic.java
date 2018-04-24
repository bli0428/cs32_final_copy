package edu.brown.cs.group.games;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.brown.cs.group.components.Board;
import edu.brown.cs.group.components.Piece;
import edu.brown.cs.group.positions.Position;

/**
 * Represents a simple Heuristic that generates the difference between the sum
 * of the players pieces' values.
 *
 * @author Brian
 *
 */
public class MaterialHeuristic implements Heuristic {

  @Override
  public List<Double> evalBoard(Board board) {
    List<Double> resultVector = new ArrayList<>();
    resultVector.add(0.0);
    resultVector.add(0.0);

    Map<Position, Piece> places = board.places();

    for (Position key : places.keySet()) {
      Piece piece = places.get(key);
      resultVector.set(piece.color(),
          resultVector.get(piece.color()) + piece.value());
    }
    Double p1 = resultVector.get(0);
    Double p2 = resultVector.get(1);

    resultVector.set(0, p1 - p2);
    resultVector.set(1, p2 - p1);
    return resultVector;
  }

}
