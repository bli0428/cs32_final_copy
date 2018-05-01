package edu.brown.cs.group.games;

import java.util.Set;

import edu.brown.cs.group.positions.Position;

public interface Game {
  void play();

  Set<Position> moves(int player, Position pos);
}
