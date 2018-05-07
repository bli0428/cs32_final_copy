package edu.brown.cs.group.games;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.jetty.websocket.api.Session;

import edu.brown.cs.group.main.ChessWebSocket;

public class WrapperGame {
  private boolean chess;
  private List<Player> players;
  private int playerNum;

  public WrapperGame(boolean chess) {
    this.chess = chess;
    players = Collections.synchronizedList(new ArrayList<Player>());
    if (chess) {
      playerNum = 2;
    } else {
      playerNum = 4;
    }
  }

  public synchronized int addPlayer(Player p) {
    while (players.size() < playerNum) {
      players.add(null);
    }
    int out = players.size();
    if (out == playerNum - 1) {
      players.add(p);
      startGame();
    } else if (out < playerNum) {
      players.add(p);
    }
    return players.size() - 1;
  }

  public synchronized int addPlayer(Player p, int pos) {
    while (players.size() < playerNum) {
      players.add(null);
    }
    Player ai = players.get(pos);
    players.set(pos, p);
    for (int i = 0; i < playerNum; i++) {
      if (players.get(i) == null) {
        players.set(i, ai);
        break;
      }
    }
    boolean start = true;
    for (int i = 0; i < playerNum; i++) {
      if (players.get(i) == null) {
        start = false;
      }
    }
    if (start)
      startGame();
    return pos;
  }

  public void startGame() {
    Game g;
    if (chess) {
      g = new ChessGame(players.get(0), players.get(1));
    } else {
      g = new BughouseGame(players.get(0), players.get(1), players.get(2),
          players.get(3));
    }
    for (Player p : players) {
      for (Session s : ChessWebSocket.playerSession.keySet()) {
        if (ChessWebSocket.playerSession.get(s).equals(p)) {
          // System.out.println("added game");
          ChessWebSocket.games.put(s, g);
        }
      }
    }
    Thread t = new Thread(() -> g.play());
    t.start();
  }

  public void makeRequest(Player p, String type) {
    int ind = players.indexOf(p);
    players.get(partner(ind)).requestPiece(type);
  }

  public boolean full() {
    return players.contains(null);
    /*
     * for (Player p : players) { if (p.equals(null)) return false; } return
     * true;
     */
  }

  public void removePlayer(int idx) {
    players.set(idx, null);
  }

  public int partner(int p) {
    if (p == 0)
      return 1;
    if (p == 1)
      return 0;
    if (p == 2)
      return 3;
    if (p == 4)
      return 2;
    return -1;
  }

}
