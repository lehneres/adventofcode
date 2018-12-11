import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@SuppressWarnings("Duplicates")
public class Advent10 {

    public static void main(String[] args) throws IOException {
        final Pattern r = Pattern.compile("position=<((-?|\\s)\\d+), ((-?|\\s)\\d+)> velocity=<((-?|\\s)\\d+), ((-?|\\s)\\d+)>");

        final Set<Point> points = Files.lines(Paths.get("src/main/resources/advent10.txt")).map(s -> {
            final Matcher matcher = r.matcher(s);
            Point         point   = null;

            if (matcher.find()) {
                int x      = Integer.parseInt(matcher.group(1).trim());
                int y      = Integer.parseInt(matcher.group(3).trim());
                int bottom = Integer.parseInt(matcher.group(5).trim());
                int left   = Integer.parseInt(matcher.group(7).trim());
                point = new Point(x, y, bottom, left);
            }

            return point;
        }).collect(Collectors.toSet());

        int     iteration = 1;
        long    minArea   = Long.MAX_VALUE;
        boolean decrease;

        do {
            points.forEach(Point::move);

            long minX = points.stream().mapToInt(Point::getX).min().getAsInt();
            long maxX = points.stream().mapToInt(Point::getX).max().getAsInt();
            long minY = points.stream().mapToInt(Point::getY).min().getAsInt();
            long maxY = points.stream().mapToInt(Point::getY).max().getAsInt();

            if (Math.abs(maxY - minY) * Math.abs(maxX - minX) < minArea) {
                minArea = Math.abs(maxY - minY) * Math.abs(maxX - minX);
                decrease = true;
            } else decrease = false;

            System.out.printf("dimension: [%d][%d] (%d) after %d iterations\n", Math.abs(maxX - minX), Math.abs(maxY - minY), Math.abs(maxX - minX) * Math.abs(maxY - minY),
                              iteration++);
        } while (decrease);

        points.forEach(Point::moveBack);

        long minX = points.stream().mapToInt(Point::getX).min().getAsInt();
        long maxX = points.stream().mapToInt(Point::getX).max().getAsInt();
        long minY = points.stream().mapToInt(Point::getY).min().getAsInt();
        long maxY = points.stream().mapToInt(Point::getY).max().getAsInt();

        for (long y = minY; y <= maxY; y++) {
            for (long x = minX; x <= maxX; x++) {
                char c = '.';
                for (Point p : points)
                    if (p.getX() == x && p.getY() == y) c = '#';
                System.out.print(c);
            }
            System.out.println();
        }
    }

    private static class Point {
        int[] data = new int[4];

        Point(int x, int y, int bottom, int right) {
            this.data[0] = x;
            this.data[1] = y;
            this.data[2] = bottom;
            this.data[3] = right;
        }

        int getX() {
            return this.data[0];
        }

        int getY() {
            return this.data[1];
        }


        void move() {
            this.data[0] += this.data[2];
            this.data[1] += this.data[3];
        }

        void moveBack() {
            this.data[0] -= this.data[2];
            this.data[1] -= this.data[3];
        }
    }
}
