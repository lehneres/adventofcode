'use strict'

// Part 1
// ======

function slide(map, traverse) {
    const width = map[0].length
    let pos     = {r: traverse.r, d: traverse.d}
    let trees   = 0
    for (; ; pos.r += traverse.r) {
        if (map[pos.d][pos.r % width] === '#') trees++
        if (pos.d + traverse.d >= map.length) break
        else pos.d += traverse.d
    }
    return trees
}

const part1 = input => {
    const map = input.split('\n').map(x => x.trim().split(''));
    return slide(map, {r: 3, d: 1})
}

// Part 2
// ======

const part2 = input => {
    const map = input.split('\n').map(x => x.trim().split(''));
    return slide(map, {r: 1, d: 1})
           * slide(map, {r: 3, d: 1})
           * slide(map, {r: 5, d: 1})
           * slide(map, {r: 7, d: 1})
           * slide(map, {r: 1, d: 2})
}

module.exports = {part1, part2}