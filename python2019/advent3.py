from aocd.models import Puzzle
from shapely.geometry import LineString, Point


def distance(cables):
    paths = []
    steps = []
    for cable in cables:
        path = [((0, 0), 0)]
        for insts in cable.split(','):
            coord, steps = path[-1]
            x, y = coord
            if insts[0] == 'U':
                y += int(insts[1:])
            elif insts[0] == 'D':
                y -= int(insts[1:])
            elif insts[0] == 'R':
                x += int(insts[1:])
            elif insts[0] == 'L':
                x -= int(insts[1:])
            else:
                raise Exception('unknown direction')
            path.append(((x, y), steps + abs(int(insts[1:]))))
        paths.append(path)
    intersections = []
    for opath in paths:
        for o in range(len(opath) - 1):
            oline = LineString([opath[o][0], opath[o + 1][0]])
            for ipath in paths:
                if opath == ipath:
                    continue
                for i in range(len(ipath) - 1):
                    iline = LineString([ipath[i][0], ipath[i + 1][0]])
                    intersection = oline.intersection(iline)
                    if not (intersection.equals(Point((0, 0))) or intersection.is_empty):
                        intersections.append((intersection.x, intersection.y))
    return min([abs(i[0] - 0) + abs(i[1] - 0) for i in set(intersections)])


if __name__ == '__main__':
    puzzle = Puzzle(year=2019, day=3)
    cables = list(puzzle.input_data.splitlines())
    puzzle.answer_a = int(distance(cables))
