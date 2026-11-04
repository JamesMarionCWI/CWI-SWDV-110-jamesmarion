# Week 4 Coding Exercise: Listening History Analyzer

**Chapter 16 – Lambda Expressions (and the Stream API)**
**Estimated time:** 4–6 hours
**Builds on:** in-class practices *Lambda Calculator*, *Student Roster*, *Sales Report With Streams*

---

## The scenario

Every music app has a "Year in Review" feature: your top artists, minutes listened, favorite genre. Behind it is a pile of raw play records and a lot of filtering, grouping and sorting.

You'll write that engine for a small music-streaming startup. The data is a CSV export of plays for three users, and it has a few problems you must clean up. Then you'll answer questions with **lambdas, method references and stream pipelines**: no `for` loops allowed in the stats class.

## What you'll practice

| Chapter 16 concept | Where you use it |
|---|---|
| Writing your own `@FunctionalInterface` | `PlayFormatter` |
| `Predicate<T>`, plus `and`, `or`, `negate` | `PlayFilters`, combining filters |
| `UnaryOperator<T>`, `List.removeIf`, `List.replaceAll` | `PlayCleaner` |
| Method references (`Play::seconds`, `Play::parse`, `compact::format`) | Throughout |
| `Comparator.comparing`, `thenComparing`, `reversed` | Sorting plays and artists |
| Streams: `filter`, `map`, `mapToInt`, `sorted`, `distinct`, `limit`, `toList` | `MusicStats` |
| Collectors: `groupingBy`, `counting`, `summingInt`, `joining` | Reports |
| `Optional` (`max`, `ifPresent`, `orElse`) | Longest play, top genre |

## Suggested time plan

| Part | Task | Time |
|---|---|---|
| 0 | Setup | 20–30 min |
| 1 | Parsing & cleaning (`Play.parse`, `PlayCleaner`) | 45 min |
| 2 | Filters as lambdas (`PlayFilters`) | 30–45 min |
| 3 | Stream reports (`MusicStats`) | 90–120 min |
| 4 | The report program (`App`) & your own formatter | 45–60 min |
| 5 | README reflection | 15 min |

---

## Part 0 – Setup

> First time using Maven? Complete **`swdv110_Exercise_Setup.md`** first.

```
listening-history/
├── pom.xml
├── plays.csv
└── src/
    ├── main/java/edu/cwi/swdv110/music/
    │   ├── App.java
    │   ├── MusicStats.java
    │   ├── Play.java
    │   ├── PlayCleaner.java
    │   ├── PlayFilters.java
    │   └── PlayFormatter.java
    └── test/java/edu/cwi/swdv110/music/
        └── MusicTest.java
```

**File:** `pom.xml`

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion>

  <groupId>edu.cwi.swdv110</groupId>
  <artifactId>listening-history</artifactId>
  <version>1.0-SNAPSHOT</version>

  <properties>
    <maven.compiler.release>17</maven.compiler.release>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    <exec.mainClass>edu.cwi.swdv110.music.App</exec.mainClass>
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

**File:** `plays.csv` (in the project folder). The bands are fictional.

