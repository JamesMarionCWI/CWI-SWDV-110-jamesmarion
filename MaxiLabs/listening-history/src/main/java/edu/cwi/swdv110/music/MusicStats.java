package edu.cwi.swdv110.music;

import java.time.DayOfWeek;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/** Every method is a stream pipeline – no for or while loops in this class! */
public final class MusicStats {

    private MusicStats() { }

    /**
     * Newest date first; same date -> artist A–Z; same artist -> song A–Z.
     * TODO Part 3: build it with Comparator.comparing(...).reversed().thenComparing(...)
     */
    public static final Comparator<Play> NEWEST_FIRST = null;

    /** Total seconds of the plays matching `filter`, divided by 60 (whole minutes). */
    public static long totalMinutes(List<Play> plays, Predicate<Play> filter) {
        // TODO – filter, mapToInt, sum
        throw new UnsupportedOperationException("TODO");
    }

    /** genre -> number of plays, sorted by genre. */
    public static Map<String, Long> playsPerGenre(List<Play> plays) {
        // TODO – groupingBy(classifier, TreeMap::new, counting())
        throw new UnsupportedOperationException("TODO");
    }

    /** user -> total seconds, sorted by user. */
    public static Map<String, Integer> secondsPerUser(List<Play> plays) {
        // TODO – summingInt
        throw new UnsupportedOperationException("TODO");
    }

    /**
     * The n artists with the most plays, most first. Ties: artist name A–Z.
     * Hint: group & count into a Map, then stream its entrySet() and sort with
     * Map.Entry.<String, Long>comparingByValue().reversed().thenComparing(Map.Entry.comparingByKey())
     */
    public static List<String> topArtists(List<Play> plays, int n) {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    /** The single longest play, or Optional.empty() for an empty list. */
    public static Optional<Play> longestPlay(List<Play> plays) {
        // TODO – max(Comparator.comparingInt(...))
        throw new UnsupportedOperationException("TODO");
    }

    /** Distinct song titles by this artist (ignore case), sorted A–Z. */
    public static List<String> songsBy(List<Play> plays, String artist) {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    /** The n most recent plays, ordered by NEWEST_FIRST. */
    public static List<Play> recent(List<Play> plays, int n) {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    /** MONDAY..SUNDAY -> number of plays (only days that have plays). Uses Week 3's java.time! */
    public static Map<DayOfWeek, Long> playsByDay(List<Play> plays) {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    /** Fraction (0.0–1.0) of this user's plays that were skips. 0.0 if the user has no plays. */
    public static double skipRate(List<Play> plays, String user) {
        // TODO – reuse PlayFilters.byUser and PlayFilters.skipped
        throw new UnsupportedOperationException("TODO");
    }

    /**
     * The genre this user played most. Ties go to the genre that comes first A–Z.
     * Optional.empty() if the user has no plays.
     */
    public static Optional<String> topGenre(List<Play> plays, String user) {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }
}
