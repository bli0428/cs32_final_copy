package edu.bown.cs.group.components;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import edu.brown.cs.group.components.Board;
import edu.brown.cs.group.components.InvalidMoveException;
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
}
