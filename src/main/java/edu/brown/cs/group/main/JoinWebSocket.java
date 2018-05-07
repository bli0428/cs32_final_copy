package edu.brown.cs.group.main;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import edu.brown.cs.group.accounts.MenuGame;
import edu.brown.cs.group.accounts.User;
import edu.brown.cs.group.games.ABCutoffAIV2;
import edu.brown.cs.group.games.WrapperGame;

@WebSocket
public class JoinWebSocket {

  public static final Gson GSON = new Gson();
  private static final Queue<Session> sessions = new ConcurrentLinkedQueue<>();

  public static final Map<Integer, String> gameTypes = new ConcurrentHashMap<Integer, String>();

  private static int nextId = 0;
  private static int nextGame = 0;

  public static enum MESSAGE_TYPE {
    CONNECT, UPDATE, JOIN_USER, START_CHESS_GAME, START_BUGHOUSE_GAME, SWITCH_TEAM, ADD_AI, LEAVE_GAME
  }

  @OnWebSocketConnect
  public void connected(Session session) throws IOException {
    // System.out.println("backend connect");
    sessions.add(session);

    JsonObject payload = new JsonObject();
    payload.addProperty("id", nextId);

    JsonObject toSend = new JsonObject();
    toSend.addProperty("type", MESSAGE_TYPE.CONNECT.ordinal());
    toSend.add("payload", payload);

    session.getRemote().sendString(GSON.toJson(toSend));
    nextId++;
    // System.out.println("here");
  }

  @OnWebSocketClose
  public void closed(Session session, int statusCode, String reason) {
    sessions.remove(session);
  }

  @OnWebSocketMessage
  public void message(Session session, String message) throws IOException {
    // System.out.println("in message");
    JsonObject received = GSON.fromJson(message, JsonObject.class);

    int messageInt = received.get("type").getAsInt();

    if (messageInt == MESSAGE_TYPE.UPDATE.ordinal()) {
      JsonObject receivedPayload = received.get("payload").getAsJsonObject();
      // TODO: create payloads and add properties

    } else if (messageInt == MESSAGE_TYPE.JOIN_USER.ordinal()) {
      // System.out.println("in join user");
      JsonObject receivedPayload = received.get("payload").getAsJsonObject();
      // System.out.println(receivedPayload.get("sparkSession").getAsString());
      int gameId = receivedPayload.get("gameId").getAsInt();
      MenuGame g = GUI.GAME_LIST.getGame(gameId);
      gameTypes.put(g.getId(), g.getGameType());

      addNextSession(GUI.GAME_ID_TO_SESSIONS.get(gameId), session);

      checkForStartGame(g, gameId);
    } else if (messageInt == MESSAGE_TYPE.SWITCH_TEAM.ordinal()) {
      System.out.println("in switch team");
      JsonObject receivedPayload = received.get("payload").getAsJsonObject();
      // System.out.println(receivedPayload.get("sparkSession").getAsString());
      int gameId = receivedPayload.get("gameId").getAsInt();
      MenuGame g = GUI.GAME_LIST.getGame(gameId);

      User[] users = g.getCurrPlayers();
      if (g.getGameType().equals("Chess")) {
        switchUsers(0, 1, users, gameId);
      } else if (g.getGameType().equals("Bughouse")) {
        for (int i = 0; i < users.length; i++) {
          User u = users[i];
          if (u != null) {
            if (u.getUserId() == receivedPayload.get("userId").getAsInt()) {
              if (i == 0 || i == 1) {
                if (users[2] == null) {
                  switchUsers(i, 2, users, gameId);
                  break;
                } else if (users[3] == null) {
                  switchUsers(i, 3, users, gameId);
                  break;
                }
              } else if (i == 2 || i == 3) {
                if (users[0] == null) {
                  switchUsers(i, 0, users, gameId);
                  break;
                } else if (users[1] == null) {
                  switchUsers(i, 1, users, gameId);
                  break;
                }
              }
            }
          }
        }
      }
      sendUpdate(g, gameId);
    } else if (messageInt == MESSAGE_TYPE.ADD_AI.ordinal()) {
      // System.out.println("in add AI");
      JsonObject receivedPayload = received.get("payload").getAsJsonObject();
      int gameId = receivedPayload.get("gameId").getAsInt();
      MenuGame g = GUI.GAME_LIST.getGame(gameId);
      g.addUser(new User(-1, "AI Player"));

      if (!ChessWebSocket.lobbies.keySet().contains(gameId)) {
        ChessWebSocket.lobbies.put(gameId,
            new WrapperGame(g.getGameType().equals("Chess")));
      }

      ChessWebSocket.lobbies.get(gameId).addPlayer(new ABCutoffAIV2(4));

      // addNextSession(GUI.GAME_ID_TO_SESSIONS.get(gameId), session);

      checkForStartGame(g, gameId);
    } else if (messageInt == MESSAGE_TYPE.LEAVE_GAME.ordinal()) {
      System.out.println("in leave game");
      JsonObject receivedPayload = received.get("payload").getAsJsonObject();
      int gameId = receivedPayload.get("gameId").getAsInt();
      int userId = receivedPayload.get("userId").getAsInt();
      MenuGame g = GUI.GAME_LIST.getGame(gameId);
      if (g != null) {
        g.removeUser(userId);
      }

      removeSession(GUI.GAME_ID_TO_SESSIONS.get(gameId), session);

      sendUpdate(g, gameId);
    }
  }
  
