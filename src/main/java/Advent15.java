import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Advent15 {

    private static final char                ELF               = 'E'; //69
    private static final char                GOBLIN            = 'G'; //71
    private static final char                EMPTY             = '.';
    private static final int                 INITIAL_HITPOINTS = 200;
    private static final int                 goblinAttackPower = 3;
    private static       int                 elfAttackPower    = 3;
    private static       boolean             ignoreDeadElf     = true;
    private final        Set<Advent15.Agent> agents            = new HashSet<>();
    private              int[][]             grid;
    private              int                 currentRound;
    private              String              sourceFile;

    public static void main(final String[] args) throws IOException, Advent15.DeadElfException {
        final Advent15 battle = new Advent15();
        battle.readGridFromFile("src/main/resources/advent15.txt");

        battle.play(false, Integer.MAX_VALUE);

        System.out.println(battle.getOutcome());

        battle.reset();

        Advent15.ignoreDeadElf = false;

        while (true) {
            try {
                battle.play(false, Integer.MAX_VALUE);
                System.out.printf("%s with attack power: %d%n", battle.getOutcome(), Advent15.elfAttackPower);
                break;
            } catch (final Advent15.DeadElfException e) {
                battle.reset();
                Advent15.elfAttackPower++;
            }
        }
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

    private void reset() throws IOException {
        this.agents.clear();
        this.currentRound = 0;
        this.readGridFromFile(this.sourceFile);
    }

    String getOutcome() {
        final int sum = this.agents.stream().filter(Advent15.Agent::isAlive).mapToInt(Advent15.Agent::getHitpoints).sum();
        return String.format("the outcome is %d * %d = %d", this.currentRound, sum, sum * this.currentRound);
    }

    void play(@SuppressWarnings("SameParameterValue") final boolean print, int maxRounds) throws Advent15.DeadElfException {
        if (print) this.print();
        while (maxRounds-- > 0 && this.hasNextRound()) if (print) this.print();
    }

    private boolean hasNextRound() throws Advent15.DeadElfException {
        final List<Advent15.Agent> candidates = this.agents.stream().filter(Advent15.Agent::isAlive).sorted(Advent15.Point::compareTo).collect(Collectors.toList());

        boolean nextRound = false;
        for (final Advent15.Agent candidate : candidates) {
            if (candidate.isDead()) continue;
            nextRound = this.hasNextTurn(candidate);
            if (!nextRound) break;
        }

        if (nextRound) this.currentRound++;
        return nextRound;
    }

    private boolean hasNextTurn(final Advent15.Agent agent) throws Advent15.DeadElfException {
        final char enemyFlag = agent.getType() % Advent15.ELF == 0 ? Advent15.GOBLIN : Advent15.ELF;

        // no enemy in reach, moving
        final Collection<Advent15.Agent> enemies = this.agents.stream()
                                                              .filter(Advent15.Agent::isAlive)
                                                              .filter(a -> a.getType() == enemyFlag)
                                                              .sorted(Advent15.Agent::compareTo)
                                                              .collect(Collectors.toList());

        // no enemy left
        if (enemies.isEmpty()) return false;

        // check if turn starts with enemy in reach
        if (this.checkAndAttack(agent, enemies)) return true;

        // find target squares adjacent to enemy
        final List<Advent15.Point> targetSquares = enemies.stream().map(enemy -> {
            final Set<Advent15.Point> tS  = new HashSet<>();
            final int                 col = enemy.getCol();
            final int                 row = enemy.getRow();
            if (this.grid[row - 1][col] == Advent15.EMPTY) tS.add(new Advent15.Point(row - 1, col));
            if (this.grid[row][col - 1] == Advent15.EMPTY) tS.add(new Advent15.Point(row, col - 1));
            if (this.grid[row + 1][col] == Advent15.EMPTY) tS.add(new Advent15.Point(row + 1, col));
            if (this.grid[row][col + 1] == Advent15.EMPTY) tS.add(new Advent15.Point(row, col + 1));
            return tS;
        }).flatMap(Collection::stream).sorted(Advent15.Point::compareTo).collect(Collectors.toList());

        // no free target squares found
        if (targetSquares.isEmpty()) return true;

        final Advent15.Point origin = new Advent15.Point(agent.getRow(), agent.getCol());
        this.move(agent, targetSquares);

        // check if agent has enemy in reach after moving
        if (!agent.equals(origin)) this.checkAndAttack(agent, enemies);

        return true;
    }

    private boolean checkAndAttack(final Advent15.Agent agent, final Collection<Advent15.Agent> enemies) throws Advent15.DeadElfException {
        final int col = agent.getCol();
        final int row = agent.getRow();

        final Optional<Advent15.Agent> victim = enemies.stream()
                                                       .filter(enemy -> Math.abs(row - enemy.getRow()) + Math.abs(col - enemy.getCol()) == 1)
                                                       .min(Advent15.Agent::compareTo);

        if (victim.isPresent()) {
            final Advent15.Agent bestTarget = victim.get();

            bestTarget.reduceHitpoints(agent.getAttackPower());

            if (bestTarget.isDead()) this.grid[bestTarget.getRow()][bestTarget.getCol()] = Advent15.EMPTY;
            return true;
        }

        return false;
    }

    private void move(final Advent15.Agent agent, final List<Advent15.Point> targetSquares) {
        final List<List<Advent15.Point>> paths = targetSquares.parallelStream()
                                                              .map(target -> this.findPath(agent, target))
                                                              .filter(path -> !path.isEmpty())
                                                              .sorted((path1, path2) -> {
            final int compareSize = Integer.compare(path1.size(), path2.size());
            if (compareSize != 0) return compareSize;
            final Advent15.Point target1 = path1.get(path1.size() - 1);
            final Advent15.Point target2 = path2.get(path2.size() - 1);
            return target1.compareTo(target2);
        }).collect(Collectors.toList());

        if (paths.isEmpty()) return;

        final Advent15.Point next = paths.get(0).get(1);

        this.grid[next.getRow()][next.getCol()] = this.grid[agent.getRow()][agent.getCol()];
        this.grid[agent.getRow()][agent.getCol()] = Advent15.EMPTY;

        agent.setCol(next.getCol());
        agent.setRow(next.getRow());
    }

    private List<Advent15.Point> findPath(final Advent15.Point origin, final Advent15.Point target) {
        final Set<Advent15.Point>                 closedSet = new HashSet<>();
        final Deque<Advent15.Point>               openSet   = new ArrayDeque<>();
        final Map<Advent15.Point, Advent15.Point> trace     = new HashMap<>();
        final Map<Advent15.Point, Integer>        gScore    = new HashMap<>();

        for (int row = 0; row < this.grid.length; row++)
            for (int col = 0; col < this.grid[row].length; col++)
                gScore.put(new Advent15.Point(row, col), Integer.MAX_VALUE);

        gScore.put(origin, 0);
        openSet.add(origin);

        do {
            final Advent15.Point current = openSet.pop();
            if (current.equals(target)) return Advent15.constructPath(trace, current);

            closedSet.add(current);

            final Advent15.Point[] candidates = {new Advent15.Point(current.getRow() - 1, current.getCol()), new Advent15.Point(current.getRow() + 1, current.getCol()),
                                                 new Advent15.Point(current.getRow(), current.getCol() - 1), new Advent15.Point(current.getRow(), current.getCol() + 1)};

            Arrays.stream(candidates)
                  .filter(neighbor -> this.grid[neighbor.getRow()][neighbor.getCol()] == Advent15.EMPTY)
                  .filter(neighbor -> !closedSet.contains(neighbor))
                  .forEach(neighbor -> {
                      final int tScore = gScore.get(current) + 1;
                      if (!openSet.contains(neighbor)) openSet.add(neighbor);
                      else if (tScore > gScore.get(neighbor)) return;
                      else if (tScore == gScore.get(neighbor)) if (current.compareTo(neighbor) > 0) return;
                      trace.put(neighbor, current);
                      gScore.put(neighbor, tScore);
                  });
        } while (!openSet.isEmpty());

        return Collections.emptyList();
    }

    void readGridFromFile(final String file) throws IOException {
        final List<String> lines = Files.lines(Paths.get(file)).collect(Collectors.toList());
        this.sourceFile = file;

        this.grid = new int[lines.size()][];
        for (int row = 0; row < lines.size(); row++) {
            final char[] segments = lines.get(row).toCharArray();
            this.grid[row] = new int[segments.length];
            for (int col = 0; col < segments.length; col++) this.grid[row][col] = segments[col];
        }

        for (int row = 0; row < this.grid.length; row++)
            for (int col = 0; col < this.grid[row].length; col++)
                if (this.grid[row][col] == Advent15.ELF) {
                    this.agents.add(new Advent15.Agent(Advent15.ELF, row, col));
                    this.grid[row][col] = Advent15.ELF;
                } else if (this.grid[row][col] == Advent15.GOBLIN) {
                    this.agents.add(new Advent15.Agent(Advent15.GOBLIN, row, col));
                    this.grid[row][col] = Advent15.GOBLIN;
                }
    }

    private void print() {
        System.out.printf("Current round %d%n", this.currentRound);
        IntStream.range(0, this.grid.length).forEach(i -> {
            Arrays.stream(this.grid[i]).forEach(anInt -> {
                if (anInt % Advent15.ELF == 0) System.out.printf("\u001B[32m%c\u001B[0m", Advent15.ELF);
                else if (anInt % Advent15.GOBLIN == 0) System.out.printf("\u001B[31m%c\u001B[0m", Advent15.GOBLIN);
                else System.out.printf("%c", anInt);
            });
            System.out.print("     ");
            this.agents.stream().filter(a -> !a.isDead()).filter(a -> a.getRow() == i).sorted(Comparator.comparingInt(Advent15.Agent::getCol)).forEach(System.out::print);
            System.out.println();
        });
        System.out.println();
    }

    int[][] getGrid() {
        final int[][] normalizedGrid = this.grid.clone();
        for (int i = 0; i < normalizedGrid.length; i++)
            for (int j = 0; j < normalizedGrid[i].length; j++) {
                if (normalizedGrid[i][j] % Advent15.ELF == 0) normalizedGrid[i][j] = Advent15.ELF;
                else if (normalizedGrid[i][j] % Advent15.GOBLIN == 0) normalizedGrid[i][j] = Advent15.GOBLIN;
            }
        return normalizedGrid;
    }

    private static class Point implements Comparable<Advent15.Point> {
        private int row;
        private int col;

        Point(final int row, final int col) {
            this.col = col;
            this.row = row;
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
    }

    private static class Agent extends Advent15.Point {

        private final char type;
        private final int  attackPower;
        private       int  hitpoints = Advent15.INITIAL_HITPOINTS;

        Agent(final char type, final int row, final int col) {
            super(row, col);
            this.type = type;
            this.attackPower = this.type == Advent15.ELF ? Advent15.elfAttackPower : Advent15.goblinAttackPower;
        }

        @SuppressWarnings("CompareToUsesNonFinalVariable")
        int compareTo(final @NotNull Advent15.Agent o) {
            final int hitDiff = this.hitpoints - o.hitpoints;
            if (hitDiff != 0) return hitDiff;
            return super.compareTo(o);
        }

        int getHitpoints() {
            return this.hitpoints;
        }

        int getAttackPower() {
            return this.attackPower;
        }

        void reduceHitpoints(final int points) throws Advent15.DeadElfException {
            this.hitpoints -= points;
            if (this.isDead() && this.type == Advent15.ELF && !Advent15.ignoreDeadElf) throw new Advent15.DeadElfException();
        }

        boolean isAlive() {
            return this.hitpoints > 0;
        }

        boolean isDead() {
            return this.hitpoints <= 0;
        }

        @Override
        public String toString() {
            return String.format("%c(%d)", this.type, this.hitpoints);
        }

        char getType() {
            return this.type;
        }

        void setCol(final int col) {
            super.setCol(col);
        }

        void setRow(final int row) {
            super.setRow(row);
        }
    }

    private static class DeadElfException extends Exception {}
}
