const assert         = require('assert');
const {part1, part2} = require('./code');

describe('Part One', () => {
    it('should return 2 with input', () => {
        let input = `1-3 a: abcde
        1-3 b: cdefg
        2-9 c: ccccccccc`;
        assert.strictEqual(part1(input), 2);
    });
});

describe('Part Two', () => {
    it('should return 1 with input', () => {
        let input = `1-3 a: abcde
        1-3 b: cdefg
        2-9 c: ccccccccc`;
        assert.strictEqual(part2(input), 1);
    });
});