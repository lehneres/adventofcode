import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

final class Advent14Test {

    @Test
    void testFirstRound() {
        final Advent14 kitchen = new Advent14(new int[]{1}, 10);

        kitchen.iterate();

        assertIterableEquals(Arrays.asList(3, 7, 1, 0), kitchen.getRecipes(), "outcome not equal after first round");
        assertEquals(0, kitchen.getElve1(), "elve1 wrong position");
        assertEquals(1, kitchen.getElve2(), "elve2 wrong position");
    }

    @Test
    void test15Rounds() {
        final Advent14 kitchen = new Advent14(new int[]{1, 5}, 10);

        int rounds = 15;
        while (rounds-- > 0) kitchen.iterate();

        assertIterableEquals(Arrays.asList(3, 7, 1, 0, 1, 0, 1, 2, 4, 5, 1, 5, 8, 9, 1, 6, 7, 7, 9, 2), kitchen.getRecipes(), "outcome not equal after first round");
        assertEquals(8, kitchen.getElve1(), "elve1 wrong position");
        assertEquals(4, kitchen.getElve2(), "elve2 wrong position");
    }

    @Test
    void testFindNextAfterFive() {
        final Advent14 kitchen = new Advent14(new int[]{5}, 10);

        int rounds = 5;
        while (rounds-- > 0) kitchen.iterate();

        assertIterableEquals(Arrays.asList(0, 1, 2, 4, 5, 1, 5, 8, 9, 1), kitchen.findNext(), "next 10 after 5 iterations are wrong");
    }

    @Test
    void testFindNextAfter18() {
        final Advent14 kitchen = new Advent14(new int[]{1, 8}, 10);

        int rounds = 18;
        while (rounds-- > 0) kitchen.iterate();

        assertIterableEquals(Arrays.asList(9, 2, 5, 1, 0, 7, 1, 0, 8, 5), kitchen.findNext(), "next 10 after 18 iterations are wrong");
    }

    @Test
    void testFindNextAfter2018() {
        final Advent14 kitchen = new Advent14(new int[]{2, 0, 1, 8}, 10);

        int rounds = 2018;
        while (rounds-- > 0) kitchen.iterate();

        assertIterableEquals(Arrays.asList(5, 9, 4, 1, 4, 2, 9, 8, 8, 2), kitchen.findNext(), "next 10 after 2018 iterations are wrong");
    }

    @Test
    void testFindNextAfter598701() {
        final Advent14 kitchen = new Advent14(new int[]{5, 9, 8, 7, 0, 1}, 10);

        int rounds = 598701;
        while (rounds-- > 0) kitchen.iterate();

        assertIterableEquals(Arrays.asList(2, 7, 7, 6, 1, 4, 1, 9, 1, 7), kitchen.findNext(), "next 10 after 598701 iterations are wrong");
    }

    @Test
    void testFindFirstAfter51589() {
        final Advent14 kitchen = new Advent14(new int[]{5, 1, 5, 8, 9}, 10);

        int subListIndex;
        do subListIndex = kitchen.iterateAndCheck(); while (subListIndex < 0);

        assertEquals(9, subListIndex, "recipes to the left are wrong");
    }

    @Test
    void testFindFirstAfter01245() {
        final Advent14 kitchen = new Advent14(new int[]{0, 1, 2, 4, 5}, 10);

        int subListIndex;
        do subListIndex = kitchen.iterateAndCheck(); while (subListIndex < 0);

        assertEquals(5, subListIndex, "recipes to the left are wrong");
    }

    @Test
    void testFindFirstAfter92510() {
        final Advent14 kitchen = new Advent14(new int[]{9, 2, 5, 1, 0}, 10);

        int subListIndex;
        do subListIndex = kitchen.iterateAndCheck(); while (subListIndex < 0);

        assertEquals(18, subListIndex, "recipes to the left are wrong");
    }

    @Test
    void testFindFirstAfter59414() {
        final Advent14 kitchen = new Advent14(new int[]{5, 9, 4, 1, 4}, 10);

        int subListIndex;
        do subListIndex = kitchen.iterateAndCheck(); while (subListIndex < 0);

        assertEquals(2018, subListIndex, "recipes to the left are wrong");
    }
}
