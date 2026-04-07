package com.TP1.LineCoverageTest;


import org.junit.jupiter.api.Test;
import org.tp1.BinarySearch;

import static org.junit.jupiter.api.Assertions.*;

public class Exo3Test {

    @Test
    void testElementFound() {
        int[] arr = {1,2,3,4,5,6,7};
        assertEquals(3, BinarySearch.binarySearch(arr,4));
    }

    @Test
    void testElementNotFound() {
        int[] arr = {1,2,3,4,5};
        assertEquals(-1, BinarySearch.binarySearch(arr,10));
    }

    @Test
    void testNullArray() {
        assertThrows(NullPointerException.class, () -> {
            BinarySearch.binarySearch(null,5);
        });
    }

}

