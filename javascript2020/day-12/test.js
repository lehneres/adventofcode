const assert         = require('assert');
const {part1, part2} = require('./code');

describe('Part One', () => {
    it('should return 37 with input', () => {
        let input = `F10
                            N3
                            F7
                            R90
                            F11`;
        assert.strictEqual(part1(input), 25);
    });
});

describe('Part Two', () => {
    it('should return 286 with input', () => {
        let input = `F10
                            N3
                            F7
                            R90
                            F11`;
        assert.strictEqual(part2(input), 286);
    });
});