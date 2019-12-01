import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@SuppressWarnings({"Duplicates", "UtilityClass"})
public final class Advent10 {

    @SuppressWarnings("OverlyLongMethod")
    public static void main(final String[] args) throws IOException {
        final Pattern r = Pattern.compile("position=<((-?|\\s)\\d+), ((-?|\\s)\\d+)> velocity=<((-?|\\s)\\d+), ((-?|\\s)\\d+)>");

        final Set<Advent10.Point> points = Files.lines(Paths.get("src/main/resources/advent10.txt")).map(s -> {
            final Matcher  matcher = r.matcher(s);
            Advent10.Point point   = null;

            if (matcher.find()) {
                int x      = Integer.parseInt(matcher.group(1).trim());
                int y      = Integer.parseInt(matcher.group(3).trim());
                int bottom = Integer.parseInt(matcher.group(5).trim());
                int left   = Integer.parseInt(matcher.group(7).trim());
                point = new Advent10.Point(x, y, bottom, left);
            }

            return point;
        }).collect(Collectors.toSet());

        int     iteration = 1;
        long    minArea   = Long.MAX_VALUE;
        boolean decrease;

        do {
            points.forEach(Advent10.Point::move);

            final long minX = points.stream().mapToInt(Advent10.Point::getX).min().getAsInt();
            final long maxX = points.stream().mapToInt(Advent10.Point::getX).max().getAsInt();
            final long minY = points.stream().mapToInt(Advent10.Point::getY).min().getAsInt();
            final long maxY = points.stream().mapToInt(Advent10.Point::getY).max().getAsInt();

            if (Math.abs(maxY - minY) * Math.abs(maxX - minX) < minArea) {
                minArea = Math.abs(maxY - minY) * Math.abs(maxX - minX);
                decrease = true;
            } else decrease = false;

            System.out.printf("dimension: [%d][%d] (%d) after %d iterations\n", Math.abs(maxX - minX), Math.abs(maxY - minY), Math.abs(maxX - minX) * Math.abs(maxY - minY),
                              iteration++);
        } while (decrease);

        points.forEach(Advent10.Point::moveBack);

        final long minX = points.stream().mapToInt(Advent10.Point::getX).min().getAsInt();
        final long maxX = points.stream().mapToInt(Advent10.Point::getX).max().getAsInt();
        final long minY = points.stream().mapToInt(Advent10.Point::getY).min().getAsInt();
        final long maxY = points.stream().mapToInt(Advent10.Point::getY).max().getAsInt();

        for (long y = minY; y <= maxY; y++) {
            for (long x = minX; x <= maxX; x++) {
                char c = '.';
                for (final Advent10.Point p : points)
                    if (p.getX() == x && p.getY() == y) c = '#';
                System.out.print(c);
            }
            System.out.println();
        }
    }

    private static final class Point {
        private int[] data = new int[4];

        Point(final int x, final int y, final int bottom, final int right) {
            this.getData()[0] = x;
            this.getData()[1] = y;
            this.getData()[2] = bottom;
            this.getData()[3] = right;
        }

        int getX() {
            return this.getData()[0];
        }

        int getY() {
            return this.getData()[1];
        }


        void move() {
            this.getData()[0] += this.getData()[2];
            this.getData()[1] += this.getData()[3];
        }

        void moveBack() {
            this.getData()[0] -= this.getData()[2];
            this.getData()[1] -= this.getData()[3];
        }

        int[] getData() {
            return this.data.clone();
        }

        public void setData(final int[] data) {
            this.data = data.clone();
        }
    }
}
