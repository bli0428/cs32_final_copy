package edu.brown.cs.group.games;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.brown.cs.group.components.Board;
import edu.brown.cs.group.components.Piece;

import edu.brown.cs.group.games.Move;
import edu.brown.cs.group.games.Player;

import edu.brown.cs.group.positions.Position;

public class GUIPlayer implements Player {

  private Set<Piece> bank;
  private Board board;
  private List<Move> moves;
  private List<Piece> toPromote;
  private int color;
  private int id;

  public GUIPlayer() {
    bank = Collections.synchronizedSet(new HashSet<Piece>());
    moves = Collections.synchronizedList(new ArrayList<Move>());
    toPromote = Collections.synchronizedList(new ArrayList<Piece>());
  }

  @Override
  public Set<Piece> bank() {
    return bank;
  }

  public void setMove(Move move) {
    moves.set(0, move);
    moves.notifyAll();
  }

  @Override
  public Move move() {
    try {
      moves.wait();
    } catch (InterruptedException e) {
      try {
        if (moves.get(0) == moves.get(1)) {
          return move();
        }
      } catch (NullPointerException npe) {
      }
      return moves.get(0);
    }
    moves.set(1, moves.get(0));
    return moves.get(0);
  }

  @Override
  public void setBoard(Board board) {
    this.board = board;
  }

  @Override
  public Piece promote(Position p) {
    try {
      moves.wait();
    } catch (InterruptedException e) {
      try {
        if (moves.get(0) == moves.get(1)) {
          return promote(p);
        }
      } catch (NullPointerException npe) {
      }
      return toPromote.get(0);
    }
    moves.set(1, moves.get(0));
    return toPromote.get(0);
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
