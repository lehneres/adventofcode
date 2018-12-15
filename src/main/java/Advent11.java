@SuppressWarnings("UtilityClass")
public final class Advent11 {

    private static final int GRID_SIZE          = 300;
    private static final int GRID_SERIAL_NUMBER = 3214;
    private static final int SUB_GRID_SIZE      = 3;

    @SuppressWarnings({"OverlyComplexMethod", "OverlyLongMethod"})
    public static void main(final String[] args) {

        final int[][] grid = new int[Advent11.GRID_SIZE][Advent11.GRID_SIZE];

        for (int x = 1; x <= Advent11.GRID_SIZE; x++)
            for (int y = 1; y <= Advent11.GRID_SIZE; y++) grid[x - 1][y - 1] = (((((x + 10) * y + Advent11.GRID_SERIAL_NUMBER) * (x + 10)) / 100) % 10) - 5;

        final int[][] sumSub = new int[Advent11.GRID_SIZE][Advent11.GRID_SIZE];

        for (int y = 0; y < Advent11.GRID_SIZE; y++) {

            int sum = 0;
            for (int x = 0; x < Advent11.SUB_GRID_SIZE; x++) {
                sum += grid[x][y];
                sumSub[x][y] = sum;
            }

            for (int x = 1; x < Advent11.GRID_SIZE - Advent11.SUB_GRID_SIZE + 1; x++) {
                sum += grid[x + Advent11.SUB_GRID_SIZE - 1][y] - grid[x - 1][y];
                sumSub[x][y] = sum;
            }
        }

        int maxSubGridSum = Integer.MIN_VALUE, maxX = 0, maxY = 0;

        for (int x = 0; x < Advent11.GRID_SIZE - Advent11.SUB_GRID_SIZE + 1; x++) {
            int sum = 0;
            for (int y = 0; y < Advent11.SUB_GRID_SIZE; y++)
                sum += sumSub[x][y];

            if (sum > maxSubGridSum) {
                maxSubGridSum = sum;
                maxX = x;
                maxY = 0;
            }

            for (int y = 1; y < Advent11.GRID_SIZE - Advent11.SUB_GRID_SIZE + 1; y++) {
                sum += sumSub[x][y + Advent11.SUB_GRID_SIZE - 1] - sumSub[x][y - 1];

                if (sum > maxSubGridSum) {
                    maxSubGridSum = sum;
                    maxX = x;
                    maxY = y;
                }
            }
        }

        System.out.format("(%d,%d)", maxX + 1, maxY + 1);
    }

}
