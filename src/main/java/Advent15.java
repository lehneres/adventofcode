import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Advent15 {

    private static final char    ELF    = 'E';
    private static final char    GOBLIN = 'G';
    private static final char    EMPTY  = '.';
    private              int[][] grid;

    public static void main(final String[] args) throws IOException {
        final Advent15 battle = new Advent15();

        battle.readGridFromFile("src/main/resources/advent15.txt");

        battle.play(false, Integer.MAX_VALUE);

        System.out.println(battle.getOutcome());
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

    String getOutcome() {
        final int sum = this.agents.values().stream().filter(agent -> !agent.isDead()).mapToInt(Advent15.Agent::getHitpoints).sum();
        return String.format("the outcome is %d * %d = %d", this.currentRound, sum, sum * this.currentRound);
    }

    void play(@SuppressWarnings("SameParameterValue") final boolean print, int maxRounds) {
        while (maxRounds-- > 0 && this.hasNextRound()) if (print) this.print();
    }

    private boolean hasNextRound() {
        final List<Advent15.Point> candidates = new ArrayList<>();
        for (int row = 0; row < this.grid.length; row++)
            for (int col = 0; col < this.grid[row].length; col++)
                if (this.grid[row][col] == Advent15.ELF || this.grid[row][col] == Advent15.GOBLIN) candidates.add(new Advent15.Point(row, col));
        return candidates.stream().mapToInt(candidate -> this.hasNextTurn(candidate) ? 1 : 0).sum() > 0;
    }

    private boolean hasNextTurn(final Advent15.Point origin) {
        final char enemyFlag = this.grid[origin.getRow()][origin.getCol()] == Advent15.ELF ? Advent15.GOBLIN : Advent15.ELF;

        final Set<Advent15.Point> enemies = new HashSet<>();

        // check if turn starts with enemy in reach
        if (this.checkAndAttack(origin, enemyFlag)) return true;

        // no enemy in reach, moving
        final List<Advent15.Agent> enemies = this.agents.values()
                                                        .stream()
                                                        .filter(agent -> !agent.isDead())
                                                        .filter(agent -> agent.getType() == enemyFlag)
                                                        .collect(Collectors.toList());

        // no enemy left
        if (enemies.isEmpty()) return false;

        // find target squares adjacent to enemy
        final List<Advent15.Point> targetSquares = new ArrayList<>();
        enemies.forEach(enemy -> {
            final int col = enemy.getCol();
            final int row = enemy.getRow();

            if (this.grid[row - 1][col] == Advent15.EMPTY) targetSquares.add(new Advent15.Point(row - 1, col));
            if (this.grid[row + 1][col] == Advent15.EMPTY) targetSquares.add(new Advent15.Point(row + 1, col));
            if (this.grid[row][col - 1] == Advent15.EMPTY) targetSquares.add(new Advent15.Point(row, col - 1));
            if (this.grid[row][col + 1] == Advent15.EMPTY) targetSquares.add(new Advent15.Point(row, col + 1));
        }

        if (targetSquares.isEmpty()) return false;

        // check if agent has enemy in reach after moving
        this.checkAndAttack(origin, enemyFlag);
        return true;
    }

    private boolean checkAndAttack(final Advent15.Point origin, final char enemyFlag) {
        final List<Advent15.Agent> enemiesInDirectReach = this.enemiesInDirectReach(origin, enemyFlag);

        if (!enemiesInDirectReach.isEmpty()) {
            final int minHitPoints = enemiesInDirectReach.stream().mapToInt(Advent15.Agent::getHitpoints).min().getAsInt();

            final Advent15.Agent bestTarget = enemiesInDirectReach.stream().filter(a -> minHitPoints == a.getHitpoints()).min((a1, a2) -> {
                final int rowDiff = a1.getRow() - a2.getRow();
                if (rowDiff != 0) return rowDiff;
                return a1.getCol() - a2.getCol();
            }).get();

            bestTarget.reduceHitpoints(this.agents.get(this.grid[origin.getRow()][origin.getCol()]).getAttackPower());

            if (bestTarget.isDead()) this.grid[bestTarget.getRow()][bestTarget.getCol()] = Advent15.EMPTY;
            return true;
        }

        return false;
    }

    private List<Advent15.Agent> enemiesInDirectReach(final Advent15.Point origin, final char enemyFlag) {
        final int col = origin.getCol();
        final int row = origin.getRow();

        final List<Advent15.Agent> targets = new ArrayList<>();
        if (this.grid[row - 1][col] % enemyFlag == 0) targets.add(this.agents.get(this.grid[row - 1][col]));
        if (this.grid[row][col - 1] % enemyFlag == 0) targets.add(this.agents.get(this.grid[row][col - 1]));
        if (this.grid[row + 1][col] % enemyFlag == 0) targets.add(this.agents.get(this.grid[row + 1][col]));
        if (this.grid[row][col + 1] % enemyFlag == 0) targets.add(this.agents.get(this.grid[row][col + 1]));

        return targets;
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

        if (paths.isEmpty()) return origin;

        final Advent15.Point next = paths.get(0).get(1);

        this.grid[next.getRow()][next.getCol()] = this.grid[origin.getRow()][origin.getCol()];
        this.grid[origin.getRow()][origin.getCol()] = Advent15.EMPTY;

        return true;
    }

    private List<Advent15.Point> findPath(final Advent15.Point start, final Advent15.Point target) {
        final Set<Advent15.Point>                 closedSet = new HashSet<>();
        final List<Advent15.Point>                openSet   = new ArrayList<>();
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
        private int row;
        private int col;

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

        private void setRow(final int row) {
            this.row = row;
        }

        int getCol() {
            return this.col;
        }

        private void setCol(final int col) {
            this.col = col;
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.col, this.row);
        }

        @SuppressWarnings("CompareToUsesNonFinalVariable")
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
            return this.compareTo(o);
        }
    }
}
