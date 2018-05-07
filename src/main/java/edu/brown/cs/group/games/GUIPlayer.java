package edu.brown.cs.group.games;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.eclipse.jetty.websocket.api.Session;

import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonObject;

import edu.brown.cs.group.components.Board;
import edu.brown.cs.group.components.Piece;
import edu.brown.cs.group.games.Move;
import edu.brown.cs.group.games.Player;
import edu.brown.cs.group.main.ChessWebSocket;
import edu.brown.cs.group.positions.Position;

public class GUIPlayer implements Player {

  private static final Map<String, Integer> bankIdx = ImmutableMap.of("p", 0,
      "r", 1, "k", 2, "b", 3, "q", 4);

  private List<Piece> bank;
  private Board board;
  private List<Move> moves;
  private List<Piece> toPromote;
  private int color;
  private int id;

  public GUIPlayer() {
    bank = Collections.synchronizedList(new ArrayList<Piece>());
    moves = Collections.synchronizedList(new ArrayList<Move>());
    toPromote = Collections.synchronizedList(new ArrayList<Piece>());
    moves.add(null);
    moves.add(null);
    toPromote.add(null);
    toPromote.add(null);
  }

  @Override
  public List<Piece> bank() {
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
      for (Session s : ChessWebSocket.playerSession.keySet()) {
        if (ChessWebSocket.playerSession.get(s).equals(this)) {
          JsonObject message = new JsonObject();
          message.addProperty("type",
              ChessWebSocket.MESSAGE_TYPE.PROMOTE.ordinal());
          JsonObject payload = new JsonObject();
          payload.addProperty("position", p.numString());
          message.add("payload", payload);
          s.getRemote().sendString(ChessWebSocket.GSON.toJson(message));
        }
      }
      System.out.println("promote sent");
      wait();
      System.out.println("promote recieved");
    } catch (InterruptedException e) {
      try {
        if (toPromote.get(0) == toPromote.get(1)) {
          return promote(p);
        }
      } catch (NullPointerException npe) {
        npe.printStackTrace();
      }
      return toPromote.get(0);
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    toPromote.set(1, toPromote.get(0));
    return toPromote.get(0);
  }

  public synchronized void setPromote(Piece p) {
    toPromote.set(0, p);
    notify();
  }

  @Override
  public void acceptPiece(Piece p) {
    JsonObject message = new JsonObject();
    message.addProperty("type", ChessWebSocket.MESSAGE_TYPE.BANKADD.ordinal());
    JsonObject payload = new JsonObject();
    payload.addProperty("idx", bankIdx.get(p.type()));
    message.add("payload", payload);
    for (Session s : ChessWebSocket.playerSession.keySet()) {
      if (ChessWebSocket.playerSession.get(s).equals(this)) {
        try {
          s.getRemote().sendString(ChessWebSocket.GSON.toJson(message));
          System.out.println("Accepted " + p.type());
        } catch (IOException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
      }
    }
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

  public void place(String s, Position pos) {
    Piece temp = null;
    for (Piece p : bank) {
      if (p.type().equals(s)) {
        temp = p;
        break;
      }
    }
    if (temp != null) {
      bank.remove(temp);
      Move m = new Move(pos, temp);
      setMove(m);
    }
  }

  @Override
  public void requestPiece(String type) {
    // TODO Auto-generated method stub
    for (Session s : ChessWebSocket.playerSession.keySet()) {
      if (ChessWebSocket.playerSession.get(s).equals(this)) {
        JsonObject message = new JsonObject();
        message.addProperty("type", ChessWebSocket.MESSAGE_TYPE.BOOP.ordinal());
        JsonObject payload = new JsonObject();
        payload.addProperty("piece", type);
        message.add("payload", payload);
        try {
          s.getRemote().sendString(ChessWebSocket.GSON.toJson(message));
        } catch (IOException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
      }
    }
  }

}
