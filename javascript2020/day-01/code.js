'use strict'

const magic_number = 2020


// Part 1
// ======

const part1 = input => {
    let expenses = input
        .split('\n')
        .map(x => parseInt(x));

    for (let i = 0; i < expenses.length; i++) {
        const found = expenses.filter(x => x !== expenses[i]).find(second => expenses[i] + second === magic_number)
        if (found) return expenses[i] * found
    }
}

// Part 2
// ======

const part2 = input => {
    let expenses = input
        .split('\n')
        .map(x => parseInt(x));

    for (let noOne of expenses)
        for (let noTwo of expenses.filter(x => x !== noOne))
            for (let noThree of expenses.filter(x => x !== noOne).filter(x => x !== noTwo))
                if (noOne + noTwo + noThree === magic_number)
                    return noOne * noTwo * noThree
}

module.exports = {part1, part2}