'use strict'

const matrix_size = 64

// Part 1
// ======

const createEmpty3dMatrix = () => {
    const matrix = []
    for (let z = 0; z < matrix_size; z++) {
        matrix[z] = []
        for (let y = 0; y < matrix_size; y++) {
            matrix[z][y] = []
            for (let x = 0; x < matrix_size; x++)
                matrix[z][y][x] = '.'
        }
    }
    return matrix
}

const print3dMatrix = (matrix) => {
    for (let z = 0; z < matrix_size; z++) {
        console.log(`dim(z) = ${z - ~~(matrix_size / 2)}`)
        for (let y = 0; y < matrix_size; y++) {
            let row = ''
            for (let x = 0; x < matrix_size; x++)
                row += matrix[z][y][x]
            console.log(`${row}`)
        }
        console.log(`--------------------------------`)
    }
    console.log(`%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%`)
}

const part1 = (input, print = false, cycles = 6) => {
    const in_matrix = input.split('\n').map(x => x.trim().split(''))
    let matrix      = createEmpty3dMatrix()
    for (let y in in_matrix)
        for (let x in in_matrix[y]) {
            const offsetY                                                                             = ~~(in_matrix.length
                                                                                                           / 2)
            const offsetX                                                                             = ~~(in_matrix[0].length
                                                                                                           / 2)
            matrix[matrix_size / 2][matrix_size / 2 + (y - offsetY)][matrix_size / 2 + (x - offsetX)] = in_matrix[y][x]
        }

    if (print) print3dMatrix(matrix)

    do {
        const new_matrix = JSON.parse(JSON.stringify(matrix))
        for (let z = 0; z < matrix_size; z++) {
            for (let y = 0; y < matrix_size; y++) {
                for (let x = 0; x < matrix_size; x++) {
                    let active_neighbors = 0
                    for (let xi = (x - 1 >= 0 ? -1 : 0); xi <= (x + 1 < matrix_size ? 1 : 0); xi++)
                        for (let yi = (y - 1 >= 0 ? -1 : 0); yi <= (y + 1 < matrix_size ? 1 : 0); yi++)
                            for (let zi = (z - 1 >= 0 ? -1 : 0); zi <= (z + 1 < matrix_size ? 1 : 0); zi++)
                                if (!(zi === 0 && yi === 0 && xi === 0))
                                    if (matrix[z + zi][y + yi][x + xi] === '#') active_neighbors++

                    new_matrix[z][y][x] = matrix[z][y][x] === '#' ? active_neighbors === 2 || active_neighbors === 3
                                                                    ? '#'
                                                                    : '.' : new_matrix[z][y][x] = active_neighbors === 3
                                                                                                  ? '#'
                                                                                                  : '.'
                }
            }
        }
        matrix = new_matrix
        if (print) print3dMatrix(matrix)
    } while (--cycles)
    return matrix.reduce((a, c) => a + c.reduce((a, c) => a + c.reduce((a, c) => a + (c === '#'), 0), 0), 0)
}

// Part 2
// ======


const createEmpty4dMatrix = () => {
    const matrix = []
    for (let w = 0; w < matrix_size; w++) {
        matrix[w] = []
        for (let z = 0; z < matrix_size; z++) {
            matrix[w][z] = []
            for (let y = 0; y < matrix_size; y++) {
                matrix[w][z][y] = []
                for (let x = 0; x < matrix_size; x++)
                    matrix[w][z][y][x] = '.'
            }
        }
    }
    return matrix
}

const print4dMatrix = (matrix) => {
    for (let w = 0; w < matrix_size; w++) {
        for (let z = 0; z < matrix_size; z++) {
            console.log(`dim(z) = ${z - ~~(matrix_size / 2)} dim(w) = ${w - ~~(matrix_size / 2)}`)
            for (let y = 0; y < matrix_size; y++) {
                let row = ''
                for (let x = 0; x < matrix_size; x++)
                    row += matrix[w][z][y][x]
                console.log(`${row}`)
            }
        }
        console.log(`--------------------------------`)
    }
    console.log(`%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%`)
}


const part2 = (input, print = false, cycles = 6) => {
    const in_matrix = input.split('\n').map(x => x.trim().split(''))
    let matrix      = createEmpty4dMatrix()
    for (let y in in_matrix)
        for (let x in in_matrix[y]) {
            const offsetY = ~~(in_matrix.length / 2)
            const offsetX = ~~(in_matrix[0].length / 2)
            matrix[matrix_size / 2][matrix_size / 2][matrix_size / 2 + (y - offsetY)][matrix_size / 2 + (x - offsetX)]
                          = in_matrix[y][x]
        }

    if (print) print4dMatrix(matrix)

    do {
        console.log(`cycles: ${cycles}`)
        const new_matrix = JSON.parse(JSON.stringify(matrix))
        for (let w = 0; w < matrix_size; w++)
            for (let z = 0; z < matrix_size; z++)
                for (let y = 0; y < matrix_size; y++)
                    for (let x = 0; x < matrix_size; x++) {
                        let active_neighbors = 0
                        for (let xi = (x - 1 >= 0 ? -1 : 0); xi <= (x + 1 < matrix_size ? 1 : 0); xi++)
                            for (let yi = (y - 1 >= 0 ? -1 : 0); yi <= (y + 1 < matrix_size ? 1 : 0); yi++)
                                for (let zi = (z - 1 >= 0 ? -1 : 0); zi <= (z + 1 < matrix_size ? 1 : 0); zi++)
                                    for (let wi = (w - 1 >= 0 ? -1 : 0); wi <= (w + 1 < matrix_size ? 1 : 0); wi++)
                                        if (!(wi === 0 && zi === 0 && yi === 0 && xi === 0))
                                            if (matrix[w + wi][z + zi][y + yi][x + xi] === '#') active_neighbors++

                        new_matrix[w][z][y][x] = matrix[w][z][y][x] === '#' ? active_neighbors === 2 || active_neighbors
                                                                              === 3 ? '#' : '.' : new_matrix[w][z][y][x]
                                                     = active_neighbors === 3 ? '#' : '.'
                    }
        matrix = new_matrix
        if (print) print4dMatrix(matrix)
    } while (--cycles)
    return matrix.reduce(
        (a, c) => a + c.reduce((a, c) => a + c.reduce((a, c) => a + c.reduce((a, c) => a + (c === '#'), 0), 0), 0), 0)
}


module.exports = {part1, part2}