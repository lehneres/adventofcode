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
    let seatmap = input.split('\n').map(x => x.trim().split(''))
    let change  = true
    do {
        change            = false
        const new_seatmap = JSON.parse(JSON.stringify(seatmap))
        for (let row_idx = 0; row_idx < seatmap.length; row_idx++)
            for (let seat_idx = 0; seat_idx < seatmap[row_idx].length; seat_idx++) {
                if (seatmap[row_idx][seat_idx] === '.') continue
                let cnt_occ = 0
                for (let r = row_idx - 1 >= 0 ? -1 : 0; r <= (row_idx + 1 < seatmap.length ? 1 : 0); r++)
                    for (let s = seat_idx - 1 >= 0 ? -1 : 0; s <= (seat_idx + 1 < seatmap[row_idx + r].length ? 1 : 0); s++) {
                        if (r === 0 && s === 0) continue
                        cnt_occ += seatmap[row_idx + r][seat_idx + s] === '#'
                    }
                if (cnt_occ === 0) new_seatmap[row_idx][seat_idx] = '#'
                else if (seatmap[row_idx][seat_idx] === '#' && cnt_occ >= 4) new_seatmap[row_idx][seat_idx] = 'L'
                change = change || !(new_seatmap[row_idx][seat_idx] === seatmap[row_idx][seat_idx])
            }
        seatmap = new_seatmap
        printSeatmap(seatmap)
    } while (change)
    return seatmap.reduce((a, b) => a + b.filter(x => x === '#').length, 0)
}

// -1-1 -100 -1+1
// 00-1 0000 00+1
// +1-1 +100 +1+1

// Part 2
// ======

const part2 = (input) => {
    let seatmap = input.split('\n').map(x => x.trim().split(''))
    let change  = true
    do {
        change            = false
        const new_seatmap = JSON.parse(JSON.stringify(seatmap))
        for (let row_idx = 0; row_idx < seatmap.length; row_idx++)
            for (let seat_idx = 0; seat_idx < seatmap[row_idx].length; seat_idx++) {
                if (seatmap[row_idx][seat_idx] === '.') continue
                let cnt_occ = 0

                // \ up
                let r = -1, s = -1
                while (row_idx + r >= 0 && seat_idx + s >= 0)
                    if (seatmap[row_idx + r][seat_idx + s] === '.') {
                        r--
                        s--
                    } else {
                        cnt_occ += seatmap[row_idx + r][seat_idx + s] === '#'
                        break
                    }
                // | up
                r = -1, s = 0
                while (row_idx + r >= 0)
                    if (seatmap[row_idx + r][seat_idx] === '.') {
                        r--
                    } else {
                        cnt_occ += seatmap[row_idx + r][seat_idx] === '#'
                        break
                    }
                // / up
                r = -1, s = +1
                while (row_idx + r >= 0 && seat_idx + s < seatmap[row_idx + r].length)
                    if (seatmap[row_idx + r][seat_idx + s] === '.') {
                        r--
                        s++
                    } else {
                        cnt_occ += seatmap[row_idx + r][seat_idx + s] === '#'
                        break
                    }
                // <-
                r = 0, s = -1
                while (seat_idx + s >= 0)
                    if (seatmap[row_idx][seat_idx + s] === '.') {
                        s--
                    } else {
                        cnt_occ += seatmap[row_idx][seat_idx + s] === '#'
                        break
                    }
                // ->
                r = 0, s = +1
                while (seat_idx + s < seatmap[row_idx].length)
                    if (seatmap[row_idx][seat_idx + s] === '.') {
                        s++
                    } else {
                        cnt_occ += seatmap[row_idx][seat_idx + s] === '#'
                        break
                    }
                // \ down
                r = +1, s = +1
                while (row_idx + r < seatmap.length && seat_idx + s < seatmap[row_idx + r].length)
                    if (seatmap[row_idx + r][seat_idx + s] === '.') {
                        r++
                        s++
                    } else {
                        cnt_occ += seatmap[row_idx + r][seat_idx + s] === '#'
                        break
                    }
                // | down
                r = +1, s = 0
                while (row_idx + r < seatmap.length)
                    if (seatmap[row_idx + r][seat_idx] === '.') {
                        r++
                    } else {
                        cnt_occ += seatmap[row_idx + r][seat_idx] === '#'
                        break
                    }
                // / down
                r = +1, s = -1
                while (row_idx + r < seatmap.length && seat_idx + s >= 0)
                    if (seatmap[row_idx + r][seat_idx + s] === '.') {
                        r++
                        s--
                    } else {
                        cnt_occ += seatmap[row_idx + r][seat_idx + s] === '#'
                        break
                    }

                if (cnt_occ === 0) new_seatmap[row_idx][seat_idx] = '#'
                else if (seatmap[row_idx][seat_idx] === '#' && cnt_occ >= 5) new_seatmap[row_idx][seat_idx] = 'L'
                change = change || !(new_seatmap[row_idx][seat_idx] === seatmap[row_idx][seat_idx])
            }
        seatmap = new_seatmap
        printSeatmap(seatmap)
    } while (change)
    return seatmap.reduce((a, b) => a + b.filter(x => x === '#').length, 0)
}

module.exports = {part1, part2}