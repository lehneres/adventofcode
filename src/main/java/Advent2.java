import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

@SuppressWarnings("UtilityClass")
public final class Advent2 {

    public static void main(final String... args) throws IOException {

        final int[] cCount = new int[2];

        Files.lines(Paths.get("src/main/resources/advent2.txt")).forEach(boxId -> {
            final int[] localCount = new int[26]; boxId.chars().forEach(i -> localCount[(i - 'a')]++);
            Arrays.stream(localCount).filter(i -> i == 2 || i == 3).distinct().forEach(i -> cCount[i - 2]++);
        });

        System.out.println(cCount[0] * cCount[1]);

    }

}