```text
user,song,artist,genre,date,seconds
maya,Paper Satellites,Static Bloom,Indie,2026-09-28,161
sam,Porch Light,Sierra Vale,Country,2026-09-28,9
sam,Text Me Back,Kiko Ray,Pop,2026-09-28,15
sam,Weekend Mode,Kiko Ray,Pop,2026-09-29,18
jordan,Pulse Width,Lumen,Electronic,2026-09-29,254
maya,Pulse Width,Lumen,  electronic ,2026-09-30,196
maya,Paper Satellites,Static Bloom,Indie,2026-09-30,166
jordan,Recursion,MC Kestrel,Hip-Hop,2026-09-30,183
jordan,Lines and Loops,MC Kestrel,Hip-Hop,2026-09-30,288
maya,Neon Sagebrush,Static Bloom,Indie,2026-09-30,262
jordan,Weekend Mode,Kiko Ray,Pop,2026-10-01,211
sam,Iron Horse,Basalt,Rock,2026-10-01,254
jordan,Boise Bounce,MC Kestrel,hip-hop,2026-10-02,255
jordan,Boise Bounce,MC Kestrel,Hip-Hop,2026-10-02,185
sam,Weekend Mode,Kiko Ray,Pop,2026-10-03,161
jordan,Lava Fields,Basalt,Rock,2026-10-02,0
jordan,Sugar Static,Kiko Ray,Pop,2026-10-03,259
maya,Neon Sagebrush,Static Bloom,Indie,2026-10-03,262
jordan,Sugar Static,Kiko Ray,Pop,2026-10-04,273
jordan,Recursion,MC Kestrel,Hip-Hop,2026-10-04,209
maya,Paper Satellites,Static Bloom,Indie,2026-10-04,184
Maya,Slow Static,Static Bloom,Indie,2026-10-04,182
jordan,Lines and Loops,MC Kestrel,Hip-Hop,2026-10-04,6
jordan,Boise Bounce,MC Kestrel,Hip-Hop,2026-10-05,279
jordan,Weekend Mode,Kiko Ray,Pop,2026-10-05,224
jordan,Sugar Static,Kiko Ray,Pop,2026-10-05,196
maya,Two A.M.,The Midnight Owls,Indie,2026-10-05,21
maya,Two A.M.,The Midnight Owls,Indie,2026-10-06,25
jordan,Night Shift,Lumen,ELECTRONIC,2026-10-06,17
sam,Iron Horse,Basalt,Rock,2026-10-07,217
sam,,Sierra Vale,Country,2026-10-07,201
sam,Porch Light,Sierra Vale,Country,2026-10-08,6
maya,Paper Satellites,Static Bloom,Indie,2026-10-08,237
sam,Weekend Mode,Kiko Ray,Pop,2026-10-08,186
jordan,Iron Horse,Basalt,Rock,2026-10-08,198
maya,Slow Static,Static Bloom,Indie,2026-10-09,186
maya,Moth to a Lamp,The Midnight Owls,Indie,2026-10-09,213
maya,Owyhee Nights,Static Bloom,Indie,2026-10-10,183
jordan,Recursion,MC Kestrel,Hip-Hop,2026-10-10,205
jordan,Boise Bounce,MC Kestrel,Hip-Hop,2026-10-11,281
maya,Afterglow,Lumen,Electronic,2026-10-11,171
maya,Slow Static,Static Bloom,Indie,2026-10-11,150
maya,Afterglow,Lumen,Electronic,2026-10-11,236
jordan,Iron Horse,Basalt,Rock,2026-10-11,219
```

Spot the problems: inconsistent genre capitalization, a capitalized username, a 0-second play, and a row with no song title.

### Starter code

**File:** `src/main/java/edu/cwi/swdv110/music/Play.java`

```java
package edu.cwi.swdv110.music;

import java.time.LocalDate;

/** One time a user played a song. */
public record Play(String user, String song, String artist, String genre, LocalDate date, int seconds) {

    /**
     * Parses "maya,Paper Satellites,Static Bloom,Indie,2026-09-28,161".
     * Trim every field. Wrong number of fields -> IllegalArgumentException.
     * (An empty song title is allowed here – PlayCleaner removes it later.)
     */
    public static Play parse(String csvLine) {
        // TODO Part 1
        throw new UnsupportedOperationException("TODO");
    }
}
```

**File:** `src/main/java/edu/cwi/swdv110/music/PlayFormatter.java`

```java
package edu.cwi.swdv110.music;

/** Turns one Play into a line of text. Implement it with a lambda. */
@FunctionalInterface
public interface PlayFormatter {
    String format(Play play);
}
```

**File:** `src/main/java/edu/cwi/swdv110/music/PlayCleaner.java`

```java
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
```

**File:** `src/main/java/edu/cwi/swdv110/music/PlayFilters.java`

```java
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
```

**File:** `src/main/java/edu/cwi/swdv110/music/MusicStats.java`

```java
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
```

**File:** `src/main/java/edu/cwi/swdv110/music/App.java`

