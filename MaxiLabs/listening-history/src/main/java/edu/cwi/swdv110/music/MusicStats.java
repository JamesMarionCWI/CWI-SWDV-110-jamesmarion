package edu.cwi.swdv110.music;

import java.time.DayOfWeek;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.Map.Entry;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/** Every method is a stream pipeline – no for or while loops in this class! */
public final class MusicStats {

    private MusicStats() { }

    /**
     * Newest date first; same date -> artist A–Z; same artist -> song A–Z.
     * Part 3: build it with Comparator.comparing(...).reversed().thenComparing(...)
     */
    public static final Comparator<Play> NEWEST_FIRST = Comparator.comparing(Play::date).reversed().thenComparing(Play::artist).thenComparing(Play::song);

    /** Total seconds of the plays matching `filter`, divided by 60 (whole minutes). */
    public static long totalMinutes(List<Play> plays, Predicate<Play> filter) {
        // filter, mapToInt, sum
        return plays.stream().filter(filter).mapToInt(Play::seconds).sum() / 60;
    }

    /** genre -> number of plays, sorted by genre. */
    public static Map<String, Long> playsPerGenre(List<Play> plays) {
        // groupingBy(classifier, TreeMap::new, counting())
        return plays.stream().collect(
            Collectors.groupingBy(
                Play::genre, // Genre
                TreeMap::new, // Sort by
                Collectors.counting() // Total occurrences
            )
        );
    }

    /** user -> total seconds, sorted by user. */
    public static Map<String, Integer> secondsPerUser(List<Play> plays) {
        // summingInt
        return plays.stream().collect(
            Collectors.groupingBy(
                Play::user, // User
                TreeMap::new, // Sort by
                Collectors.summingInt(Play::seconds) // Total seconds
            )
        );
    }

    /**
     * The n artists with the most plays, most first. Ties: artist name A–Z.
     * Hint: group & count into a Map, then stream its entrySet() and sort with
     * Map.Entry.<String, Long>comparingByValue().reversed().thenComparing(Map.Entry.comparingByKey())
     */
    public static List<String> topArtists(List<Play> plays, int n) {
        return
            plays.stream().collect(
                Collectors.groupingBy(
                    Play::artist,
                    HashMap::new,
                    Collectors.counting()
                )
            ).entrySet().stream().sorted(
                Entry.<String, Long>comparingByValue()
                .reversed()
                .thenComparing(Entry.comparingByKey())
            )
            .limit(n)
            .map(Entry::getKey)
            .toList();
    }

    /** The single longest play, or Optional.empty() for an empty list. */
    public static Optional<Play> longestPlay(List<Play> plays) {
        // max(Comparator.comparingInt(...))
        return plays.stream().max(Comparator.comparingInt(Play::seconds));
    }

    /** Distinct song titles by this artist (ignore case), sorted A–Z. */
    public static List<String> songsBy(List<Play> plays, String artist) {
        return plays.stream()
            .filter(play -> play.artist().equalsIgnoreCase(artist))
            .map(Play::song)
            .distinct()
            .toList();
    }

    /** The n most recent plays, ordered by NEWEST_FIRST. */
    public static List<Play> recent(List<Play> plays, int n) {
        return
            plays.stream()
            .sorted(NEWEST_FIRST)
            .limit(n)
            .toList();
    }

    /** MONDAY..SUNDAY -> number of plays (only days that have plays). Uses Week 3's java.time! */
    public static Map<DayOfWeek, Long> playsByDay(List<Play> plays) {
        return plays.stream()
        .map(Play::date)
        .collect(
            Collectors.groupingBy(
                DayOfWeek::from,
                TreeMap::new,
                Collectors.counting()
            )
        );
    }

    /** Fraction (0.0–1.0) of this user's plays that were skips. 0.0 if the user has no plays. */
    public static double skipRate(List<Play> plays, String user) {
        // reuse PlayFilters.byUser and PlayFilters.skipped
        List<Play> playsByUser = plays.stream().filter(PlayFilters.byUser(user)).toList();
        
        if(playsByUser.isEmpty()) {
            // Keeps us from dividing by 0 later!
            return 0.0;
        }

        List<Play> skippedPlaysByUser = playsByUser.stream().filter(PlayFilters.skipped()).toList();
        return (double) skippedPlaysByUser.size() / playsByUser.size();
    }

    /**
     * The genre this user played most. Ties go to the genre that comes first A–Z.
     * Optional.empty() if the user has no plays.
     */
    public static Optional<String> topGenre(List<Play> plays, String user) {
        return plays.stream()
            .filter(PlayFilters.byUser(user))
            .collect(
                Collectors.groupingBy(
                    Play::genre,
                    HashMap::new,
                    Collectors.counting()
                )
            ).entrySet().stream()
            .max(
                Map.Entry.<String, Long>comparingByValue()
                // Reverses only the Key comparator, and only in cases where the value ties
                // Needs to be reversed (aka, in descending order) because we're in a Max function
                .thenComparing(Entry.comparingByKey(Comparator.reverseOrder()))
            )
            .map(Entry::getKey);
    }
}
