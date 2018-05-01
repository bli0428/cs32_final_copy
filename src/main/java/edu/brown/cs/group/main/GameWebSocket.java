package edu.brown.cs.group.main;

import org.eclipse.jetty.websocket.api.annotations.WebSocket;

@WebSocket
public class GameWebSocket {
  private static enum MESSAGE_TYPE {
    CONNECT, SCORE, UPDATE
  }
}
