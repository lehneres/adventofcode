from aocd.models import Puzzle
from math import floor
import numpy as np
from scipy.spatial.distance import cdist
from scipy.sparse import coo_matrix, find


def get_dimensions(cable):
    m_x, m_y = -1, -1
    x, y = 0, 0
    for point in cable.split(','):
        if point[0] == 'U' or point[0] == 'D':
            if abs(y + int(point[1:])) > abs(y):
                m_y = y
            y += int(point[1:])
        elif point[0] == 'R' or point[0] == 'L':
            if abs(x + int(point[1:])) > abs(x):
                m_x = x
            x += int(point[1:])
    return max(m_x * 2, m_y * 2) + 1


def distance(cables):
    crossings, center, matrices = find_overlap(cables)
    distances = cdist(np.array([[center, center]]), crossings, metric='cityblock')
    return min(distances[0])


def find_overlap(cables):
    dim = max([get_dimensions(cable) for cable in cables])
    matrices = [build_matrix(cable, dim) for cable in cables]
    center = floor(dim / 2)
    crossings = find(sum([m.sign() for m in matrices]) == 2)
    crossings = np.stack((crossings[0], crossings[1]), axis=-1)
    crossings = np.delete(crossings, (crossings == (center, center)).all(axis=1).nonzero(), 0)
    return crossings, center, matrices


def steps(cables):
    crossings, center, matrices = find_overlap(cables)
    total_steps = sum(matrices)
    return min([total_steps[tuple(c)] for c in crossings])


def build_matrix(cable, dim):
    step = 0
    x_coords = []
    y_coords = []
    data = []
    x, y = floor(dim / 2), floor(dim / 2)
    for point in cable.split(','):
        if point[0] == 'U':
            for i in range(y, y + int(point[1:])):
                step = move(x, i, x_coords, y_coords, step, data)
            y += int(point[1:])
        elif point[0] == 'D':
            for i in range(y, y - int(point[1:]), -1):
                step = move(x, i, x_coords, y_coords, step, data)
            y -= int(point[1:])
        elif point[0] == 'R':
            for i in range(x, x + int(point[1:])):
                step = move(i, y, x_coords, y_coords, step, data)
            x += int(point[1:])
        elif point[0] == 'L':
            for i in range(x, x - int(point[1:]), -1):
                step = move(i, y, x_coords, y_coords, step, data)
            x -= int(point[1:])
    return coo_matrix((data, (y_coords, x_coords)), shape=(dim, dim))


def move(x, y, x_coords, y_coords, step, data):
    x_coords.append(x)
    y_coords.append(y)
    data.append(step)
    step += 1
    return step


if __name__ == '__main__':
    puzzle = Puzzle(year=2019, day=3)
    cables = list(puzzle.input_data.splitlines())
    puzzle.answer_a = int(distance(cables))
    puzzle.answer_b = steps(cables)
