package wordsuggestionengine;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

public class Main {
  public static void main(String[] args) throws IOException {

    AffinityAnalysis aa = new AffinityAnalysis();

    // this takes in user input or text input
    Scanner sc = new Scanner(System.in);
    System.out.print("Enter the next word in the text: ");
    String enteredWord = sc.next();

    // this display and calculates the confidence and support
    aa.calConfidenceSupport(enteredWord);
    // this prints the next words
    aa.printNextWords();
  }
}
