package edu.brown.cs.group.main;

import java.io.IOException;
import java.net.HttpCookie;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import edu.brown.cs.group.accounts.MenuGame;
import edu.brown.cs.group.accounts.User;
import edu.brown.cs.group.games.ABCutoffAI;
import edu.brown.cs.group.games.ChessGame;
import edu.brown.cs.group.games.GUIPlayer;
import edu.brown.cs.group.games.Game;
import edu.brown.cs.group.games.Move;
import edu.brown.cs.group.games.Player;
import edu.brown.cs.group.positions.Position;
import edu.brown.cs.group.positions.PositionException;

@WebSocket
public class JoinWebSocket {
  public static final Gson GSON = new Gson();
  private static final Queue<Session> sessions = new ConcurrentLinkedQueue<>();
  
  private static int nextId = 0;
  private static int nextGame = 0;

  public static enum MESSAGE_TYPE {
    CONNECT, UPDATE, JOIN_USER
  }

  @OnWebSocketConnect
  public void connected(Session session) throws IOException {
    System.out.println("backend connect");
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
