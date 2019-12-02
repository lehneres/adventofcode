import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings("UtilityClass")
final class Advent8 {

    public static void main(final String... args) throws IOException {

        final List<Integer> input = Files.lines(Paths.get("java2018/main/resources/advent8.txt"))
                                         .map(s -> s.split(" "))
                                         .flatMap(Arrays::stream)
                                         .map(Integer::valueOf)
                                         .collect(Collectors.toList());

        final Advent8.Node tree = Advent8.getTree(input);

        final Stack<Advent8.Node> nodes = new Stack<Node>() {{ this.add(tree); }};

        int countMetaData = 0;

        while (!nodes.isEmpty()) {
            final Advent8.Node currentNode = nodes.pop();
            countMetaData += currentNode.getMetadata().stream().mapToInt(i -> i).sum();
            nodes.addAll(currentNode.getChildren());
        }

        System.out.println(countMetaData);
        System.out.println(tree != null ? tree.getValue() : 0);
    }

    private static Advent8.Node getTree(final Iterable<Integer> input) {
        Advent8.Node            tree     = null;
        final Iterator<Integer> iterator = input.iterator();
        if (iterator.hasNext()) tree = Advent8.processInputSequence(iterator);
        return tree;
    }

    private static Advent8.Node processInputSequence(final Iterator<Integer> iterator) {
        int                childs      = iterator.next();
        int                meta        = iterator.next();
        final Advent8.Node currentNode = new Advent8.Node();
        while (childs-- > 0) currentNode.addChild(Advent8.processInputSequence(iterator));
        while (meta-- > 0) currentNode.addMetaData(iterator.next());
        return currentNode;
    }

    private static class Node {
        private final List<Advent8.Node>  children = new ArrayList<>();
        private final Collection<Integer> metadata = new ArrayList<>();

        final int getValue() {
            return this.children.isEmpty()
                   ? this.metadata.stream().mapToInt(i -> i).sum()
                   : this.metadata.stream().mapToInt(i -> i).filter(i -> i <= this.children.size()).map(i -> this.children.get(i - 1).getValue()).sum();
        }

        final Collection<Advent8.Node> getChildren() {
            return Collections.unmodifiableCollection(this.children);
        }

        final Collection<Integer> getMetadata() {
            return Collections.unmodifiableCollection(this.metadata);
        }

        final boolean addMetaData(final int metdata) {
            return this.metadata.add(metdata);
        }

        final boolean addChild(final Advent8.Node child) {
            return this.children.add(child);
        }

        public final int getMetaDataCount() {
            return this.metadata.size();
        }

        public final int getChildrenCount() {
            return this.children.size();
        }
    }
}