  private void sendUpdate(MenuGame g, int gameId) throws IOException {
    JsonObject payload = new JsonObject();
    payload.addProperty("list", menuGameToUsersHtml(g));

    JsonObject toSend = new JsonObject();
    toSend.addProperty("type", MESSAGE_TYPE.UPDATE.ordinal());
    toSend.add("payload", payload);

    Session[] sessions = GUI.GAME_ID_TO_SESSIONS.get(gameId);
    for (Session s : sessions) {
      if (s != null) {
        s.getRemote().sendString(GSON.toJson(toSend));
      }
    }
  }
  
  private void addNextSession(Session[] sessions, Session s) {
    for (int i = 0; i < sessions.length; i++) {
      Session session = sessions[i];
      if (session == null) {
        sessions[i] = s;
        return;
      }
    }
  }
  
  private void removeSession(Session[] sessions, Session s) {
    for (int i = 0; i < sessions.length; i++) {
      Session session = sessions[i];
      if (s.equals(session)) {
        sessions[i] = null;
        return;
      }
    }
  }
  
  private void checkForStartGame(MenuGame g, int gameId) throws IOException {
    JsonObject payload = new JsonObject();
    payload.addProperty("list", menuGameToUsersHtml(g));

    JsonObject toSend = new JsonObject();
    toSend.addProperty("type", MESSAGE_TYPE.UPDATE.ordinal());
    toSend.add("payload", payload);

    Session[] sessions = GUI.GAME_ID_TO_SESSIONS.get(gameId);
    for (Session s : sessions) {
      if (s != null) {
        s.getRemote().sendString(GSON.toJson(toSend));
      }
    }
    
    if (g.getGameType().equals("Chess") && g.getCurrPlayersSize() == 2) {
      // toSend.addProperty("type", MESSAGE_TYPE.START_CHESS_GAME.ordinal());
      // toSend.add("payload", payload);

      for (int i = 0; i < sessions.length; i++) {
        Session s = sessions[i];
        if (s != null) {
          JsonObject toSendB = new JsonObject();
          toSendB.addProperty("type", MESSAGE_TYPE.START_CHESS_GAME.ordinal());
          JsonObject payloadB = new JsonObject();
          payloadB.addProperty("gamePosition", i);
          toSendB.add("payload", payloadB);
          s.getRemote().sendString(GSON.toJson(toSendB));
        }
      }
      GUI.GAME_LIST.removeGame(g);
    } else if (g.getGameType().equals("Bughouse")
        && g.getCurrPlayersSize() == 4) {
      for (int i = 0; i < sessions.length; i++) {
        Session s = sessions[i];
        if (s != null) {
          JsonObject toSendB = new JsonObject();
          toSendB.addProperty("type",
              MESSAGE_TYPE.START_BUGHOUSE_GAME.ordinal());
          JsonObject payloadB = new JsonObject();
          payloadB.addProperty("gamePosition", i);
          toSendB.add("payload", payloadB);
          s.getRemote().sendString(GSON.toJson(toSendB));
        }
      }
      GUI.GAME_LIST.removeGame(g);
    }
  }
  
  private void switchUsers(int index1, int index2, User[] users, int gameId) {
    User u = users[index1];
    users[index1] = users[index2];
    users[index2] = u;
    Session[] sessions = GUI.GAME_ID_TO_SESSIONS.get(gameId);
    Session s = sessions[index1];
    sessions[index1] = sessions[index2];
    sessions[index2] = s;
  }

  private String menuGameToUsersHtml(MenuGame g) {
		User[] users = g.getCurrPlayers();
		String html = "";
		for (int i = 0; i < users.length; i++) {
			html += "<div class='col' style='margin-top: 2%'><div class='card text-center'>"
					+ "<div class='card-body'><h2 class='card-title' style='margin-top:0px'>" + colorPicker(i) + "</h2>";
			if (users[i] == null) {
				html += "<p class='card-text'>Waiting for Player...</p><button class='btn btn-info'"
						+ "onclick='addAI(" + i + ")'>Add AI Player</button>";
			} else if (users[i].getUsername().equals("AI Player")) {
				html += "<p class='card-text'>" + users[i].getUsername() + "</p><button class='btn btn-info'"
						+ "onclick='removeAI()'>Remove AI</button>";
			} else {
				html += "<p class='card-text'>" + users[i].getUsername() + "</p><button class='btn btn-info'"
						+ "onclick='switchTeam()'>Switch Team</button>";
			}
			html += "</div></div></div>";
		}
		return html;
	}
	
	private String colorPicker(int i) {
		if (i == 0 || i == 3) {
			return "White";
		} else if (i == 1 || i ==2){
			return "Black";
		} else {
			return null;
		}
	}

}
