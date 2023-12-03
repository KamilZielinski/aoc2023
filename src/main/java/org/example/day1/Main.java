package org.example.day1;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {

  private static final String NUMERIC_CHARACTERS = "1234567890";
  private static final Map<String, String> numbersMap = new HashMap<>();

  public static void main(String[] args) throws URISyntaxException, IOException {
    numbersMap.put("one", "1");
    numbersMap.put("two", "2");
    numbersMap.put("three", "3");
    numbersMap.put("four", "4");
    numbersMap.put("five", "5");
    numbersMap.put("six", "6");
    numbersMap.put("seven", "7");
    numbersMap.put("eight", "8");
    numbersMap.put("nine", "9");
    List<String> strings = Files.readAllLines(Paths.get(
        Main.class.getClassLoader().getResource("day1/input.txt").toURI()));
    Integer sum = strings.stream()
//        .map(Main::replaceWordsWithNumbers) // uncomment for part2 answer
        .map(Main::removeAlphaCharacters)
        .filter(num -> !num.isEmpty())
        .map(Main::shortenLongNumbers)
        .map(Main::doubleSingleNumbers)
        .map(Integer::valueOf)
        .mapToInt(Integer::intValue)
        .sum();
    System.out.println(sum);
  }

  public static String removeAlphaCharacters(String input) {
    StringBuilder builder = new StringBuilder();
    for (char c : input.toCharArray()) {
      if (NUMERIC_CHARACTERS.contains(String.valueOf(c))) {
        builder.append(c);
      }
    }
    return builder.toString();
  }

  public static String shortenLongNumbers(String input) {
    StringBuilder builder = new StringBuilder();
    if (input.length() >= 3) {
      builder.append(input.charAt(0));
      builder.append(input.charAt(input.length() - 1));
      return builder.toString();
    } else {
      return input;
    }
  }

  public static String doubleSingleNumbers(String input) {
    StringBuilder builder = new StringBuilder();
    if (input.length() == 1) {
      builder.append(input);
      builder.append(input);
      return builder.toString();
    } else {
      return input;
    }
  }

  public static String replaceWordsWithNumbers(String word) {
    if (word.length() <= 2) {
      return word;
    }

    int index = 1;
    while (index < word.length() + 1) {
      String substring = word.substring(0, index);
      for (String key : numbersMap.keySet()) {
        if (substring.contains(key)) {
          String lastLetter = substring.substring(substring.length() - 1);
          String replace = substring.replace(key, numbersMap.get(key)) + lastLetter;
          word = word.substring(index);
          word = replace + word;
          index = 1;
        }
      }
      ++index;
    }
    return word;
  }
}