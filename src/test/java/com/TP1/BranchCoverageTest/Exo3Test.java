package com.TP1.BranchCoverageTest;



import org.junit.jupiter.api.Test;
import org.tp1.BinarySearch;

import static org.junit.jupiter.api.Assertions.*;

public class Exo3Test {

    @Test
    void testFoundAtBeginning() {
        int[] arr = {1,2,3,4,5};
        assertEquals(0, BinarySearch.binarySearch(arr,1));
    }

    @Test
    void testFoundAtEnd() {
        int[] arr = {1,2,3,4,5};
        assertEquals(4, BinarySearch.binarySearch(arr,5));
    }

    @Test
    void testSearchLeftBranch() {
        int[] arr = {1,3,5,7,9};
        assertEquals(1, BinarySearch.binarySearch(arr,3));
    }

    @Test
    void testSearchRightBranch() {
        int[] arr = {2,4,6,8,10};
        assertEquals(3, BinarySearch.binarySearch(arr,8));
    }

    @Test
    void testElementAbsent() {
        int[] arr = {1,2,3,4};
        assertEquals(-1, BinarySearch.binarySearch(arr,7));
    }

}

