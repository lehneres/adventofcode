from aocd.models import Puzzle


def num_solutions(start, to, restrictive=False):
    all_pot = range(int(start), int(to) + 1)
    adj = []
    if restrictive:
        for e in list(map(str, [i for i in all_pot if i])):
            doubles = [(a, b) for a, b in zip(e, e[1:]) if a == b]
            triples = [(a, b, c) for a, b, c in zip(e, e[1:], e[2:]) if a == b == c]
            if triples:
                doubles = [i for i in doubles if i not in [(a, b) for a, b, c in triples]]
            if doubles:
                adj.append(e)
    else:
        adj = [e for e in list(map(str, [i for i in all_pot if i])) if [(a, b) for a, b in zip(e, e[1:]) if a == b]]
    inc = [e for e in adj if all(e[i] <= e[i + 1] for i in range(len(e) - 1))]
    return len(inc)


if __name__ == '__main__':
    puzzle = Puzzle(year=2019, day=4)
    start, end = list(puzzle.input_data.split('-'))
    puzzle.answer_a = num_solutions(start=start, to=end)
    puzzle.answer_b = num_solutions(start=start, to=end, restrictive=True)
