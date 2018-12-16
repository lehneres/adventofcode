import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class Advent15 {

    private static final char    ELF    = 'E';
    private static final char    GOBLIN = 'G';
    private static final char    EMPTY  = '.';
    private              int[][] grid;

    public static void main(final String[] args) throws IOException {
        final Advent15 battle = new Advent15();

        battle.readGridFromFile("src/main/resources/advent15.txt");

        while (battle.hasNextRound()) ;
    }

    private static List<Advent15.Point> constructPath(final Map<Advent15.Point, ? extends Advent15.Point> trace, Advent15.Point target) {
        final List<Advent15.Point> path = new ArrayList<>();
        path.add(target);
        while (trace.containsKey(target)) {
            target = trace.get(target);
            path.add(target);
        }
        Collections.reverse(path);
        return path;
    }

    boolean hasNextRound() {
        final List<Advent15.Point> candidates = new ArrayList<>();
        for (int row = 0; row < this.grid.length; row++)
            for (int col = 0; col < this.grid[row].length; col++)
                if (this.grid[row][col] == Advent15.ELF || this.grid[row][col] == Advent15.GOBLIN) candidates.add(new Advent15.Point(row, col));
        return candidates.stream().mapToInt(candidate -> this.hasNextTurn(candidate) ? 1 : 0).sum() > 0;
    }

    private boolean hasNextTurn(final Advent15.Point origin) {
        final char enemyFlag = this.grid[origin.getRow()][origin.getCol()] == Advent15.ELF ? Advent15.GOBLIN : Advent15.ELF;

        final Set<Advent15.Point> enemies = new HashSet<>();

        for (int row = 0; row < this.grid.length; row++)
            for (int col = 0; col < this.grid[row].length; col++)
                if (this.grid[row][col] == enemyFlag) enemies.add(new Advent15.Point(row, col));

        if (enemies.isEmpty()) return false;

        final Set<Advent15.Point> targetSquares = new HashSet<>();

        for (final Advent15.Point enemy : enemies) {
            final int col = enemy.getCol();
            final int row = enemy.getRow();

            if (this.grid[row - 1][col] == Advent15.EMPTY) targetSquares.add(new Advent15.Point(row - 1, col));
            if (this.grid[row + 1][col] == Advent15.EMPTY) targetSquares.add(new Advent15.Point(row + 1, col));
            if (this.grid[row][col - 1] == Advent15.EMPTY) targetSquares.add(new Advent15.Point(row, col - 1));
            if (this.grid[row][col + 1] == Advent15.EMPTY) targetSquares.add(new Advent15.Point(row, col + 1));
        }

        if (targetSquares.isEmpty()) return false;

        for (final Advent15.Point enemy : enemies) {
            final int col = enemy.getCol();
            final int row = enemy.getRow();

            if (row - 1 == origin.getRow() && col == origin.getCol()) return this.attack(origin);
            if (row + 1 == origin.getRow() && col == origin.getCol()) return this.attack(origin);
            if (row == origin.getRow() && col - 1 == origin.getCol()) return this.attack(origin);
            if (row == origin.getRow() && col + 1 == origin.getCol()) return this.attack(origin);
        }


        return this.hasMove(origin, targetSquares);
    }

    private boolean hasMove(final Advent15.Point origin, final Set<Advent15.Point> targetSquares) {
        final List<List<Advent15.Point>> paths = targetSquares.stream()
                                                              .map(targetSquare -> this.findPath(origin, targetSquare))
                                                              .filter(path -> !path.isEmpty())
                                                              .sorted((path1, path2) -> {
                                                                  final int compareSize = Integer.compare(path1.size(), path2.size());
                                                                  if (compareSize != 0) return compareSize;
                                                                  final Advent15.Point target1 = path1.get(path1.size() - 1);
                                                                  final Advent15.Point target2 = path2.get(path2.size() - 1);
                                                                  return target1.compareTo(target2);
                                                              })
                                                              .collect(Collectors.toList());

        if (paths.isEmpty()) return false;

        final Advent15.Point next = paths.get(0).get(1);

        this.grid[next.getRow()][next.getCol()] = this.grid[origin.getRow()][origin.getCol()];
        this.grid[origin.getRow()][origin.getCol()] = Advent15.EMPTY;

        return true;
    }

    private List<Advent15.Point> findPath(final Advent15.Point start, final Advent15.Point target) {
        final Set<Advent15.Point>                 closedSet = new HashSet<>();
        final Set<Advent15.Point>                 openSet   = new HashSet<>();
        final Map<Advent15.Point, Advent15.Point> trace     = new HashMap<>();
        final Map<Advent15.Point, Integer>        gScore    = new HashMap<>();

        for (int row = 0; row < this.grid.length; row++)
            for (int col = 0; col < this.grid[row].length; col++)
                gScore.put(new Advent15.Point(row, col), Integer.MAX_VALUE);


        final Map<Advent15.Point, Integer> fScore = new HashMap<>(gScore);

        gScore.put(start, 0);
        fScore.put(start, start.computeDistance(target));

        openSet.add(start);

        do {
            final Advent15.Point current = openSet.stream().min(Comparator.comparing(fScore::get)).get();
            if (current.equals(target)) return Advent15.constructPath(trace, current);

            openSet.remove(current);
            closedSet.add(current);

            final Advent15.Point[] candidates = {new Advent15.Point(current.getRow() - 1, current.getCol()), new Advent15.Point(current.getRow() + 1, current.getCol()),
                                                 new Advent15.Point(current.getRow(), current.getCol() - 1), new Advent15.Point(current.getRow(), current.getCol() + 1)};

            for (final Advent15.Point neighbor : candidates) {
                if (this.grid[neighbor.getRow()][neighbor.getCol()] != Advent15.EMPTY) continue;
                if (closedSet.contains(neighbor)) continue;

                final int tScore = gScore.get(current) + 1;

                if (!openSet.contains(neighbor)) openSet.add(neighbor);
                else if (tScore > gScore.get(neighbor)) continue;
                else if (tScore == gScore.get(neighbor)) if (current.getClockWisePosition(trace.get(neighbor)) > 0) continue;

                trace.put(neighbor, current);
                gScore.put(neighbor, tScore);
                fScore.put(neighbor, tScore + current.computeDistance(target));
            }
        } while (!openSet.isEmpty());

        return Collections.emptyList();
    }

    private boolean attack(final Advent15.Point point) {
//        throw new NotImplementedException("to be implemented");
        return true;
    }

    void readGridFromFile(final String file) throws IOException {
        final List<String> lines = Files.lines(Paths.get(file)).collect(Collectors.toList());

        this.grid = new int[lines.size()][];
        for (int row = 0; row < lines.size(); row++) {
            final char[] segments = lines.get(row).toCharArray();
            this.grid[row] = new int[segments.length];

            for (int col = 0; col < segments.length; col++)
                this.grid[row][col] = segments[col];
        }
    }

    void print() {
        Arrays.stream(this.grid).forEach(ints -> {
            Arrays.stream(ints).forEach(anInt -> System.out.printf("%c", anInt));
            System.out.println();
        });
    }

    int[][] getGrid() {
        return this.grid.clone();
    }

    private static class Point implements Comparable<Advent15.Point> {
        private final int col;
        private final int row;

        Point(final int row, final int col) {
            this.col = col;
            this.row = row;
        }

        int computeDistance(final Advent15.Point otherPoint) {
            return Math.abs(this.col - otherPoint.col) + Math.abs(this.row - otherPoint.row);
        }

        @Override
        public boolean equals(final Object obj) {
            if (obj instanceof Advent15.Point) return this.compareTo((Advent15.Point) obj) == 0;
            return super.equals(obj);
        }

        int getRow() {
            return this.row;
        }

        int getCol() {
            return this.col;
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.col, this.row);
        }

        @Override
        public int compareTo(final @NotNull Advent15.Point o) {
            final int rowDiff = this.row - o.row;
            if (rowDiff != 0) return rowDiff;
            return this.col - o.col;
        }

        @Override
        public String toString() {
            return String.format("( %2d,%2d )", this.row, this.col);
        }

        int getClockWisePosition(final Advent15.Point o) {
            final int rowDiff = this.row - o.row;
            if (rowDiff != 0) return rowDiff;
            return this.col - o.col;
        }
    }
}