```java
package edu.cwi.swdv110.music;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class App {

    public static void main(String[] args) throws IOException {
        // TODO Part 4a: ONE stream pipeline: read all lines of plays.csv, skip the header,
        //   drop blank lines, map with Play::parse, and collect into an ArrayList
        //   (Collectors.toCollection(ArrayList::new) – clean() needs a list it can modify).
        List<Play> plays = new ArrayList<>();

        // TODO Part 4b: clean the list and print "Loaded N plays (M bad rows removed)"

        // TODO Part 4c: create two PlayFormatter lambdas:
        //   compact  -> "Afterglow – Lumen"
        //   detailed -> "2026-10-11  maya    Afterglow          Lumen              3:56"

        // TODO Part 4d: print the reports shown in the instructions, using Map.forEach((k, v) -> ...)
        //   and Optional.ifPresent(...)

        // TODO Part 4e: loop – ask for a user name (or 'quit') and print their summary
        Scanner sc = new Scanner(System.in);
    }
}
```

### Provided tests

**File:** `src/test/java/edu/cwi/swdv110/music/MusicTest.java`

```java
package edu.cwi.swdv110.music;

import static org.junit.jupiter.api.Assertions.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import org.junit.jupiter.api.Test;

class MusicTest {

    private static Play play(String user, String song, String artist, String genre, String date, int secs) {
        return new Play(user, song, artist, genre, LocalDate.parse(date), secs);
    }

    /** Small, hand-checkable data set. 2026-10-05 is a Monday. */
    private static final List<Play> DATA = List.of(
            play("maya", "Afterglow", "Lumen", "Electronic", "2026-10-05", 240),
            play("maya", "Two A.M.", "The Midnight Owls", "Indie", "2026-10-05", 20),
            play("maya", "Slow Static", "Static Bloom", "Indie", "2026-10-06", 180),
            play("maya", "Slow Static", "Static Bloom", "Indie", "2026-10-07", 200),
            play("jordan", "Recursion", "MC Kestrel", "Hip-Hop", "2026-10-06", 300),
            play("jordan", "Afterglow", "Lumen", "Electronic", "2026-10-07", 10),
            play("jordan", "Night Shift", "Lumen", "Electronic", "2026-10-07", 250),
            play("sam", "Porch Light", "Sierra Vale", "Country", "2026-10-07", 120));

    // ---------- Part 1 ----------
    @Test
    void parseTrimsFields() {
        assertEquals(play("maya", "Pulse Width", "Lumen", "electronic", "2026-09-30", 196),
                Play.parse("maya, Pulse Width,Lumen,  electronic ,2026-09-30, 196"));
        assertThrows(IllegalArgumentException.class, () -> Play.parse("maya,Pulse Width,Lumen"));
    }

    @Test
    void titleCase() {
        assertEquals("Hip-Hop", PlayCleaner.titleCase("  hip-HOP "));
        assertEquals("Electronic", PlayCleaner.titleCase("ELECTRONIC"));
        assertEquals("Classic Rock", PlayCleaner.titleCase("classic rock"));
    }

    @Test
    void cleanRemovesAndFixes() {
        List<Play> list = new ArrayList<>(List.of(
                play("Maya", "Afterglow", "Lumen", "electronic", "2026-10-05", 240),
                play("sam", "", "Sierra Vale", "Country", "2026-10-07", 201),
                play("jordan", "Lava Fields", "Basalt", "Rock", "2026-10-02", 0),
                play("jordan", "Recursion", "MC Kestrel", "hip-hop", "2026-10-06", 300)));
        assertEquals(2, PlayCleaner.clean(list));
        assertEquals(List.of(
                play("maya", "Afterglow", "Lumen", "Electronic", "2026-10-05", 240),
                play("jordan", "Recursion", "MC Kestrel", "Hip-Hop", "2026-10-06", 300)), list);
    }

    // ---------- Part 2 ----------
    @Test
    void filtersAndCombinations() {
        assertEquals(4, DATA.stream().filter(PlayFilters.byUser("MAYA")).count());
        assertEquals(3, DATA.stream().filter(PlayFilters.byGenre("electronic")).count());
        assertEquals(2, DATA.stream().filter(PlayFilters.skipped()).count());
        assertEquals(6, DATA.stream().filter(PlayFilters.between(
                LocalDate.of(2026, 10, 6), LocalDate.of(2026, 10, 7))).count());

        Predicate<Play> mayaIndieFull = PlayFilters.byUser("maya")
                .and(PlayFilters.byGenre("Indie"))
                .and(PlayFilters.skipped().negate());
        assertEquals(2, DATA.stream().filter(mayaIndieFull).count());
    }

    // ---------- Part 3 ----------
    @Test
    void totals() {
        assertEquals(10, MusicStats.totalMinutes(DATA, PlayFilters.byUser("maya")));   // 640 s
        assertEquals(22, MusicStats.totalMinutes(DATA, p -> true));                      // 1320 s
        assertEquals(Map.of("jordan", 560, "maya", 640, "sam", 120), MusicStats.secondsPerUser(DATA));
        assertEquals(List.of("jordan", "maya", "sam"),
                List.copyOf(MusicStats.secondsPerUser(DATA).keySet()), "sorted by user");
    }

    @Test
    void genres() {
        Map<String, Long> g = MusicStats.playsPerGenre(DATA);
        assertEquals(Map.of("Country", 1L, "Electronic", 3L, "Hip-Hop", 1L, "Indie", 3L), g);
        assertEquals(List.of("Country", "Electronic", "Hip-Hop", "Indie"), List.copyOf(g.keySet()));
    }

    @Test
    void topArtistsWithTieBreak() {
        // Lumen 3, Static Bloom 2, then MC Kestrel / Sierra Vale / The Midnight Owls tied at 1
        assertEquals(List.of("Lumen", "Static Bloom", "MC Kestrel"), MusicStats.topArtists(DATA, 3));
        assertEquals(List.of(), MusicStats.topArtists(List.of(), 3));
    }

    @Test
    void longestAndSongs() {
        assertEquals(Optional.of(DATA.get(4)), MusicStats.longestPlay(DATA));
        assertEquals(Optional.empty(), MusicStats.longestPlay(List.of()));
        assertEquals(List.of("Afterglow", "Night Shift"), MusicStats.songsBy(DATA, "lumen"));
    }

    @Test
    void recentUsesComparatorChain() {
        List<Play> r = MusicStats.recent(DATA, 3);
        assertEquals(List.of("Afterglow", "Night Shift", "Porch Light"),
                r.stream().map(Play::song).toList());
    }

    @Test
    void byDayOfWeek() {
        assertEquals(Map.of(DayOfWeek.MONDAY, 2L, DayOfWeek.TUESDAY, 2L, DayOfWeek.WEDNESDAY, 4L),
                MusicStats.playsByDay(DATA));
        assertEquals(DayOfWeek.MONDAY, MusicStats.playsByDay(DATA).keySet().iterator().next());
    }

    @Test
    void skipRateAndTopGenre() {
        assertEquals(0.25, MusicStats.skipRate(DATA, "maya"), 0.0001);
        assertEquals(0.0, MusicStats.skipRate(DATA, "nobody"), 0.0001);
        assertEquals(Optional.of("Indie"), MusicStats.topGenre(DATA, "maya"));
        assertEquals(Optional.of("Electronic"), MusicStats.topGenre(DATA, "jordan"));
        assertEquals(Optional.empty(), MusicStats.topGenre(DATA, "nobody"));
    }

    @Test
    void topGenreTieGoesToAlphabeticallyFirst() {
        List<Play> tie = List.of(
                play("ana", "x", "A", "Rock", "2026-10-01", 100),
                play("ana", "y", "B", "Country", "2026-10-01", 100));
        assertEquals(Optional.of("Country"), MusicStats.topGenre(tie, "ana"));
    }
}
```

