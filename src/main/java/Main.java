package main.java;

import static java.util.Arrays.asList;
import static java.util.Comparator.reverseOrder;
import static java.util.Map.Entry.comparingByValue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class Main {

  public static void main(final String... args) {
    final Map<String, Integer> resultMap = new HashMap<>();

    final List<String> inputFileNames = getInputFileNames();

    inputFileNames.forEach(fileName -> {
      try (Stream<String> lines = Files.lines(Paths.get(fileName))) {
        lines.forEach(
          line -> getSentences(getMessage(line)).stream().filter(sentence -> sentence.contains("?")).forEach(sentence -> {
            final String rectifiedSentence = rectifySentence(sentence);
            //TODO: Add NLP here by creating tokens from the sentence and using those as keys in the resultMap
            if (resultMap.containsKey(rectifiedSentence)) {
              resultMap.put(rectifiedSentence, resultMap.get(rectifiedSentence) + 1);
            } else {
              resultMap.put(rectifiedSentence, 1);
            }
          }));
      } catch (final IOException e) {
        e.printStackTrace();
      }
    });

    System.out.println("####top 50 questions####");
    resultMap.entrySet()
      .stream()
      .sorted(comparingByValue(reverseOrder()))
      .limit(50)
      .forEach(entry -> System.out.println(entry.getKey() + " : " + entry.getValue() + " times asked"));
    System.out.println("####the end####");
  }

  private static String getMessage(final String line) {
    return line.split(":", 2)[1].trim();
  }

  private static List<String> getSentences(final String message) {
    return asList(message.split("(?<=[.?!])"));
  }

  private static String rectifySentence(final String line) {
    return line.toLowerCase().replace(",", "").trim();
  }

  private static List<String> getInputFileNames() {
    final List<String> inputFileNames = new ArrayList<>();
    inputFileNames.add("./main/resources/input1.txt");
    inputFileNames.add("./main/resources/input2.txt");

    return inputFileNames;
  }
}
