package edu.cwi.swdv110.contacts;

/** Static helper methods that clean up one piece of a messy contact line. */
public final class ContactParser {

    private ContactParser() { }

    /**
    * Capitalizes the first letter of the word and every letter that follows a hyphen.
    * All other letters become lowercase.
    * "jOHN" -> "John"
    * "garcia-LOPEZ" -> "Garcia-Lopez"
    * "" -> ""
    */
    public static String titleCase(String word) {
        // Part 1 – hint: StringBuilder + setCharAt

        // Lowercase everything by default
        StringBuilder sb = new StringBuilder(word.toLowerCase());

        // First letter should always be uppercase
        boolean uppercaseMe = true;
        for (int i = 0; i < sb.length(); i++) {
            char currentChar = sb.charAt(i);

            // Stretch Goal: Apostrophes: make titleCase capitalize after ', so o'neil becomes O'Neil. Then update makeUsername so it still produces poneil.
            if (currentChar == '-' || currentChar == '\'') {
                // Uppercase the next letter
                uppercaseMe = true;
            } else if (uppercaseMe) {
                sb.setCharAt(i, Character.toUpperCase(currentChar));
                // Ensure the next letters aren't uppercased like this one was
                uppercaseMe = false;
            }
        }

        return sb.toString();
    }

    /**
    * Cleans a raw name.
    * - trims it and collapses runs of spaces to one space
    * - "Last, First Middle" is reordered to "First Middle Last"
    * - every word is title-cased
    * Returns "" for a blank name.
    * " smith, JOHN " -> "John Smith"
    * "DE LA CRUZ, ana" -> "Ana De La Cruz"
    * "maria garcia-lopez" -> "Maria Garcia-Lopez"
    */
    public static String normalizeName(String raw) {
        // Part 1 – hint: indexOf(','), substring, split("\\s+"), String.join

        // Trim and check if empty
        String resultString = raw.trim();
        if (resultString.isEmpty()) {
            return resultString;
        }

        // Title case every word and also replace all multiple occurrences of spaces
        String[] individualNames = resultString.split("\\s+");
        for (int i = 0; i < individualNames.length; i++) {
            individualNames[i] = ContactParser.titleCase(individualNames[i]);
        }
        resultString = String.join(" ", individualNames);

        // Side note: It'd toootally be crazy if someone included multiple commas, riiiiiight...?
        int commaIndex = resultString.indexOf(',');
        if (commaIndex >= 0) {
            // Ensure parts are trimmed
            String firstPart = resultString.substring(0, commaIndex).trim();
            String secondPart = resultString.substring(commaIndex + 1).trim();
            // Swap the parts
            resultString = secondPart + " " + firstPart;
        }

        return resultString;
    }

    /**
    * Returns true when the (trimmed) email:
    * 1. has no spaces,
    * 2. has exactly one '@' with at least one character before it, and
    * 3. has a domain (the part after '@') that contains a '.' that is not its first
    * character, does not end with '.', and never has two dots in a row.
    */
    public static boolean isValidEmail(String email) {
        // Part 2 – no regular expressions; use indexOf / lastIndexOf / substring

        // Trim the email
        email = email.trim();

        // Tracks validation progress
        boolean validationResult = true;

        // Has at least some text
        validationResult = validationResult && email.length() > 0;

        // Has no spaces in it
        validationResult = validationResult && email.indexOf(' ') < 0;

        int atSignIndex = email.indexOf('@');
        // @ sign has at least one character before it
        validationResult = validationResult && atSignIndex > 0;
        // Has exactly one @ sign in it
        validationResult = validationResult && atSignIndex == email.lastIndexOf('@');

        String domainPart = email.substring(atSignIndex + 1);
        // Ensure the domain part exists
        validationResult = validationResult && domainPart.length() > 0;
        // Ensure . is not the first character of the domain
        validationResult = validationResult && domainPart.indexOf('.') > 0;
        // Ensure . is not the last character of the domain
        validationResult = validationResult && domainPart.lastIndexOf('.') < domainPart.length() - 1;

        // Ensure the domain doesn't have two dots in a row
        validationResult = validationResult && domainPart.indexOf("..") == -1;
        
        // Return the final result
        return validationResult;
    }

