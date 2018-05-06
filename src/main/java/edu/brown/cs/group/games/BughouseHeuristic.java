package edu.brown.cs.group.games;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.brown.cs.group.components.Board;
import edu.brown.cs.group.components.Piece;
import edu.brown.cs.group.components.PromotedPawn;
import edu.brown.cs.group.positions.Position;

public class BughouseHeuristic implements Heuristic {
  private int pieceSquareValue;
  private int[] requests;
  
  private int[][] pawnT = {
      {0,  0,  0,  0,  0,  0,  0,  0}, 
      {50, 50, 50, 50, 50, 50, 50, 50},
      {10, 10, 20, 30, 30, 20, 10, 10},
      {5,  5, 10, 25, 25, 10,  5,  5},
      {0,  0,  0, 20, 20,  0,  0,  0},
      {5, -5,-10,  0,  0,-10, -5,  5},
      {5, 10, 10,-20,-20, 10, 10,  5},
      { 0,  0,  0,  0,  0,  0,  0,  0}
  };
  
  private int[][] knightT = {
      {-50,-40,-30,-30,-30,-30,-40,-50}, 
      {-40,-20,  0,  0,  0,  0,-20,-40},
      {-30,  0, 10, 15, 15, 10,  0,-30},
      {-30,  5, 15, 20, 20, 15,  5,-30},
      {-30,  0, 15, 20, 20, 15,  0,-30},
      {-30,  5, 10, 15, 15, 10,  5,-30},
      {-40,-20,  0,  5,  5,  0,-20,-40},
      {-40,-20,  0,  5,  5,  0,-20,-40}
  };
  
  private int[][] bishopT = {
      {-20,-10,-10,-10,-10,-10,-10,-20},
      {-10,  0,  0,  0,  0,  0,  0,-10},
      {-10,  0,  5, 10, 10,  5,  0,-10},
      {-10,  5,  5, 10, 10,  5,  5,-10},
      {-10,  0, 10, 10, 10, 10,  0,-10},
      {-10, 10, 10, 10, 10, 10, 10,-10},
      {-10,  5,  0,  0,  0,  0,  5,-10},
      {-20,-10,-10,-10,-10,-10,-10,-20}
  };
  
  private int[][] rookT = {
     {0,  0,  0,  0,  0,  0,  0,  0},
     {5, 10, 10, 10, 10, 10, 10,  5},
     {-5,  0,  0,  0,  0,  0,  0, -5},
     {-5,  0,  0,  0,  0,  0,  0, -5},
     {-5,  0,  0,  0,  0,  0,  0, -5},
     {-5,  0,  0,  0,  0,  0,  0, -5},
     {-5,  0,  0,  0,  0,  0,  0, -5},
     {0,  0,  0,  5,  5,  0,  0,  0}
  };
  
  public BughouseHeuristic() {
    requests = new int[]{0,0,0,0,0};
  }
  
  public void addRequest(String type) {
    int index;
    switch (type) {
      case "q":
        index = 0;
        break;
      case "r":
        index = 1;
        break;
      case "b":
        index = 2;
        break;
      case "k":
        index = 3;
        break;
      case "p":
        index = 4;
        break;
      default:
        index = 0;
        break;
    }
    
    
  }
  
  
  private void pieceSquare(Piece piece, Position position) {
    if (piece.color() == 0) {
      String type;
      
      if (piece.type().equals("pp")) {
        type = ((PromotedPawn) piece).type();
      } else {
        type = piece.type();
      }
      
      switch (type) {
        case "p": 
          pieceSquareValue += pawnT[9-position.row()-1][9-position.col()-1];
          break;
        case "k":
          pieceSquareValue += knightT[9-position.row()-1][9-position.col()-1];
          break;
        case "b":
          pieceSquareValue += bishopT[9-position.row()-1][9-position.col()-1];
        case "r":
          pieceSquareValue += rookT[9-position.row()-1][9-position.col()-1];
        default:
          break;
      }
    }
    
    if (piece.color() == 1) {
      String type;
      
      if (piece.type().equals("pp")) {
        type = ((PromotedPawn) piece).type();
      } else {
        type = piece.type();
      }
      
      switch (type) {
        case "p": 
          pieceSquareValue -= pawnT[position.row()-1][position.col()-1];
          break;
        case "k":
          pieceSquareValue -= knightT[position.row()-1][position.col()-1];
          break;
        case "b":
          pieceSquareValue -= bishopT[position.row()-1][position.col()-1];
        case "r":
          pieceSquareValue -= rookT[position.row()-1][position.col()-1];
        default:
          break;
      }
    }
  }
  
  @Override
  public List<Double> evalBoard(Board board) {
    pieceSquareValue = 0;
    List<Double> resultVector = new ArrayList<>();
    resultVector.add(0.0);
    resultVector.add(0.0);
    

    Map<Position, Piece> places = board.places();

    for (Position key : places.keySet()) {
      Piece piece = places.get(key);
      pieceSquare(piece, key);
      
      resultVector.set(piece.color(),
          resultVector.get(piece.color()) + piece.value());
    }
    Double p1 = resultVector.get(0);
    Double p2 = resultVector.get(1);
    
    Double whiteVal = p1 - p2;
//    System.out.println(String.format("whiteVal is %f", whiteVal));
    whiteVal += pieceSquareValue;
//    System.out.println(String.format("pieceSquareValue is %d and new whiteVal is %f", pieceSquareValue, whiteVal));

    resultVector.set(0, whiteVal);
    resultVector.set(1, -whiteVal);
    return resultVector;
  }


}
