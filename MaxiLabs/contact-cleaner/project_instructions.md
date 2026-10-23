# Week 1 Coding Exercise: Contact List Cleaner

**Chapter 14 – Advanced String Manipulation**
**Estimated time:** 3–5 hours
**Builds on:** in-class practices *Login Checker*, *Name & Email Parser*, *Text Analyzer*

---

## The scenario

The CWI tutoring center exported its contact list from an old spreadsheet, and the data is a mess. Names are in mixed formats, emails have random capital letters, and phone numbers look like `555.123.4567`, `(208) 555-0199` or `+1 (986) 555-0123`.

You'll write a Java program that reads the messy file, cleans every line, flags problems, and prints a neat report. It also lets the user search the cleaned list by name.

## What you'll practice

| Skill from Chapter 14 | Where you use it |
|---|---|
| `trim()`, `toLowerCase()`, `toUpperCase()` | Normalizing names and emails |
| `indexOf()`, `lastIndexOf()`, `substring()` | Validating emails, reordering "Last, First" names |
| `split()` with a regular-expression delimiter, `String.join()` | Splitting lines on `\|` and names on whitespace |
| `equals()` / `equalsIgnoreCase()` / `contains()` | Searching and comparing |
| Looping over characters, `Character.isDigit()` / `isLetter()` | Pulling digits out of phone numbers |
| `StringBuilder` (`append`, `setCharAt`, `deleteCharAt`) | Building names, phone digits, and the report |
| `String.format()` | Lining up the report columns |

## Suggested time plan

| Part | Task | Time |
|---|---|---|
| 0 | Project setup | 20–30 min |
| 1 | Names: `titleCase`, `normalizeName` | 45–60 min |
| 2 | Emails: `isValidEmail`, `normalizeEmail`, `maskEmail` | 45 min |
| 3 | Phones: `formatPhone` | 30–45 min |
| 4 | Usernames & parsing a full line | 30–45 min |
| 5 | Report & search program | 45–60 min |
| 6 | README reflection | 15 min |

---

## Part 0 – Setup

> First time using Maven? Complete **`swdv110_Exercise_Setup.md`** first.

Create a folder named `contact-cleaner` with this structure:

```
contact-cleaner/
├── pom.xml
├── contacts.txt
└── src/
├── main/java/edu/cwi/swdv110/contacts/
│ ├── App.java
│ ├── Contact.java
│ ├── ContactParser.java
│ └── ContactReport.java
└── test/java/edu/cwi/swdv110/contacts/
└── ContactParserTest.java
```

**File:** `pom.xml`

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
<modelVersion>4.0.0</modelVersion>

<groupId>edu.cwi.swdv110</groupId>
<artifactId>contact-cleaner</artifactId>
<version>1.0-SNAPSHOT</version>

<properties>
<maven.compiler.release>17</maven.compiler.release>
<project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
<exec.mainClass>edu.cwi.swdv110.contacts.App</exec.mainClass>
</properties>

<dependencies>
<dependency>
<groupId>org.junit.jupiter</groupId>
<artifactId>junit-jupiter</artifactId>
<version>5.14.4</version>
<scope>test</scope>
</dependency>
</dependencies>

<build>
<plugins>
<plugin>
<groupId>org.apache.maven.plugins</groupId>
<artifactId>maven-compiler-plugin</artifactId>
<version>3.15.0</version>
</plugin>
<plugin>
<groupId>org.apache.maven.plugins</groupId>
<artifactId>maven-surefire-plugin</artifactId>
<version>3.6.0</version>
</plugin>
<plugin>
<groupId>org.codehaus.mojo</groupId>
<artifactId>exec-maven-plugin</artifactId>
<version>3.6.4</version>
</plugin>
</plugins>
</build>
</project>
```

**File:** `contacts.txt` (in the project folder, **not** in `src`)

```text
# name | email | phone
smith, JOHN | JSmith@Example.COM | 555.123.4567
maria garcia-lopez|maria.g@college.edu|(208) 555-0199
DE LA CRUZ, ana | ana.delacruz@mail.com | 1-208-555-0142
bob | bob@ | 555-0100
lee, KIM|kim.lee@cwi.edu|208 555 0175
this line is broken
TRAN, minh| minh tran@example.com |2085550111
o'neil, pat | pat.oneil@example.org | +1 (986) 555-0123
| nobody@example.com | 208-555-0000
```

### Starter code

**File:** `src/main/java/edu/cwi/swdv110/contacts/Contact.java`

```java
package edu.cwi.swdv110.contacts;

