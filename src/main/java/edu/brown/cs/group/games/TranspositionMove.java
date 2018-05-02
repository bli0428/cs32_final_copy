package edu.brown.cs.group.games;

import edu.brown.cs.group.positions.Position;

public class TranspositionMove {
  private Position start;
  private Position end;
  private int iterDepth;
  // not used currently
  private int type;
  
  public TranspositionMove(Position start, Position end, int depth) {
    this.start = start;
    this.end = end;
    this.iterDepth = depth;
  }
  
  public Position getStart() {
    return start;
  }
  
  public Position getEnd() {
    return end;
  }
  
  public int getDepth() {
    return iterDepth;
  }
  
  @Override
  public String toString() {
    return String.format("start: %s, end: %s", start.numString(), end.numString());
  }
}