    /** Trims and lowercases an email: " JSmith@Example.COM " -> "jsmith@example.com" */
    public static String normalizeEmail(String email) {
        // Part 2
        return email.trim().toLowerCase();
    }

    /**
    * Hides all but the first character before the '@':
    * "jsmith@example.com" -> "j*****@example.com"
    * If there are fewer than 2 characters before the '@' (or no '@'), return the email unchanged.
    */
    public static String maskEmail(String email) {
        // Part 2 – hint: "*".repeat(n)
        int atSignIndex = email.indexOf('@');
        // Fewer than 2 chars before the @, or @ sign isn't found
        if (atSignIndex < 2) {
            return email;
        }
        // Build the masked email using first char, asterisks up to the at sign, and then the remainder
        return email.charAt(0) + ("*".repeat(atSignIndex - 1)) + email.substring(atSignIndex);
    }

    /**
    * Keeps only the digits. If there are 11 digits and the first is '1', drop that leading 1.
    * Exactly 10 digits -> "(208) 555-0199". Anything else -> "INVALID".
    */
    public static String formatPhone(String raw) {
        // Part 3 – hint: loop over toCharArray(), Character.isDigit, StringBuilder
        StringBuilder sb = new StringBuilder();
        for (char c : raw.toCharArray()) {
            if (Character.isDigit(c)) {
                // Append only digits
                sb.append(c);
            }
        }
        if (sb.length() == 11 && sb.charAt(0) == '1') {
            sb.deleteCharAt(0);
        }
        if (sb.length() != 10) {
            return "INVALID";
        }

        return String.format("(%s) %s-%s", sb.substring(0, 3), sb.substring(3, 6), sb.substring(6, 10));
    }

    /**
    * Username = first letter of the first word + the whole LAST word of a cleaned name,
    * lowercase, letters only, at most 8 characters.
    * "John Smith" -> "jsmith"
    * "Maria Garcia-Lopez" -> "mgarcial"
    * "Ana De La Cruz" -> "acruz"
    * A one-word name uses just that word: "Bob" -> "bob"
    */
    public static String makeUsername(String fullName) {
        // usernames are lowercase
        fullName = fullName.toLowerCase();
        // Part 4
        StringBuilder sb = new StringBuilder();
        if (fullName.length() > 0) {
            sb.append(fullName.charAt(0));
        }

        int indexOfLastWord = fullName.lastIndexOf(' ');
        // Last word OR if there are no spaces, then continue on with the only name provided
        indexOfLastWord = indexOfLastWord >= 0 ? indexOfLastWord : 1;

        // Continue up to the end of the last word in the name OR until 8 characters has been reached
        for (int i = indexOfLastWord; i < fullName.length() && sb.length() < 8; i++) {
            // Ensure that only letters are included
            if (Character.isLetter(fullName.charAt(i))) {
                sb.append(fullName.charAt(i));
            }
        }

        return sb.toString();
    }

    /**
    * Parses one line of the file: "name | email | phone".
    * Returns a Contact with a normalized name, normalized email and formatted phone.
    * Throws IllegalArgumentException if the line does not have exactly 3 fields
    * or the name is blank. (An invalid email or phone is NOT an exception –
    * the report flags those.)
    */
    public static Contact parseLine(String line) {
        // Part 4 – careful: '|' means something special to split(), so escape it: "\\|"
        String[] lineParts = line.split("\\|");
        if (lineParts.length != 3) {
            throw new IllegalArgumentException("expected 3 fields but found 1");
        }
        String name = normalizeName(lineParts[0]);
        if (name.isBlank()) {
            throw new IllegalArgumentException("name is blank");
        }
        String email = normalizeEmail(lineParts[1]);
        String phone = formatPhone(lineParts[2]);
        return new Contact(name, email, phone);
    }
}
