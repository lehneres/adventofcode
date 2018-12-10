import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

        System.out.println(Arrays.stream(Advent9.playMarble(players, maxMarble)).max().getAsInt());
        System.out.println(Arrays.stream(Advent9.playMarble(players, maxMarble * 100)).max().getAsInt());
    }

    private static int[] playMarble(final int players, final int maxMarble) {
        final Advent9.CircularArrayList<Integer> circle = new Advent9.CircularArrayList<>(0);

        circle.add(0);

        int currentPlayer = 1;
        int currentMarble = 0;
        int lastMarble    = 0;

        final int[] scores = new int[players];

        while (lastMarble + 1 < maxMarble) {
            if ((lastMarble + 1) % Advent9.MAGICNUMBER == 0) {
                scores[currentPlayer] += (lastMarble + 1) + circle.get(currentMarble - 7);

                circle.remove(currentMarble - 7);
                currentMarble -= 7;
            } else currentMarble = circle.addCircular(currentMarble + 2, lastMarble + 1);
            currentPlayer = ++currentPlayer % players;
            lastMarble++;

            if ((lastMarble + 1) % 100000 == 0) System.out.println(lastMarble + 1);
        }

        return scores;
    }

    static class CircularArrayList<E> extends ArrayList<E> {
        CircularArrayList(final int maxMarble) {
            super(maxMarble);
        }

        @SuppressWarnings("NonBooleanMethodNameMayNotStartWithQuestion")
        int addCircular(final int index, final E element) {
            final int circularIndex = this.size() == 1 ? 1 : Math.floorMod(index, this.size());
            this.add(circularIndex, element);
            return circularIndex;
        }

        @Override
        public E remove(final int index) {
            return super.remove(Math.floorMod(index, this.size()));
        }

        @Override
        public E get(final int index) {
            return super.get(Math.floorMod(index, this.size()));
        }
    }

}
