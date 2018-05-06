package edu.brown.cs.group.accounts;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

public class GameList {
  private ConcurrentHashMap<Integer, MenuGame> gameList;
  private int gameId;

  public GameList() {
    gameList = new ConcurrentHashMap<>();
  }
  
  synchronized public MenuGame getGame(Integer gameId) {
    return gameList.get(gameId);
  }
  
  synchronized public int addGame(int numPlayers) {
    gameId++;
    gameList.put(gameId, new MenuGame(gameId, numPlayers));
    return gameId;
  }
  
  synchronized public void removeGame(MenuGame game) {
    gameList.remove(game.getId());
  }
  
  public String printListHtml() {
    Iterator<Entry<Integer, MenuGame>> iter = gameList.entrySet().iterator();
    String html = "<ul>";
    while (iter.hasNext()) {
      MenuGame game = iter.next().getValue();
      html += "<li>";
      html += "Type: " + game.getGameType() + ", ";
      html += "Players: " + game.getCurrPlayersSize();
      try {
        html += "<form method='GET' action='/joingame/" + java.net.URLEncoder.encode(game.getId() + "", "UTF-8") + "'><input type='submit' value='Join'></form>";
      } catch (UnsupportedEncodingException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      html += "</li>";
    }
    html += "</ul>";
    return html;
  }
}
