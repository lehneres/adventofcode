import java.io.IOException;
import java.nio.CharBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@SuppressWarnings("UtilityClass")
public final class Advent12 {

    private static final int    HASH         = 35;
    private static final int    GENERATIONS  = 20;
    private static final String initialState = "#...#..##.......####.#..###..#.##..########.#.#...#.#...###.#..###.###.#.#..#...#.#..##..#######.##";

    public static void main(final String[] args) throws IOException {
        final Pattern r = Pattern.compile("([\\.#])([\\.#])([\\.#])([\\.#])([\\.#]) => ([\\.#])");

        final Set<Advent12.Rule> rules = Files.lines(Paths.get("src/main/resources/advent12.txt")).map(s -> {
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

        char[] pots = new char[(Advent12.initialState.length() + Advent12.GENERATIONS) * 2 - 1];
        Arrays.fill(pots, '.');

        for (int i = 0; i < Advent12.initialState.length(); i++)
            pots[i + Advent12.initialState.length() - 1] = Advent12.initialState.charAt(i);
        System.out.println(CharBuffer.wrap(pots).chars().mapToObj(Character::toString).collect(Collectors.joining()));


        pots = Advent12.computeGenerations(rules, pots);

        int indexSum = 0;
        for (int i = 0; i < pots.length; i++)
            if (pots[i] == Advent12.HASH) indexSum += -Advent12.initialState.length() + 1 + i;

        System.out.println(indexSum);
    }

    private static char[] computeGenerations(final Set<Advent12.Rule> rules, final char[] pots) {
        char[] curGen = pots;
        for (int g = 0; g < Advent12.GENERATIONS; g++) {
            final char[] nextGen = new char[curGen.length];
            Arrays.fill(nextGen, '.');

            for (int i = 0; i < (Advent12.initialState.length() + Advent12.GENERATIONS) - 2; i++) {
                final int leftIndex  = Advent12.initialState.length() + Advent12.GENERATIONS - 1 - i;
                final int rightIndex = Advent12.initialState.length() + Advent12.GENERATIONS - 1 + i;
                Advent12.applyRules(rules, curGen, nextGen, leftIndex);
                Advent12.applyRules(rules, curGen, nextGen, rightIndex);
            }

            curGen = nextGen;
            System.out.println(CharBuffer.wrap(curGen).chars().mapToObj(Character::toString).collect(Collectors.joining()));
        }
        return curGen;
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
