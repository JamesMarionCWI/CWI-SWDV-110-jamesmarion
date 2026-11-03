package edu.cwi.swdv110.music;

/** Turns one Play into a line of text. Implement it with a lambda. */
@FunctionalInterface
public interface PlayFormatter {
    String format(Play play);
}
