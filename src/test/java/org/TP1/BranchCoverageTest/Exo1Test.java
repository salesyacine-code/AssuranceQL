package org.TP1.BranchCoverageTest;

import org.junit.jupiter.api.Test;
import org.tp1.Palindrome;

import static org.junit.jupiter.api.Assertions.*;

public class Exo1Test {

    // branche : s == null
    @Test
    void testNullString() {
        assertThrows(NullPointerException.class, () -> {
            Palindrome.isPalindrome(null);
        });
    }

    // branche : palindrome vrai
    @Test
    void testPalindrome() {
        assertTrue(Palindrome.isPalindrome("kayak"));
    }

    // branche : palindrome faux
    @Test
    void testNotPalindrome() {
        assertFalse(Palindrome.isPalindrome("hello"));
    }

}
