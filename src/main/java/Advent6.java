import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@SuppressWarnings("UtilityClass")
public final class Advent6 {

    private static final int MAX_DIST = 10000;

    @SuppressWarnings({"OverlyLongMethod", "OverlyComplexMethod"})
    public static void main(final String... args) throws IOException {

        final Set<Integer[]> coordinates = Files.lines(Paths.get("src/main/resources/advent6.txt")).map(s -> {
            String[] split = s.split(",");
            return new Integer[]{Integer.valueOf(split[0].trim()), Integer.valueOf(split[1].trim())};
        }).collect(Collectors.toSet());

        final int maxX = coordinates.stream().mapToInt(coord -> coord[0]).max().getAsInt();
        final int maxY = coordinates.stream().mapToInt(coord -> coord[1]).max().getAsInt();

        final Integer[][][]               areas        = new Integer[maxX][maxY][2];
        final Map<Integer[], Integer[][]> distMatrices = new HashMap<>();

        for (final Integer[] coord : coordinates) {
            final Integer[][] matrix = new Integer[maxX][maxY];
            for (int x = 0; x < maxX; x++)
                for (int y = 0; y < maxY; y++)
                    matrix[x][y] = Math.abs(x - coord[0]) + Math.abs(y - coord[1]);
            distMatrices.put(coord, matrix);
        }


        final Set<Integer[]> infiniteAreaCoords = new HashSet<>();

        for (int x = 0; x < maxX; x++)
            for (int y = 0; y < maxY; y++) {
                int minDistance = Integer.MAX_VALUE;
                for (final Integer[] coord : distMatrices.keySet()) {
                    final int distance = distMatrices.get(coord)[x][y];
                    if (minDistance > distance) {
                        minDistance = distance;
                        areas[x][y] = coord;
                    } else if (minDistance == distance) areas[x][y] = null;
                }
                if (x == 0 || x == maxX - 1 || y == 0 || y == maxY - 1) if (areas[x][y] != null) infiniteAreaCoords.add(areas[x][y]);
            }

        final Map<Integer[], Long> resultAreas = Arrays.stream(areas)
                                                       .flatMap(Arrays::stream)
                                                       .filter(Objects::nonNull)
                                                       .filter(e -> !infiniteAreaCoords.contains(e))
                                                       .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        final Map.Entry<Integer[], Long> resultCoord = Collections.max(resultAreas.entrySet(), (e1, e2) -> (int) (e1.getValue() - e2.getValue()));

        System.out.format("(%d,%d) with area %d\n", resultCoord.getKey()[0], resultCoord.getKey()[1], resultCoord.getValue());

        final Integer[][] superMatrix = distMatrices.values().stream().reduce((matrix1, matrix2) -> {
            final Integer[][] matrix = new Integer[maxX][maxY];
            for (int x = 0; x < maxX; x++)
                for (int y = 0; y < maxY; y++)
                    matrix[x][y] = matrix1[x][y] + matrix2[x][y];
            return matrix;
        }).get();

        final long count = Arrays.stream(superMatrix).flatMap(Arrays::stream).filter(i -> i < Advent6.MAX_DIST).count();

        System.out.println(count);
    }

}
