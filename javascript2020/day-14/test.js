const assert         = require('assert');
const {part1, part2} = require('./code');

describe('Part One', () => {
    it('should return 165 with input', () => {
        let input = `mask = XXXXXXXXXXXXXXXXXXXXXXXXXXXXX1XXXX0X
                            mem[8] = 11
                            mem[7] = 101
                            mem[8] = 0`;
        assert.strictEqual(part1(input), 165);
    });
});

describe('Part Two', () => {
    it('should return 208 with input', () => {
        let input = `mask = 000000000000000000000000000000X1001X
                            mem[42] = 100
                            mask = 00000000000000000000000000000000X0XX
                            mem[26] = 1`;
        assert.strictEqual(part2(input), 208);
    });
});