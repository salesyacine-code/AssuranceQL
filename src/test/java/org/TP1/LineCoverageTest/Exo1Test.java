package org.TP1.LineCoverageTest;

import org.junit.jupiter.api.Test;
import org.tp1.Palindrome;
import static org.junit.jupiter.api.Assertions.*;

public class Exo1Test {

    @Test
    void testPalindromeTrue() {
        assertTrue(Palindrome.isPalindrome("kayak"));       // couvre while + return true
    }

    @Test
    void testPalindromeFalse() {
        assertFalse(Palindrome.isPalindrome("ab"));         // couvre return false dans while
    }

    @Test
    void testNull() {
        assertThrows(NullPointerException.class, () ->
                Palindrome.isPalindrome(null));                 // couvre throw
    }

    @Test
    void testWithSpaces() {
        assertTrue(Palindrome.isPalindrome("Esope reste ici et se repose")); // couvre replaceAll + toLowerCase
    }
    @Test
    void testChaineVide(){
        assertTrue(Palindrome.isPalindrome(""));
    }
}