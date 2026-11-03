package edu.cwi.swdv110.music;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/** One time a user played a song. */
public record Play(String user, String song, String artist, String genre, LocalDate date, int seconds) {

    /**
     * Parses "maya,Paper Satellites,Static Bloom,Indie,2026-09-28,161".
     * Trim every field. Wrong number of fields -> IllegalArgumentException.
     * (An empty song title is allowed here – PlayCleaner removes it later.)
     */
    public static Play parse(String csvLine) throws IllegalArgumentException {
        String[] parts = csvLine.split(",");
        
        // Test the number of fields we identified in the csv
        if(parts.length != 6) {
            throw new IllegalArgumentException("Wrong number of fields!");
        }

        // Test if we even can parse the date
        LocalDate date;
        try {
            // Make sure these fields are trimmed before we parse
            date = LocalDate.parse(parts[4].trim());
        } catch (DateTimeParseException ex) {
            throw new IllegalArgumentException("Could not parse the date part!");
        }

        // Test if we even can parse the seconds
        int seconds;
        try {
            // Make sure these fields are trimmed before we parse
            seconds = Integer.parseInt(parts[5].trim());
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Could not parse the seconds part!");
        }

        // Return the trimmed results
        return new Play(
            parts[0].trim(),
            parts[1].trim(),
            parts[2].trim(),
            parts[3].trim(),
            date,
            seconds
        );
    }
}
