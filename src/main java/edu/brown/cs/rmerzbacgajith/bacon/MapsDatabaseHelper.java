package edu.brown.cs.rmerzbacgajith.bacon;

import java.sql.Connection;

/**
 * Class that handles all the queries and caching for Bacon.
 *
 * @author gokulajith
 *
 */
public class MapsDatabaseHelper {

  private static Connection conn = null;

  /**
   * Instantiates all the data structures for querying and caching.
   *
   * @param conn
   *
   */
  public MapsDatabaseHelper(Connection conn) {

    MapsDatabaseHelper.conn = conn;

  }
}
