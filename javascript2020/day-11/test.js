const assert         = require('assert');
const {part1, part2} = require('./code');

describe('Part One', () => {
    it('should return 37 with input', () => {
        let input = `L.LL.LL.LL
                            LLLLLLL.LL
                            L.L.L..L..
                            LLLL.LL.LL
                            L.LL.LL.LL
                            L.LLLLL.LL
                            ..L.L.....
                            LLLLLLLLLL
                            L.LLLLLL.L
                            L.LLLLL.LL`;
        assert.strictEqual(part1(input), 37);
    });
});

describe('Part Two', () => {
    it('should return 26 with input', () => {
        let input = `L.LL.LL.LL
                            LLLLLLL.LL
                            L.L.L..L..
                            LLLL.LL.LL
                            L.LL.LL.LL
                            L.LLLLL.LL
                            ..L.L.....
                            LLLLLLLLLL
                            L.LLLLLL.L
                            L.LLLLL.LL`;
        assert.strictEqual(part2(input), 26);
    });
});