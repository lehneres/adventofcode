const assert         = require('assert');
const {part1, part2} = require('./code');

describe('Part One', () => {
    it('should return 7 with input', () => {
        let input = `..##.......
                            #...#...#..
                            .#....#..#.
                            ..#.#...#.#
                            .#...##..#.
                            ..#.##.....
                            .#.#.#....#
                            .#........#
                            #.##...#...
                            #...##....#
                            .#..#...#.#`;
        assert.strictEqual(part1(input), 7);
    });
});

describe('Part Two', () => {
    it('should return 336 with input', () => {
        let input = `..##.......
                            #...#...#..
                            .#....#..#.
                            ..#.#...#.#
                            .#...##..#.
                            ..#.##.....
                            .#.#.#....#
                            .#........#
                            #.##...#...
                            #...##....#
                            .#..#...#.#`;
        assert.strictEqual(part2(input), 336);
    });
});