/** One cleaned-up contact. Records give us a constructor, getters, equals and toString for free. */
public record Contact(String name, String email, String phone) { }
```

**File:** `src/main/java/edu/cwi/swdv110/contacts/ContactParser.java`

```java
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
// TODO Part 1 – hint: StringBuilder + setCharAt
throw new UnsupportedOperationException("TODO");
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
// TODO Part 1 – hint: indexOf(','), substring, split("\\s+"), String.join
throw new UnsupportedOperationException("TODO");
}

/**
* Returns true when the (trimmed) email:
* 1. has no spaces,
* 2. has exactly one '@' with at least one character before it, and
* 3. has a domain (the part after '@') that contains a '.' that is not its first
* character, does not end with '.', and never has two dots in a row.
*/
public static boolean isValidEmail(String email) {
// TODO Part 2 – no regular expressions; use indexOf / lastIndexOf / substring
throw new UnsupportedOperationException("TODO");
}

/** Trims and lowercases an email: " JSmith@Example.COM " -> "jsmith@example.com" */
public static String normalizeEmail(String email) {
// TODO Part 2
throw new UnsupportedOperationException("TODO");
}

/**
* Hides all but the first character before the '@':
* "jsmith@example.com" -> "j*****@example.com"
* If there are fewer than 2 characters before the '@' (or no '@'), return the email unchanged.
*/
public static String maskEmail(String email) {
// TODO Part 2 – hint: "*".repeat(n)
throw new UnsupportedOperationException("TODO");
}

/**
* Keeps only the digits. If there are 11 digits and the first is '1', drop that leading 1.
* Exactly 10 digits -> "(208) 555-0199". Anything else -> "INVALID".
*/
public static String formatPhone(String raw) {
// TODO Part 3 – hint: loop over toCharArray(), Character.isDigit, StringBuilder
throw new UnsupportedOperationException("TODO");
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
// TODO Part 4
throw new UnsupportedOperationException("TODO");
}

/**
* Parses one line of the file: "name | email | phone".
* Returns a Contact with a normalized name, normalized email and formatted phone.
* Throws IllegalArgumentException if the line does not have exactly 3 fields
* or the name is blank. (An invalid email or phone is NOT an exception –
* the report flags those.)
*/
public static Contact parseLine(String line) {
// TODO Part 4 – careful: '|' means something special to split(), so escape it: "\\|"
throw new UnsupportedOperationException("TODO");
}
}
```

**File:** `src/main/java/edu/cwi/swdv110/contacts/ContactReport.java`

```java
package edu.cwi.swdv110.contacts;

import java.util.List;

