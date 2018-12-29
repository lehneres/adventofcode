import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class Advent17Test {

    private static char[][] parseTestGrid() throws IOException {
        return Files.lines(Paths.get("src/test/resources/advent17_Test_Grid.txt")).map(String::toCharArray).toArray(size -> new char[size][1]);
    }

    @Test
    void testInputParse() throws IOException {
        Advent17.setWell(6, 0);

        final Advent17 ground = new Advent17();

        final Set<int[]> input = Advent17.parseInput("src/test/resources/advent17_Test_Input.txt");

        ground.initGrid(input);

        final char[][] grid         = ground.getGrid();
        final char[][] gridFromFile = Advent17Test.parseTestGrid();

        Advent17.printGrid(grid, Advent17.EMPTY_POS);
        System.out.println();
        Advent17.printGrid(gridFromFile, Advent17.EMPTY_POS);

        assertTrue(Arrays.deepEquals(grid, gridFromFile), "grid is not equal");
    }

    @Test
    void testFlow() throws IOException {
        Advent17.setWell(6, 0);

        final Advent17 ground = new Advent17();

        final Set<int[]> input = Advent17.parseInput("src/test/resources/advent17_Test_Input.txt");

        ground.initGrid(input);

        ground.flow(Advent17.getWell());

        Advent17.printGrid(ground.getGrid(), Advent17.EMPTY_POS);

        assertEquals(57, ground.getWetTiles(true), "wet tiles not equal");
    }

}
