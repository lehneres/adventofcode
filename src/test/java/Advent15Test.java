import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
    void testBasicMovement() throws IOException {
        final Advent15 battle = new Advent15();

        battle.readGridFromFile("src/test/resources/advent15_SmallTest_Movement.txt");

        battle.print();
        battle.hasNextRound();
        battle.print();

        assertTrue(Arrays.deepEquals(battle.getGrid(), Advent15Test.readGridFromFile("src/test/resources/advent15_SmallTest_Movement_Result.txt")), "result is not equal");
    }

    @Test
    void testMovement() throws IOException {
        final Advent15 battle = new Advent15();

        battle.readGridFromFile("src/test/resources/advent15_Test_Movement.txt");

        battle.print();
        battle.hasNextRound();
        battle.print();
        battle.hasNextRound();
        battle.print();
        battle.hasNextRound();
        battle.print();

        assertTrue(Arrays.deepEquals(battle.getGrid(), Advent15Test.readGridFromFile("src/test/resources/advent15_Test_Movement_Result.txt")), "result is not equal");
    }
}
