import itertools

from aocd.models import Puzzle

from intcode import IntCodeMachine


def boot_amplifiers(program, phase):
    amps = [IntCodeMachine(program) for i in range(0, len(phase))]
    energy = 0
    for i in range(0, len(phase)):
        amps[i].set_inputs([phase[i], energy])
        energy = next(amps[i].run())

    while all(not amp.isStopped for amp in amps):
        for i in range(0, len(phase)):
            try:
                amps[i].set_inputs([energy])
                energy = next(amps[i].run())
            except StopIteration:
                pass

    return energy


if __name__ == '__main__':
    puzzle = Puzzle(year=2019, day=7)
    program = list(map(int, puzzle.input_data.split(',')))
    max_energy = -1
    for phase in list(itertools.permutations(range(0, 5))):
        max_energy = max(max_energy, boot_amplifiers(program, phase))
    puzzle.answer_a = max_energy
    for phase in list(itertools.permutations(range(5, 10))):
        max_energy = max(max_energy, boot_amplifiers(program, phase))
    puzzle.answer_b = max_energy
    pass
