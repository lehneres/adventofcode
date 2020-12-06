const assert         = require('assert');
const {part1, part2} = require('./code');

describe('Part One', () => {
    it('should return 11 with input', () => {
        let input = `abc

                            a
                            b
                            c
                            
                            ab
                            ac
                            
                            a
                            a
                            a
                            a
                            
                            b`;
        assert.strictEqual(part1(input), 11);
    });
});

describe('Part Two', () => {
    it('should return 6 with input', () => {
        let input = `abc

                            a
                            b
                            c
                            
                            ab
                            ac
                            
                            a
                            a
                            a
                            a
                            
                            b`;
        assert.strictEqual(part2(input), 6);
    });
});