import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@SuppressWarnings("UtilityClass")
public final class Advent2 {

    public static void main(final String... args) throws IOException {

        final int[]       cCount = new int[2];
        final Set<String> boxIds = Files.lines(Paths.get("src/main/resources/advent2.txt")).collect(Collectors.toSet());

        boxIds.forEach(boxId -> {
            final int[] localCount = new int[26];
            boxId.chars().forEach(i -> localCount[(i - 'a')]++);
            Arrays.stream(localCount).filter(i -> i == 2 || i == 3).distinct().forEach(i -> cCount[i - 2]++);
        });

        System.out.println(cCount[0] * cCount[1]);

        final String[] boxes = new String[2];

        outer:
        for (final String boxId1 : boxIds)
            for (final String boxId2 : boxIds)
                if (Advent2.HammingDistance(boxId1, boxId2) == 1) {
                    boxes[0] = boxId1;
                    boxes[1] = boxId2;
                    break outer;
                }

        System.out.println((IntStream.range(0, boxes[0].length())
                                     .filter(i -> boxes[0].charAt(i) == boxes[1].charAt(i))
                                     .mapToObj(i -> String.valueOf(boxes[0].charAt(i)))
                                     .collect(Collectors.joining())));
    }

    @SuppressWarnings("StandardVariableNames")
    private static int HammingDistance(final String a, final String b) {
        return a.length() == b.length() ? (int) IntStream.range(0, a.length()).filter(i -> a.charAt(i) != b.charAt(i)).count() : -1;
    }
}
