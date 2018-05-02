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
    if (players.size() == playerNum - 1) {
      players.add(p);
      startGame();
    } else if (players.size() < playerNum) {
      players.add(p);
    }
    return players.size() - 1;
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
        if (ChessWebSocket.playerSession.get(s) == p) {
          ChessWebSocket.games.put(s, g);
        }
      }
    }
    Thread t = new Thread(() -> g.play());
    t.start();
  }

}
