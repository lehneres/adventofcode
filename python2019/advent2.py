from aocd.models import Puzzle
from intcode import IntCodeMachine

if __name__ == '__main__':
    puzzle = Puzzle(year=2019, day=2)
    data = list(map(int, puzzle.input_data.split(',')))
    data[1] = 12
    data[2] = 2
    intcode = IntCodeMachine(data)
    intcode.run()
    puzzle.answer_a = intcode.memory[0]

    for noun in range(100):
        for verb in range(100):
            data = list(map(int, puzzle.input_data.split(',')))
            data[1] = noun
            data[2] = verb
            if intcode.memory[0] == 19690720:
                puzzle.answer_b = 100 * noun + verb
