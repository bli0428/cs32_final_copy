package edu.brown.cs.rmerzbac.main;

/**
 * Bigram object that stores a word with its previous word. Also stores
 * frequency with which the bigram appears.
 *
 * @author gokulajith
 *
 */
public class Bigram {

  private String bigram;
  private String word;
  private int frequency;

  /**
   * On new creation of a pairing, store the two words and set frequency to 1.
   *
   * @param wordItself
   *          Actual word in txt file
   * @param prevWord
   *          Word prior to it (bigram)
   */
  public Bigram(String wordItself, String prevWord) {

    word = wordItself;
    bigram = prevWord;
    frequency = 1;
  }

  /**
   * Getter for bigram.
   *
   * @return Word that appeared before this word.
   */
  public String getBigram() {
    return bigram;
  }

  /**
   * Setter for the word itself of this bigram.
   *
   * @param word
   *          actual word for this bigram.
   */
  public void setWord(String word) {
    this.word = word;
  }

  /**
   * Getter for the word itself of the bigram.
   *
   * @return String word itself.
   */
  public String getWord() {
    return word;
  }

  /**
   * Setter for the number of times the bigram has appeared in Trie.
   *
   * @param freq
   *          integer new frequency for this bigram.
   */
  public void setFrequency(int freq) {
    frequency = freq;
  }

  /**
   * Getter for the frequency this bigram has been entered to the Trie.
   *
   * @return Integer frequency.
   */
  public int getFrequency() {
    return frequency;
  }

}
