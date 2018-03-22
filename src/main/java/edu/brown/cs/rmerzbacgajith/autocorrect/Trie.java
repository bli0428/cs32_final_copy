package edu.brown.cs.rmerzbacgajith.autocorrect;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Multimap;

/**
 * Trie class that is built on mapping characters to other Tries, so that words
 * can be inserted character by character. This allows for efficient storage
 * with corpi, as well as the ability to keep track of what is in the Trie,
 * different properties of words in the tree, and the ability to run functions
 * to generate the suggestions with just the last word from user input.
 *
 * @author gokulajith
 *
 */
public class Trie {

  private Map<Character, Trie> characterChild;
  private ListMultimap<String, Bigram> bigrams;
  private ListMultimap<Integer, String> ledValues;
  private List<String> whitespaceWords;
  private boolean endWord;
  private int occurences;

  /**
   * Instantiate all necessary storage for successful creation of tree and
   * execution of commands.
   */
  public Trie() {

    // Map to create the tree that maps a character to another Trie.
    characterChild = new HashMap<>();

    // MultiMap of Bigrams to store the Bigrams of every word inserted to the
    // Trie.
    bigrams = ArrayListMultimap.create();

    // MultiMap of ledValues to be used for smart ranking.
    ledValues = ArrayListMultimap.create();

    whitespaceWords = new ArrayList<String>();

    // Boolean to see if the end of a word has been reached in the Trie.
    endWord = false;

    // Integer to store how many times the word has appeared in the Trie.
    occurences = 0;
  }

  /**
   * Method that starts inserting the words into the Trie, as well as populates
   * bigrams Hashmap as it goes.
   *
   * @param words
   *          ArrayList of Strings that holds every word in the txt file.
   */
  public void construct(List<String> words) {

    // Loop through every word
    for (int i = 0; i < words.size(); i++) {

      // Insert the word into the Trie.
      this.insert(words.get(i));

      // Insert the bigram into the Hashmap
      if (i != 0) {

        // If the word already is in the Bigram Map
        if (bigrams.containsKey(words.get(i))) {

          // See if the bigram also exists with the word
          List<Bigram> currWordBigrams = bigrams.get(words.get(i));
          boolean bigramExists = false;

          // If the bigram does, increase its frequency by one with helper
          // method inside Bigram class.
          for (Bigram bigram : currWordBigrams) {
            if (bigram.getBigram().equals(words.get(i - 1))) {

              bigram.setFrequency(bigram.getFrequency() + 1);
              bigramExists = true;
            }
          }

          // If it does not exist, create new Bigram with this word and its
          // previous word and put it in the map.
          if (!bigramExists) {
            Bigram bigram = new Bigram(words.get(i), words.get(i - 1));
            bigrams.put(words.get(i), bigram);
          }

        } else {

          // If word has not been in inserted yet, insert into Bigram map with
          // its previous word.
          Bigram bigram = new Bigram(words.get(i), words.get(i - 1));
          bigrams.put(words.get(i), bigram);
        }
      }
    }
  }

  /**
   * Method that inserts a word directly into the Trie.
   *
   * @param word
   *          String to be inserted.
   */
  public void insert(String word) {

    // Get first character of the word
    char first = word.charAt(0);

    // If the hashmap of the current Trie does not have a mapping to this first
    // character
    if (!characterChild.containsKey(first)) {

      // Create the mapping by putting the character and a Trie into the map.
      Trie mapTree = new Trie();
      characterChild.put(first, mapTree);
    }

    // Remove the first letter from the word
    word = word.substring(1);

    // If there is more of the word, insert the rest of the word recursively.
    // Thus, the first character will continuously be mapped from the previous
    // first character until the end of the word is reached. This creates a path
    // through the nodes of the Trie.
    if (word.length() > 0) {
      characterChild.get(first).insert(word);

    } else {

      // if the end of the word is reached, set the boolean and update
      // Occurrences.
      characterChild.get(first).endWord = true;
      characterChild.get(first).occurences++;

    }
  }

  /**
   * Method that searches the main Trie and sees how many times a word has been
   * placed into it.
   *
   * @param word
   *          String to be searched for.
   * @return Integer of occurrences in Trie.
   */
  public Integer getOccurences(String word) {

    // If end of word is reached, return the occurences stored.
    if (word.length() == 0) {
      return occurences;
    }

    // Store first character of the word and then remove it from the word.
    Character curr = word.charAt(0);
    word = word.substring(1);

    // IF the current Trie's hashmap contains a mapping to the character,
    // recursively move on to there.
    if (characterChild.containsKey(curr)) {
      return characterChild.get(curr).getOccurences(word);

      // If not, the end of the path has been reached, meaning the word is not
      // in the Trie.
    } else {
      return 0;
    }
  }

