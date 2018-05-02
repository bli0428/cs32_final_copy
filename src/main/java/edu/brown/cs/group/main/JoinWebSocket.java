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

@WebSocket
public class JoinWebSocket {
  public static final Gson GSON = new Gson();
  private static final Queue<Session> sessions = new ConcurrentLinkedQueue<>();

  public static final Map<Integer, String> gameTypes = new ConcurrentHashMap<Integer, String>();

  private static int nextId = 0;
  private static int nextGame = 0;

  public static enum MESSAGE_TYPE {
    CONNECT, UPDATE, JOIN_USER, START_CHESS_GAME, START_BUGHOUSE_GAME
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
    System.out.println("here");
  }

  @OnWebSocketClose
  public void closed(Session session, int statusCode, String reason) {
    sessions.remove(session);
  }

  @OnWebSocketMessage
  public void message(Session session, String message) throws IOException {
    System.out.println("in message");
    JsonObject received = GSON.fromJson(message, JsonObject.class);

    int messageInt = received.get("type").getAsInt();

    if (messageInt == MESSAGE_TYPE.UPDATE.ordinal()) {
      JsonObject receivedPayload = received.get("payload").getAsJsonObject();
      // TODO: create payloads and add properties

    } else if (messageInt == MESSAGE_TYPE.JOIN_USER.ordinal()) {
      System.out.println("in join user");
      JsonObject receivedPayload = received.get("payload").getAsJsonObject();
      System.out.println(receivedPayload.get("sparkSession").getAsString());
      int gameId = receivedPayload.get("gameId").getAsInt();
      MenuGame g = GUI.GAME_LIST.getGame(gameId);
      gameTypes.put(g.getId(), g.getGameType());
      System.out.println(menuGameToUsersHtml(g));

      GUI.GAME_ID_TO_SESSIONS.get(gameId).add(session);

      JsonObject payload = new JsonObject();
      payload.addProperty("list", menuGameToUsersHtml(g));

      JsonObject toSend = new JsonObject();
      toSend.addProperty("type", MESSAGE_TYPE.UPDATE.ordinal());
      toSend.add("payload", payload);

      List<Session> sessions = GUI.GAME_ID_TO_SESSIONS.get(gameId);
      for (Session s : sessions) {
        s.getRemote().sendString(GSON.toJson(toSend));
      }

      if (g.getGameType().equals("Chess") && g.getCurrPlayers().size() == 2) {
        toSend.addProperty("type", MESSAGE_TYPE.START_CHESS_GAME.ordinal());
        toSend.add("payload", payload);

        for (Session s : sessions) {
          s.getRemote().sendString(GSON.toJson(toSend));
        }
        GUI.GAME_LIST.removeGame(g);
      } else if (g.getGameType().equals("Bughouse")
          && g.getCurrPlayers().size() == 4) {
        toSend.addProperty("type", MESSAGE_TYPE.START_BUGHOUSE_GAME.ordinal());
        toSend.add("payload", payload);

        for (Session s : sessions) {
          s.getRemote().sendString(GSON.toJson(toSend));
        }
        GUI.GAME_LIST.removeGame(g);
      }
    }

  }

  private String menuGameToUsersHtml(MenuGame g) {
    List<User> users = g.getCurrPlayers();
    String html = "<ul>";
    for (int i = 0; i < users.size(); i++) {
      html += "<li>" + users.get(i).getUserId() + "</li>";
    }
    html += "</ul>";
    return html;
  }

}
