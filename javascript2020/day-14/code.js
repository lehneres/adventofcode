'use strict'

function parseInstruction(input) {
    const m  = input.match(/mem\[(\d+)\] = (\d+)/)
    let bits = parseInt(m[2]).toString(2).split('').map(x => parseInt(x))
    while (bits.length < 36)
        bits.unshift(0)
    return [parseInt(m[1]), bits]
}

const maskRegex = /^mask = ([a-zA-Z0-9]+)$/

// Part 1
// ======


const part1 = (input) => {
    let progam = input.split('\n').map(x => x.trim())
    let mask
    let mem    = []
    for (const row of progam) {
        if (maskRegex.test(row))
            mask = row.match(maskRegex)[1].split('')
        else {
            const inst   = parseInstruction(row)
            mem[inst[0]] = inst[1].map((bit, pos) => mask[pos] === 'X' ? bit : parseInt(mask[pos]))
        }
    }
    return mem.reduce((a, b) => a + parseInt(b.join(''), 2), 0)
}

// Part 2
// ======

function* getAllAddresses(address) {
    if (address.includes('X')) {
        yield* getAllAddresses(address.replace('X', '1'))
        yield* getAllAddresses(address.replace('X', '0'))
    } else yield address
}

const part2 = (input) => {
    let progam = input.split('\n').map(x => x.trim())
    let mask   = []
    let mem    = []
    for (const row of progam)
        if (maskRegex.test(row))
            mask = row.match(maskRegex)[1]
        else {
            let [badress, val] = parseInstruction(row)
            badress            = badress.toString(2).split('').map(x => parseInt(x))
            while (badress.length < 36)
                badress.unshift(0)
            const addresses = [...getAllAddresses(badress.map((bit, pos) => mask[pos] === '0' ? bit : mask[pos] === 'X' ? 'X' : parseInt(mask[pos])).join(''))]
            console.log(`writing ${val.join('')} to ${addresses.length} addresses`)
            for (let m of addresses)
                mem[parseInt(m, 2)] = val
        }
    let sum = 0;
    for (let i in mem) sum += parseInt(mem[i].join(''), 2);
    return sum;
    // return mem.reduce((a, b) => a + parseInt(b.join(''), 2), 0)
}


module.exports = {part1, part2}