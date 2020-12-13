const assert         = require('assert');
const {part1, part2} = require('./code');

describe('Part One', () => {
    it('should return 295 with input', () => {
        let input = `939
                            7,13,x,x,59,x,31,19`;
        assert.strictEqual(part1(input), 295);
    });
});

describe('Part Two', () => {
    it('should return 1068781 with input', () => {
        let input = `939
                            7,13,x,x,59,x,31,19`;
        assert.strictEqual(part2(input), 1068781);
    });
    it('should return 3417 with input', () => {
        let input = `939
                            17,x,13,19`;
        assert.strictEqual(part2(input), 3417);
    });
    it('should return 754018 with input', () => {
        let input = `939
                            67,7,59,61`;
        assert.strictEqual(part2(input), 754018);
    });
    it('should return 779210 with input', () => {
        let input = `939
                            67,x,7,59,61`;
        assert.strictEqual(part2(input), 779210);
    });
    it('should return 1261476 with input', () => {
        let input = `939
                            67,7,x,59,61`;
        assert.strictEqual(part2(input), 1261476);
    });
    it('should return 1202161486 with input', () => {
        let input = `939
                            1789,37,47,1889`;
        assert.strictEqual(part2(input), 1202161486);
    }).timeout(0);
});