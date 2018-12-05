import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Advent4 {

    public static void main(String[] args) throws IOException {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Pattern          r   = Pattern.compile("\\[(\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2})\\] (.+#(\\d{1,4})|.+)");

        long[][] obs = Files.lines(Paths.get("src/main/resources/advent4.txt")).map(s -> {
            long[] entry = new long[3];
            try {
                Matcher m = r.matcher(s);
                if (m.find()) {
                    entry[0] = (sdf.parse(m.group(1)).getTime());

                    if (m.group(3) != null) {
                        entry[1] = 1;
                        entry[2] = Integer.parseInt(m.group(3));
                    } else {
                        entry[1] = m.group(2).contains("wakes up") ? 1 : 0;
                        entry[2] = -1;
                    }
                }
            } catch (ParseException e) {
                System.err.println("could not parse string");
                e.printStackTrace();
            }
            return entry;
        }).sorted(Comparator.comparing(a -> a[0])).toArray(size -> new long[size][3]);

        Map<Long, int[]> minutesMap = new HashMap<>();

        long onDuty = -1;
        for (int i = 0; i < obs.length; i++) {

            if (obs[i][2] > -1) onDuty = obs[i][2];
            else obs[i][2] = onDuty;

            if (i > 0 && obs[i - 1][1] == 0 && obs[i][1] == 1 && obs[i - 1][2] == obs[i][2]) {

                int[] minutes;
                if (!minutesMap.containsKey(obs[i][2])) minutesMap.put(obs[i][2], new int[60]);
                minutes = minutesMap.get(obs[i][2]);
                for (int m = (int) (60 + ((obs[i - 1][0] / 1000) % 86400) / 60); m <= (int) (60 + ((obs[i][0] / 1000) % 86400) / 60) - 1; m++)
                    minutes[m]++;
            }
        }

        Map<Long, Long> guardSleepTimeSummary = minutesMap.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> (long) Arrays.stream(e.getValue()).sum()));
        long            sleepiestGuard        = Collections.max(guardSleepTimeSummary.entrySet(), Comparator.comparingLong(Map.Entry::getValue)).getKey();
        int             sleepyMinute          = findMaxArrayIndex(minutesMap.get(sleepiestGuard));

        System.out.println(sleepyMinute * sleepiestGuard);

        int sleepiesMinute2 = -1, sleepTime = -1, sleepiestGuard2 = -1;

        for (long guard : minutesMap.keySet()) {
            int sleepiesMinute = findMaxArrayIndex(minutesMap.get(guard));
            if (sleepTime < minutesMap.get(guard)[sleepiesMinute]) {
                sleepTime = minutesMap.get(guard)[sleepiesMinute];
                sleepiesMinute2 = sleepiesMinute;
                sleepiestGuard2 = (int) guard;
            }
        }

        System.out.println(sleepiestGuard2 * sleepiesMinute2);
    }

    private static int findMaxArrayIndex(int[] array) {
        int maxIndex = 0;
        for (int i = 0; i < array.length; i++)
            if (array[maxIndex] < array[i]) maxIndex = i;
        return maxIndex;
    }

}
