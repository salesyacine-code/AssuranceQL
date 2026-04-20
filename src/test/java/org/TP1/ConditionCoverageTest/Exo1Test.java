package org.TP1.ConditionCoverageTest;

import org.junit.jupiter.api.Test;
import org.tp1.Palindrome;

import static org.junit.jupiter.api.Assertions.*;

public class Exo1Test {

    // condition : s == null
    @Test
    void testNullCondition() {
        assertThrows(NullPointerException.class, () -> {
            Palindrome.isPalindrome(null);
        });
    }

    // condition : string vide
    @Test
    void testEmptyString() {
        assertTrue(Palindrome.isPalindrome(""));
    }

    // condition : palindrome avec espaces
    @Test
    void testSentencePalindrome() {
        assertTrue(Palindrome.isPalindrome("Esope reste ici et se repose"));
    }

    // condition : non palindrome
    @Test
    void testDifferentChars() {
        assertFalse(Palindrome.isPalindrome("abcdef"));
    }

}
