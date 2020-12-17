const assert         = require('assert');
const {part1, part2} = require('./code');

describe('Part One', () => {
    it('should return 112 with input', () => {
        let input = `.#.
                            ..#
                            ###`;
        assert.strictEqual(part1(input, true), 112);
    });
});

describe('Part Two', () => {
    it('should return 848 with input', () => {
        let input = `.#.
                            ..#
                            ###`;
        assert.strictEqual(part2(input, true), 848);
    });
});