const assert         = require('assert');
const {part1, part2} = require('./code');

describe('Part One', () => {
    it('should return 5 with input', () => {
        let input = `nop +0
                            acc +1
                            jmp +4
                            acc +3
                            jmp -3
                            acc -99
                            acc +1
                            jmp -4
                            acc +6`;
        assert.strictEqual(part1(input), 5);
    });
});

describe('Part Two', () => {
    it('should return 8 with input', () => {
        let input = `nop +0
                            acc +1
                            jmp +4
                            acc +3
                            jmp -3
                            acc -99
                            acc +1
                            jmp -4
                            acc +6`;
        assert.strictEqual(part2(input), 8);
    });
});