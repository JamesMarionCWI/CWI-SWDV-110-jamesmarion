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
