import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class Advent15Test {

    static int[][] readGridFromFile(final String file) throws IOException {
        final List<String> lines = Files.lines(Paths.get(file)).collect(Collectors.toList());
        final int[][]      grid  = new int[lines.size()][];
        for (int row = 0; row < lines.size(); row++) {
            final char[] segments = lines.get(row).toCharArray();
            grid[row] = new int[segments.length];

            for (int col = 0; col < segments.length; col++)
                grid[row][col] = segments[col];
        }
        return grid;
    }

    @Test
    void testBasicMovement() throws Exception {
        final Advent15 battle = new Advent15();

        battle.readGridFromFile("test/resources/advent15_SmallTest_Movement.txt");

        battle.play(false, 1);

        assertTrue(Arrays.deepEquals(battle.getGrid(), Advent15Test.readGridFromFile("test/resources/advent15_SmallTest_Movement_Result.txt")), "result is not equal");
    }

    @Test
    void testMovement() throws Exception {
        final Advent15 battle = new Advent15();

        battle.readGridFromFile("test/resources/advent15_Test_Movement.txt");

        battle.play(false, 3);

        assertTrue(Arrays.deepEquals(battle.getGrid(), Advent15Test.readGridFromFile("test/resources/advent15_Test_Movement_Result.txt")), "result is not equal");
    }

    @Test
    void testAttack1() throws Exception {
        final Advent15 battle = new Advent15();

        battle.readGridFromFile("test/resources/advent15_Test_Attack.txt");

        battle.play(false, Integer.MAX_VALUE);

        assertTrue(Arrays.deepEquals(battle.getGrid(), Advent15Test.readGridFromFile("test/resources/advent15_Test_Attack_Result.txt")), "result is not equal");

        final String outcome = battle.getOutcome();

        assertEquals("the outcome is 47 * 590 = 27730", outcome, "wrong outcome");
    }

    @Test
    void testAttack2() throws Exception {
        final Advent15 battle = new Advent15();

        battle.readGridFromFile("test/resources/advent15_Test_Attack2.txt");

        battle.play(false, Integer.MAX_VALUE);

        assertTrue(Arrays.deepEquals(battle.getGrid(), Advent15Test.readGridFromFile("test/resources/advent15_Test_Attack2_Result.txt")), "result is not equal");

        final String outcome = battle.getOutcome();

        assertEquals("the outcome is 37 * 982 = 36334", outcome, "wrong outcome");
    }

    @Test
    void testAttack3() throws Exception {
        final Advent15 battle = new Advent15();

        battle.readGridFromFile("test/resources/advent15_Test_Attack3.txt");

        battle.play(false, Integer.MAX_VALUE);

        assertTrue(Arrays.deepEquals(battle.getGrid(), Advent15Test.readGridFromFile("test/resources/advent15_Test_Attack3_Result.txt")), "result is not equal");

        final String outcome = battle.getOutcome();

        assertEquals("the outcome is 46 * 859 = 39514", outcome, "wrong outcome");
    }

    @Test
    void testAttack4() throws Exception {
        final Advent15 battle = new Advent15();

        battle.readGridFromFile("test/resources/advent15_Test_Attack4.txt");

        battle.play(false, Integer.MAX_VALUE);

        assertTrue(Arrays.deepEquals(battle.getGrid(), Advent15Test.readGridFromFile("test/resources/advent15_Test_Attack4_Result.txt")), "result is not equal");

        final String outcome = battle.getOutcome();

        assertEquals("the outcome is 35 * 793 = 27755", outcome, "wrong outcome");
    }

    @Test
    void testAttack5() throws Exception {
        final Advent15 battle = new Advent15();

        battle.readGridFromFile("test/resources/advent15_Test_Attack5.txt");

        battle.play(false, Integer.MAX_VALUE);

        assertTrue(Arrays.deepEquals(battle.getGrid(), Advent15Test.readGridFromFile("test/resources/advent15_Test_Attack5_Result.txt")), "result is not equal");

        final String outcome = battle.getOutcome();

        assertEquals("the outcome is 54 * 536 = 28944", outcome, "wrong outcome");
    }

    @Test
    void testAttack6() throws Exception {
        final Advent15 battle = new Advent15();

        battle.readGridFromFile("test/resources/advent15_Test_Attack6.txt");

        battle.play(false, Integer.MAX_VALUE);

        assertTrue(Arrays.deepEquals(battle.getGrid(), Advent15Test.readGridFromFile("test/resources/advent15_Test_Attack6_Result.txt")), "result is not equal");

        final String outcome = battle.getOutcome();

        assertEquals("the outcome is 20 * 937 = 18740", outcome, "wrong outcome");
    }
}
