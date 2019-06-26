package livetest;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

public class OccurrenceCounter {

    public Map<Character, Long> occurrences(String text) {
        if (text == null)
            throw new IllegalArgumentException("Input was null");

        Map<Character, Long> tbl = new HashMap<>();

        for (Character c : text.toCharArray()) {
            addToTable(tbl, c);
        }

        return tbl;
    }

    public Map<Character, Long> occurrences(Stream<Character> stream) {
        if (stream == null)
            throw new IllegalArgumentException("Input was null");

        Map<Character, Long> tbl = new ConcurrentHashMap<>();

        stream.parallel().forEach(c -> addToTable(tbl, c));

        return tbl;
    }

    private void addToTable(Map<Character, Long> tbl, Character c) {
        tbl.compute(c, (k, v) -> v == null ? 1l : v+1l);
    }
}
