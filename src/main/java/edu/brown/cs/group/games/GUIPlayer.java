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
    moves.add(null);
    moves.add(null);
    toPromote.add(null);
    toPromote.add(null);
  }

  @Override
  public Set<Piece> bank() {
    return bank;
  }

  public synchronized void setMove(Move move) {
    moves.set(0, move);
    notify();
  }

  @Override
  public synchronized Move move() {
    // System.out.println("Started move");
    try {
      // System.out.println("Try block");
      wait();
      // System.out.println("After wait");
    } catch (InterruptedException e) {
      System.out.println("SHIT");
      try {
        if (moves.get(0) == moves.get(1)) {
          return move();
        }
      } catch (NullPointerException npe) {
        npe.printStackTrace();
      }
      return moves.get(0);
    }
    // System.out.println("exited try");
    moves.set(1, moves.get(0));
    return moves.get(0);
  }

  @Override
  public void setBoard(Board board) {
    this.board = board;
  }

  @Override
  public synchronized Piece promote(Position p) {
    try {
      wait();
    } catch (InterruptedException e) {
      try {
        if (toPromote.get(0) == toPromote.get(1)) {
          return promote(p);
        }
      } catch (NullPointerException npe) {
        npe.printStackTrace();
      }
      return toPromote.get(0);
    }
    toPromote.set(1, toPromote.get(0));
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
