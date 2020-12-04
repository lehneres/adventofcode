'use strict'


// Part 1
// ======

const part1 = input => {
    return input.split(/\n\s*\n/)
                .map(x => x.trim())
                .filter(x => x.length > 0)
                .map(x => JSON.parse(`{${x.replace(/(\w+):([\w,#]+)/g, '"$1":"$2",')}}`.replace(',}', '}')))
                .filter(x => 'byr' in x && 'iyr' in x && 'eyr' in x && 'hgt' in x && 'hcl' in x && 'ecl' in x && 'pid' in x)
        .length
}

// Part 2
// ======

const part2 = input => {
    return input.split(/\n\s*\n/)
                .map(x => x.trim())
                .filter(x => x.length > 0)
                .map(x => JSON.parse(`{${x.replace(/(\w+):([\w,#]+)/g, '"$1":"$2",')}}`.replace(',}', '}')))
                .filter(x => 'byr' in x && x.byr.match(/\d{4}/) && parseInt(x.byr) >= 1920 && parseInt(x.byr) <= 2002)
                .filter(x => 'iyr' in x && x.iyr.match(/\d{4}/) && parseInt(x.iyr) >= 2010 && parseInt(x.iyr) <= 2020)
                .filter(x => 'eyr' in x && x.eyr.match(/\d{4}/) && parseInt(x.eyr) >= 2020 && parseInt(x.eyr) <= 2030)
                .filter(x => {
                    if ('hgt' in x) {
                        if (/\d+in/.test(x.hgt)) {
                            let value = parseInt(x.hgt.match(/(\d+)in/)[1])
                            return value >= 59 && value <= 76
                        }
                        if (/\d+cm/.test(x.hgt)) {
                            let value = parseInt(x.hgt.match(/(\d+)cm/)[1])
                            return value >= 150 && value <= 193
                        }
                    }
                })
                .filter(x => 'hcl' in x && /^#[0-9a-f]{6}$/.test(x.hcl))
                .filter(x => 'ecl' in x && ['amb', 'blu', 'brn', 'gry', 'grn', 'hzl', 'oth'].includes(x.ecl))
                .filter(x => 'pid' in x && /^\d{9}$/.test(x.pid))
        .length
}

module.exports = {part1, part2}