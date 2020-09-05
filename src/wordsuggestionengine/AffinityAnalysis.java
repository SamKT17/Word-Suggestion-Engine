package wordsuggestionengine;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

class AffinityAnalysis {

  private ArrayList<String> results = new ArrayList<>();
  private List<Double> resultConfidence = new ArrayList<>();
  private Map<String, Integer> fullResults = new HashMap<>();
  private Bigram bg = new Bigram();

  /**
   * This is where the confidence and support is calculated. It first loads the file and then
   * calculates and then displays the information.
   *
   * @param enteredWord - user input
   * @throws IOException - loading in text file can result in error if not found
   */
  void calConfidenceSupport(String enteredWord) throws IOException {
    // used to load the file and generate the bigram
    bg.loadFile();

    // this sets the validResults to the created bigram
    Map<HashSet<String>, Integer> validResults = bg.getBgram();

    // this sets wordLit from all the words in the text file
    List<String> wordList = bg.getWords();

    // this initializes fullResults by keeping count of how many times each word appears
    initializeFullResults(wordList);

    // this is the calculating the conf. and support of each bigram
    for (HashSet<String> featureSet : validResults.keySet()) {
      List<String> featureList = featureSet.stream().collect(Collectors.toList());

      // First how many times was there an item with another item (confidence)
      // Then how many times was there an item at all (support)
      double confidence =
          (double) validResults.get(featureSet) / fullResults.get(featureList.get(0));
      double support = (double) validResults.get(featureSet) / 248;

      // display conf. and support only for the entered word and its conf. value is > 65%
      if (featureList.contains(enteredWord) && confidence >= .65) {
        System.out.println("\nConfidence and Support:");
        System.out.printf(
            "We show a confidence of %f that a person who "
                + "said %s will also then say %s%n and a support of %f that "
                + "a person will say these words together at all.",
            confidence, featureList.get(0), featureList.get(1), support);
        System.out.println();

        // this sets results to all the results that match the given condition
        results.add(featureList.get(0));

        // this adds their conf. values to another list to keep track of
        resultConfidence.add(confidence);
      }
    }
  }

  /**
   * This function is responsible for printing off the next words based on the confidence. It first
   * checks to see if there are any words that match the user inputted word. It then checks to see
   * how many results for the next word there are and then displays them in highest order to lowest
   * order.
   */
  void printNextWords() {
    System.out.println("\nNext Words:");
    if (!results.isEmpty()) {

      // used to find the max confidence of the word
      Double max = Collections.max(resultConfidence);

      // if the results of the bigram pair is 3
      if (results.size() == 3) {
        System.out.println(
            "The first word that should be suggested is: "
                + results.get(resultConfidence.indexOf(max)));

        // this is after the first highest conf. is found
        // and i removed from both list the word and its conf.
        results.remove(resultConfidence.indexOf(max));
        resultConfidence.remove(max);

        // found the max of the two conf. values in the list
        max = Collections.max(resultConfidence);

        System.out.println(
            "The second word that should be suggested is: "
                + results.get(resultConfidence.indexOf(max)));

        // this is after the second highest conf. is found
        // and i removed from both list the word and its conf.
        results.remove(resultConfidence.indexOf(max));
        resultConfidence.remove(max);

        // this finds the max of only one conf. value
        // i still used max to find the highest conf. value
        // because where the last value is stored will not be
        // the same every time.
        max = Collections.max(resultConfidence);

        System.out.println(
            "The third word that should be suggested is: "
                + results.get(resultConfidence.indexOf(max)));

        // if the size of the result is 2
      } else if (results.size() == 2) {
        // find the bigger number of the two and display highest to lowest
        if (resultConfidence.get(0) < resultConfidence.get(1)) {
          System.out.println("The first word that should be suggested is: " + results.get(1));
          System.out.println("The second word that should be suggested is: " + results.get(0));
          System.out.println("The third word that should be suggested is: the");
        } else {
          System.out.println("The first word that should be suggested is: " + results.get(0));
          System.out.println("The second word that should be suggested is: " + results.get(1));
          System.out.println("The third word that should be suggested is: the");
        }
      }

      // this is if there is only one word over the confidence 65%
      if (results.size() == 1) {
        System.out.println("The first word that should be suggested is: " + results.get(0));
        System.out.println("The second word that should be suggested is: this");
        System.out.println("The third word that should be suggested is: the");
      }

      // this if there are no words with the confidence over 65%
      // this means the word entered was not part of the text file and
      // therefore we dont know the conf. value and this give 3
      // genaric next words
    } else {
      System.out.println("The first word that should be suggested is: of");
      System.out.println("The second word that should be suggested is: this");
      System.out.println("The third word that should be suggested is: the");
    }
    System.out.println();
  }

  /**
   * This function is used to generate the fullResults, the reason for fullResults is to get a count
   * of how many times a word is found in the text file using a map.
   *
   * @param wordList - this is full list of words in text file
   */
  private void initializeFullResults(List<String> wordList) {
    for (int i = 1; i < wordList.size(); i++) {
      fullResults.merge(wordList.get(i), 1, Integer::sum);
    }
  }
}
