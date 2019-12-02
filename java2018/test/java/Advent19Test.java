import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class Advent19Test {

    @Test
    void testRegister() throws IOException {
        RegisterV2.setRegister(new int[]{0, 0, 0, 0, 0, 0});
        final List<Map.Entry<RegisterV2.OPCODE, int[]>> input = Advent19.readInput("test/resources/advent19_Test_Input.txt");

        RegisterV2.process(input);

        assertArrayEquals(new int[]{6, 5, 6, 0, 0, 9}, RegisterV2.getRegister(), "register not equal");
    }

}
