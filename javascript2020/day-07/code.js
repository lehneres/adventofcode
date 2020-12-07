'use strict'

function extractGraph(input) {
    const rules = input.split(/\n/)
                       .map(x => x.trim())
                       .map(x => x.match(/(\w+ \w+) bags? contain (?:no other|(\d+ \w+ \w+ bags?.*))/))
                       .map(x => [x[1], ...(x[2] || '').split(',')
                                                       .map(x => x.trim())
                                                       .filter(x => x.length !== 0)
                                                       .flatMap(x => x.match(/(\d+) (\w+ \w+)/))])

    const adjacencyList = {}
    for (let rule of rules) {
        adjacencyList[rule[0]] = adjacencyList[rule[0]] || {}
        let pointer            = 1
        while (rule[pointer]) {
            adjacencyList[rule[0]][rule[pointer + 2]] = parseInt(rule[pointer + 1])
            pointer += 3
        }
    }
    return adjacencyList
}

// Part 1
// ======

const part1 = input => {
    const adjacencyList = extractGraph(input)
    const dfs           = (node) => {
        if (node === 'shiny gold') return 1
        return Object.keys(adjacencyList[node]).reduce((a, c) => a + dfs(c), 0)
    };
    return Object.keys(adjacencyList).filter(n => n !== 'shiny gold').reduce((a, c) => a + Math.sign(dfs(c)), 0)
}

// Part 2
// ======

const part2 = input => {
    const adjacencyList = extractGraph(input)
    const dfs           = (node) =>
        Object.keys(adjacencyList[node]).reduce((a, c) => a + adjacencyList[node][c] + adjacencyList[node][c] * dfs(c), 0)
    return dfs('shiny gold')
}

module.exports = {part1, part2}