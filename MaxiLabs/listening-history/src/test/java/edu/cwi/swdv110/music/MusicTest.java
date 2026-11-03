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
    private static final List<Play> DATA = List.of(play("maya", "Afterglow", "Lumen", "Electronic", "2026-10-05", 240), play("maya", "Two A.M.", "The Midnight Owls", "Indie", "2026-10-05", 20), play("maya", "Slow Static", "Static Bloom", "Indie", "2026-10-06", 180), play("maya", "Slow Static", "Static Bloom", "Indie", "2026-10-07", 200), play("jordan", "Recursion", "MC Kestrel", "Hip-Hop", "2026-10-06", 300), play("jordan", "Afterglow", "Lumen", "Electronic", "2026-10-07", 10), play("jordan", "Night Shift", "Lumen", "Electronic", "2026-10-07", 250), play("sam", "Porch Light", "Sierra Vale", "Country", "2026-10-07", 120));

    // ---------- Part 1 ----------
    @Test
    void parseTrimsFields() {
        assertEquals(play("maya", "Pulse Width", "Lumen", "electronic", "2026-09-30", 196), Play.parse("maya, Pulse Width,Lumen,  electronic ,2026-09-30, 196"));
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
        List<Play> list = new ArrayList<>(List.of(play("Maya", "Afterglow", "Lumen", "electronic", "2026-10-05", 240), play("sam", "", "Sierra Vale", "Country", "2026-10-07", 201), play("jordan", "Lava Fields", "Basalt", "Rock", "2026-10-02", 0), play("jordan", "Recursion", "MC Kestrel", "hip-hop", "2026-10-06", 300)));
        assertEquals(2, PlayCleaner.clean(list));
        assertEquals(List.of(play("maya", "Afterglow", "Lumen", "Electronic", "2026-10-05", 240), play("jordan", "Recursion", "MC Kestrel", "Hip-Hop", "2026-10-06", 300)), list);
    }

    // ---------- Part 2 ----------
    @Test
    void filtersAndCombinations() {
        assertEquals(4, DATA.stream().filter(PlayFilters.byUser("MAYA")).count());
        assertEquals(3, DATA.stream().filter(PlayFilters.byGenre("electronic")).count());
        assertEquals(2, DATA.stream().filter(PlayFilters.skipped()).count());
        assertEquals(6, DATA.stream().filter(PlayFilters.between(LocalDate.of(2026, 10, 6), LocalDate.of(2026, 10, 7))).count());

        Predicate<Play> mayaIndieFull = PlayFilters.byUser("maya").and(PlayFilters.byGenre("Indie")).and(PlayFilters.skipped().negate());
        assertEquals(2, DATA.stream().filter(mayaIndieFull).count());
    }

    // ---------- Part 3 ----------
    @Test
    void totals() {
        assertEquals(10, MusicStats.totalMinutes(DATA, PlayFilters.byUser("maya"))); // 640 s
        assertEquals(22, MusicStats.totalMinutes(DATA, p -> true)); // 1320 s
        assertEquals(Map.of("jordan", 560, "maya", 640, "sam", 120), MusicStats.secondsPerUser(DATA));
        assertEquals(List.of("jordan", "maya", "sam"), List.copyOf(MusicStats.secondsPerUser(DATA).keySet()), "sorted by user");
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
        assertEquals(List.of("Afterglow", "Night Shift", "Porch Light"), r.stream().map(Play::song).toList());
    }

    @Test
    void byDayOfWeek() {
        assertEquals(Map.of(DayOfWeek.MONDAY, 2L, DayOfWeek.TUESDAY, 2L, DayOfWeek.WEDNESDAY, 4L), MusicStats.playsByDay(DATA));
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
        List<Play> tie = List.of(play("ana", "x", "A", "Rock", "2026-10-01", 100), play("ana", "y", "B", "Country", "2026-10-01", 100));
        assertEquals(Optional.of("Country"), MusicStats.topGenre(tie, "ana"));
    }
}
