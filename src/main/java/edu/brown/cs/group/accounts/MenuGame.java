package edu.brown.cs.group.accounts;

public class MenuGame {
  private int id;
  private int numPlayers;
  User[] currPlayers;

  public MenuGame(int id, int numPlayers) {
    this.id = id;
    this.numPlayers = numPlayers;
    this.currPlayers = new User[numPlayers];
  }

  synchronized public boolean addUser(User u) {
    for (int i = 0; i < currPlayers.length; i++) {
      if (currPlayers[i] == null) {
        currPlayers[i] = u;
        return true;
      }
    }
    return false;
  }

  synchronized public void removeUser(User u) {
    for (int i = 0; i < currPlayers.length; i++) {
      if (currPlayers[i] != null && currPlayers[i].equals(u)) {
        currPlayers[i] = null;
        return;
      }
    }
  }

  synchronized public void removeUser(int userId) {
    for (int i = 0; i < currPlayers.length; i++) {
      if (currPlayers[i] != null && currPlayers[i].getUserId() == userId) {
        currPlayers[i] = null;
        return;
      }
    }
  }

  public int getId() {
    return id;
  }

  public User[] getCurrPlayers() {
    return currPlayers;
  }

  public int getCurrPlayersSize() {
    int size = 0;
    for (int i = 0; i < currPlayers.length; i++) {
      if (currPlayers[i] != null) {
        size++;
      }
    }
    return size;
  }

  public int getNumPlayers() {
    return numPlayers;
  }

  public String getGameType() {
    if (numPlayers == 2) {
      return "Chess";
    } else if (numPlayers == 4) {
      return "Bughouse";
    } else {
      return "Unknown";
    }
  }
}
