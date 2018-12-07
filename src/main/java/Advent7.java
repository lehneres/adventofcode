import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Advent7 {

    private static final int                DURATION  = 60;
    private static final int                numWorker = 5;
    private final        Set<Character>     doneNode  = new HashSet<>();
    private final        int[][]            worker    = new int[Advent7.numWorker][2];
    private              int[][]            adj;
    private              int                countNodes;
    private              Set<Character>     nodes;
    private              TreeSet<Character> availableNodes;

    public static void main(final String... args) throws IOException {

        final Advent7 advent = new Advent7();

        final Pattern r = Pattern.compile("Step (\\w) must be finished before step (\\w) can begin.");

        final Collection<Character[]> edges = Files.lines(Paths.get("src/main/resources/advent7.txt")).map(s -> {
            final Matcher     matcher = r.matcher(s);
            final Character[] edge    = new Character[2];

            if (matcher.find()) {
                edge[0] = matcher.group(1).charAt(0);
                edge[1] = matcher.group(2).charAt(0);
            }

            return edge;
        }).collect(Collectors.toSet());

        final List<Character> sequence = advent.getSequence(edges);

        System.out.println(sequence.stream().map(Object::toString).reduce((acc, e) -> acc + e).get());

        advent.reset();

        System.out.println(advent.process());
    }

    private static int getStepDuration(final Character node) {
        return Advent7.DURATION + (node - 'A');
    }

    private void reset() {
        this.doneNode.clear();
        this.availableNodes.clear();
        IntStream.range(0, Advent7.numWorker).forEach(i -> Arrays.fill(this.worker[i], -1));
    }

    private int process() {
        int seconds = 0;

        this.initAvailableNodes();

        while (!this.availableNodes.isEmpty() || this.areSomeWorkersWorking()) {
            this.processStep();
            seconds++;
        }

        return seconds;
    }

    private boolean areSomeWorkersWorking() {
        return IntStream.range(0, Advent7.numWorker).anyMatch(i -> this.worker[i][1] > 0);
    }

    @SuppressWarnings("ConstantConditions")
    private void processStep() {
        for (int i = 0; i < Advent7.numWorker; i++)
            if (this.worker[i][0] == -1 || this.worker[i][1] == 0) {
                if (this.worker[i][1] == 0) this.follow((char) this.worker[i][0]);
                if (!this.availableNodes.isEmpty()) {
                    final Character node = this.availableNodes.pollFirst();
                    this.worker[i][0] = node;
                    this.worker[i][1] = Advent7.getStepDuration(node);
                }
            } else this.worker[i][1]--;
    }

    private List<Character> getSequence(final Collection<Character[]> edges) {
        this.countNodes = (int) edges.stream().flatMap(Arrays::stream).distinct().count();

        this.adj = new int[this.countNodes][this.countNodes];

        this.nodes = edges.stream().flatMap(Arrays::stream).collect(Collectors.toSet());

        edges.stream().map(e -> new Integer[]{e[0] % this.countNodes, e[1] % this.countNodes}).forEach(edge -> this.adj[edge[0]][edge[1]] = 1);

        final List<Character> output = new ArrayList<>();

        this.initAvailableNodes();

        while (!this.availableNodes.isEmpty()) output.addAll(this.follow(this.availableNodes.pollFirst()));

        return output;
    }

    private void initAvailableNodes() {
        this.availableNodes = this.nodes.stream().filter(node -> {
            int hasIncoming = 0;
            for (final Character source : this.nodes)
                if (this.adj[source % this.countNodes][node % this.countNodes] > 0) hasIncoming++;
            return hasIncoming == 0;
        }).sorted().collect(Collectors.toCollection(TreeSet::new));
    }

    private List<Character> follow(final Character node) {
        final List<Character> output = new ArrayList<>();

        this.doneNode.add(node);
        output.add(node);

        for (final Character target : this.nodes)
            if (this.adj[node % this.countNodes][target % this.countNodes] == 1 && this.checkNodeAvailable(target)) this.availableNodes.add(target);

        //while (!next.isEmpty()) output.addAll(this.follow(next.pollFirst()));

        return output;
    }

    private boolean checkNodeAvailable(final Character node) {
        for (final Character source : this.nodes)
            if (this.adj[source % this.countNodes][node % this.countNodes] == 1 && !this.doneNode.contains(source)) return false;
        return true;
    }


}
