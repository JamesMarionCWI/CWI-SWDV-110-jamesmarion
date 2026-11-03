package edu.cwi.swdv110.music;

import java.time.LocalDate;
import java.util.function.Predicate;

/** Factory methods that build reusable Predicate<Play> objects. Each body is ONE lambda. */
public final class PlayFilters {

    private PlayFilters() { }

    /** Matches plays by this user, ignoring case. */
    public static Predicate<Play> byUser(String user) {
        // Part 2
        return play -> play.user().equalsIgnoreCase(user);
    }

    /** Matches plays in this genre, ignoring case. */
    public static Predicate<Play> byGenre(String genre) {
        // Part 2
        return play -> play.genre().equalsIgnoreCase(genre);
    }

    /** Matches plays from `from` through `to`, inclusive on both ends. */
    public static Predicate<Play> between(LocalDate from, LocalDate to) {
        // Part 2
        return play -> (
            play.date().isAfter(from) || play.date().isEqual(from) // inclusive on both ends
        ) && (
            play.date().isBefore(to) || play.date().isEqual(to) // inclusive on both ends
        );
    }

    /** A "skip" is any play shorter than 30 seconds. */
    public static Predicate<Play> skipped() {
        // Part 2
        return play -> play.seconds() < 30;
    }
}
