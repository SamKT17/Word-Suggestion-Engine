package wordsuggestionengine;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Bigram {

  private Map<HashSet<String>, Integer> bgrams = new HashMap<>();

  private List<String> words;

  /**
   * This method loads the appropriate file and separates it into words. This method also calls the
   * createMap method that is used to keep track of how many times each pair of words appears
   * together.
   *
   * @throws IOException - the file can have a chance of not existing
   */
  protected void loadFile() throws IOException {
    Path path = Paths.get("src/messages.txt");

    /*
     * This reads each line of the file using the Java stream class and
     * it filters out the lines that are blank.
     */
    Stream<String> lines = Files.lines(path).filter(line -> !line.isEmpty());

    Stream<String> messLines = Files.lines(path).filter(line -> !line.isEmpty());
    /*
     * This creates a list of all the words from the text file by
     * taking each line and making it lower case,
     * then splitting the line up by spaces (\\s) giving us words,
     * then merges all the lists of words per line into one big list,
     * then we strip off the commas making each word a just a word.
     *
     * This could be better, but I need to spend more time with regex.
     *
     * example: The cat is fluffy, but not fat.
     * it takes fluffy, and makes it fluffy
     */

    List<String> sportsWords =
        lines
            .map(String::toLowerCase)
            .map(line -> line.split("\\s"))
            .flatMap(Arrays::stream)
            .map(word -> word.replaceAll(",", ""))
            .collect(Collectors.toList());

    List<String[]> messWords =
        messLines
            .map(String::toLowerCase)
            .map(line -> line.split("\\s"))
            .collect(Collectors.toList());

    createMap(messWords);
    words = sportsWords;
  }

  /**
   * This method creates the map of each pair of words and keeps tracks of how many times that pair
   * is found.
   *
   * @param wordList - this is a list of all the words in the file.
   */
  private void createMap(List<String[]> wordList) {
    for (String[] elm : wordList) {
      for (int i = 1; i < elm.length; i++) {

        bgrams.merge(new HashSet<>(Arrays.asList(elm[i], elm[i - 1])), 1, Integer::sum);
      }
    }
  }

  /**
   * This method prints off the map and is ordered by value, in this case the value is the number of
   * times that pair was found together in the file.
   */
  void printWordsByValue() {
    bgrams.forEach((key, value) -> System.out.println(key + ", " + value));
  }

  /**
   * This method is a getter for the bigram generated in this class to be used in the rest of the
   * program.
   *
   * @return - the is the bigram that was created in this class.
   */
  Map<HashSet<String>, Integer> getBgram() {
    return bgrams;
  }

  /**
   * The method is used for getting the words from the text file.
   *
   * @return - a list of words
   */
  List<String> getWords() {
    return words;
  }
}
