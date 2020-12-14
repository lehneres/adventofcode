'use strict'


// Part 1
// ======

function findNextBus(tstamp, busses) {
    let next
    let cur_tstamp = tstamp + 1
    do next = busses.filter(b => cur_tstamp % b === 0).pop()
    while (!next && cur_tstamp++)
    return {next, cur_tstamp}
}

const part1 = (input) => {
    const data               = input.split('\n').map(x => x.trim())
    const tstamp             = parseInt(data[0])
    const busses             = data[1].split(',').map(x => parseInt(x))
    const {next, cur_tstamp} = findNextBus(tstamp, busses)
    return (cur_tstamp - tstamp) * next
}

// Part 2
// ======

const part2 = (input) => {
    const data   = input.split('\n').map(x => x.trim())
    const busses = data[1].split(',').map(x => parseInt(x))
    let tstamp   = busses[0]
    let factor   = busses[0]
    for (let offset = 1; offset < busses.length; offset++)
        if (!isNaN(busses[offset]))
            while (true) {
                if ((tstamp + offset) % busses[offset] === 0) {
                    factor *= busses[offset]
                    break
                }
                tstamp += factor
            }
    return tstamp
}


module.exports = {part1, part2}