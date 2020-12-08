'use strict'

// Part 1
// ======

const part1 = input => {
    const program = input.split('\n').map(x => x.match(/(\w{3}) ((?:\+|-)\d+)/)).map(x => ({'op': x[1], 'val': parseInt(x[2])}))
    return runProgram(program, true).accumulator
}

// Part 2
// ======

const runProgram = (program, stopAtAnyLoop) => {
    const visited   = new Set()
    let next        = 0
    let accumulator = 0
    let source
    let inLoop
    let inEndlessLoop
    do {
        source          = next
        const {op, val} = program[next]
        switch (op) {
            case 'acc':
                accumulator += val
                next++
                break
            case 'jmp':
                next += val
                break
            case 'nop':
                next++
                break
        }
        visited.add({source, next})
        inLoop        = [...visited].map(x => x.source).filter(x => x === next).length > 0
        inEndlessLoop = [...visited].filter(x => x === {source, next}).length > 0
    } while (!(stopAtAnyLoop && (inLoop || inEndlessLoop)) && (!stopAtAnyLoop || next < program.length))
    return {visited, inLoop, inEndlessLoop, accumulator}
}

const part2 = input => {
    const program   = input.split('\n').map(x => x.match(/(\w{3}) ((?:\+|-)\d+)/)).map(x => ({'op': x[1], 'val': parseInt(x[2])}))
    const {length}  = program
    let next        = 0
    let accumulator = 0
    do {
        const {op, val} = program[next]
        // noinspection FallThroughInSwitchStatementJS
        switch (op) {
            case 'acc':
                accumulator += val
                next++
                break
            case 'jmp':
            case 'nop':
                const prog_jmp      = Array.from(program)
                prog_jmp[next]      = {'op': 'jmp', 'val': val}
                let jmp_result      = runProgram(prog_jmp, true)
                let prog_jmp_inLoop = jmp_result.inLoop || jmp_result.inEndlessLoop

                const prog_nop      = Array.from(program)
                prog_nop[next]      = {'op': 'nop', 'val': val}
                let nop_result      = runProgram(prog_nop, true)
                let prog_nop_inLoop = nop_result.inLoop || nop_result.inEndlessLoop

                if (!prog_jmp_inLoop)
                    next += val
                else if (!prog_nop_inLoop)
                    next++
                else if (op === 'jmp')
                    next += val
                else
                    next++
        }
    } while (next < length)
    return accumulator
}

module.exports = {part1, part2}