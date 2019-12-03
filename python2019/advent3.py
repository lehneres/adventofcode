from aocd.models import Puzzle
from shapely.geometry import LineString, Point


def distance(cables):
    paths = []
    for cable in cables:
        path = [(0, 0)]
        for insts in cable.split(','):
            x, y = path[-1]
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
            path.append((x, y))
        paths.append(path)
    intersections = []
    for opath in paths:
        for o in range(len(opath) - 1):
            oline = LineString([opath[o], opath[o + 1]])
            for ipath in paths:
                if opath == ipath:
                    continue
                for i in range(len(ipath) - 1):
                    iline = LineString([ipath[i], ipath[i + 1]])
                    intersection = oline.intersection(iline)
                    if not (intersection.equals(Point((0, 0))) or intersection.is_empty):
                        intersections.append((intersection.x, intersection.y))
    intersections = set(intersections)
    distances = []
    for i in intersections:
        distances.append(abs(i[0] - 0) + abs(i[1] - 0))
    return min(distances)


if __name__ == '__main__':
    puzzle = Puzzle(year=2019, day=3)
    cables = list(puzzle.input_data.splitlines())
    puzzle.answer_a = int(distance(cables))