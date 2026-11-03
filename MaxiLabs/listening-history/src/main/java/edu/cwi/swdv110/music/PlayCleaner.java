package edu.cwi.swdv110.music;

import java.util.List;
import java.util.function.UnaryOperator;

/** Fixes messy rows IN PLACE using the List methods that take lambdas. */
public final class PlayCleaner {

    private PlayCleaner() { }

    /** "  hip-HOP " -> "Hip-Hop",  "ELECTRONIC" -> "Electronic"  (capitalize after '-' and ' ') */
    public static String titleCase(String text) {
        // TODO Part 1 – you wrote one of these in Week 1!
        throw new UnsupportedOperationException("TODO");
    }

    /**
     * 1. removeIf: delete plays with seconds <= 0 or a blank song title.
     * 2. replaceAll: records are immutable, so replace each Play with a NEW Play whose
     *    user is lowercase and whose genre is title-cased. Write the lambda as a
     *    UnaryOperator<Play> variable first, then pass it to replaceAll.
     * Returns how many plays were removed.
     */
    public static int clean(List<Play> plays) {
        // TODO Part 1
        throw new UnsupportedOperationException("TODO");
    }
}
