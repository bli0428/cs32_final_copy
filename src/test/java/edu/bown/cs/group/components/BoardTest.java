package edu.bown.cs.group.components;

import static org.junit.Assert.assertTrue;

import java.util.HashMap;

import org.junit.Test;

import edu.brown.cs.group.components.Bishop;
import edu.brown.cs.group.components.Board;
import edu.brown.cs.group.components.InvalidMoveException;
import edu.brown.cs.group.components.King;
import edu.brown.cs.group.components.Piece;
import edu.brown.cs.group.components.Queen;
import edu.brown.cs.group.games.ReplPlayer;
import edu.brown.cs.group.positions.Position;
import edu.brown.cs.group.positions.PositionException;

public class BoardTest {
  @Test
  public void testEquality() {
    try {
      Board b1 = new Board(new ReplPlayer(), new ReplPlayer());
      Board b2 = new Board(new ReplPlayer(), new ReplPlayer());
      
      assertTrue(b1.equals(b2));
      
      b1.processMove(new Position(2,2), new Position(2,4), false);
      assertTrue(!b1.equals(b2));
      b2.processMove(new Position(2,2), new Position(2,4), false);
      assertTrue(b1.equals(b2));
    } catch (PositionException | InvalidMoveException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
  
  @Test
  public void testGameOver() {
    
    try {
      HashMap<Position, Piece> temp = new HashMap<>();
      Position p1 = new Position(2,8);
      Piece k1 = new King(p1, 1);
      temp.put(p1, k1);
      
      Position p2 = new Position(4,2);
      Piece k2 = new King(p2, 0);
      temp.put(p2, k2);
      
      Position p3 = new Position(4,5);
      Piece bishop = new Bishop(p3, 0);
      temp.put(p3, bishop);
      Board b1 = new Board(temp, new ReplPlayer(), new ReplPlayer());
      System.out.println(b1);
      System.out.println(b1.getValidMoves(1));
      
    } catch (PositionException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    
  }
  
  @Test
  public void testBishopThreaten() {
    try {
      HashMap<Position, Piece> temp = new HashMap<>();
      Position bishopPos = new Position(6,8);
      Piece bishop = new Queen(bishopPos, 1);
      
      temp.put(bishopPos, bishop);
      Board b1 = new Board(temp, new ReplPlayer(), new ReplPlayer());
      System.out.println(b1.places().size());
      System.out.println(bishop.threatens(b1).size());
    } catch (PositionException e) {
      e.printStackTrace();
    }
  }
}
