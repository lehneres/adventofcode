import itertools
from PIL import Image
import numpy as np
from aocd.models import Puzzle


def color(pixel):
    for c in pixel:
        if c < 2:
            return c
    return 2


def collapse_layers(data, width, heigth):
    collapsed = [[[] for i in range(0, width)] for i in range(0, heigth)]
    for i in range(0, heigth):
        for j in range(0, width):
            collapsed[i][j] = color([data[l][i][j] for l in range(0, len(data))])
    return collapsed


def parse_picture(data, width, height):
    return [[list(map(int, list(data[i:i + width]))) for i in range(0, len(data), width)][i:i + height] for i in range(0, len(data) // width, height)]


if __name__ == '__main__':
    puzzle = Puzzle(year=2019, day=8)
    data = parse_picture(puzzle.input_data, 25, 6)
    most_dense = sorted([list(itertools.chain(*layer)) for layer in data], key=lambda l: len([1 for i in l if i == 0]))[0]
    puzzle.answer_a = len([1 for i in most_dense if i == 1]) * len([2 for i in most_dense if i == 2])
    img = collapse_layers(data, 25, 6)
    
    for r in range(0, 6):
        l = ''
        for c in range(0, 25):
            if img[r][c] == 1:
                l += '#'
            else:
                l += ' '
        print(l)
