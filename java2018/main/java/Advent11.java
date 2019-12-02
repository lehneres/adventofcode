import java.util.Arrays;
import java.util.Comparator;

@SuppressWarnings("UtilityClass")
public final class Advent11 {

    private static final int GRID_SIZE          = 300;
    private static final int GRID_SERIAL_NUMBER = 3214;
    private static final int SUB_GRID_SIZE      = 3;

    public static void main(final String[] args) {

        final int[][] grid = new int[Advent11.GRID_SIZE][Advent11.GRID_SIZE];

        Advent11.ComputeMaximumGrid computeMaximumGrid = new Advent11.ComputeMaximumGrid(grid, Advent11.SUB_GRID_SIZE, Advent11.GRID_SERIAL_NUMBER).invoke();
        int                         maxX               = computeMaximumGrid.getMaxX();
        int                         maxY               = computeMaximumGrid.getMaxY();
        int                         maxPower;

        System.out.format("(%d,%d)\n\n", maxX + 1, maxY + 1);

        final int[][] powerStates = new int[Advent11.GRID_SIZE][4];

        for (int i = 1; i <= Advent11.GRID_SIZE; i++) {
            computeMaximumGrid = new Advent11.ComputeMaximumGrid(grid, i, Advent11.GRID_SERIAL_NUMBER).invoke();
            maxX = computeMaximumGrid.getMaxX();
            maxY = computeMaximumGrid.getMaxY();
            maxPower = computeMaximumGrid.getMaxPower();

            powerStates[i - 1] = new int[]{maxX + 1, maxY + 1, i, maxPower};
        }

        Arrays.stream(powerStates).sorted(Comparator.comparingInt(p -> -p[3])).forEach(p -> System.out.format("(%4d,%4d,%4d) -- %d\n", p[0], p[1], p[2], p[3]));
    }

    @SuppressWarnings("unused")
    private static void printMatrix(final int[][] matrix) {
        for (final int[] matrix1 : matrix) {
            for (final int i : matrix1) System.out.printf("%4d", i);
            System.out.println();
        }
    }


    private static class ComputeMaximumGrid {
        private final int[][] grid;
        private final int     subGridSize;
        private final int     serialNumber;
        private       int     maxX;
        private       int     maxY;
        private       int     maxSubGridSum;

        ComputeMaximumGrid(final int[][] grid, final int subGridSize, final int serialNumber) {
            this.grid = grid;
            this.subGridSize = subGridSize;
            this.serialNumber = serialNumber;
        }

        int getMaxX() {
            return this.maxX;
        }

        int getMaxY() {
            return this.maxY;
        }

        @SuppressWarnings({"OverlyComplexMethod", "OverlyLongMethod"})
        Advent11.ComputeMaximumGrid invoke() {
            for (int x = 1; x <= Advent11.GRID_SIZE; x++)
                for (int y = 1; y <= Advent11.GRID_SIZE; y++) this.grid[x - 1][y - 1] = (((((x + 10) * y + this.serialNumber) * (x + 10)) / 100) % 10) - 5;

            final int[][] sumSub = new int[Advent11.GRID_SIZE][Advent11.GRID_SIZE];

            for (int y = 0; y < Advent11.GRID_SIZE; y++) {

                int sum = 0;
                for (int x = 0; x < this.subGridSize; x++) {
                    sum += this.grid[x][y];
                    sumSub[x][y] = sum;
                }

                for (int x = 1; x < Advent11.GRID_SIZE - this.subGridSize + 1; x++) {
                    sum += this.grid[x + this.subGridSize - 1][y] - this.grid[x - 1][y];
                    sumSub[x][y] = sum;
                }
            }

            this.maxSubGridSum = Integer.MIN_VALUE;
            this.maxX = 0;
            this.maxY = 0;

            for (int x = 0; x < Advent11.GRID_SIZE - this.subGridSize + 1; x++) {
                int sum = 0;
                for (int y = 0; y < this.subGridSize; y++)
                    sum += sumSub[x][y];

                if (sum > this.maxSubGridSum) {
                    this.maxSubGridSum = sum;
                    this.maxX = x;
                    this.maxY = 0;
                }

                for (int y = 1; y < Advent11.GRID_SIZE - this.subGridSize + 1; y++) {
                    sum += sumSub[x][y + this.subGridSize - 1] - sumSub[x][y - 1];

                    if (sum > this.maxSubGridSum) {
                        this.maxSubGridSum = sum;
                        this.maxX = x;
                        this.maxY = y;
                    }
                }
            }
            return this;
        }

        int getMaxPower() {
            return this.maxSubGridSum;
        }
    }
}
