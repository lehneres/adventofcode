import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@SuppressWarnings("MultipleTopLevelClassesInFile")
enum Advent19 {
    ;

    public static final int SUM_OF_DIVISORS = 10551364;

    @SuppressWarnings("FeatureEnvy")
    public static void main(final String... args) throws IOException {
        RegisterV2.setRegister(new int[]{0, 0, 0, 0, 0, 0});
        final List<Map.Entry<RegisterV2.OPCODE, int[]>> input = Advent19.readInput("java2018/main/resources/advent19.txt");

        RegisterV2.process(input);
        System.out.printf("Register after program part I: %s%n", Arrays.toString(RegisterV2.getRegister()));


        System.out.printf("Register after program part II: %s%n", IntStream.range(1, Advent19.SUM_OF_DIVISORS + 1).filter(x -> Advent19.SUM_OF_DIVISORS % x == 0).sum());
    }

    static List<Map.Entry<RegisterV2.OPCODE, int[]>> readInput(final String file) throws IOException {
        final List<String> lines = Files.lines(Paths.get(file)).collect(Collectors.toList());

        final List<Map.Entry<RegisterV2.OPCODE, int[]>> input = new ArrayList<>();

        lines.forEach(line -> {
            if (line.startsWith("#ip")) RegisterV2.setIPRegister(Integer.valueOf(line.split(" ")[1]));
            else {
                final String[] split = line.split(" ");
                input.add(new AbstractMap.SimpleEntry<>(RegisterV2.OPCODE.valueOf(split[0].toUpperCase()),
                                                        Arrays.stream(Arrays.copyOfRange(split, 1, split.length)).mapToInt(Integer::parseInt).toArray()));
            }
        });

        return input;
    }

}

@SuppressWarnings({"MultipleTopLevelClassesInFile", "ClassNameDiffersFromFileName"})
enum RegisterV2 {
    ;

    private static int[] register = new int[4];
    private static int   ipRegister;

    static int[] getRegister() {
        return RegisterV2.register.clone();
    }

    static void setRegister(final int[] input) {
        RegisterV2.register = input.clone();
    }

    public static void setIPRegister(final int i) {
        RegisterV2.ipRegister = i;
    }

    public static void process(final List<Map.Entry<RegisterV2.OPCODE, int[]>> input) {
        while (true) {
            final Map.Entry<RegisterV2.OPCODE, int[]> opcodeEntry = input.get(RegisterV2.register[RegisterV2.ipRegister]);
            final int[]                               value       = opcodeEntry.getValue();

            opcodeEntry.getKey().apply(value[0], value[1], value[2]);

            if (RegisterV2.register[RegisterV2.ipRegister] + 1 >= input.size()) break;
            else RegisterV2.register[RegisterV2.ipRegister]++;
        }
    }

    @SuppressWarnings({"StandardVariableNames", "NonFinalStaticVariableUsedInClassInitialization", "UnqualifiedStaticUsage", "NonSerializableFieldInSerializableClass", "unused"})
    enum OPCODE {

        ADDR((a, b, c) -> register[c] = register[a] + register[b]),
        ADDI((a, b, c) -> register[c] = register[a] + b),
        MULR((a, b, c) -> register[c] = register[a] * register[b]),
        MULI((a, b, c) -> register[c] = register[a] * b),
        BANR((a, b, c) -> register[c] = register[a] & register[b]),
        BANI((a, b, c) -> register[c] = register[a] & b),
        BORR((a, b, c) -> register[c] = register[a] | register[b]),
        BORI((a, b, c) -> register[c] = register[a] | b),
        SETR((a, b, c) -> register[c] = register[a]),
        SETI((a, b, c) -> register[c] = a),
        GTIR((a, b, c) -> register[c] = a > register[b] ? 1 : 0),
        GTRI((a, b, c) -> register[c] = register[a] > b ? 1 : 0),
        GTRR((a, b, c) -> register[c] = register[a] > register[b] ? 1 : 0),
        EQIR((a, b, c) -> register[c] = a == register[b] ? 1 : 0),
        EQRI((a, b, c) -> register[c] = register[a] == b ? 1 : 0),
        EQRR((a, b, c) -> register[c] = register[a] == register[b] ? 1 : 0);

        private final RegisterV2.OPCODE.IntTertiaryOperator op;

        OPCODE(final RegisterV2.OPCODE.IntTertiaryOperator op) {
            this.op = op;
        }

        void apply(final int a, final int b, final int c) {
            this.op.applyAsInt(a, b, c);
        }

        @FunctionalInterface
        interface IntTertiaryOperator {
            void applyAsInt(int a, int b, int c);
        }

    }
}
