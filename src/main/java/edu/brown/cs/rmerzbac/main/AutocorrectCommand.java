package edu.brown.cs.rmerzbac.main;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;

import com.google.common.collect.Multimap;

/**
 * Class that handles all commands for Autocorrect.
 *
 * @author gokulajith
 *
 */
public class AutocorrectCommand {

  // Trie instance.
  private Trie trie;

  // Strings storing statuses of Autocorrect Settings.
  private String prefixStatus = "off";
  private String whitespaceStatus = "off";
  private String smartStatus = "off";
  private int led = 0;
  private List<String> wsWords = new ArrayList<String>();

  /**
   * Method that takes care of the ac command itself.
   *
   * @param words
   *          Everything in user input command that came after "ac".
   * @return List of Strings that contain final 5 ranked suggestions.
   */
  public List<String> acCommand(String[] words) {

    // If no corpus has yet been loaded.
    if (trie == null) {
      return null;
    }

    // Split parsed input into words
    // String[] words = parsed.split(" ");
    int numWords = words.length;
    String parsed = null;

    List<String> result = new ArrayList<String>();
    // If only one word input
    if (numWords == 1) {
      result = this.autocorrect(null, words[numWords - 1]);
      parsed = "";

      // If more than one word
    } else {

      // Call appropriate helper method with last word and the word before it.
      result = this.autocorrect(words[numWords - 2], words[numWords - 1]);
      parsed = words[0];
      for (int i = 1; i < words.length - 1; i++) {
        parsed = new StringBuilder(parsed + " " + words[i]).toString();
      }
    }

    // Print everything in result arraylist with proper formatting.
    for (String word : result) {
      if (parsed.equals("")) {
        System.out.println(word);
      } else {
        StringBuilder finalBuilder = new StringBuilder(parsed + " " + word);
        System.out.println(finalBuilder.toString());
      }
    }

    return result;
  }

  /**
   * Method that handles any of the keyword commands for setting or checking the
   * settings for autocorrect.
   *
   * @param parsed
   *          Array of Strings where each index holds a word.
   */
  public void setSettings(String[] parsed) {

    // IF too many arguments, error.
    if (parsed.length > 2) {
      System.out.println("ERROR: Invalid number of arguments.");
      return;
    }

    // If user is just checking the status, tell them what it currently is.
    if (parsed.length == 1) {
      switch (parsed[0]) {
        case "prefix":
          System.out
              .println(new StringBuilder("prefix " + prefixStatus).toString());
          break;
        case "whitespace":
          System.out.println(
              new StringBuilder("whitespace " + whitespaceStatus).toString());
          break;
        case "smart":
          System.out
              .println(new StringBuilder("smart " + smartStatus).toString());
          break;
        case "led":
          System.out.println(new StringBuilder("led " + led).toString());
          break;
        default:
          System.out.println("ERROR: Command could not be recognized");
          break;
      }

      // Otherwise if user is trying to set a setting on or off, check the
      // second word.
    } else if (parsed.length == 2) {
      switch (parsed[1]) {

        // Set the setting on based on the first word of the input
        case "on":
          switch (parsed[0]) {
            case "prefix":
              prefixStatus = "on";
              break;
            case "whitespace":
              whitespaceStatus = "on";
              break;
            case "smart":
              smartStatus = "on";
              break;
            default:
              System.out.println("ERROR: Command not recognized");
              break;
          }
          break;

        // Set the setting off based on the first word of the input
        case "off":
          switch (parsed[0]) {
            case "prefix":
              prefixStatus = "off";
              break;
            case "whitespace":
              whitespaceStatus = "off";
              break;
            case "smart":
              smartStatus = "off";
              break;
            default:
              System.out.println("ERROR: Command not recognized");
              break;
          }
          break;

        // Otherwise, parse led value and set it to be the led integer.
        default:
          if (parsed[0].equals("led")) {
            int newLed = -1;
            try {
              newLed = Integer.parseInt(parsed[1]);
            } catch (Exception ex) {

              // integer check
              System.out.println("ERROR: led value must be an integer.");
              return;
            }

            // not negative check.
            if (newLed < 0) {
              System.out
                  .println("ERROR: led value must be a nonnegative integer.");
              return;
            }
            led = newLed;
            break;
          } else {
            System.out.println("ERROR: Setting must be on or off.");
            return;
          }
      }
    }
  }

