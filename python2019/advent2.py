from aocd.models import Puzzle
import numpy as np


def intcode(memory, n):
    nmemory = np.array(memory)
    for instruction in [nmemory[i:i + n] for i in range(0, len(nmemory), n)]:
        if instruction[0] == 1:
            nmemory[instruction[3]] = nmemory[instruction[1]] + nmemory[instruction[2]]
        elif instruction[0] == 2:
            nmemory[instruction[3]] = nmemory[instruction[1]] * nmemory[instruction[2]]
        elif instruction[0] == 99:
            return nmemory.tolist()
        else:
            raise Exception("unkown intcode")
    raise Exception("did not meet stop criteria")


if __name__ == '__main__':
    puzzle = Puzzle(year=2019, day=2)
    data = list(map(int, puzzle.input_data.split(',')))
    data[1] = 12
    data[2] = 2
    puzzle.answer_a = intcode(data, 4)[0]

    for noun in range(100):
        for verb in range(100):
            data = list(map(int, puzzle.input_data.split(',')))
            data[1] = noun
            data[2] = verb
            if intcode(data, 4)[0] == 19690720:
                puzzle.answer_b = 100 * noun + verb
