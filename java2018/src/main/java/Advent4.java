import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@SuppressWarnings("UtilityClass")
public final class Advent4 {

    private static final int DAYSECONDS  = 86400;
    private static final int HOURSECONDS = 60;

    @SuppressWarnings({"OverlyComplexMethod", "OverlyLongMethod"})
    public static void main(final String... args) throws IOException {

        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        final Pattern          r   = Pattern.compile("\\[(\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2})\\] (.+#(\\d{1,4})|.+)");

        final long[][] obs = Files.lines(Paths.get("src/main/resources/advent4.txt")).map(s -> {
            long[] entry = new long[3];
            try {
                Matcher matcher = r.matcher(s);
                if (matcher.find()) {
                    entry[0] = (sdf.parse(matcher.group(1)).getTime());

                    if (matcher.group(3) != null) {
                        entry[1] = 1;
                        entry[2] = Integer.parseInt(matcher.group(3));
                    } else {
                        entry[1] = matcher.group(2).contains("wakes up") ? 1 : 0;
                        entry[2] = -1;
                    }
                }
            } catch (ParseException e) {
                System.err.println("could not parse string");
            }
            return entry;
        }).sorted(Comparator.comparing(a -> a[0])).toArray(size -> new long[size][3]);

        final Map<Long, int[]> minutesMap = new HashMap<>();

        long onDuty = -1;
        for (int i = 0; i < obs.length; i++) {

            if (obs[i][2] > -1) onDuty = obs[i][2];
            else obs[i][2] = onDuty;

            if (i > 0 && obs[i - 1][1] == 0 && obs[i][1] == 1 && obs[i - 1][2] == obs[i][2]) {

                final int[] minutes;
                if (!minutesMap.containsKey(obs[i][2])) minutesMap.put(obs[i][2], new int[Advent4.HOURSECONDS]);
                minutes = minutesMap.get(obs[i][2]);
                for (int m = (int) (Advent4.HOURSECONDS + ((obs[i - 1][0] / 1000) % Advent4.DAYSECONDS) / Advent4.HOURSECONDS);
                     m <= (int) (Advent4.HOURSECONDS + ((obs[i][0] / 1000) % Advent4.DAYSECONDS) / Advent4.HOURSECONDS) - 1; m++)
                    minutes[m]++;
            }
        }

        final Map<Long, Long> sleepTimeSummary = minutesMap.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> (long) Arrays.stream(e.getValue()).sum()));
        final long            sleepiestGuard   = Collections.max(sleepTimeSummary.entrySet(), Comparator.comparingLong(Map.Entry::getValue)).getKey();
        final int             sleepyMinute     = Advent4.findMaxArrayIndex(minutesMap.get(sleepiestGuard));

        System.out.println(sleepyMinute * sleepiestGuard);

        int sleepiestMinute2 = -1, sleepTime = -1, sleepiestGuard2 = -1;

        for (final long guard : minutesMap.keySet()) {
            final int sleepiesMinute = Advent4.findMaxArrayIndex(minutesMap.get(guard));
            if (sleepTime < minutesMap.get(guard)[sleepiesMinute]) {
                sleepTime = minutesMap.get(guard)[sleepiesMinute];
                sleepiestMinute2 = sleepiesMinute;
                sleepiestGuard2 = (int) guard;
            }
        }

        System.out.println(sleepiestGuard2 * sleepiestMinute2);
    }

    private static int findMaxArrayIndex(final int[] array) {
        int maxIndex = 0;
        for (int i = 0; i < array.length; i++)
            if (array[maxIndex] < array[i]) maxIndex = i;
        return maxIndex;
    }

}
