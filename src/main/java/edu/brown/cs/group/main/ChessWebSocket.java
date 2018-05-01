package edu.brown.cs.group.main;

import java.io.IOException;
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

import edu.brown.cs.group.games.ABCutoffAI;
import edu.brown.cs.group.games.ChessGame;
import edu.brown.cs.group.games.GUIPlayer;
import edu.brown.cs.group.games.Game;
import edu.brown.cs.group.games.Move;
import edu.brown.cs.group.games.Player;
import edu.brown.cs.group.positions.Position;
import edu.brown.cs.group.positions.PositionException;

@WebSocket
public class ChessWebSocket {
  private static final Gson GSON = new Gson();
  private static final Queue<Session> sessions = new ConcurrentLinkedQueue<>();
  private static final Map<Session, Game> games = new ConcurrentHashMap<Session, Game>();
  private static final Map<Player, Integer> playerNum = new ConcurrentHashMap<Player, Integer>();
  private static final Map<Session, GUIPlayer> playerSession = new ConcurrentHashMap<Session, GUIPlayer>();
  private static int nextId = 0;
  private static int nextGame = 0;

  private static enum MESSAGE_TYPE {
    CONNECT, MOVE, PLACEMENT, UPDATE, GAMEOVER, PROMOTE, CREATEGAME, JOINGAME, HIGHLIGHT, TOHIGHLIGHT
  }

  @OnWebSocketConnect
  public void connected(Session session) throws IOException {
    sessions.add(session);

    JsonObject payload = new JsonObject();
    payload.addProperty("id", nextId);

    JsonObject toSend = new JsonObject();
    toSend.addProperty("type", MESSAGE_TYPE.CONNECT.ordinal());
    toSend.add("payload", payload);

    // TODO: add black or white to payload

    session.getRemote().sendString(GSON.toJson(toSend));

    ////////////////////
    // TODO: Replace this:
    GUIPlayer p = new GUIPlayer();
    playerSession.put(session, p);
    try {
      ChessGame g = new ChessGame(p, new ABCutoffAI());
      playerNum.put(p, 0);
      games.put(session, g);
      g.play();
    } catch (PositionException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    ////////////////////

    nextId++;
  }

  @OnWebSocketClose
  public void closed(Session session, int statusCode, String reason) {
    sessions.remove(session);
  }

  @OnWebSocketMessage
  public void message(Session session, String message) throws IOException {
    JsonObject received = GSON.fromJson(message, JsonObject.class);

    int messageInt = received.get("type").getAsInt();

    if (messageInt == MESSAGE_TYPE.MOVE.ordinal()) { // regular move from one
                                                     // square to another
      JsonObject recievedPayload = received.get("payload").getAsJsonObject();
      // TODO: create payloads and add properties
      GUIPlayer p = playerSession.get(session);
      String[] p1 = recievedPayload.get("ogCoordinate").getAsString()
          .split(",");
      String[] p2 = recievedPayload.get("newCoordinate").getAsString()
          .split(",");
      try {
        Position start = new Position(Integer.parseInt(p1[0]),
            Integer.parseInt(p1[1]));
        Position end = new Position(Integer.parseInt(p2[0]),
            Integer.parseInt(p2[1]));
        Move m = new Move(start, end);
        p.setMove(m);
      } catch (PositionException pe) {
        pe.printStackTrace();
        // Shouldn't get here
      }

    } else if (messageInt == MESSAGE_TYPE.PLACEMENT.ordinal()) { // placement
                                                                 // move from
                                                                 // the bank
                                                                 // onto the
                                                                 // board
      JsonObject recievedPayload = received.get("payload").getAsJsonObject();
      // TODO: create payloads and add properties

    } else if (messageInt == MESSAGE_TYPE.TOHIGHLIGHT.ordinal()) {
      JsonObject recievedPayload = received.get("payload").getAsJsonObject();
      String piece = recievedPayload.get("piece").getAsString();
      String[] p1 = piece.split(",");
      try {
        Position start = new Position(Integer.parseInt(p1[0]),
            Integer.parseInt(p1[1]));
        Set<Position> moves = games.get(session)
            .moves(playerNum.get(playerSession.get(session)), start);
        JsonArray outMoves = new JsonArray();
        for (Position p : moves) {
          outMoves.add(p.numString());
        }
        JsonObject payload = new JsonObject();
        payload.add("validMoves", outMoves);
        payload.addProperty("id", recievedPayload.get("id").getAsInt());
        JsonObject out = new JsonObject();
        out.add("payload", payload);
        out.addProperty("type", MESSAGE_TYPE.HIGHLIGHT.ordinal());
        session.getRemote().sendString(GSON.toJson(out));
      } catch (PositionException pe) {
        pe.printStackTrace();
        // Shouldn't get here
      }

    } else if (messageInt == MESSAGE_TYPE.CREATEGAME.ordinal()) {
      nextGame++;
    }

    // TODO: update payload needs to send if a piece was removed in the move

    // JsonObject payload = new JsonObject();
    // payload.addProperty("id", recievedPayload.get("id").getAsString());
    //
    //
    // JsonObject toSend = new JsonObject();
    // toSend.addProperty("type", MESSAGE_TYPE.UPDATE.ordinal());
    // toSend.add("payload", payload);
    //
    // for (Session k : sessions) {
    // k.getRemote().sendString(GSON.toJson(toSend));
    // }

  }

}
