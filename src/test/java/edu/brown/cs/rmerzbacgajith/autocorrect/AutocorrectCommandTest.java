package edu.brown.cs.rmerzbacgajith.autocorrect;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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

    REPL repl = new REPL();
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

    Repl repl = new Repl();
    AutocorrectCommand ac = repl.getAC();

    String filename = "data/autocorrect/great_expectations.txt";
    ac.corpusCommand(filename);

    repl.run("prefix on");
    repl.run("whitespace on");

    // check ac command with only one word.
    List<String> oneWord = ac.acCommandParser("the");
    assertEquals(oneWord.size(), 5);
    assertEquals(oneWord.get(0), "the");
    assertEquals(oneWord.get(1), "there");
    assertEquals(oneWord.get(2), "t he");
    assertEquals(oneWord.get(3), "then");
    assertEquals(oneWord.get(4), "they");

    // check ac command with multiple words.
    List<String> mulWord = ac.acCommandParser("this is");
    assertEquals(mulWord.size(), 5);
    assertEquals(mulWord.get(0), "is");
    assertEquals(mulWord.get(1), "i s");
    assertEquals(mulWord.get(2), "isn");
    assertEquals(mulWord.get(3), "island");
    assertEquals(mulWord.get(4), "issue");

    // check smart ranking
    repl.run("smart on");
    repl.run("led 2");
    List<String> smart = ac.acCommandParser("boa");
    assertEquals(smart.size(), 5);
    assertEquals(smart.get(0), "boy");
    assertEquals(smart.get(1), "boat");
    assertEquals(smart.get(2), "boar");
    assertEquals(smart.get(3), "box");
    assertEquals(smart.get(4), "bow");

  }

}
