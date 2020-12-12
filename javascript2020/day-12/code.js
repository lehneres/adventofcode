'use strict'

function printSeatmap(seatmap) {
    for (let row_idx = 0; row_idx < seatmap.length; row_idx++) {
        let row = ''
        for (let seat_idx = 0; seat_idx < seatmap[row_idx].length; seat_idx++)
            row += seatmap[row_idx][seat_idx]
        console.log(`${row}`)
    }
    console.log(`--------------------------------`)
}

// Part 1
// ======

const part1 = (input) => {
    let directions = input.split('\n').map(x => {
        let dir = x.trim().match(/(\w)(\d+)/)
        return [dir[1], parseInt(dir[2])]
    })
    let ship       = {n: 0, s: 0, w: 0, e: 0, cog: 90}
    for (let step of directions) {
        let inst = step[0]
        if (inst === 'F')
            switch (Math.trunc(ship.cog / 90)) {
                case 0:
                    inst = 'N'
                    break
                case 1:
                    inst = 'E'
                    break
                case 2:
                    inst = 'S'
                    break
                case 3:
                    inst = 'W'
                    break
            }
        switch (inst) {
            case 'N':
                ship.n += step[1]
                break
            case 'S':
                ship.s += step[1]
                break
            case 'E':
                ship.e += step[1]
                break
            case 'W':
                ship.w += step[1]
                break
            case 'L':
                ship.cog = (ship.cog - step[1]) - 360 * Math.floor((ship.cog - step[1]) / 360)
                break
            case 'R':
                ship.cog = (ship.cog + step[1]) - 360 * Math.floor((ship.cog + step[1]) / 360)
                break
        }
    }
    return Math.abs(ship.n - ship.s) + Math.abs(ship.e - ship.w)
}

// Part 2
// ======

const part2 = (input) => {
    let directions = input.split('\n').map(x => {
        let dir = x.trim().match(/(\w)(\d+)/)
        return [dir[1], parseInt(dir[2])]
    })
    let ship       = {n: 0, s: 0, w: 0, e: 0, cog: 90}
    let wpt        = {n: 1, s: 0, w: 0, e: 10}
    for (let step of directions) {
        let inst = step[0]
        if (inst === 'F')
            ship = {n: ship.n + wpt.n * step[1], s: ship.s + wpt.s * step[1], w: ship.w + wpt.w * step[1], e: ship.e + wpt.e * step[1], cog: ship.cog}
        else
            switch (inst) {
                case 'N':
                    wpt.n += step[1]
                    break
                case 'S':
                    wpt.s += step[1]
                    break
                case 'E':
                    wpt.e += step[1]
                    break
                case 'W':
                    wpt.w += step[1]
                    break
                case 'L':
                    switch (Math.trunc(step[1] / 90)) {
                        case 1:
                            wpt = {n: wpt.e, e: wpt.s, s: wpt.w, w: wpt.n}
                            break
                        case 2:
                            wpt = {n: wpt.s, e: wpt.w, s: wpt.n, w: wpt.e}
                            break
                        case 3:
                            wpt = {n: wpt.w, e: wpt.n, s: wpt.e, w: wpt.s}
                            break
                        case 4:
                            break
                    }
                    break
                case 'R':
                    switch (Math.trunc(step[1] / 90)) {
                        case 1:
                            wpt = {n: wpt.w, e: wpt.n, s: wpt.e, w: wpt.s}
                            break
                        case 2:
                            wpt = {n: wpt.s, e: wpt.w, s: wpt.n, w: wpt.e}
                            break
                        case 3:
                            wpt = {n: wpt.e, e: wpt.s, s: wpt.w, w: wpt.n}
                            break
                        case 4:
                            break
                    }
                    break
            }
    }
    return Math.abs(ship.n - ship.s) + Math.abs(ship.e - ship.w)
}

module.exports = {part1, part2}