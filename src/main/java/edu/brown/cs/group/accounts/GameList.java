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
		String html = "";
		while (iter.hasNext()) {
			html += "<div class='col-md-3' style='margin-top: 2%'><div class='card'>"
					+ "<div class='card-header'><h3 class='card-title' style='margin-top: 0px;margin-bottom: 0px'>";
			MenuGame game = iter.next().getValue();
			html += game.getGameType();
			html += "</h3></div><div class='card-body'><p class='card-text' style='margin-top: 0px'>";
			html += "Current Players: " + game.getCurrPlayersSize() + "</p>";
			try {
				html += "<form method='GET' action='/joingame/" + java.net.URLEncoder.encode(game.getId() + "", "UTF-8")
						+ "'><input class='btn btn-primary' type='submit' value='Join Game'></form>";
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			html += "</div></div></div>";
		}
		return html;
	}
}
