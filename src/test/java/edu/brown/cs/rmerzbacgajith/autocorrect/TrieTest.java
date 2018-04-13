package edu.brown.cs.rmerzbacgajith.autocorrect;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;

import org.junit.Test;

import com.google.common.collect.Multimap;


/**
 * Test all public methods in Trie. NOTE: All of the private methods, including
 * private insert method and the helper methods for the suggestion generations,
 * were tested as a result of the tests in AutocorrectCommand class, as well as
 * in System Tests.
 *
 * @author gokulajith
 *
 */
public class TrieTest {

  /**
   * Test Empty Construction of Trie.
   */
  @Test
  public void testEmptyConstruction() {
    Trie trie = new Trie();
    assertNotNull(trie);
  }

  /**
   * Test Multiple word construction of Trie. This also tests getOccurences
   * public method, and insert() private method.
   */
  @Test
  public void testMultipleConstruction() {
    Trie trie = new Trie();

    String input = "This is a sentence that will be added to the Trie. "
        + "This is another sentence that will be added to the Trie. "
        + "I think the Trie is really cool.";
    
    List<String> words = new TextReader(new Scanner(input)).getWords();
    trie.construct(words);

    // Make sure all words are inserted correctly with correct occurences.
    assertEquals(trie.getOccurences("trie"), new Integer(3));
    assertEquals(trie.getOccurences("this"), new Integer(2));
    assertEquals(trie.getOccurences("think"), new Integer(1));
    assertEquals(trie.getOccurences("hello"), new Integer(0));

  }

  /**
   * Test getBigrams() getter to get Multimap of Bigrams. This also ensures the
   * bigrams are being added correctly.
   */
  @Test
  public void testBigramsMap() {
    Trie trie = new Trie();

    String input = "This is a sentence that will be added to the Trie. "
        + "This is another sentence that will be added to the Trie. "
        + "I think the Trie is really cool.";

    List<String> words = new TextReader(new Scanner(input)).getWords();
    trie.construct(words);

    Multimap<String, Bigram> bigrams = trie.getBigrams();

    // check bigrams of the word "sentence" which should be "a" and "another"
    Collection<Bigram> sentenceBigrams = bigrams.get("sentence");
    assertEquals(sentenceBigrams.size(), 2);

    boolean abigramExists = false;
    boolean anotherbigramExists = false;

    for (Bigram bigram : sentenceBigrams) {
      if (bigram.getBigram().equals("a")) {
        abigramExists = true;
      }

      if (bigram.getBigram().equals("another")) {
        anotherbigramExists = true;
      }
    }

    // Make sure both bigrams were mapped
    assertTrue(abigramExists);
    assertTrue(anotherbigramExists);

  }

  /**
   * Tests Whitespace method to see if a word can be split, and if so split it.
   * This also tests the getPrevWord method that returns the first word the
   * combined word was split into.
   */
  @Test
  public void testWhitespaceandgetPrevWordMethods() {
    Trie trie = new Trie();

    String input = "split words";

    List<String> words = new TextReader(new Scanner(input)).getWords();
    trie.construct(words);

    // Ensure word that cannot be split is not split
    List<String> nosplit = trie.whitespace("xxxxsplit");
    assertEquals(nosplit.size(), 0);

    // Make sure split works correctly
    List<String> split = trie.whitespace("splitwords");
    String prevWord = trie.getPrevWord(split.get(0));

    assertEquals(split.size(), 1);
    assertEquals(split.get(0), "split words");
    assertEquals(prevWord, "split");

  }

  /**
   * Tests Prefix method to take in prefix and finds every word in tree that has
   * this prefix. This also tests all private Prefix helper methods.
   */
  @Test
  public void testPrefixMethod() {
    Trie trie = new Trie();

    String input = "the those hi who what these that them those they this";

    List<String> words = new TextReader(new Scanner(input)).getWords();
    trie.construct(words);

    // all prefixes are included
    List<String> prefixes = trie.prefix("the");
    assertEquals(prefixes.size(), 4);
    assertEquals(prefixes.get(0), "the");
    assertEquals(prefixes.get(1), "these");
    assertEquals(prefixes.get(2), "they");
    assertEquals(prefixes.get(3), "them");

  }

  /**
   * Tests findLev method to take in user input and finds every word in tree
   * that is within the input led value away from the word.
   */
  @Test
  public void testFindLevMethod() {
    Trie trie = new Trie();

    String input = "them was hi she he they these";

    List<String> words = new TextReader(new Scanner(input)).getWords();
    trie.construct(words);

    // lev value 0
    List<String> levs = trie.findLev("the", 0);
    assertEquals(levs.size(), 0);

    // lev value 1
    levs = trie.findLev("the", 1);
    assertEquals(levs.size(), 4);

    // led value 2
    trie = new Trie();
    input = "shel shell dog hel hell cat hes lion";
    words = new TextReader(new Scanner(input)).getWords();
    trie.construct(words);
    levs = trie.findLev("the", 2);
    assertEquals(levs.size(), 3);

  }

  /**
   * Tests getLedValues method to ensure Multimap of LedValues is populated
   * correctly, and that the clearLedValues method correctly empties it.
   */
  @Test
  public void testLedValuesMethods() {
    Trie trie = new Trie();
    String input = "shel them shell dog hel he hell cat hes lion";
    List<String> words = new TextReader(new Scanner(input)).getWords();
    trie.construct(words);

    trie.findLev("the", 2);

    Multimap<Integer, String> ledValues = trie.getLedValues();
    Collection<String> oneLed = ledValues.get(1);
    Collection<String> twoLed = ledValues.get(2);

    // Make sure all led mappings exist.
    assertEquals(oneLed.size(), 2);
    assertEquals(twoLed.size(), 3);
    assertTrue(oneLed.contains("them"));
    assertTrue(oneLed.contains("he"));
    assertTrue(twoLed.contains("shel"));
    assertTrue(twoLed.contains("hel"));
    assertTrue(twoLed.contains("hes"));

    // Ensure clear method works as expected
    trie.clearLedValues();
    assertEquals(trie.getLedValues().size(), 0);

  }

}