/** Builds the printable report for a list of cleaned contacts. */
public final class ContactReport {

/** Use this format for the header and every row so the columns line up. */
private static final String ROW = "%-22s %-30s %-15s %-9s%n";

private ContactReport() { }

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
```

**File:** `src/main/java/edu/cwi/swdv110/contacts/App.java`

```java
package edu.cwi.swdv110.contacts;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class App {

public static void main(String[] args) throws IOException {
// Reading files is covered later in Murach – this one line gives you every line of the file.
List<String> lines = Files.readAllLines(Path.of("contacts.txt"));
List<Contact> contacts = new ArrayList<>();

// TODO Part 5a: loop over lines
// - skip blank lines and lines that start with '#'
// - parseLine(...) each remaining line and add it to contacts
// - catch IllegalArgumentException and print "Skipped line N: <message>"
// (N is the line number in the file, starting at 1)

// TODO Part 5b: print ContactReport.buildReport(contacts)

// TODO Part 5c: search loop
// - prompt "Search (or 'quit'): "
// - 'quit' (any capitalization) ends the program
// - otherwise print every contact whose name contains the term, ignoring case,
// showing the name and the MASKED email, then "N match(es)"
Scanner sc = new Scanner(System.in);
}
}
```

### Provided tests

Don't change this file. Run `mvn test` as you finish each Part and watch the failures disappear.

**File:** `src/test/java/edu/cwi/swdv110/contacts/ContactParserTest.java`

```java
package edu.cwi.swdv110.contacts;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.Test;

class ContactParserTest {

// ---------- Part 1: names ----------
@Test
void titleCaseSimpleWord() {
assertEquals("John", ContactParser.titleCase("jOHN"));
}

@Test
void titleCaseHyphenatedWord() {
assertEquals("Garcia-Lopez", ContactParser.titleCase("garcia-LOPEZ"));
}

@Test
void titleCaseEmpty() {
assertEquals("", ContactParser.titleCase(""));
}

@Test
void normalizeNameReordersLastFirst() {
assertEquals("John Smith", ContactParser.normalizeName(" smith, JOHN "));
}

@Test
void normalizeNameMultiWordLastName() {
assertEquals("Ana De La Cruz", ContactParser.normalizeName("DE LA CRUZ, ana"));
}

@Test
void normalizeNameCollapsesSpaces() {
assertEquals("Maria Garcia-Lopez", ContactParser.normalizeName("maria garcia-lopez"));
}

@Test
void normalizeNameBlank() {
assertEquals("", ContactParser.normalizeName(" "));
}

// ---------- Part 2: emails ----------
@Test
void validEmails() {
assertTrue(ContactParser.isValidEmail("JSmith@Example.COM"));
assertTrue(ContactParser.isValidEmail(" maria.g@college.edu "));
assertTrue(ContactParser.isValidEmail("a@b.co"));
}

@Test
void invalidEmails() {
assertFalse(ContactParser.isValidEmail("bob@"), "nothing after @");
assertFalse(ContactParser.isValidEmail("@example.com"), "nothing before @");
assertFalse(ContactParser.isValidEmail("a@b"), "no dot in domain");
assertFalse(ContactParser.isValidEmail("minh tran@example.com"), "space");
assertFalse(ContactParser.isValidEmail("a@@b.com"), "two @");
assertFalse(ContactParser.isValidEmail("a@b..com"), "double dot");
assertFalse(ContactParser.isValidEmail("a@.com"), "dot first");
assertFalse(ContactParser.isValidEmail("a@com."), "dot last");
}

@Test
void normalizeEmail() {
assertEquals("jsmith@example.com", ContactParser.normalizeEmail(" JSmith@Example.COM "));
}

@Test
void maskEmail() {
assertEquals("j*****@example.com", ContactParser.maskEmail("jsmith@example.com"));
assertEquals("a@b.co", ContactParser.maskEmail("a@b.co"));
}

// ---------- Part 3: phones ----------
@Test
void formatPhoneVariousStyles() {
assertEquals("(555) 123-4567", ContactParser.formatPhone("555.123.4567"));
assertEquals("(208) 555-0199", ContactParser.formatPhone("(208) 555-0199"));
assertEquals("(208) 555-0175", ContactParser.formatPhone("208 555 0175"));
assertEquals("(208) 555-0111", ContactParser.formatPhone("2085550111"));
}

@Test
void formatPhoneDropsLeadingOne() {
assertEquals("(208) 555-0142", ContactParser.formatPhone("1-208-555-0142"));
assertEquals("(986) 555-0123", ContactParser.formatPhone("+1 (986) 555-0123"));
}

@Test
void formatPhoneInvalid() {
assertEquals("INVALID", ContactParser.formatPhone("555-0100"));
assertEquals("INVALID", ContactParser.formatPhone("call me"));
assertEquals("INVALID", ContactParser.formatPhone("22085550111"));
}

// ---------- Part 4: usernames & lines ----------
@Test
void makeUsername() {
assertEquals("jsmith", ContactParser.makeUsername("John Smith"));
assertEquals("mgarcial", ContactParser.makeUsername("Maria Garcia-Lopez"));
assertEquals("acruz", ContactParser.makeUsername("Ana De La Cruz"));
assertEquals("poneil", ContactParser.makeUsername("Pat O'neil"));
assertEquals("bob", ContactParser.makeUsername("Bob"));
}

@Test
void parseLineCleansEveryField() {
Contact c = ContactParser.parseLine(" smith, JOHN | JSmith@Example.COM | 555.123.4567");
assertEquals(new Contact("John Smith", "jsmith@example.com", "(555) 123-4567"), c);
}

@Test
void parseLineKeepsBadPhoneAsInvalid() {
Contact c = ContactParser.parseLine("bob | bob@ | 555-0100");
assertEquals("INVALID", c.phone());
assertEquals("bob@", c.email());
}

@Test
void parseLineRejectsWrongFieldCount() {
assertThrows(IllegalArgumentException.class,
() -> ContactParser.parseLine("this line is broken"));
}

@Test
void parseLineRejectsBlankName() {
assertThrows(IllegalArgumentException.class,
() -> ContactParser.parseLine(" | nobody@example.com | 208-555-0000"));
}

// ---------- Part 5: report ----------
@Test
void reportHasHeaderRowsAndTotals() {
List<Contact> list = List.of(
new Contact("John Smith", "jsmith@example.com", "(555) 123-4567"),
new Contact("Bob", "bob@", "INVALID"));
String report = ContactReport.buildReport(list);

assertTrue(report.startsWith("NAME"), "report should start with the header");
assertTrue(report.contains("-".repeat(78)));
assertTrue(report.contains(String.format("%-22s %-30s %-15s %-9s",
"John Smith", "jsmith@example.com", "(555) 123-4567", "jsmith")));
assertTrue(report.contains("!! bob@"), "invalid emails are flagged with !!");
assertTrue(report.contains("Total contacts: 2 | Invalid emails: 1 | Invalid phones: 1"));
}
}
```

---

## Part 1 – Names (`titleCase`, `normalizeName`)

1. `titleCase`: lowercase the whole word, then capitalize index 0 and every character right after a `-`. A `StringBuilder` lets you change one character with `setCharAt`.
2. `normalizeName`:
- `trim()` first. If the result is empty, return `""`.
- If there's a comma, swap the parts: everything **after** the comma, a space, then everything **before** it.
- `split("\\s+")` breaks on *one or more* whitespace characters. Title-case each word, then `String.join(" ", words)`.

✅ Checkpoint: `mvn test` – the five name tests pass.

## Part 2 – Emails

- `isValidEmail`: work through the rules in the Javadoc one at a time. `indexOf('@') == lastIndexOf('@')` tells you there's only one `@`. Use `substring` to get the domain.
- Don't use regular expressions (`matches`) here; the point is to practice the search methods.
- `maskEmail`: one line with `charAt`, `"*".repeat(...)`, and `substring`.

## Part 3 – Phone numbers

Loop over `raw.toCharArray()` and `append` only the digits to a `StringBuilder`. Handle the leading `1`, check the length, then build the result with `String.format("(%s) %s-%s", ...)` and three `substring` calls.

## Part 4 – Usernames & full lines

- `makeUsername`: watch the `"Pat O'neil"` → `"poneil"` case. The apostrophe is not a letter.
- `parseLine`: `line.split("\\|")`. A plain `"|"` won't work, because `split` treats its argument as a regular expression and `|` means "or". Check the number of fields, clean each field with your earlier methods, and return a new `Contact`.

## Part 5 – Report & search (`App`)

Finish `buildReport`, then complete `main`. Run it with:

```bash
mvn -q compile exec:java
```

Your output should match this, including the column alignment:

```text
Skipped line 7: expected 3 fields but found 1
Skipped line 10: name is blank

NAME EMAIL PHONE USERNAME
------------------------------------------------------------------------------
John Smith jsmith@example.com (555) 123-4567 jsmith
Maria Garcia-Lopez maria.g@college.edu (208) 555-0199 mgarcial
Ana De La Cruz ana.delacruz@mail.com (208) 555-0142 acruz
Bob !! bob@ INVALID bob
Kim Lee kim.lee@cwi.edu (208) 555-0175 klee
Minh Tran !! minh tran@example.com (208) 555-0111 mtran
Pat O'neil pat.oneil@example.org (986) 555-0123 poneil
------------------------------------------------------------------------------
Total contacts: 7 | Invalid emails: 2 | Invalid phones: 1

Search (or 'quit'): AN
Ana De La Cruz a***********@mail.com
Minh Tran m********@example.com
2 match(es)

Search (or 'quit'): quit
Bye!
```

---

## Part 6 – README reflection

Create `README.md` in the project folder and answer in 2–4 sentences each:

1. Strings are **immutable**. Name a place in your code where forgetting that would have caused a bug.
2. Why does `split("|")` behave differently from `split("\\|")`?
3. Where did you choose `StringBuilder` instead of `+`, and why?
4. Paste the output of `mvn test` (the summary line is enough).

---

## Stretch goals (pick any)

- **Apostrophes:** make `titleCase` capitalize after `'`, so `o'neil` becomes `O'Neil`. Then update `makeUsername` so it still produces `poneil`.
- **Duplicates:** flag two contacts whose emails match with `equalsIgnoreCase`.
- **Export:** write a cleaned `contacts_clean.csv` using `String.join(",", ...)` and `Files.writeString(...)`.
- **Text block:** print a welcome banner with a text block and `formatted()`.
- **More tests:** add at least 3 test methods of your own to `ContactParserTest` for edge cases you thought of.

---

## Submission checklist

- [ ] `mvn test`: **all provided tests pass**
- [ ] `mvn -q compile exec:java` prints the report and the search works
- [ ] `README.md` answers all Part 6 questions
- [ ] Code is committed and pushed to GitHub (with a `.gitignore` that excludes `target/`)

## Grading (100 points)

| Area | Points |
|---|---|
| Provided tests pass (Parts 1–4) | 40 |
| Report formatting & totals (Part 5) | 15 |
| `App`: skips bad lines with messages, search works | 15 |
| Code quality: names, comments, no copy-paste, uses `StringBuilder` appropriately | 15 |
| README reflection | 10 |
| Git: multiple meaningful commits | 5 |
| Stretch goals | up to +5 bonus |