---

## Part 1 – Parsing & cleaning

- `Play.parse`: `split(",")`, check that there are 6 fields, and `trim()` each one. `LocalDate.parse` reads `2026-09-28` directly.
- `PlayCleaner.clean`:
  - `plays.removeIf(p -> ...)` deletes every element that matches.
  - `plays.replaceAll(op)` swaps each element for `op.apply(element)`. Because `Play` is a record, you can't *change* a play; you build a corrected copy.

## Part 2 – Filters as lambdas

Each method returns a lambda, a piece of behavior stored in a variable. The payoff comes in the test `filtersAndCombinations`: small predicates snap together with `and`, `or` and `negate` into new questions, without writing any new methods.

## Part 3 – Stream reports

Work through `MusicStats` one method at a time, running `mvn test` after each. Tips:

- Read each pipeline aloud as a sentence: *"From the plays, **keep** maya's, **turn each into** its seconds, **add them up**."*
- When you need "the top N of something," it's almost always two steps: **group & count** into a `Map`, then **stream the entries** and sort them.
- `topGenre` needs a tie-breaker. `max` keeps the *greatest* element, so think about which key ordering makes `"Country"` greater than `"Rock"`.
- Remember that **no loops** are allowed in this class.

✅ Checkpoint: `mvn test`. All 12 tests pass.

