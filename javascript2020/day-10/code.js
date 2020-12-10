'use strict'

// Part 1
// ======

const part1 = (input) => {
    const data = input.split('\n').map(x => parseInt(x))
    data.push(Math.max(...data) + 3)
    let jolt     = 0
    let adapters = {1: 0, 2: 0, 3: 0}
    do {
        let delta = 1
        do {
            const adapter = data.findIndex(x => x === jolt + delta)
            if (adapter >= 0) {
                jolt += delta
                data.splice(adapter, 1)
                adapters[delta]++
                break
            }
        } while (delta++ < 3)
    } while (data.length)
    return adapters[1] * adapters[3]
}

// Part 2
// ======

//stole with pride from reddit after 4h of recursion
const solution = (data) => {
    return data.reduce((computed, jolt) => {
        computed[jolt] =
            (computed[jolt - 3] || 0) +
            (computed[jolt - 2] || 0) +
            (computed[jolt - 1] || 0)
        return computed
    }, [1]).pop();
}

const combination = (input, max_jolt) => {
    let stack = [[input, 0, [0]]]
    let paths = []
    while (stack.length) {
        let delta              = 1
        let data, jolt, pre_path
        [data, jolt, pre_path] = stack.pop()
        do {
            const adapter = data.findIndex(x => x === jolt + delta)
            if (adapter >= 0) {
                let new_path     = [...pre_path, data[adapter]]
                let copy_of_data = [...data]
                copy_of_data.splice(adapter, 1)
                if (jolt + delta < max_jolt)
                    stack.push([copy_of_data, jolt + delta, new_path])
                else
                    paths.push(new_path)
            }
        } while (delta++ < 3)
    }

    return paths
}

const part2 = (input, preamble_length = 25) => {
    const data = input.split('\n').map(x => parseInt(x)).sort((a, b) => a - b)
    data.push(Math.max(...data) + 3)
    return solution(data)
    // return combination(data, Math.max(...data)).length
}

module.exports = {part1, part2}