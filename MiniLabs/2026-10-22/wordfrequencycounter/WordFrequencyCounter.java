import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;

public class WordFrequencyCounter {
    public static void main(String[] args) {
        System.out.println("Welcome to the word frequency counter program!");

        try(Scanner scanner = new Scanner(System.in)) {
            // 1. Read a paragraph from the user (or a hard-coded text block)
            System.out.println("Enter your paragraph:");
            // 2. Lowercase it and strip punctuation with replaceAll("[^a-z ]", "")
            String paragraph = scanner.nextLine().toLowerCase().replaceAll("[^a-z ]", "");

            // 3. Count every word with a Map<String, Integer>
            Map<String, Integer> wordCounts = new TreeMap<>();
            Set<String> uniqueWords = new HashSet<>();
            for(String word : paragraph.split(" ")) {
                int currentWordCount = wordCounts.getOrDefault(word, 0);
                wordCounts.put(word, currentWordCount + 1);

                uniqueWords.add(word);
            }
            System.out.println();
            System.out.println("Words and their counts:");
            // Gathering some data for later...
            Set<String> wordsWithMaxCount = new HashSet<>();
            int maxCount = 0;
            // 4. Print words alphabetically with their counts
            // (automatically alphabetical due to the usage of TreeMap)
            for(Map.Entry<String, Integer> entry : wordCounts.entrySet()) {
                if(entry.getValue() > maxCount) {
                    maxCount = entry.getValue();
                    wordsWithMaxCount.clear();
                    wordsWithMaxCount.add(entry.getKey());
                } else if(entry.getValue() == maxCount) {
                    wordsWithMaxCount.add(entry.getKey());
                }
                System.out.printf("%s - %d%n", entry.getKey(), entry.getValue());
            }
            System.out.println();
            // 5. Print how many unique words there are (hint: a Set)
            System.out.printf("Number of unique words: %d%n", uniqueWords.size());
            System.out.println();
            // 6. Print the most frequent word
            System.out.print("Most frequent word");
            if(wordsWithMaxCount.size() > 1) System.out.print("s");
            System.out.println(":");
            for(String wordWithMaxCount : wordsWithMaxCount) {
                System.out.printf("- %s%n", wordWithMaxCount);
            }

            System.out.println();
            // Stretch: print a bar chart using "*".repeat(count)
            System.out.println("Time for a bar chart:");
            for(Map.Entry<String, Integer> entry : wordCounts.entrySet()) {
                System.out.printf("%s %s%n", entry.getKey(), "*".repeat(entry.getValue()));
            }
        }
        
        System.out.println();
        System.out.println("Thank you for using this program. Have a nice day! :)");
    }
}