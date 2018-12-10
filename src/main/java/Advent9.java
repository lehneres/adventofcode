import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

@SuppressWarnings("UtilityClass")
public final class Advent9 {


    private static final int MAGICNUMBER = 23;

    public static void main(final String... args) throws IOException {

        final String input = Files.lines(Paths.get("src/main/resources/advent9.txt")).findFirst().get();

        final Pattern r       = Pattern.compile("(\\d+) players; last marble is worth (\\d+) points");
        final Matcher matcher = r.matcher(input);

        int players   = 0;
        int maxMarble = 0;

        if (matcher.find()) {
            players = Integer.parseInt(matcher.group(1));
            maxMarble = Integer.parseInt(matcher.group(2));
        }

        System.out.println(Arrays.stream(Advent9.playMarble(players, maxMarble)).max().getAsLong());
        System.out.println(Arrays.stream(Advent9.playMarble(players, maxMarble * 100)).max().getAsLong());
    }

    private static long[] playMarble(final int players, final int maxMarble) {
        final Advent9.CircleDeque<Integer> circle = new Advent9.CircleDeque<>(maxMarble);

        circle.add(0);

        int currentPlayer = 1;
        int lastMarble    = 0;

        final long[] scores = new long[players];

        while (lastMarble + 1 < maxMarble) {
            if ((lastMarble + 1) % Advent9.MAGICNUMBER == 0) {
                circle.rotate(-7);
                scores[currentPlayer] += (lastMarble + 1) + circle.pop();

            } else {
                circle.rotate(2);
                circle.addLast(lastMarble + 1);
            }
            currentPlayer = ++currentPlayer % players;
            lastMarble++;

            //if (lastMarble % 100000 == 0) System.out.println(lastMarble);
        }

        return scores;
    }

    static class CircleDeque<T> extends ArrayDeque<T> {
        CircleDeque(final int maxMarble) {
            super(maxMarble);
        }

        void rotate(final int num) {
            if (num == 0) return;
            if (num > 0) IntStream.range(0, num).mapToObj(i -> this.removeLast()).forEach(this::addFirst);
            else IntStream.range(0, Math.abs(num) - 1).mapToObj(i -> this.remove()).forEach(this::addLast);
        }
    }
}