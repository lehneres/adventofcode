'use strict'


// Part 1
// ======

const part1 = input => {
    return Math.max(...input.split('\n')
                            .map(x => x.trim().split(''))
                            .map(b => {
                                const pos = {rows: [0, 127], columns: [0, 7]}
                                for (let s of b) {
                                    switch (s) {
                                        case 'F':
                                            pos.rows[1] -= Math.ceil((pos.rows[1] - pos.rows[0]) / 2)
                                            break
                                        case 'B':
                                            pos.rows[0] += Math.ceil((pos.rows[1] - pos.rows[0]) / 2)
                                            break
                                        case 'L':
                                            pos.columns[1] -= Math.ceil((pos.columns[1] - pos.columns[0]) / 2)
                                            break
                                        case 'R':
                                            pos.columns[0] += Math.ceil((pos.columns[1] - pos.columns[0]) / 2)
                                            break
                                    }
                                }
                                return pos.rows[0] * 8 + pos.columns[0]
                            }))
}

// Part 2
// ======

const part2 = input => {
    const seats = input.split('\n')
                       .map(x => x.trim().split(''))
                       .map(b => {
                           const pos = {rows: [0, 127], columns: [0, 7]}
                           for (let s of b) {
                               switch (s) {
                                   case 'F':
                                       pos.rows[1] -= Math.ceil((pos.rows[1] - pos.rows[0]) / 2)
                                       break
                                   case 'B':
                                       pos.rows[0] += Math.ceil((pos.rows[1] - pos.rows[0]) / 2)
                                       break
                                   case 'L':
                                       pos.columns[1] -= Math.ceil((pos.columns[1] - pos.columns[0]) / 2)
                                       break
                                   case 'R':
                                       pos.columns[0] += Math.ceil((pos.columns[1] - pos.columns[0]) / 2)
                                       break
                               }
                           }
                           return {'id': pos.rows[0] * 8 + pos.columns[0], 'pos': pos}
                       }).sort((s1, s2) => s1.id - s2.id)
    for (let i = 1; i < seats.length; i++)
        if (seats[i - 1].id - seats[i].id !== -1)
            return seats[i].id - 1

}

module.exports = {part1, part2}