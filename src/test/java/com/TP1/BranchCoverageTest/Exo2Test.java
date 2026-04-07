package com.TP1.BranchCoverageTest;

import org.junit.jupiter.api.Test;
import org.tp1.Anagram;

import static org.junit.jupiter.api.Assertions.*;

public class Exo2Test {

    // branche null
    @Test
    void testNullStrings() {
        assertThrows(NullPointerException.class, () -> {
            Anagram.isAnagram(null, null);
        });
    }

    // branche longueur différente
    @Test
    void testLengthBranch() {
        assertFalse(Anagram.isAnagram("abc", "abcd"));
    }

    // branche anagram vrai
    @Test
    void testTrueBranch() {
        assertTrue(Anagram.isAnagram("listen", "silent"));
    }

    // branche anagram faux
    @Test
    void testFalseBranch() {
        assertFalse(Anagram.isAnagram("apple", "pale"));
    }
}
