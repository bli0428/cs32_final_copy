package edu.brown.cs.rmerzbacgajith.tree;

import java.util.Comparator;

/**
 * A class representing a point, which only holds coordinates.
 *
 * @author Reid
 */
public class Point {
  private double[] coords;

  /**
   * Constructs a new point with a given set of coordinates.
   *
   * @param paramCoords
   *          the desired coordinates
   */
  public Point(double[] paramCoords) {
    coords = paramCoords;
  }

  /**
   * Gets the point's coordinates.
   *
   * @return coords
   */
  public double[] getCoords() {
    return coords;
  }

  /**
   * Sets the point's coordinates.
   *
   * @param pCoords
   *          the desired coordinates
   */
  public void setCoords(double[] pCoords) {
    coords = pCoords;
  }

  /**
   * Compares two point based on a specific coordinate.
   *
   * @param index
   *          the index of the coordinate
   * @return the comparator class
   */
  public static Comparator<Point> compareStars(int index) {
    Comparator<Point> c = new Comparator<Point>() {

      /**
       * Compares two point based on a specific coordinate.
       *
       * @param p1
       *          the first point
       * @param p2
       *          the second point
       * @return 1 if the first point was greater, -1 if the second point was
       *         greater, 0 if equal
       */
      @Override
      public int compare(Point p1, Point p2) {
        if (p1.coords[index] == p2.coords[index]) {
          return 0;
        } else if (p1.coords[index] > p2.coords[index]) {
          return 1;
        } else {
          return -1;
        }
      };
    };
    return c;
  }

  /**
   * Prints a visual representation of the point.
   */
  @Override
  public String toString() {
    return "[" + printCoords() + "]";
  }

  /**
   * Prints a visual representation of the point's coordinates.
   *
   * @return the visual representation
   */
  public String printCoords() {
    String str = "(";
    int i;
    for (i = 0; i < coords.length - 1; i++) {
      str = str + coords[i] + " ";
    }
    str = str + coords[i] + ")";
    return str;
  }

}
