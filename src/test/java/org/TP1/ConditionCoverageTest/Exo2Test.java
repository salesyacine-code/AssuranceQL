package org.TP1.ConditionCoverageTest;

import org.junit.jupiter.api.Test;
import org.tp1.Anagram;

import static org.junit.jupiter.api.Assertions.*;

public class Exo2Test {

    @Test
    void testWithSpaces() {
        assertTrue(Anagram.isAnagram("conversation", "voices rant on"));
    }

    @Test
    void testUpperCase() {
        assertTrue(Anagram.isAnagram("Listen", "Silent"));
    }

    @Test
    void testDifferentLetters() {
        assertFalse(Anagram.isAnagram("abcd", "abce"));
    }

    @Test
    void testEmptyStrings() {
        assertTrue(Anagram.isAnagram("", ""));
    }

}
