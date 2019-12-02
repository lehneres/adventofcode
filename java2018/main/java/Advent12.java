import java.io.IOException;
import java.nio.CharBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@SuppressWarnings("UtilityClass")
public final class Advent12 {

    private static final int    HASH         = 35;
    private static final long   GENERATIONS  = 50000000000L;
    private static final String initialState = "#...#..##.......####.#..###..#.##..########.#.#...#.#...###.#..###.###.#.#..#...#.#..##..#######.##";

    @SuppressWarnings("OverlyLongMethod")
    public static void main(final String[] args) throws IOException {
        final Pattern r = Pattern.compile("([\\.#])([\\.#])([\\.#])([\\.#])([\\.#]) => ([\\.#])");

        final Set<Advent12.Rule> rules = Files.lines(Paths.get("java2018/main/resources/advent12.txt")).map(s -> {
            final Matcher matcher = r.matcher(s);
            Advent12.Rule rule    = null;

            if (matcher.find()) {
                char l2  = matcher.group(1).charAt(0);
                char l1  = matcher.group(2).charAt(0);
                char c   = matcher.group(3).charAt(0);
                char r1  = matcher.group(4).charAt(0);
                char r2  = matcher.group(5).charAt(0);
                char out = matcher.group(6).charAt(0);
                rule = new Advent12.Rule(l2, l1, c, r1, r2, out);
            }
            return rule;
        }).collect(Collectors.toSet());

        final int cappedGenerations = (int) Math.min(10000, Advent12.GENERATIONS);

        final List<int[]> generations = new ArrayList<>();

        final int initialLength = Advent12.initialState.length();
        char[]    curGen        = new char[(initialLength + cappedGenerations) * 2 - 1];
        Arrays.fill(curGen, '.');

        for (int i = 0; i < initialLength; i++)
            curGen[i + initialLength - 1] = Advent12.initialState.charAt(i);

        generations.add(CharBuffer.wrap(curGen).chars().toArray());

        int lastDiff = 0;
        for (int g = 0; g < cappedGenerations; g++) {
            final char[] nextGen = new char[curGen.length];
            Arrays.fill(nextGen, '.');

            for (int i = 0; i < (initialLength + cappedGenerations) - 2; i++) {
                Advent12.applyRules(rules, curGen, nextGen, initialLength + cappedGenerations - 1 - i);
                Advent12.applyRules(rules, curGen, nextGen, initialLength + cappedGenerations - 1 + i);
            }

            generations.add(CharBuffer.wrap(nextGen).chars().toArray());

            final int diff = Advent12.getIndexSum(CharBuffer.wrap(nextGen).chars().toArray()) - Advent12.getIndexSum(CharBuffer.wrap(curGen).chars().toArray());
            if (diff == lastDiff) break;
            else lastDiff = diff;

            curGen = nextGen;
        }

        Advent12.printGenerations(generations);

        if (generations.size() == cappedGenerations) System.err.println("did not meet exit condition");

        final long skipped      = Math.max(0, Advent12.GENERATIONS - generations.size());
        final int  lastIndexSum = Advent12.getIndexSum(generations.get(generations.size() - 1));

        System.out.format("final index sum: %d\n", lastIndexSum + lastDiff * (skipped + 1));

    }

    private static void printGenerations(final List<int[]> generations) {
        int minI = Integer.MAX_VALUE, maxI = Integer.MIN_VALUE;
        for (final int[] g : generations)
            for (int i = 0; i < g.length; i++)
                if (g[i] == Advent12.HASH && i < minI) minI = i;
                else if (g[i] == Advent12.HASH && i > maxI) maxI = i;

        int prevIndexSum = 0;
        for (int g = 0; g < generations.size(); g++) {
            final int[] curGen   = generations.get(g);
            final int   indexSum = Advent12.getIndexSum(curGen);
            System.out.printf("#%3d: %5d (%3d)   ", g, indexSum, indexSum - prevIndexSum);
            prevIndexSum = indexSum;
            for (int i = minI; i <= maxI; i++)
                System.out.print((char) curGen[i]);
            System.out.println();
        }
    }

    private static int getIndexSum(final int[] pots) {
        return IntStream.range(0, pots.length).filter(i -> pots[i] == Advent12.HASH).map(i -> -Advent12.initialState.length() + 1 + i).sum();
    }

    private static void applyRules(final Set<? extends Advent12.Rule> rules, final char[] prevGen, final char[] nextGen, final int i) {
        rules.stream().filter(rule -> rule.matchesRule(prevGen[i - 2], prevGen[i - 1], prevGen[i], prevGen[i + 1], prevGen[i + 2])).forEach(rule -> nextGen[i] = rule.apply());
    }

    private static class Rule {
        private final char l2;
        private final char l1;
        private final char c;
        private final char r1;
        private final char r2;
        private final char out;

        Rule(final char l2, final char l1, final char c, final char r1, final char r2, final char out) {
            this.l2 = l2;
            this.l1 = l1;
            this.c = c;
            this.r1 = r1;
            this.r2 = r2;
            this.out = out;
        }

        char apply() {
            return this.out;
        }

        boolean matchesRule(final char l2In, final char l1In, final char cIn, final char r1In, final char r2In) {
            return this.l2 == l2In && this.l1 == l1In && this.c == cIn && this.r1 == r1In && this.r2 == r2In;
        }
    }
}
