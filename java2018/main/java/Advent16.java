import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@SuppressWarnings({"MultipleTopLevelClassesInFile", "AccessingNonPublicFieldOfAnotherObject", "FeatureEnvy"})
enum Advent16 {
    ;

    private static final Pattern NON_NUMBERS = Pattern.compile("[^\\d.]");

    public static void main(final String[] args) throws IOException {
        final Set<Advent16.Input> inputs = Advent16.readInput1();

        int moreThanTwo = 0;

        for (final Advent16.Input input : inputs) {
            Register.setRegister(input.before);
            if (Register.findValidOpcodes(input.opcode, input.after).size() > 2) moreThanTwo++;
        }

        System.out.printf("Samples with more than 2 (three or more) matching operations: %d%n", moreThanTwo);

        final Map<Integer, Register.OPCODE> opCodes = Advent16.findOpCodes(inputs);

        final List<Integer[]> operations = Advent16.readInput2();

        Register.setRegister(new int[]{0, 0, 0, 0});

        operations.forEach(op -> opCodes.get(op[0]).apply(op[1], op[2], op[3]));

        System.out.printf("register [%d,%d,%d,%d]", Register.getRegister()[0], Register.getRegister()[1], Register.getRegister()[2], Register.getRegister()[3]);
    }

    private static List<Integer[]> readInput2() throws IOException {
        return Files.lines(Paths.get("java2018/main/resources/advent16_2.txt")).map(s -> {
            final String[]  split = s.split(" ");
            final Integer[] asInt = new Integer[split.length];
            for (int i = 0; i < split.length; i++) asInt[i] = Integer.valueOf(split[i]);
            return asInt;
        }).collect(Collectors.toList());
    }

    private static Set<Advent16.Input> readInput1() throws IOException {
        final Set<Advent16.Input> inputs = new HashSet<>();

        final String[] strings = Files.lines(Paths.get("java2018/main/resources/advent16_1.txt")).toArray(String[]::new);

        for (int i = 2; i < strings.length; i += 4) {
            final String before = strings[i - 2];
            final String opcode = strings[i - 1];
            final String after  = strings[i];

            final Advent16.Input input = new Advent16.Input();

            input.before = Advent16.NON_NUMBERS.matcher(before).replaceAll("").chars().map(Character::getNumericValue).toArray();
            input.opcode = Arrays.stream(opcode.split(" ")).mapToInt(Integer::valueOf).toArray();
            input.after = Advent16.NON_NUMBERS.matcher(after).replaceAll("").chars().map(Character::getNumericValue).toArray();

            inputs.add(input);
        }

        return inputs;
    }

    private static Map<Integer, Register.OPCODE> findOpCodes(final Iterable<Advent16.Input> inputs) {
        final Map<Integer, Register.OPCODE> opcodeMap        = new HashMap<>();
        final Map<Register.OPCODE, Integer> inverseOpcodeMap = new EnumMap<>(Register.OPCODE.class);

        for (final Advent16.Input input : inputs) {
            if (opcodeMap.containsKey(input.opcode[0])) continue;

            Register.setRegister(input.before);
            final Set<Register.OPCODE> validOpcodes = Register.findValidOpcodes(input.opcode, input.after);

            if (validOpcodes.isEmpty()) {
                System.err.println("no valid opcode");
                continue;
            }

            validOpcodes.removeAll(validOpcodes.stream().filter(op -> inverseOpcodeMap.containsKey(op) && inverseOpcodeMap.get(op) != input.opcode[0]).collect(Collectors.toSet()));

            if (validOpcodes.size() == 1) {
                final Register.OPCODE op = validOpcodes.stream().findFirst().get();
                opcodeMap.put(input.opcode[0], op);
                inverseOpcodeMap.put(op, input.opcode[0]);
            }
        }

        Arrays.stream(Register.OPCODE.values()).filter(opcode -> !inverseOpcodeMap.containsKey(opcode)).forEach(opcode -> {
            inverseOpcodeMap.put(opcode, null);
            opcodeMap.put(null, opcode);
        });

        return opcodeMap;
    }

    private static class Input {
        private int[] before, opcode, after;
    }
}

@SuppressWarnings({"MultipleTopLevelClassesInFile", "unused", "ClassNameDiffersFromFileName"})
enum Register {
    ;

    private static int[] register = new int[4];

    static int[] getRegister() {
        return Register.register.clone();
    }

    static void setRegister(final int[] input) {
        Register.register = input.clone();
    }

    static Set<Register.OPCODE> findValidOpcodes(final int[] input, final int[] result) {
        return Arrays.stream(Register.OPCODE.values()).filter(opcode -> {
            final int[] current = Register.register.clone();
            opcode.apply(input[1], input[2], input[3]);
            final boolean valid = Arrays.equals(result, Register.register);
            Register.register = current;
            return valid;
        }).collect(Collectors.toSet());
    }

    @SuppressWarnings({"StandardVariableNames", "NonFinalStaticVariableUsedInClassInitialization", "UnqualifiedStaticUsage", "NonSerializableFieldInSerializableClass"})
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

        private final Register.OPCODE.IntTertiaryOperator op;

        OPCODE(final Register.OPCODE.IntTertiaryOperator op) {
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