  /**
   * Method that handles seeing if the input last word of user input can be
   * split into two words.
   *
   * @param word
   *          String to be checked
   * @return List of new words that have been split apart.
   */
  public List<String> whitespace(String word) {

    List<String> words = new ArrayList<String>();

    // Loop through the word itself
    for (int i = 0; i < word.length(); i++) {

      // Split the word into every possible 2 parts
      String firstPart = word.substring(0, i);
      String secPart = word.substring(i, word.length());

      // Check if both parts are words in the Trie
      if (this.getOccurences(firstPart) > 0
          && this.getOccurences(secPart) > 0) {

        // If they are, combine them
        String splitWord = new StringBuilder(firstPart + " " + secPart)
            .toString();

        whitespaceWords.add(splitWord);
        whitespaceWords.add(firstPart);

        // Use Bigrams hashmap to set the Bigrams of the new word to be the same
        // as those of the first part of the word. Example, "mousecheese" would
        // be split into "mouse cheese", and assigned the same previous words as
        // "mousecheese".

        List<Bigram> curr = bigrams.get(firstPart);
        for (Bigram bigram : curr) {
          Bigram newBigram = new Bigram(splitWord, bigram.getBigram());
          newBigram.setFrequency(bigram.getFrequency());
          bigrams.put(splitWord, newBigram);
        }

        // Add new split word to final list of suggestions.
        words.add(splitWord);
      }
    }

    // For smart ranking, add words to Multimap of Led values for all
    // suggestions.
    for (String combinedword : words) {
      ledValues.put(this.findLevDistance(word, combinedword), combinedword);
    }

    return words;
  }

  /**
   * Method that starts finding suggestions based on prefix matching.
   *
   * @param word
   *          Last word of user input
   * @return List of Strings of prefix matching suggestions.
   */
  public List<String> prefix(String word) {

    List<String> answer = new ArrayList<String>();

    // Use helper method to find the Trie that holds this
    // word.
    Trie start = this.findPrefixTree(word);

    if (start != null) {

      // If this prefix itself is a word, add it to the suggestions
      if (start.endWord) {
        answer.add(word);
      }

      // Call recursive helper function to populate answer ArrayList.
      prefixHelper(start, word, answer, word);
    }

    return answer;
  }

  /**
   * Use input prefix to find the Trie that maps up to the last word of user
   * input.
   *
   * @param prefix
   *          Word being found in the Trie.
   * @return Trie that is contained at the character path to the word.
   */
  private Trie findPrefixTree(String prefix) {

    // If the end of the word has been reached, return the current Trie.
    if (prefix.length() == 0) {
      return this;
    }

    // Store and remove first character from the word.
    Character curr = prefix.charAt(0);
    prefix = prefix.substring(1);

    // If the map of the current Trie has a child is the next character in the
    // word, recursively call this method on that Trie.
    if (characterChild.containsKey(curr)) {
      return characterChild.get(curr).findPrefixTree(prefix);

      // Otherwise, the word does not exist in the Trie, and no prefixes can be
      // found.
    } else {
      return null;
    }
  }

  /**
   * Continuously check all the characters that the current prefix maps to, and
   * see if adding those characters creates a prefix match. Continue searching
   * through all the mappings until the end of the word is reached for all
   * characters.
   *
   * @param trie
   *          Current Trie to be checked
   * @param prefix
   *          Word that is being currently checked for prefix matching
   * @param prefixAnswer
   *          final ArrayList of prefix suggestions
   * @param originalWord
   *          Original last word from the user input
   */
  private void prefixHelper(Trie trie, String prefix, List<String> prefixAnswer,
      String originalWord) {

    // Get all the characters that the current trie's hashmap of children
    // contains
    for (Character curr : trie.characterChild.keySet()) {

      // Get the nextTrie stored in the Hashmap
      Trie nextTrie = trie.characterChild.get(curr);

      // If the nextTrie's boolean tells us it is a valid word from a corpus,
      // then add that to the answer list and populate the ledValues Multimap.
      if (nextTrie.endWord) {
        prefixAnswer.add(new StringBuilder(prefix + curr).toString());
        ledValues.put(findLevDistance(originalWord,
            new StringBuilder(prefix + curr).toString()), prefix + curr);
      }

      // Recursively continue searching by adding the character from the keySet
      // to the current prefix.
      prefixHelper(nextTrie, new StringBuilder(prefix + curr).toString(),
          prefixAnswer, originalWord);
    }
  }

