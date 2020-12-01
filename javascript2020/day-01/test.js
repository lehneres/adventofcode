const assert = require('assert');
const {part1, part2} = require('./code');

describe('Part One', () => {
    it('should return 514579 with input', () => {
        let input = `1721
      979
      366
      299
      675
      1456`;
        assert.strictEqual(part1(input), 514579);
    });
});

describe('Part Two', () => {
    it('should return 241861950 with input', () => {
        let input = `1721
      979
      366
      299
      675
      1456`;
        assert.strictEqual(part2(input), 241861950);
    });
});