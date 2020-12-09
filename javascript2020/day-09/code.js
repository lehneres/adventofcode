'use strict'

// Part 1
// ======

const twoSum = (nums, preamble_length, target_index) => {
    const map        = [];
    const indexnum   = [];
    const target_num = nums[target_index]

    for (let x = target_index - preamble_length; x < target_index; x++)
        if (map[nums[x]] == null)
            map[target_num - nums[x]] = x;
        else {
            indexnum[0] = map[nums[x]];
            indexnum[1] = x;
            break;
        }
    return indexnum;
}


const part1 = (input, preamble_length = 25) => {
    const data = input.split('\n').map(x => parseInt(x))
    let i      = preamble_length
    do i++; while (twoSum(data, preamble_length, i).length > 0)
    return data[i]
}

// Part 2
// ======

const part2 = (input, preamble_length = 25) => {
    const target = part1(input, preamble_length)
    const data   = input.split('\n').map(x => parseInt(x))
    for (let i = 0; i < data.length; i++) {
        let arr = [data[i]]
        for (let j = i + 1; j < data.length - i; j++) {
            arr.push(data[j])
            if (arr.reduce((a, b) => a + b) === target)
                return Math.min(...arr) + Math.max(...arr)
        }
    }
    return -1
}

module.exports = {part1, part2}