const assert         = require('assert');
const {part1, part2} = require('./code');

describe('Part One', () => {
    it('should return 35 with input', () => {
        let input = `16
                            10
                            15
                            5
                            1
                            11
                            7
                            19
                            6
                            12
                            4`;
        assert.strictEqual(part1(input), 7 * 5);
    });
    it('should return 220 with input', () => {
        let input = `28
                            33
                            18
                            42
                            31
                            14
                            46
                            20
                            48
                            47
                            24
                            23
                            49
                            45
                            19
                            38
                            39
                            11
                            1
                            32
                            25
                            35
                            8
                            17
                            7
                            9
                            4
                            2
                            34
                            10
                            3`;
        assert.strictEqual(part1(input), 22 * 10);
    });
});

describe('Part Two', () => {
    it('should return 8 with input', () => {
        let input = `16
                            10
                            15
                            5
                            1
                            11
                            7
                            19
                            6
                            12
                            4`;
        assert.strictEqual(part2(input), 8);
    });
    it('should return 19208 with input', () => {
        let input = `28
                            33
                            18
                            42
                            31
                            14
                            46
                            20
                            48
                            47
                            24
                            23
                            49
                            45
                            19
                            38
                            39
                            11
                            1
                            32
                            25
                            35
                            8
                            17
                            7
                            9
                            4
                            2
                            34
                            10
                            3`;
        assert.strictEqual(part2(input), 19208);
    });
});