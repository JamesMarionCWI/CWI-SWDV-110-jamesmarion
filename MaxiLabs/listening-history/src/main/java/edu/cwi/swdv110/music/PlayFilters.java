package edu.cwi.swdv110.music;

import java.time.LocalDate;
import java.util.function.Predicate;

/** Factory methods that build reusable Predicate<Play> objects. Each body is ONE lambda. */
public final class PlayFilters {

    private PlayFilters() { }

    /** Matches plays by this user, ignoring case. */
    public static Predicate<Play> byUser(String user) {
        // TODO Part 2
        throw new UnsupportedOperationException("TODO");
    }

    /** Matches plays in this genre, ignoring case. */
    public static Predicate<Play> byGenre(String genre) {
        // TODO Part 2
        throw new UnsupportedOperationException("TODO");
    }

    /** Matches plays from `from` through `to`, inclusive on both ends. */
    public static Predicate<Play> between(LocalDate from, LocalDate to) {
        // TODO Part 2
        throw new UnsupportedOperationException("TODO");
    }

    /** A "skip" is any play shorter than 30 seconds. */
    public static Predicate<Play> skipped() {
        // TODO Part 2
        throw new UnsupportedOperationException("TODO");
    }
}
