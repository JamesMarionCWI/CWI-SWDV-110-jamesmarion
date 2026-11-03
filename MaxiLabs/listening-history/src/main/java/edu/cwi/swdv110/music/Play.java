package edu.cwi.swdv110.music;

import java.time.LocalDate;

/** One time a user played a song. */
public record Play(String user, String song, String artist, String genre, LocalDate date, int seconds) {

    /**
     * Parses "maya,Paper Satellites,Static Bloom,Indie,2026-09-28,161".
     * Trim every field. Wrong number of fields -> IllegalArgumentException.
     * (An empty song title is allowed here – PlayCleaner removes it later.)
     */
    public static Play parse(String csvLine) {
        // TODO Part 1
        throw new UnsupportedOperationException("TODO");
    }
}
