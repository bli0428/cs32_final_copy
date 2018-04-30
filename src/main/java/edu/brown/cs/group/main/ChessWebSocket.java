package edu.brown.cs.group.main;

import java.io.IOException;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import edu.brown.cs.group.components.Board;

@WebSocket
public class ChessWebSocket {
	private static final Gson GSON = new Gson();
	private static final Queue<Session> sessions = new ConcurrentLinkedQueue<>();
	private static int nextId = 0;

	private static enum MESSAGE_TYPE {
		CONNECT, MOVE, PLACEMENT, UPDATE
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
		
		if (messageInt == MESSAGE_TYPE.MOVE.ordinal()) { //regular move from one square to another
			JsonObject recievedPayload = received.get("payload").getAsJsonObject();
			//TODO: create payloads and add properties
		
		} else if (messageInt == MESSAGE_TYPE.PLACEMENT.ordinal()) { //placement move from the bank onto the board
			JsonObject recievedPayload = received.get("payload").getAsJsonObject();
			//TODO: create payloads and add properties
			
		}

		//TODO: update payload needs to send if a piece was removed in the move

//		JsonObject payload = new JsonObject();
//		payload.addProperty("id", recievedPayload.get("id").getAsString());
//	
//
//		JsonObject toSend = new JsonObject();
//		toSend.addProperty("type", MESSAGE_TYPE.UPDATE.ordinal());
//		toSend.add("payload", payload);
//
//		for (Session k : sessions) {
//			k.getRemote().sendString(GSON.toJson(toSend));
//		}

	}

}
