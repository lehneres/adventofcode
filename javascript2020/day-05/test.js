const assert         = require('assert');
const {part1, part2} = require('./code');

describe('Part One', () => {
    it('should return 357 with single input', () => {
        let input = `FBFBBFFRLR`;
        assert.strictEqual(part1(input), 357);
    });
    it('should return 567 with single input', () => {
        let input = `BFFFBBFRRR`;
        assert.strictEqual(part1(input), 567);
    });
    it('should return 119 with single input', () => {
        let input = `FFFBBBFRRR`;
        assert.strictEqual(part1(input), 119);
    });
    it('should return 820 with single input', () => {
        let input = `BBFFBBFRLL`;
        assert.strictEqual(part1(input), 820);
    });
    it('should return 820 with multiple inputs', () => {
        let input = `BBFFBBFRLL
                            FBFBBFFRLR
                            BFFFBBFRRR
                            FFFBBBFRRR`;
        assert.strictEqual(part1(input), 820);
    });
});