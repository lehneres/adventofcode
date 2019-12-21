from aocd.models import Puzzle

from intcode import IntCodeMachine

if __name__ == '__main__':
    puzzle = Puzzle(year=2019, day=9)
    m = IntCodeMachine(list(map(int, puzzle.input_data.split(','))))
    m.set_inputs([1])
    puzzle.answer_a = next(m.run())
    m.reset()
    m.set_inputs([2])
    puzzle.answer_b = next(m.run())
