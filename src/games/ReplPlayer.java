package games;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import components.Bishop;
import components.Board;
import components.Knight;
import components.Piece;
import components.Queen;
import components.Rook;
import positions.Position;
import positions.PositionException;

/**
 * Player whose commands are entered via the command line.
 *
 * @author charliecutting
 *
 */
public class ReplPlayer implements Player {

  private Set<Piece> bank;
  private Board board;
  private int color;

  private static final String[] WB = { "W", "B" };
  private static final String[] WHITE_BLACK = { "White", "Black" };

  /**
   * Public constructor.
   */
  public ReplPlayer() {
    bank = Collections.synchronizedSet(new HashSet<Piece>());
  }

  @Override
  public void setColor(int color) {
    this.color = color;
  }

  @Override
  public Set<Piece> bank() {
    return bank;
  }

  @Override
  public Move move() {
    Map<Position, Set<Position>> validMoves = board.getValidMoves(color);

    // System.out.println(board.check(color));

    if (board.check(color)) {
      System.out.println("Check!");
    }

    // for (Position p : board.places().keySet()) {
    // System.out.println(
    // WB[board.places().get(p).color()] + board.places().get(p).type() + " "
    // + board.places().get(p).position().col() + ","
    // + board.places().get(p).position().row());
    // for (Position t : board.places().get(p).threatens(board)) {
    // System.out.println(t.col() + "," + t.row());
    // }
    // // }
    //
    // for (Position p : validMoves.keySet()) {
    // for (Position q : validMoves.get(p)) {
    // System.out.println(board.places().get(p).type() + " "
    // + WB[board.places().get(p).color()] + " " + p.col() + "," + p.row()
    // + " " + q.col() + "," + q.row());
    // }
    // }
    // System.out.println(color);
    // System.out.println("-------------");
    // for (Position p : board.places().keySet()) {
    // System.out.println(board.places().get(p).type() + " "
    // + WB[board.places().get(p).color()] + " " + p.col() + "," + p.row());
    // }

    ////////////////

    System.out.println(WHITE_BLACK[color]
        + "'s turn. Enter your move, or type 'board' to view your board:");
    System.out.println();
    String s = "";
    try {
      BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
      s = br.readLine();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    if (s.matches("board") || s.matches("Board")) {
      board.print();
      return move();
    } else if (s.matches("bank") || s.matches("Bank")) {
      for (Piece p : bank) {
        System.out.println(p.type());
      }
      return move();
    } else if (s.matches("[1-8],[1-8]\\s+[1-8],[1-8]")) {
      String[] tokens = s.split("\\s");
      String[] p1 = tokens[0].split(",");
      String[] p2 = tokens[1].split(",");
      try {
        Position start = new Position(Integer.parseInt(p1[0]),
            Integer.parseInt(p1[1]));
        Position end = new Position(Integer.parseInt(p2[0]),
            Integer.parseInt(p2[1]));
        if (validMoves.containsKey(start)
            && validMoves.get(start).contains(end)) {
          return new Move(start, end);
        } else {
          System.out.println("That is not a valid move!");
          return move();
        }
      } catch (NumberFormatException e) {
        System.out.println("Couldn't read that input!");
        return move();
      } catch (PositionException e) {
        System.out.println("That position isn't legal!");
        return move();
      }

    } else {
      System.out
          .println("Sorry, that wasn't a valid input. Enter another move.");
      return move();
    }
  }

  @Override
  public void setBoard(Board board) {
    this.board = board;
  }

  @Override
  public Piece promote(Position p) {
    System.out.println("Enter the type of piece you'd like to promote to:"
        + " k for knight, b for bishop, r for rook, q for queen:");
    System.out.println();
    String s = "";
    try (BufferedReader br = new BufferedReader(
        new InputStreamReader(System.in))) {
      s = br.readLine();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    // TODO: fix these constructor calls
    switch (s) {
    case "k":
    case "K":
      return new Knight(p, color);
    case "b":
    case "B":
      return new Bishop(p, color);
    case "q":
    case "Q":
      return new Queen(p, color);
    case "r":
    case "R":
      return new Rook(p, color);
    default:
      System.out.println("Didn't recognize that token");
      return promote(p);
    }
  }

  @Override
  public void acceptPiece(Piece p) {
    bank.add(p);
  }

}
