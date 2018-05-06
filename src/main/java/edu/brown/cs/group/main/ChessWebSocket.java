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
import edu.brown.cs.group.games.WrapperGame;
import edu.brown.cs.group.positions.Position;
import edu.brown.cs.group.positions.PositionException;

@WebSocket
public class ChessWebSocket {
  public static final Gson GSON = new Gson();
  private static final Queue<Session> sessions = new ConcurrentLinkedQueue<>();
  public static final Map<Session, Game> games = new ConcurrentHashMap<Session, Game>();
  public static final Map<Player, Integer> playerNum = new ConcurrentHashMap<Player, Integer>();
  public static final Map<Session, GUIPlayer> playerSession = new ConcurrentHashMap<Session, GUIPlayer>();
  public static final Map<Integer, WrapperGame> lobbies = new ConcurrentHashMap<Integer, WrapperGame>();
  private static int nextId = 0;
  // private static int nextGame = 0;

  public static enum MESSAGE_TYPE {
    CONNECT, MOVE, PLACEMENT, UPDATE, GAMEOVER, PROMOTE, CREATEGAME, JOINGAME, HIGHLIGHT, TOHIGHLIGHT, TOPROMOTE, DISPLAY
  }

  private static final boolean[] WB = { false, true, false, true };

  @OnWebSocketConnect
  public void connected(Session session) throws IOException {
    System.out.println("backend connect");
    sessions.add(session);

    JsonObject payload = new JsonObject();
    payload.addProperty("id", nextId);

    JsonObject toSend = new JsonObject();
    toSend.addProperty("type", MESSAGE_TYPE.CONNECT.ordinal());
    toSend.add("payload", payload);

    // TODO: add black or white to payload

    session.getRemote().sendString(GSON.toJson(toSend));

    ////////////////////
    // // TODO: Replace this:
    // GUIPlayer p = new GUIPlayer();
    // playerSession.put(session, p);
    // ChessGame g = new ChessGame(p, new ABCutoffAI());
    // playerNum.put(p, 0);
    // games.put(session, g);
    // Thread t = new Thread((() -> g.play()));
    // t.start();
    // System.out.println("here");
    ////////////////////

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

    if (messageInt == MESSAGE_TYPE.MOVE.ordinal()) { // regular move from one

      System.out.println("recieved move");

      // square to another
      JsonObject recievedPayload = received.get("payload").getAsJsonObject();

      // TODO: create payloads and add properties
      GUIPlayer p = playerSession.get(session);
      String[] p1 = recievedPayload.get("moveFrom").getAsString().split(",");
      String[] p2 = recievedPayload.get("moveTo").getAsString().split(",");
      try {
        Position start = new Position(Integer.parseInt(p1[0]),
            Integer.parseInt(p1[1]));
        Position end = new Position(Integer.parseInt(p2[0]),
            Integer.parseInt(p2[1]));
        Move m = new Move(start, end);

        p.setMove(m);
        System.out.println("set move");
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
      System.out.println("recieved tohighlight");
      JsonObject recievedPayload = received.get("payload").getAsJsonObject();
      String piece = recievedPayload.get("piece").getAsString();
      String[] p1 = piece.split(",");
      try {
        Position start = new Position(Integer.parseInt(p1[0]),
            Integer.parseInt(p1[1]));
        Set<Position> moves = games.get(session)
            .moves(playerNum.get(playerSession.get(session)), start);
        System.out.println(games.get(session));
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
      // nextGame++;
    } else if (messageInt == MESSAGE_TYPE.JOINGAME.ordinal()) {
      System.out.println("Joining game");
      JsonObject recievedPayload = received.get("payload").getAsJsonObject();
      int id = recievedPayload.get("id").getAsInt();
      if (!lobbies.containsKey(id) && id != 99) {
        lobbies.put(id,
            new WrapperGame(chessOrBug(JoinWebSocket.gameTypes.get(id))));
      }

      GUIPlayer p = new GUIPlayer();
      playerSession.put(session, p);
      if (id == 99) {
        ChessGame g = new ChessGame(p, new ABCutoffAI());
        playerNum.put(p, 0);
        games.put(session, g);
        Thread t = new Thread((() -> g.play()));
        t.start();
        JsonObject msg = new JsonObject();
        msg.addProperty("type", MESSAGE_TYPE.DISPLAY.ordinal());
        JsonObject displayPayload = new JsonObject();
        displayPayload.addProperty("color", 0);
        msg.add("payload", displayPayload);
        session.getRemote().sendString(GSON.toJson(msg));
      } else {
        int pid = lobbies.get(id).addPlayer(p);
        playerNum.put(p, pid);
        JsonObject msg = new JsonObject();
        msg.addProperty("type", MESSAGE_TYPE.DISPLAY.ordinal());
        JsonObject displayPayload = new JsonObject();
        displayPayload.addProperty("color", WB[pid]);

        msg.add("payload", displayPayload);
        session.getRemote().sendString(GSON.toJson(msg));
      }
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

  public boolean chessOrBug(String in) {
    if (in.equals("Chess"))
      return true;
    if (in.equals("Bughouse")) {
      return false;
    }

    return false;
  }

}
