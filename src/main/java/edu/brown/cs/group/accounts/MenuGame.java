package edu.brown.cs.group.accounts;

import java.util.ArrayList;
import java.util.List;

public class MenuGame {
  private int id;
  private int numPlayers;
  List<User> currPlayers = new ArrayList<>();
  
  public MenuGame(int id, int numPlayers) {
    this.id = id;
    this.numPlayers = numPlayers;
  }
  
  synchronized public boolean addUser(User u) {
    if (currPlayers.size() < numPlayers) {
      currPlayers.add(u);
      return true;
    } else {
      return false;
    }
  }
  
  synchronized public void removeUser(User u) {
    currPlayers.remove(u);
  }
  
  public int getId() {
    return id;
  }
  
  public List<User> getCurrPlayers() {
    return currPlayers;
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
