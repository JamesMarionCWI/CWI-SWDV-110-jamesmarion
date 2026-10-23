package edu.cwi.swdv110.contacts;

import java.util.List;

/** Builds the printable report for a list of cleaned contacts. */
public final class ContactReport {

    /** Use this format for the header and every row so the columns line up. */
    private static final String ROW = "%-22s %-30s %-15s %-9s%n";

    private ContactReport() { }

    /**
     * Find duplicate emails
     */
    private static boolean duplicateEmailFound(String findMe, List<Contact> contacts) {
        int numberOfSameEmail = 0;
        for (Contact contact : contacts) {
            // Stretch: **Duplicates:** flag two contacts whose emails match with `equalsIgnoreCase`.
            if (contact.email().equalsIgnoreCase(findMe)) {
                numberOfSameEmail++;
                if (numberOfSameEmail > 1) {
                    // Break out early if we have multiple of the same email
                    break;
                }
            }
        }
        return numberOfSameEmail > 1;
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
        // Part 5 – build it with a StringBuilder and String.format(ROW, ...)
        StringBuilder fullReport = new StringBuilder();
        fullReport.append(String.format(ContactReport.ROW, "NAME", "EMAIL", "PHONE", "USERNAME"));
        fullReport.append("-".repeat(78));
        fullReport.append('\n');
        int numInvalidEmails = 0;
        int numInvalidPhones = 0;
        for (Contact contact : contacts) {
            StringBuilder reportRow = new StringBuilder();
            String email = contact.email();
            boolean isValidEmail = ContactParser.isValidEmail(email);
            boolean isDuplicateEmail = ContactReport.duplicateEmailFound(email, contacts);
            if (isDuplicateEmail) {
                email = "** " + email;
            }
            if (!isValidEmail) {
                email = "!! " + email;
                numInvalidEmails++;
            }
            if (contact.phone().equals("INVALID")) {
                numInvalidPhones++;
            }
            reportRow.append(String.format(ContactReport.ROW, contact.name(), email, contact.phone(), ContactParser.makeUsername(contact.name())));

            fullReport.append(reportRow.toString());
        }
        fullReport.append("-".repeat(78));
        fullReport.append('\n');
        fullReport.append(String.format("Total contacts: %d | Invalid emails: %d | Invalid phones: %d", contacts.size(), numInvalidEmails, numInvalidPhones));
        return fullReport.toString();
    }
}