  /**
   * Generate suggestions for a word based on the current settings the user has
   * set.
   *
   * @param word
   *          Last word String of user input to be autocorrected
   * @return HashSet of Strings with all the unranked suggestions.
   */
  private HashSet<String> getSuggestions(String word) {

    HashSet<String> suggestions = new HashSet<String>();

    // If smart ranking, clear the Trie's ledValues Multimap.
    if (smartStatus.equals("on") && led > 0) {
      trie.clearLedValues();
    }

    // Prefix suggestions if setting is on
    if (prefixStatus.equals("on")) {

      List<String> prefixes = trie.prefix(word);

      if (prefixes.size() > 0) {
        suggestions.addAll(prefixes);
      }
    }

    // Whitespace suggestions if setting is on
    if (whitespaceStatus.equals("on")) {

      List<String> combinedWords = trie.whitespace(word);

      for (String wsword : combinedWords) {
        suggestions.add(wsword);
        wsWords.add(wsword);

      }
    }

    // Led suggestions if led > 0.
    if (led > 0) {
      List<String> lev = trie.findLev(word, led);
      if (lev.size() > 0) {
        suggestions.addAll(lev);
      }
    }

    return suggestions;
  }

  /**
   * Method that takes care of normal ranking for autocorrect suggestions based
   * on current user settings.
   *
   * @param beforeWord
   *          Second to last word of user input for ac.
   * @param lastWord
   *          Last word of user input for ac.
   * @return List of Strings with ranked suggestions.
   */
  private List<String> autocorrect(String beforeWord, String lastWord) {

    List<String> result = new ArrayList<String>();

    // Call helper method to generate suggestions based on current settings.
    HashSet<String> suggestions = this.getSuggestions(lastWord);

    // Exact match to last word means that must be first suggestion.
    if (trie.getOccurences(lastWord) > 0) {
      result.add(lastWord);

      if (suggestions.contains(lastWord)) {
        suggestions.remove(lastWord);
      }
    }

    // Instantiate Priority Queue for bigram ranking with comparator.
    PriorityQueue<Bigram> sortFrequency = new PriorityQueue<Bigram>(
        freqComparator);
    List<String> added = new ArrayList<String>();

    if (beforeWord != null) {

      // Generate bigram Multimap that stores every Bigram in the Trie
      // currently.
      Multimap<String, Bigram> bigrams = trie.getBigrams();

      // loop through all generated suggestions
      for (String suggestion : suggestions) {

        // Get every bigram of the current suggestion
        Collection<Bigram> currWordBigrams = bigrams.get(suggestion);

        for (Bigram bigram : currWordBigrams) {

          // If the suggestion has a bigram that is same as the second to last
          // word of user input
          if (bigram.getBigram().equals(beforeWord)) {

            // Add bigram to the priority queue, and suggestion to list of added
            // suggestions.
            sortFrequency.add(bigram);
            added.add(suggestion);
          }
        }
      }
    }

    // Remove already added suggestions from suggestions hashset.
    for (String remove : added) {
      suggestions.remove(remove);
    }

    // If bigrams were not enough to populate result arraylist
    List<String> occurencesSorted = new ArrayList<String>();
    if (sortFrequency.size() < 5) {

      // Sort rest of hashset based on unigrams with second comparator.
      List<String> sug = new ArrayList<String>(suggestions);
      occurencesSorted = this.sortOccurences(sug);
    }

    // Populate final result arraylist by first adding everything from priority
    // queue in order, and then from unigram Arraylist.
    while (result.size() < 5) {
      if (sortFrequency.size() > 0) {

        result.add(sortFrequency.poll().getWord());

      } else if (occurencesSorted.size() > 0) {
        result.add(occurencesSorted.get(0));
        occurencesSorted.remove(occurencesSorted.get(0));

      } else {
        break;
      }
    }

    return result;
  }

