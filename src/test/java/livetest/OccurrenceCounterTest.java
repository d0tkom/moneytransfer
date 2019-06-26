package livetest;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;

public class OccurrenceCounterTest {
    OccurrenceCounter subject = new OccurrenceCounter();

    @Test
    public void normalString() {
        String input = "apple";

        Map<Character, Long> expected = new HashMap<>();
        expected.put('a', 1l);
        expected.put('p', 2l);
        expected.put('l', 1l);
        expected.put('e', 1l);

        Map<Character, Long> result = subject.occurrences(input);

        assertEquals(expected, result);
    }

    @Test
    public void emptyString() {
        String input = "";

        Map<Character, Long> expected = new HashMap<>();

        Map<Character, Long> result = subject.occurrences(input);

        assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullInput() {
        String input = null;
        subject.occurrences(input);
    }

    @Test
    public void longStringUsingStream() {
        Stream input = Stream.generate(() -> 'a').limit(Integer.MAX_VALUE + 1l);

        Map<Character, Long> expected = new HashMap<>();
        expected.put('a', Integer.MAX_VALUE + 1l);

        Map<Character, Long> result = subject.occurrences(input);

        assertEquals(expected, result);
    }
}
