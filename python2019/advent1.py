from aocd.models import Puzzle
import math

puzzle = Puzzle(year=2019, day=1)


def fuel_for_mass(mass):
    return math.floor((int(mass) / 3)) - 2


def rec_fuel_for_mass(mass):
    fuel = math.floor((int(mass) / 3)) - 2
    if fuel > 0:
        return fuel + rec_fuel_for_mass(fuel)
    else:
        return 0


# def rec_fuel_for_mass(data):


if __name__ == '__main__':
    first = list(map(fuel_for_mass, puzzle.input_data.splitlines()))
    if not puzzle.answer_a == str(sum(first)):
        puzzle.answer_a = sum(first)
    else:
        second = list(map(rec_fuel_for_mass,  puzzle.input_data.splitlines()))
        puzzle.answer_b = sum(second)
