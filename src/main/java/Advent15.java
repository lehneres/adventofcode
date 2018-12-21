import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Advent15 {

    private static final char                         ELF               = 'E'; //69
    private static final char                         GOBLIN            = 'G'; //71
    private static final char                         EMPTY             = '.';
    private static final int                          INITIAL_HITPOINTS = 200;
    private static final int                          ATTACK_POWER      = 3;
    private final        Map<Integer, Advent15.Agent> agents            = new HashMap<>();
    private              int[][]                      grid;
    private              int                          currentRound;

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
        final List<Advent15.Agent> candidates = this.agents.values().stream().sorted(Advent15.Point::compareTo).collect(Collectors.toList());

        boolean nextRound = false;
        for (final Advent15.Agent candidate : candidates) {
            if (candidate.isDead()) continue;
            nextRound = this.hasNextTurn(candidate);
            if (!nextRound) break;
        }

        if (nextRound) this.currentRound++;
        return nextRound;
    }

    private boolean hasNextTurn(final Advent15.Agent current) {
        final char           enemyFlag = this.grid[current.getRow()][current.getCol()] % Advent15.ELF == 0 ? Advent15.GOBLIN : Advent15.ELF;
        final Advent15.Point origin    = new Advent15.Point(current.getRow(), current.getCol());

        // check if turn starts with enemy in reach
        if (this.checkAndAttack(current, enemyFlag)) return true;

        // no enemy in reach, moving
        final List<Advent15.Agent> enemies = this.agents.values()
                                                        .stream()
                                                        .filter(agent -> !agent.isDead())
                                                        .filter(agent -> agent.getType() == enemyFlag).sorted(Advent15.Agent::compareTo)
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
        });

        // no free target squares found
        if (targetSquares.isEmpty()) return true;

        targetSquares.sort(Advent15.Point::compareTo);

        final Advent15.Point next = this.move(current, targetSquares);

        if (!next.equals(origin))
            // check if agent has enemy in reach after moving
            this.checkAndAttack(current, enemyFlag);

        return true;
    }

    private boolean checkAndAttack(final Advent15.Agent agent, final char enemyFlag) {
        final int col = agent.getCol();
        final int row = agent.getRow();

        final List<Advent15.Agent> targets = new ArrayList<>();
        if (this.grid[row - 1][col] % enemyFlag == 0) targets.add(this.agents.get(this.grid[row - 1][col]));
        if (this.grid[row + 1][col] % enemyFlag == 0) targets.add(this.agents.get(this.grid[row + 1][col]));
        if (this.grid[row][col - 1] % enemyFlag == 0) targets.add(this.agents.get(this.grid[row][col - 1]));
        if (this.grid[row][col + 1] % enemyFlag == 0) targets.add(this.agents.get(this.grid[row][col + 1]));

        if (!targets.isEmpty()) {
            final Advent15.Agent bestTarget = targets.stream().min(Advent15.Agent::compareTo).get();

            final int targetRow = bestTarget.getRow();
            final int targetCol = bestTarget.getCol();
            bestTarget.reduceHitpoints(agent.getAttackPower());

            if (bestTarget.isDead()) this.grid[targetRow][targetCol] = Advent15.EMPTY;
            return true;
        }

        return false;
    }

    private Advent15.Point move(final Advent15.Agent agent, final List<Advent15.Point> targetSquares) {
        final List<List<Advent15.Point>> paths = targetSquares.stream().map(targetSquare -> this.findPath(agent, targetSquare))
                                                              .filter(path -> !path.isEmpty())
                                                              .sorted((path1, path2) -> {
                                                                  final int compareSize = Integer.compare(path1.size(), path2.size());
                                                                  if (compareSize != 0) return compareSize;
                                                                  final Advent15.Point target1 = path1.get(path1.size() - 1);
                                                                  final Advent15.Point target2 = path2.get(path2.size() - 1);
                                                                  return target1.compareTo(target2);
                                                              })
                                                              .collect(Collectors.toList());

        if (paths.isEmpty()) return agent;

        final Advent15.Point next = paths.get(0).get(1);

        this.grid[next.getRow()][next.getCol()] = this.grid[agent.getRow()][agent.getCol()];
        this.grid[agent.getRow()][agent.getCol()] = Advent15.EMPTY;

        agent.setCol(next.getCol());
        agent.setRow(next.getRow());
        return next;
    }

    private List<Advent15.Point> findPath(final Advent15.Point start, final Advent15.Point target) {
        final Set<Advent15.Point>                 closedSet = new HashSet<>();
        final Set<Advent15.Point>                 openSet   = new HashSet<>();
        final Map<Advent15.Point, Advent15.Point> trace     = new HashMap<>();
        final Map<Advent15.Point, Integer>        gScore    = new HashMap<>();

        for (int row = 0; row < this.grid.length; row++)
            for (int col = 0; col < this.grid[row].length; col++)
                gScore.put(new Advent15.Point(row, col), Integer.MAX_VALUE);

        gScore.put(start, 0);
        openSet.add(start);

        do {
            final Advent15.Point current = openSet.stream().min(Advent15.Point::compareTo).get();
            if (current.equals(target)) return Advent15.constructPath(trace, current);

            openSet.remove(current);
            closedSet.add(current);

            final Advent15.Point[] candidates = {new Advent15.Point(current.getRow() - 1, current.getCol()), new Advent15.Point(current.getRow(), current.getCol() - 1),
                                                 new Advent15.Point(current.getRow() + 1, current.getCol()), new Advent15.Point(current.getRow(), current.getCol() + 1)};

            for (final Advent15.Point neighbor : candidates) {
                if (this.grid[neighbor.getRow()][neighbor.getCol()] != Advent15.EMPTY) continue;
                if (closedSet.contains(neighbor)) continue;

                final int tScore = gScore.get(current) + 1;

                if (!openSet.contains(neighbor)) openSet.add(neighbor);
                else if (tScore > gScore.get(neighbor)) continue;
                else if (tScore == gScore.get(neighbor)) if (current.getClockWisePosition(trace.get(neighbor)) > 0) continue;

                trace.put(neighbor, current);
                gScore.put(neighbor, tScore);
            }
        } while (!openSet.isEmpty());

        return Collections.emptyList();
    }

    void readGridFromFile(final String file) throws IOException {
        final List<String> lines = Files.lines(Paths.get(file)).collect(Collectors.toList());

        this.grid = new int[lines.size()][];
        for (int row = 0; row < lines.size(); row++) {
            final char[] segments = lines.get(row).toCharArray();
            this.grid[row] = new int[segments.length];
            for (int col = 0; col < segments.length; col++) this.grid[row][col] = segments[col];
        }

        int elfCounter    = 0;
        int goblinCounter = 0;

        for (int row = 0; row < this.grid.length; row++)
            for (int col = 0; col < this.grid[row].length; col++) {
                if (this.grid[row][col] == Advent15.ELF) {
                    this.agents.put(Advent15.ELF * ++elfCounter, new Advent15.Agent(Advent15.ELF, row, col));
                    this.grid[row][col] = Advent15.ELF * elfCounter;
                } else if (this.grid[row][col] == Advent15.GOBLIN) {
                    this.agents.put(Advent15.GOBLIN * ++goblinCounter, new Advent15.Agent(Advent15.GOBLIN, row, col));
                    this.grid[row][col] = Advent15.GOBLIN * goblinCounter;
                }
            }
    }

    private void print() {
        IntStream.range(0, this.grid.length).forEach(i -> {
            Arrays.stream(this.grid[i]).forEach(anInt -> {
                if (anInt % Advent15.ELF == 0) System.out.printf("%c", Advent15.ELF);
                else if (anInt % Advent15.GOBLIN == 0) System.out.printf("%c", Advent15.GOBLIN);
                else System.out.printf("%c", anInt);
            });
            System.out.print("     ");
            this.agents.values().stream().filter(a -> !a.isDead()).filter(a -> a.getRow() == i).sorted(Comparator.comparingInt(Advent15.Agent::getCol)).forEach(System.out::print);
            System.out.println();
        });
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

    /**
     * Note: this class has a natural ordering that is inconsistent with equals.
     */
    private static class Agent extends Advent15.Point {

        private final char type;
        private final int  attackPower = Advent15.ATTACK_POWER;
        private       int  hitpoints   = Advent15.INITIAL_HITPOINTS;

        Agent(final char type, final int row, final int col) {
            super(row, col);
            this.type = type;
        }


        int getHitpoints() {
            return this.hitpoints;
        }

        int getAttackPower() {
            return this.attackPower;
        }

        void reduceHitpoints(final int points) {
            this.hitpoints -= points;

            if (this.hitpoints < 0) {
                super.setRow(-1);
                super.setCol(-1);
            }
        }

        boolean isDead() {
            return this.hitpoints <= 0;
        }

        @SuppressWarnings("CompareToUsesNonFinalVariable")
        @Override
        public int compareTo(final @NotNull Advent15.Point o) {
            if (o instanceof Advent15.Agent) {
                final int pointDiff = this.hitpoints - ((Advent15.Agent) o).hitpoints;
                if (pointDiff != 0) return pointDiff;
            }
            return super.compareTo(o);
        }

        @Override
        public String toString() {
            return String.format("%c(%d)", this.type, this.hitpoints);
        }

        void setCol(final int col) {
            super.setCol(col);
        }

        void setRow(final int row) {
            super.setRow(row);
        }

        char getType() {
            return this.type;
        }
    }
}
