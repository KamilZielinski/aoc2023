package org.example.day2;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Main {

  private final static Map<String, Integer> colorsAmount = new HashMap<>();

  record Game(Integer id, List<Take> takes) {
    record Take(List<Color> colors) {
      record Color(String color, Integer amount) {
      }
    }
  }

  public static void main(String[] args) throws URISyntaxException, IOException {
    colorsAmount.put("red", 12);
    colorsAmount.put("green", 13);
    colorsAmount.put("blue", 14);

    List<String> lines = Files.readAllLines(Paths.get(
        org.example.day1.Main.class.getClassLoader().getResource("day2/input.txt").toURI()));

    Map<String, String> games = new HashMap<>();
    for (String line : lines) {
      String[] game = line.split(":");
      games.put(game[0], game[1].strip());
    }

    List<Game> playedGames = new ArrayList<>();
    for (Entry<String, String> singleGame : games.entrySet()) {
      Integer gameId = Integer.valueOf(singleGame.getKey().replace("Game ", ""));
      List<String> takesList = Arrays.stream(singleGame.getValue().split(";")).toList();
      List<Game.Take> takes = new ArrayList<>();
      for (String take : takesList) {
        String[] colors = take.split(",");
        List<Game.Take.Color> takeColors = new ArrayList<>();
        for (String color : colors) {
          Integer takeAmount = Integer.valueOf(color.strip().split(" ")[0]);
          String takeColor = color.strip().split(" ")[1];
          takeColors.add(new Game.Take.Color(takeColor, takeAmount));
        }
        takes.add(new Game.Take(takeColors));
      }
      playedGames.add(new Game(gameId, takes));
    }
    List<Game> sortedGames = playedGames.stream().sorted(Comparator.comparing(o -> o.id)).toList();

    int sumPartOne = sortedGames.stream()
        .filter(game -> game.takes.stream().noneMatch(Main::notMatchingRules))
        .map(entry -> entry.id)
        .mapToInt(Integer::intValue)
        .sum();

    int sumPartTwo = sortedGames.stream()
        .map(game -> multipliedMinimumAmountsOfCubes(game.takes))
        .mapToInt(Integer::intValue)
        .sum();
    System.out.println("part one: " + sumPartOne);
    System.out.println("part two: " + sumPartTwo);
  }

  private static Integer multipliedMinimumAmountsOfCubes(List<Game.Take> takes) {
    Map<String, Integer> minValues = new HashMap<>();
    minValues.put("red", 0);
    minValues.put("blue", 0);
    minValues.put("green", 0);
    for (Game.Take take : takes) {

      take.colors().forEach(entry -> {
        Integer currentColorValue = minValues.get(entry.color);
        if (currentColorValue < entry.amount) {
          minValues.put(entry.color, entry.amount);
        }
      });
      for (Entry<String, Integer> entry : minValues.entrySet()) {
        if (entry.getValue() == 0) {
          minValues.remove(entry);
        }
      }
    }
    return minValues.entrySet().stream()
        .map(Entry::getValue)
        .mapToInt(Integer::intValue)
        .reduce(1, (a, b) -> (a * b));
  }

  private static boolean notMatchingRules(Game.Take take) {
    Map<String, Integer> clonedColorsAmount = new HashMap<>(colorsAmount);
    take.colors().forEach(color -> clonedColorsAmount.put(color.color,
        clonedColorsAmount.get(color.color) - color.amount));

    boolean isNotMatchingRules = false;
    for (Integer entry : clonedColorsAmount.values()) {
      if (entry < 0) {
        isNotMatchingRules = true;
        break;
      }
    }
    return isNotMatchingRules;
  }

}
