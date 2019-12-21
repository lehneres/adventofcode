from unittest import mock

from aocd.models import Puzzle

from intcode import IntCodeMachine

if __name__ == '__main__':
    puzzle = Puzzle(year=2019, day=5)
    intcode = IntCodeMachine(list(map(int, puzzle.input_data.split(','))))
    intcode.run()
    puzzle.answer_a = intcode.output[-1]
    intcode.reset()
    intcode.run()
    puzzle.answer_b = intcode.output[-1]
