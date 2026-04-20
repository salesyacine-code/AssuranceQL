package org.TP1.LineCoverageTest;

import org.junit.jupiter.api.Test;
import org.tp1.Anagram;

import static org.junit.jupiter.api.Assertions.*;

public class Exo2Test {

    @Test
    void testAnagramTrue() {
        assertTrue(Anagram.isAnagram("chien", "niche"));
    }

    @Test
    void testAnagramFalse() {
        assertFalse(Anagram.isAnagram("hello", "world"));
    }

    @Test
    void testDifferentLength() {
        assertFalse(Anagram.isAnagram("abc", "ab"));
    }

    @Test
    void testNull() {
        assertThrows(NullPointerException.class, () -> {
            Anagram.isAnagram(null, "abc");
        });
    }
}
