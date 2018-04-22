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
import positions.BankPosition;
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
      // Map<Position, Piece> b = board.places();
      // for (Position p : b.keySet()) {
      // System.out.println(p.col() + "," + p.row() + " " + WB[b.get(p).color()]
      // + " " + b.get(p).type());
      // }

      for (int i = 1; i <= 8; i++) {
        StringBuilder sb = new StringBuilder();
        for (int j = 1; j <= 8; j++) {
          Position p;
          try {
            p = new Position(j, i);
            if (board.places().containsKey(p)) {
              sb.append(WB[board.places().get(p).color()]);
              sb.append(board.places().get(p).type());
            } else {
              sb.append("__");
            }
            sb.append(" ");
          } catch (PositionException e) {
            // TODO Auto-generated catch block
            // Should never be reached
            e.printStackTrace();
          }
        }
        System.out.println(sb.toString());
      }
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
  public Piece promote() {
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
      return new Knight(new BankPosition(), color);
    case "b":
    case "B":
      return new Bishop(new BankPosition(), color);
    case "q":
    case "Q":
      return new Queen(new BankPosition(), color);
    case "r":
    case "R":
      return new Rook(new BankPosition(), color);
    default:
      return promote();
    }
  }

  @Override
  public void acceptPiece(Piece p) {
    bank.add(p);
  }

}
