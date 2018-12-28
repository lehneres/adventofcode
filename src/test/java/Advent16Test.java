import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Advent16Test {

    @Test
    void testSample() {
        Register.setRegister(new int[]{3, 2, 1, 1});
        assertEquals(3, Register.findValidOpcodes(new int[]{9, 2, 1, 2}, new int[]{3, 2, 2, 1}).size(), "incorrect number of opcodes");
    }
}