import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@SuppressWarnings({"OptionalGetWithoutIsPresent", "UtilityClass", "StandardVariableNames"})
public final class Advent3 {


    @SuppressWarnings("OverlyLongMethod")
    public static void main(final String[] args) throws IOException {

        final Pattern r = Pattern.compile("#(\\d+) @ (\\d+),(\\d+): (\\d+)x(\\d+)");


        final Collection<int[]> claims = Files.lines(Paths.get("java2018/main/resources/advent3.txt")).map(s -> {
            Matcher matcher = r.matcher(s);

            int[] claim = new int[5];

            if (matcher.find()) {
                claim[0] = Integer.valueOf(matcher.group(1)); //id
                claim[1] = Integer.valueOf(matcher.group(2)); //left
                claim[2] = Integer.valueOf(matcher.group(3)); //top
                claim[3] = Integer.valueOf(matcher.group(4)); //width
                claim[4] = Integer.valueOf(matcher.group(5)); //height
            } else System.err.println("NO MATCH");

            return claim;

        }).collect(Collectors.toSet());

        final int totalWidth  = claims.stream().mapToInt(i -> i[1] + i[3]).max().getAsInt();
        final int totalHeight = claims.stream().mapToInt(i -> i[2] + i[4]).max().getAsInt();

        final int[][] fabric    = new int[totalWidth][totalHeight];
        final int[][] fabricIds = new int[totalWidth][totalHeight];

        final Set<Integer> idsWithOverlap = new HashSet<>();

        int inches = 0;

        for (final int[] claim : claims)
            for (int l = claim[1]; l < claim[1] + claim[3]; l++)
                for (int t = claim[2]; t < claim[2] + claim[4]; t++) {
                    if (++fabric[l][t] == 1) fabricIds[l][t] = claim[0];
                    else {
                        idsWithOverlap.add(fabricIds[l][t]);
                        idsWithOverlap.add(claim[0]);
                    }
                    if (fabric[l][t] == 2) inches++;
                }

        final int[] finalPatch = claims.stream().mapToInt(c -> c[0]).filter(id -> !idsWithOverlap.contains(id)).toArray();

        System.out.println(inches);
        System.out.println(Arrays.toString(finalPatch));
    }

}