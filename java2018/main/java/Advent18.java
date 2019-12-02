import java.io.IOException;
import java.nio.CharBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings("StaticMethodOnlyUsedInOneClass")
class Advent18 {
    private static final char                        OPEN        = '.';
    private static final char                        TREE        = '|';
    private static final char                        LUMBERYARD  = '#';
    private final        Map<Integer, List<Integer>> knownCycles = new HashMap<>();
    private              char[][]                    grid;

    static void printGrid(final char[][] grid) {
        for (final char[] chars : grid) {
            for (final char aChar : chars)
                System.out.print(aChar);
            System.out.println();
        }
        System.out.printf("%n%n");
    }

    @SuppressWarnings("AccessingNonPublicFieldOfAnotherObject")
    public static void main(final String... args) throws IOException {
        final Advent18 area = new Advent18();
        area.parseGridFromFile("java2018/main/resources/advent18.txt");

        int       minutes = 1;
        final int maxMin  = 1000;

        int          cycleStart  = Integer.MIN_VALUE;
        int          cycleLength = 0;
        final long[] results     = new long[maxMin];

        do {
            area.change(minutes);
            if (minutes == 10) System.out.printf("Total resource value after 10 minutes: %d%n", area.getResourceValue());
            if (area.knownCycles.containsKey(Arrays.deepHashCode(area.grid))) {
                if (cycleStart == Integer.MIN_VALUE) {
                    cycleStart = minutes;
                    System.err.printf("found cycle after %d minutes: %s%n", minutes, area.knownCycles.get(Arrays.deepHashCode(area.grid)));
                }
                if (cycleLength == 0 && area.knownCycles.get(Arrays.deepHashCode(area.grid)).size() > 1) {
                    cycleLength = minutes - cycleStart;
                    System.err.printf("cycle ends after %d minutes%n", cycleLength);
                }
                if (cycleLength > 0 && minutes > cycleStart + 2 * cycleLength) break;
            }
            if (cycleLength > 0) results[minutes % cycleLength] = area.getResourceValue();
        }
        while (++minutes < maxMin);

        System.out.printf("Total resource value after 1000000000 minutes: %d%n", results[1000000000 % cycleLength]);
    }

    final long getResourceValue() {
        return Arrays.stream(this.grid).flatMap(line -> CharBuffer.wrap(line).chars().mapToObj(tile -> (char) tile)).filter(c -> c == Advent18.LUMBERYARD).count() * Arrays.stream(
                this.grid).flatMap(line -> CharBuffer.wrap(line).chars().mapToObj(tile -> (char) tile)).filter(c -> c == Advent18.TREE).count();
    }

    final void parseGridFromFile(final String file) throws IOException {
        final List<String> lines = Files.lines(Paths.get(file)).collect(Collectors.toList());

        this.grid = new char[lines.size()][];
        for (int row = 0; row < lines.size(); row++)
            this.grid[row] = lines.get(row).toCharArray();
    }

    final void change(final int curMin) {
        this.knownCycles.putIfAbsent(Arrays.deepHashCode(this.grid), new ArrayList<>());
        this.knownCycles.get(Arrays.deepHashCode(this.grid)).add(curMin);
        final char[][] newGrid = this.getGrid();

        for (int row = 0; row < newGrid.length; row++) {
            for (int col = 0; col < newGrid[row].length; col++) {
                final List<Character> adjacent = this.getAdjacentAcres(row, col);
                switch (this.grid[row][col]) {
                    case Advent18.OPEN:
                        if (adjacent.stream().filter(c -> c == Advent18.TREE).count() > 2) newGrid[row][col] = Advent18.TREE;
                        break;
                    case Advent18.TREE:
                        if (adjacent.stream().filter(c -> c == Advent18.LUMBERYARD).count() > 2) newGrid[row][col] = Advent18.LUMBERYARD;
                        break;
                    case Advent18.LUMBERYARD:
                        newGrid[row][col] = adjacent.stream().anyMatch(c -> c == Advent18.LUMBERYARD) && adjacent.stream().anyMatch(c -> c == Advent18.TREE)
                                            ? Advent18.LUMBERYARD
                                            : Advent18.OPEN;
                        break;
                    default:
                        System.err.println("unknown element");
                }
            }
        }

        this.grid = newGrid;
    }

    private List<Character> getAdjacentAcres(final int... origin) {
        final List<Character> adjacent = new ArrayList<>();
        for (int row = Math.max(origin[0] - 1, 0); row <= Math.min(this.grid.length - 1, origin[0] + 1); row++)
            for (int col = Math.max(origin[1] - 1, 0); col <= Math.min(this.grid[row].length - 1, origin[1] + 1); col++)
                if (row != origin[0] || col != origin[1]) adjacent.add(this.grid[row][col]);
        return adjacent;
    }

    final char[][] getGrid() {
        final char[][] newGrid = new char[this.grid.length][];
        for (int i = 0; i < this.grid.length; i++) {
            newGrid[i] = new char[this.grid[i].length];
            System.arraycopy(this.grid[i], 0, newGrid[i], 0, this.grid[i].length);
        }
        return newGrid;
    }

}
