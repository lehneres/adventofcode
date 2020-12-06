'use strict'


// Part 1
// ======

const part1 = input => {
    return input.split(/\n\s*\n/)
                .map(x => x.trim())
                .filter(x => x.length > 0)
                .map(x => new Set(x.split('\n').map(x => x.trim()).flatMap(x => x.split(''))))
                .reduce((a, b) => a + b.size, 0)
}

// Part 2
// ======

const part2 = input => {
    return input.split(/\n\s*\n/)
                .map(x => x.trim())
                .filter(x => x.length > 0)
                .map(x => new Set(x.split('\n').map(x => x.trim()).map(x => x.split('')).reduce((p, c) => p.filter(e => c.includes(e)))))
                .reduce((a, b) => a + b.size, 0)
}

module.exports = {part1, part2}