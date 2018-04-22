package main;

import games.ABCutoffAI;
import games.ChessGame;
import games.Player;
import games.ReplPlayer;
import positions.PositionException;

/**
 * Main class.
 *
 * @author charliecutting
 *
 */
public final class Main {

  private Main() {

  }

  /**
   * Main method.
   *
   * @param args
   *          command line arguments
   */
  public static void main(String[] args) {
    // TODO: Make a real REPL with game start/end commands, player names, player
    // types, chess vs bughouse, etc.

    Player p1 = new ReplPlayer();
    Player p2 = new ABCutoffAI();

    try {
      ChessGame game = new ChessGame(p1, p2);
      game.play();
    } catch (PositionException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
}
