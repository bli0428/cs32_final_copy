package edu.brown.cs.group.accounts;

public class User {
  private int userId;

  public User(int userId) {
    this.userId = userId;
  }

  public int getUserId() {
    return userId;
  }

  public String getUsername(DatabaseManager dbm) {
    return dbm.getUsername(userId);
  }
}
