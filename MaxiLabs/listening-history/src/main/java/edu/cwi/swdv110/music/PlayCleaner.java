package edu.cwi.swdv110.music;

import java.util.List;
import java.util.function.UnaryOperator;

/** Fixes messy rows IN PLACE using the List methods that take lambdas. */
public final class PlayCleaner {

    private PlayCleaner() { }

    /** "  hip-HOP " -> "Hip-Hop",  "ELECTRONIC" -> "Electronic"  (capitalize after '-' and ' ') */
    public static String titleCase(String text) {
        // Part 1 – you wrote one of these in Week 1!
        
        // Ensure the text is trimmed
        StringBuilder titleCasedString = new StringBuilder(text.trim());

        // Loop through each character to determine if it should be uppercased
        for(int i = 0; i < titleCasedString.length(); i++) {
            // If it's the first character OR if the previous character was a dash or a space
            if(i == 0 || titleCasedString.charAt(i-1) == '-' || titleCasedString.charAt(i-1) == ' ') {
                // Uppercase the current letter
                titleCasedString.setCharAt(i, Character.toUpperCase(titleCasedString.charAt(i)));
            } else {
                // Otherwise, lowercase the current letter
                titleCasedString.setCharAt(i, Character.toLowerCase(titleCasedString.charAt(i)));
            }
        }

        // Return the results
        return titleCasedString.toString();
    }

    /**
     * 1. removeIf: delete plays with seconds <= 0 or a blank song title.
     * 2. replaceAll: records are immutable, so replace each Play with a NEW Play whose
     *    user is lowercase and whose genre is title-cased. Write the lambda as a
     *    UnaryOperator<Play> variable first, then pass it to replaceAll.
     * Returns how many plays were removed.
     */
    public static int clean(List<Play> plays) {
        // Part 1
        
        // Note down the starting size so we can easily calculate how many we removed
        int startCount = plays.size();

        // Filter out invalid state
        plays.removeIf(play -> play.seconds() <= 0);
        plays.removeIf(play -> play.song().isBlank());

        // Ensure proper formatting on the valid state
        UnaryOperator<Play> cleanRecords =
            play -> new Play(
                play.user().toLowerCase(),
                play.song(),
                play.artist(),
                titleCase(play.genre()),
                play.date(),
                play.seconds()
            );
        
        // (use the formatting corrector above)
        plays.replaceAll(cleanRecords);

        // Return the number of plays we removed
        return startCount - plays.size();
    }
}