  /**
   * Method that begins finding Levenshtein suggestions.
   *
   * @param userPhrase
   *          Last String word from user input.
   * @param lev
   *          Current setting for max led.
   * @return List of Strings of suggestions.
   */
  public List<String> findLev(String userPhrase, int lev) {

    List<String> levAnswer = new ArrayList<String>();
    String checkPhrase = "";

    // Call helper method to populate levAnswer ArrayList. Note that checkPhrase
    // starts as blank string but characters are added to it and filtered by led
    // values in
    // helper.
    this.levHelper(userPhrase, checkPhrase, lev, levAnswer);

    // Map the ledValues for the suggestions.
    for (String suggestion : levAnswer) {
      ledValues.put(this.findLevDistance(userPhrase, suggestion), suggestion);
    }

    return levAnswer;

  }

  /**
   * Helper method that recursively adds characters from the current Trie's
   * children map, and sees if this is an appropriate word within the given led
   * distance.
   *
   * @param userPhrase
   *          Last word from user.
   * @param checkPhrase
   *          Phrase being checked as characters are added to it.
   * @param led
   *          Max led value specified by user.
   * @param levAnswer
   *          Final arrayList of Strings with suggestions.
   */
  private void levHelper(String userPhrase, String checkPhrase, int led,
      List<String> levAnswer) {

    // Use helper method to find the edit distance between the user's word and
    // the current phrase being checked.
    int checkPhraseLev = findLevDistance(userPhrase, checkPhrase);

    // If the current phrase is a word and it's led is within bounds, add phrase
    // to suggestions list.
    if (this.endWord && checkPhraseLev <= led) {

      levAnswer.add(checkPhrase);

    }

    // while the phrases being checked are still within max led limits
    if (checkPhrase.length() - userPhrase.length() <= led) {

      // Recursively call this method on every character in the characterChild
      // of the current Trie.
      for (Character curr : this.characterChild.keySet()) {

        characterChild.get(curr).levHelper(userPhrase,
            new StringBuilder(checkPhrase + curr).toString(), led, levAnswer);

      }
    }
  }

  /**
   * Method to calculate Levenshtein Edit Distance between two words.
   *
   * @param word1
   *          First String
   * @param word2
   *          Second String
   * @return Integer number of minimum substitutions, insertions, or deletions
   *         of characters to get from word1 to word2.
   */
  private int findLevDistance(String word1, String word2) {
    int size1 = word1.length();
    int size2 = word2.length();

    // 2D array that holds distances between two words at indices.
    int[][] cost = new int[size1 + 1][size2 + 1];

    for (int i = 0; i <= size1; i++) {
      cost[i][0] = i;
    }

    for (int i = 0; i <= size2; i++) {
      cost[0][i] = i;
    }

    for (int j = 1; j <= size2; j++) {
      for (int i = 1; i <= size1; i++) {

        // Calculate costs for each action
        int delCost = cost[i - 1][j] + 1;
        int insCost = cost[i][j - 1] + 1;
        int subCost = 1;

        if (word1.charAt(i - 1) == word2.charAt(j - 1)) {
          subCost = 0;
        }

        // Store the minimum cost at the current index
        cost[i][j] = Math.min((Math.min(delCost, insCost)),
            cost[i - 1][j - 1] + subCost);

      }
    }

    // Overall led between the two words is stored at max size index in both
    // dimensions.
    return cost[size1][size2];
  }

  /**
   * Getter for Multimap of Bigrams in the Trie.
   *
   * @return Immutable Multimap of Bigrams in the Trie
   */
  public Multimap<String, Bigram> getBigrams() {

    Multimap<String, Bigram> immutableBigrams;
    immutableBigrams = new ImmutableListMultimap.Builder<String, Bigram>()
        .putAll(this.bigrams).build();

    return immutableBigrams;
  }

  /**
   * Getter for Multimap of LedValues in the Trie.
   *
   * @return Immutable Multimap of LedValues in the Trie
   */
  public Multimap<Integer, String> getLedValues() {

    Multimap<Integer, String> immutableLedValues;
    immutableLedValues = new ImmutableListMultimap.Builder<Integer, String>()
        .putAll(this.ledValues).build();

    return immutableLedValues;
  }

  /**
   * Getter for the previous word on whitespace suggestions.
   *
   * @param word
   *          String split word to find the previous word of.
   * @return First part of whitespace word.
   */
  public String getPrevWord(String word) {

    int index = whitespaceWords.indexOf(word);

    return whitespaceWords.get(index + 1);
  }

  /**
   * Method that clears LedValues Multimap before running commands.
   */
  public void clearLedValues() {
    ledValues.clear();
  }
}
