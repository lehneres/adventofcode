import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class Advent18Test {


    @Test
    final void test10Minutes() throws IOException {
        final Advent18 area = new Advent18();

        area.parseGridFromFile("src/test/resources/advent18_Test_Area.txt");

        Advent18.printGrid(area.getGrid());

        int minutes = 10;

        do area.change(minutes);
        while (--minutes > 0);

        Advent18.printGrid(Advent18Test.getGridFromFile());

        assertTrue(Arrays.deepEquals(area.getGrid(), Advent18Test.getGridFromFile()), "grid is not equal");
        assertEquals(1147, area.getResourceValue(), "resource value not equal");
    }

    private static char[][] getGridFromFile() throws IOException {
        final List<String> lines = Files.lines(Paths.get("src/test/resources/advent18_Test_After10.txt")).collect(Collectors.toList());

        final char[][] grid = new char[lines.size()][];
        for (int row = 0; row < lines.size(); row++)
            grid[row] = lines.get(row).toCharArray();

        return grid;
    }

}
