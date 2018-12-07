import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("UtilityClass")
public final class Advent5 {

    private static final int reaction = 32;
    private static final int A        = 65;
    private static final int Z        = 90;

    public static void main(final String[] args) throws IOException {
        Files.lines(Paths.get("src/main/resources/advent5.txt")).forEach(s -> {
            final List<Integer> chars = Advent5.collapse(s);
            System.out.println(chars.stream().map(e -> Character.toString((char) e.intValue())).reduce((acc, e) -> acc + e).get());
            System.out.println(chars.size());

            int shortest = Integer.MAX_VALUE;
            for (int i = Advent5.A; i < Advent5.Z; i++) {
                final int currentChar = i;
                final String filtered = chars.stream()
                                             .filter(e -> e != currentChar && e != currentChar + Advent5.reaction)
                                             .map(e -> Character.toString((char) e.intValue()))
                                             .reduce((acc, e) -> acc + e)
                                             .get();
                final List<Integer> collapsedList = Advent5.collapse(filtered);
                if (shortest > collapsedList.size()) shortest = collapsedList.size();
            }

            System.out.println(shortest);
        });
    }

    private static @NotNull List<Integer> collapse(final String s) {
        final ArrayList<Integer> chars = s.chars().boxed().collect(Collectors.toCollection(ArrayList::new));
        int                      i     = 1;

        while (i < chars.size()) {
            final int moleculeA = chars.get(i - 1);
            final int moleculeB = chars.get(i);

            if (Math.abs(moleculeA - moleculeB) == Advent5.reaction) {
                chars.remove(i);
                chars.remove(i - 1);
                i = Math.max(1, i - 1);
            } else i++;
        }
        return chars;
    }
}
