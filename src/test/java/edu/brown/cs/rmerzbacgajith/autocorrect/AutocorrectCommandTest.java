package edu.brown.cs.rmerzbacgajith.autocorrect;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import edu.brown.cs.rmerzbacgajith.main.REPL;

/**
 * Test all public methods in the AutocorrectCommand class that is created in
 * the REPL to handle all commands related to Autocorrect. The private methods
 * in this class are tested as a result of
 *
 *
 * @author gokulajith
 *
 */
public class AutocorrectCommandTest {

  /**
   * Test Corpus command to create Trie. This also checks to make sure the
   * getTrie() method correctly returns the Trie.
   */
  @Test
  public void testSetSettingsMethod() {

    AutocorrectCommand ac = new AutocorrectCommand();

    // Check initial statuses are all off
    assertEquals(ac.getPrefixStatus(), "off");
    assertEquals(ac.getSmartStatus(), "off");
    assertEquals(ac.getWhitespaceStatus(), "off");
    assertEquals(ac.getLed(), 0);

    // Calls the setSettings()
    // method to set everything on.
    String[] prefixOn = {"prefix", "on"};
    String[] smartOn = {"smart", "on"};
    String[] wsOn = {"whitespace", "on"};
    String[] ledOn = {"led", "3"};
    ac.setSettings(prefixOn);
    ac.setSettings(smartOn);
    ac.setSettings(wsOn);
    ac.setSettings(ledOn);

    // Ensure that the statuses have been changed
    assertEquals(ac.getPrefixStatus(), "on");
    assertEquals(ac.getSmartStatus(), "on");
    assertEquals(ac.getWhitespaceStatus(), "on");
    assertEquals(ac.getLed(), 3);

    // Ensure that they can also be turned off.
    // Calls the setSettings()
    // method to set everything on.
    String[] prefixOff = {"prefix", "off"};
    String[] smartOff = {"smart", "off"};
    String[] wsOff = {"whitespace", "off"};
    String[] ledOff = {"led", "0"};
    ac.setSettings(prefixOff);
    ac.setSettings(smartOff);
    ac.setSettings(wsOff);
    ac.setSettings(ledOff);
    // Ensure that the statuses have been changed
    assertEquals(ac.getPrefixStatus(), "off");
    assertEquals(ac.getSmartStatus(), "off");
    assertEquals(ac.getWhitespaceStatus(), "off");
    assertEquals(ac.getLed(), 0);

  }

  /**
   * Test acCommand that takes in the user input after the ac command and
   * returns ranked suggestions.
   */
  @Test
  public void testacCommand() {

    REPL repl = new REPL();
    
    repl.processCommand("map data/maps/maps.sqlite3");
    
    AutocorrectCommand ac = repl.getMapCommand().getAC();
    String[] prefixOn = {"prefix", "on"};
    
    ac.setSettings(prefixOn);

    // check ac command with only one word.
    String[] word = {"Tha"};
    List<String> oneWord = ac.acCommand(word);
    assertEquals(oneWord.size(), 5);
    assertEquals(oneWord.get(0), "Thames Street");
    assertEquals(oneWord.get(1), "Thames St");
    assertEquals(oneWord.get(2), "Thayer Street");
    assertEquals(oneWord.get(3), "Thatcher Street");
    assertEquals(oneWord.get(4), "Thayer Road");

  // check ac command with multiple words.
      String newWord[] = {"Thayer S"};
      List<String> mulWord = ac.acCommand(newWord);
      assertEquals(mulWord.size(), 2);
      assertEquals(mulWord.get(0), "Thayer Street");
      assertEquals(mulWord.get(1), "Thayer St");


  }

}
