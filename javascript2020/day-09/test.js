const assert         = require('assert');
const {part1, part2} = require('./code');

describe('Part One', () => {
    it('should return 127 with input', () => {
        let input = `35
                            20
                            15
                            25
                            47
                            40
                            62
                            55
                            65
                            95
                            102
                            117
                            150
                            182
                            127
                            219
                            299
                            277
                            309
                            576`;
        assert.strictEqual(part1(input, 5), 127);
    });
});

describe('Part Two', () => {
    it('should return 62 with input', () => {
        let input = `35
                            20
                            15
                            25
                            47
                            40
                            62
                            55
                            65
                            95
                            102
                            117
                            150
                            182
                            127
                            219
                            299
                            277
                            309
                            576`;
        assert.strictEqual(part2(input, 5), 62);
    });
});