## Part 4 – The report program

Finish `App` and run `mvn -q compile exec:java`. Your output should match:

```text
Loaded 42 plays (2 bad rows removed)

Minutes per user:
  jordan    62 min
  maya      47 min
  sam       14 min

Plays per genre:
  Country      ##
  Electronic   #####
  Hip-Hop      #########
  Indie        #############
  Pop          #########
  Rock         ####

Plays by day:
  MONDAY     7
  TUESDAY    4
  WEDNESDAY  6
  THURSDAY   6
  FRIDAY     4
  SATURDAY   5
  SUNDAY     10

Top 3 artists: [Static Bloom, Kiko Ray, MC Kestrel]
Longest play:  Lines and Loops – MC Kestrel

Most recent:
  2026-10-11  jordan  Iron Horse         Basalt             3:39
  2026-10-11  maya    Afterglow          Lumen              2:51
  2026-10-11  maya    Afterglow          Lumen              3:56
  2026-10-11  jordan  Boise Bounce       MC Kestrel         4:41
  2026-10-11  maya    Slow Static        Static Bloom       2:30

User to summarize (or 'quit'): maya
maya: 16 plays, 47 min, top genre Indie, skip rate 13%
  Indie minutes: 37
  Full listens:  Afterglow – Lumen; Moth to a Lamp – The Midnight Owls; Neon Sagebrush – Static Bloom; Owyhee Nights – Static Bloom; Paper Satellites – Static Bloom; Pulse Width – Lumen; Slow Static – Static Bloom

User to summarize (or 'quit'): quit
```

The last two summary lines show how far one predicate can go:

- `byUser(user).and(byGenre("Indie"))` feeds `totalMinutes`.
- `byUser(user).and(skipped().negate())`, then `.map(compact::format)` (a method reference to *your own* lambda), then `.distinct().sorted()`, then `Collectors.joining("; ")`.

---

## Part 5 – README reflection

1. Pick one method in `MusicStats` and rewrite it with a `for` loop in your README. Which version is easier to read? Which is easier to change?
2. `PlayFormatter` and `Function<Play, String>` have the same shape. Why might a team still create its own functional interface?
3. Why must the list passed to `PlayCleaner.clean` be an `ArrayList` and not the result of `List.of(...)` or `stream().toList()`?
4. A stream does nothing until a *terminal* operation runs. Name three terminal operations you used.

---

## Stretch goals (pick any)

- **Streaks:** find each user's longest run of consecutive days with at least one play.
- **Command filters:** let the user type `genre=Indie user=maya after=2026-10-01` and build one combined `Predicate` from the parts with `reduce(Predicate::and)`.
- **Your data:** export your own listening history (most services let you download it), convert a slice to this CSV format, and run your analyzer on it.
- **Parallel:** time `plays.stream()` vs `plays.parallelStream()` on a list copied 100,000 times. Explain the result.

---

## Submission checklist

- [ ] `mvn test`: all provided tests pass
- [ ] `MusicStats` contains **no** `for`/`while` loops
- [ ] Program output matches Part 4
- [ ] `README.md` answers the Part 5 questions
- [ ] Pushed to GitHub with a `.gitignore`

## Grading (100 points)

| Area | Points |
|---|---|
| Parsing & cleaning tests pass | 15 |
| Filter tests pass | 10 |
| `MusicStats` tests pass | 30 |
| `App` output and the two `PlayFormatter` lambdas | 15 |
| Idiomatic lambdas & method references, no loops in `MusicStats` | 10 |
| README reflection | 15 |
| Git: meaningful commits | 5 |
| Stretch goals | up to +5 bonus |
