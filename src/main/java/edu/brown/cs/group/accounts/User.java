package edu.brown.cs.group.accounts;

public class User {
  private int userId;
  private String username = "Unknown user";

  public User(int userId, String username) {
    this.userId = userId;
    this.setUsername(username);
  }

  public int getUserId() {
    return userId;
  }

  public String getUsername(DatabaseManager dbm) {
    return dbm.getUsername(userId);
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }
}
