'use strict'

// Part 1
// ======

const part1 = input => {
    let policies = input.split('\n').map(x => x.trim());

    let valid = 0;
    for (let policy of policies) {
        let [minmax, char, password] = policy.split(' ')
        const [min, max]             = minmax.split('-').map(x => parseInt(x))
        char                         = char.replace(':', '')

        const matches = (password.match(new RegExp(`${char}`, 'g')) || []).length
        if (min <= matches && matches <= max)
            valid++
    }
    return valid
}

// Part 2
// ======

const part2 = input => {
    let policies = input.split('\n').map(x => x.trim());

    let valid = 0;
    for (let policy of policies) {
        let [positions, char, password] = policy.split(' ')
        const [posone, postwo]          = positions.split('-').map(x => parseInt(x))
        char                            = char.replace(':', '')

        if ((password.charAt(posone - 1) === char) + (password.charAt(postwo - 1) === char) === 1)
            valid++
    }
    return valid
}

module.exports = {part1, part2}