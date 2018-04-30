package main.java.edu.brown.cs.group.accounts;

public class Protection {
  private final int SALT_LENGTH = 8;

  public Protection() {
  }

  public String generateSalt() {
    String chars = "abcdefghijklmnopqrstuvwxyz1234567890";
    String salt = "";
    for (int i = 0; i < SALT_LENGTH; i++) {
      salt += chars.charAt((int) (Math.random() * chars.length()));
    }
    return salt;
  }

  public String hash(String text) {
    return org.apache.commons.codec.digest.DigestUtils.sha256Hex(text);
  }

  public String hash(String text, String salt) {
    return org.apache.commons.codec.digest.DigestUtils.sha256Hex(text + salt);
  }
}
