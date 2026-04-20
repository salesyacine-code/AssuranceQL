package org.TP1.ConditionCoverageTest;

import org.junit.jupiter.api.Test;
import org.tp1.BinarySearch;

import static org.junit.jupiter.api.Assertions.*;

public class Exo3Test {

    @Test
    void testSingleElementFound() {
        int[] arr = {5};
        assertEquals(0, BinarySearch.binarySearch(arr,5));
    }

    @Test
    void testSingleElementNotFound() {
        int[] arr = {5};
        assertEquals(-1, BinarySearch.binarySearch(arr,2));
    }

    @Test
    void testTwoElementsFirst() {
        int[] arr = {2,4};
        assertEquals(0, BinarySearch.binarySearch(arr,2));
    }

    @Test
    void testTwoElementsSecond() {
        int[] arr = {2,4};
        assertEquals(1, BinarySearch.binarySearch(arr,4));
    }

    @Test
    void testLargeArray() {
        int[] arr = {1,3,5,7,9,11,13,15,17};
        assertEquals(6, BinarySearch.binarySearch(arr,13));
    }

    @Test
    void testNegativeNumbers() {
        int[] arr = {-10,-5,0,5,10};
        assertEquals(1, BinarySearch.binarySearch(arr,-5));
    }

    @Test
    void testZeroSearch() {
        int[] arr = {-3,-2,-1,0,1,2,3};
        assertEquals(3, BinarySearch.binarySearch(arr,0));
    }

}
