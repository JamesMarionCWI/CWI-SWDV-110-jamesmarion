package edu.cwi.swdv110.contacts;

import java.util.List;

/** Builds the printable report for a list of cleaned contacts. */
public final class ContactReport {

    /** Use this format for the header and every row so the columns line up. */
    private static final String ROW = "%-22s %-30s %-15s %-9s%n";

    private ContactReport() {
    }

    /**
    * Returns the full report as ONE string (see the sample output in Part 5):
    * - a header row: NAME EMAIL PHONE USERNAME
    * - a line of 78 dashes
    * - one row per contact; put "!! " in front of an invalid email
    * - another line of dashes
    * - "Total contacts: N | Invalid emails: N | Invalid phones: N"
    */
    public static String buildReport(List<Contact> contacts) {
        // TODO Part 5 – build it with a StringBuilder and String.format(ROW, ...)
        throw new UnsupportedOperationException("TODO");
    }
}