  /**
   * Comparator that takes 2 Bigrams and ranks them based on occurence of the
   * bigram, then unigram, and then alphabetical order.
   */
  private Comparator<Bigram> freqComparator = new Comparator<Bigram>() {

    @Override
    public int compare(Bigram b1, Bigram b2) {

      // Bigram Frequency
      if (b1.getFrequency() < b2.getFrequency()) {
        return 1;
      } else if (b1.getFrequency() > b2.getFrequency()) {
        return -1;
      } else {

        // Unigram Frequency
        String firstWord = b1.getWord();
        String secWord = b2.getWord();

        int currOccurences = trie.getOccurences(firstWord);
        int nextOccurences = trie.getOccurences(secWord);

        if (wsWords.contains(firstWord)) {
          currOccurences = trie.getOccurences(trie.getPrevWord(firstWord));
        }

        if (wsWords.contains(secWord)) {
          nextOccurences = trie.getOccurences(trie.getPrevWord(secWord));
        }

        if (currOccurences > nextOccurences) {
          return -1;
        } else if (currOccurences < nextOccurences) {
          return 1;
        } else {

          // Alphabetical Order
          if (firstWord.compareTo(secWord) < 0) {
            return -1;
          } else {
            return 1;
          }
        }
      }
    }
  };

  /**
   * Unigram comparator that takes input arraylist of Strings and sorts it based
   * on which words have appeared the most. Tie-breaker is alphabetical order.
   *
   * @param sug
   *          Input arraylist of suggestions
   * @return Ranked arraylist of suggestions.
   */
  private List<String> sortOccurences(List<String> sug) {
    Collections.sort(sug, new Comparator<String>() {
      @Override
      public int compare(String s1, String s2) {

        // Check by occurences of each string in the tree.
        int s1freq = trie.getOccurences(s1);
        int s2freq = trie.getOccurences(s2);

        if (wsWords.contains(s1)) {
          s1freq = trie.getOccurences(trie.getPrevWord(s1));
        }
        if (wsWords.contains(s2)) {
          s2freq = trie.getOccurences(trie.getPrevWord(s2));
        }

        if (s1freq > s2freq) {
          return -1;
        } else if (s1freq < s2freq) {
          return 1;
        } else {
          // Check alphabetical order if occurrences are equal.
          if (s1.compareTo(s2) < 0) {
            return -1;
          } else {
            return 1;
          }
        }
      }
    });

    return sug;
  }

  /**
   * Getter for the Trie. (I was told this did not need to be made immutable).
   *
   * @return Trie instance.
   */
  public Trie getTrie() {
    return trie;
  }

  /**
   * Setter for the Trie.
   *
   * @param trie
   *          trie instance.
   */
  public void setTrie(Trie trie) {
    this.trie = trie;
  }

  /**
   * Getter for the current prefix status.
   *
   * @return current Prefix status on or off.
   */
  public String getPrefixStatus() {
    return prefixStatus;
  }

  /**
   * Getter for the current whitespace status.
   *
   * @return current Whitespace status on or off.
   */
  public String getWhitespaceStatus() {
    return whitespaceStatus;
  }

  /**
   * Getter for the current smart status.
   *
   * @return current Smart status on or off.
   */
  public String getSmartStatus() {
    return smartStatus;
  }

  /**
   * Getter for the current led value.
   *
   * @return current led int value.
   */
  public int getLed() {
    return led;
  }
}
