import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.HashSet;

@SuppressWarnings("UtilityClass")
public final class Advent1 {

    public static void main(final String[] args) throws IOException {

        int                       sum  = 0;
        final int[]               ints = Files.lines(Paths.get("java2018/main/resources/advent1.txt")).mapToInt(Integer::parseInt).toArray();
        final Collection<Integer> seen = new HashSet<>(ints.length);
        int                       i    = 0;

        while (true) {
            sum += ints[i++];
            if (seen.contains(sum)) break;
            else seen.add(sum);
            if (i == ints.length) i = 0;
        }

        System.out.println(sum);

    }

}
