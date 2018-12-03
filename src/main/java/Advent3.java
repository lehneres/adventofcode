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

@SuppressWarnings({"OptionalGetWithoutIsPresent", "ForLoopReplaceableByForEach"})
public class Advent3 {


    public static void main(String[] args) throws IOException {

        Pattern r = Pattern.compile("#(\\d+) @ (\\d+),(\\d+): (\\d+)x(\\d+)");


        Collection<int[]> claims = Files.lines(Paths.get("src/main/resources/advent3.txt")).map(s -> {
            Matcher m = r.matcher(s);

            int[] claim = new int[5];

            if (m.find()) {
                claim[0] = Integer.valueOf(m.group(1)); //id
                claim[1] = Integer.valueOf(m.group(2)); //left
                claim[2] = Integer.valueOf(m.group(3)); //top
                claim[3] = Integer.valueOf(m.group(4)); //width
                claim[4] = Integer.valueOf(m.group(5)); //height
            } else System.err.println("NO MATCH");

            return claim;

        }).collect(Collectors.toSet());

        int totalWidth = claims.stream().mapToInt(c -> c[1] + c[3]).max().getAsInt(); int totalHeight = claims.stream().mapToInt(c -> c[2] + c[4]).max().getAsInt();

        int[][] fabric = new int[totalWidth][totalHeight]; int[][] fabricIds = new int[totalWidth][totalHeight];

        Set<Integer> idsWithOverlap = new HashSet<>();

        for (int[] c : claims)
            for (int l = c[1]; l < c[1] + c[3]; l++)
                for (int t = c[2]; t < c[2] + c[4]; t++)
                    if (++fabric[l][t] == 1) fabricIds[l][t] = c[0];
                    else {
                        idsWithOverlap.add(fabricIds[l][t]); idsWithOverlap.add(c[0]);
                    }

        int inches = 0;

        int[] finalPatch = claims.stream().mapToInt(c -> c[0]).filter(id -> !idsWithOverlap.contains(id)).toArray();

        for (int l = 0; l < fabric.length; l++)
            for (int t = 0; t < fabric[l].length; t++) if (fabric[l][t] > 1) inches++;

        System.out.println(inches); System.out.println(Arrays.toString(finalPatch));
    }

}