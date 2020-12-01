'use strict'

// Part 1
// ======

const part1 = input => {
    let expenses = input
        .split('\n')
        .map(x => parseInt(x));

    for (let i = 0; i < expenses.length; i++) {
        const found = expenses.filter(x => x !== expenses[i]).find(second => expenses[i] + second === 2020)
        if (found) return expenses[i] * found
    }
}

// Part 2
// ======

const part2 = input => {
    let expenses = input
        .split('\n')
        .map(x => parseInt(x));

    for (let no1 of expenses)
        for (let no2 of expenses.filter(x => x !== no1))
            for (let no3 of expenses.filter(x => x !== no1).filter(x => x !== no2))
                if (no1 + no2 + no3 === 2020)
                    return no1 * no2 * no3
}

module.exports = {part1, part2}