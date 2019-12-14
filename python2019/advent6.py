from aocd.models import Puzzle
from pandas.core.common import flatten


def find_orbits(data):
    orbits = {}
    for obj in data:
        body, satellite = obj.split(')')

        if body not in orbits:
            orbits[body] = []
        if satellite not in orbits:
            orbits[satellite] = []

        orbits[satellite].append([body])
        orbits[satellite].append(orbits[body])

    return orbits


if __name__ == '__main__':
    puzzle = Puzzle(year=2019, day=6)
    orbits = find_orbits(puzzle.input_data.splitlines())
    puzzle.answer_a = sum([len(set(flatten(v))) for k, v in orbits.items()])
    you = list(flatten(orbits['YOU']))
    san = list(flatten(orbits['SAN']))
    shared = [i for i in you if i in san]
    puzzle.answer_b = you.index(shared[0]) + san.index(shared[0])
