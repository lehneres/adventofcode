import java.io.IOException;
import java.nio.CharBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Objects;
import java.util.OptionalInt;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class Advent17 {

    static final                                    int[]    EMPTY_POS = new int[0];
    private static final                            char     CLAY      = '#';
    private static final                            char[][] NULL_GRID = new char[0][];
    private static final                            Pattern  r         = Pattern.compile("(x|y)=(\\d+), (x|y)=(\\d+)..(\\d+)");
    private static final                            char     WATER     = '~';
    private static final                            char     FLOW      = '|';
    private static final                            char     EMPTY     = '.';
    private static final                            char     WELL      = '+';
    @SuppressWarnings("MagicNumber") private static int[]    well      = {500, 0};
    private                                         char[][] grid;

    public static void main(final String... args) throws IOException {
        final Advent17 ground = new Advent17();

        final Set<int[]> input = Advent17.parseInput("java2018/main/resources/advent17.txt");

        ground.initGrid(input);

        ground.flow(Advent17.getWell());

        Advent17.printGrid(ground.getGrid());

        System.out.printf("wet tiles: %d%n", ground.getWetTiles(true));
        System.out.printf("wet tiles: %d%n", ground.getWetTiles(false));
    }

    private static void printGrid(final char[][] grid) {
        Advent17.printGrid(grid, Advent17.EMPTY_POS);
    }

    static void printGrid(final char[][] grid, final int... curPos) {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j] == Advent17.WATER)
                    System.out.print("\u001B[34m");
                if (grid[i][j] == Advent17.FLOW)
                    System.out.print("\u001B[36m");
                if (curPos.length > 0 && i == curPos[1] && j == curPos[0])
                    System.out.print("\u001B[31m");
                System.out.print(grid[i][j] + "\u001B[0m");
            }
            System.out.println();
        }
        System.out.printf("%n%n");
    }

    static Set<int[]> parseInput(final String filename) throws IOException {
        return Files.lines(Paths.get(filename)).map(s -> {
            final int[]   entry   = new int[4];
            final Matcher matcher = Advent17.r.matcher(s);
            if (matcher.find()) {
                switch (matcher.group(1)) {
                    case "x":
                        entry[0] = entry[1] = Integer.parseInt(matcher.group(2));
                        break;
                    case "y":
                        entry[2] = entry[3] = Integer.parseInt(matcher.group(2));
                        break;
                    default:
                        System.err.printf("could not parse line: %s%n", s);
                }

                switch (matcher.group(3)) {
                    case "x":
                        entry[0] = Integer.parseInt(matcher.group(4));
                        entry[1] = Integer.parseInt(matcher.group(5));
                        break;
                    case "y":
                        entry[2] = Integer.parseInt(matcher.group(4));
                        entry[3] = Integer.parseInt(matcher.group(5));
                        break;
                    default:
                        System.err.printf("could not parse line: %s%n", s);
                }
            }
            return entry;
        }).collect(Collectors.toSet());
    }

    static int[] getWell() {
        return Advent17.well.clone();
    }

    static void setWell(final int... pos) {
        Advent17.well = pos.clone();
    }

    char[][] getGrid() {
        return Objects.requireNonNullElse(this.grid, Advent17.NULL_GRID).clone();
    }

    void initGrid(final Set<int[]> input) {
        final int maxX = input.stream().map(ints -> new int[]{ints[0], ints[1]}).flatMapToInt(Arrays::stream).max().getAsInt();
        final int maxY = input.stream().map(ints -> new int[]{ints[2], ints[3]}).flatMapToInt(Arrays::stream).max().getAsInt();

        this.grid = new char[maxY + 1][maxX + 2];
        Arrays.stream(this.grid).forEach(line -> Arrays.fill(line, Advent17.EMPTY));

        this.grid[Advent17.well[1]][Advent17.well[0]] = Advent17.WELL;

        input.forEach(ints -> {
            for (int x = ints[0]; x <= ints[1]; x++)
                for (int y = ints[2]; y <= ints[3]; y++)
                    this.grid[y][x] = Advent17.CLAY;
        });
    }

    int getWetTiles(final boolean includeFlow) {
        int minRow = 0;
        outer:
        for (int i = 0; i < this.grid.length; i++)
            for (int j = 0; j < this.grid[i].length; j++)
                if (this.grid[i][j] == Advent17.CLAY) {
                    minRow = i;
                    break outer;
                }

        final char[][] subGrid = Arrays.copyOfRange(this.grid, minRow, this.grid.length);

        return (int) Arrays.stream(subGrid)
                           .flatMap(line -> CharBuffer.wrap(line).chars().mapToObj(tile -> (char) tile))
                           .filter(c -> includeFlow ? c == Advent17.WATER || c == Advent17.FLOW : c == Advent17.WATER)
                           .count();
    }

    void flow(final int... origin) {
        int       row = origin[1];
        final int col = origin[0];

        if (this.grid[row][col] == Advent17.EMPTY)
            this.grid[row][col] = Advent17.FLOW;

        while (row + 1 < this.grid.length && this.grid[row + 1][col] == Advent17.EMPTY)
            this.grid[++row][col] = Advent17.FLOW;

        if (row + 1 != this.grid.length) {

            this.flowRight(col, row);
            this.flowLeft(col, row);

            final Advent17.Containment containment = new Advent17.Containment(col, row).invoke();
            if (row - 1 > 0 && containment.isContained())
                this.flow(col, row - 1);
        }
    }

    private void flowLeft(final int... origin) {
        int       col = origin[0];
        final int row = origin[1];

        while (--col >= 0)
            if (this.hasHorizontalFlow(row, col, origin))
                break;
    }

    private boolean hasHorizontalFlow(final int row, final int col, final int... origin) {
        if (this.grid[row + 1][col] == Advent17.EMPTY) {
            this.grid[row][col] = Advent17.FLOW;
            this.flow(col, row);
            return true;
        } else if (this.grid[row][col] == Advent17.CLAY) {
            this.fillRow(origin);
            return true;
        } else
            return this.updateNextTile(col, row);
    }

    private void fillRow(final int... origin) {
        final Advent17.Containment containment = new Advent17.Containment(origin).invoke();

        if (containment.isContained()) {
            int cursor = containment.getWallToTheLeft().getAsInt();
            while (cursor++ < containment.getWallToTheRight().getAsInt() - 1)
                this.grid[origin[1]][cursor] = Advent17.WATER;
        }
    }

    private boolean updateNextTile(final int col, final int row) {
        if (this.grid[row][col] == Advent17.EMPTY && (this.grid[row + 1][col] == Advent17.CLAY || this.grid[row + 1][col] == Advent17.WATER))
            this.grid[row][col] = Advent17.FLOW;
        else
            return true;
        return false;
    }

    private void flowRight(final int... origin) {
        int       col = origin[0];
        final int row = origin[1];

        while (++col < this.grid[row].length)
            if (this.hasHorizontalFlow(row, col, origin))
                break;
    }

    private class Containment {
        private final int[]       origin;
        private       OptionalInt wallToTheLeft;
        private       OptionalInt wallToTheRight;

        Containment(final int... origin) {this.origin = origin;}

        OptionalInt getWallToTheLeft() {
            return this.wallToTheLeft;
        }

        OptionalInt getWallToTheRight() {
            return this.wallToTheRight;
        }

        Advent17.Containment invoke() {
            this.wallToTheLeft = IntStream.iterate(this.origin[0], left -> left >= 0, left -> left - 1)
                                          .takeWhile(left -> Advent17.this.grid[this.origin[1] + 1][left] != Advent17.EMPTY)
                                          .filter(left -> Advent17.this.grid[this.origin[1]][left] == Advent17.CLAY)
                                          .findFirst();

            this.wallToTheRight = IntStream.range(this.origin[0], Advent17.this.grid[this.origin[1]].length)
                                           .takeWhile(right -> Advent17.this.grid[this.origin[1] + 1][right] != Advent17.EMPTY)
                                           .filter(right -> Advent17.this.grid[this.origin[1]][right] == Advent17.CLAY)
                                           .findFirst();
            return this;
        }

        boolean isContained() {
            return this.wallToTheLeft.isPresent() && this.wallToTheRight.isPresent();
        }
    }
}