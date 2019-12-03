import unittest
from advent3 import distance


class AdventTest(unittest.TestCase):
    def test_fist_1(self):
        data = ['R8,U5,L5,D3', 'U7,R6,D4,L4']
        result = 6
        assert result == distance(data)

    def test_fist_2(self):
        data = ['R75,D30,R83,U83,L12,D49,R71,U7,L72', 'U62,R66,U55,R34,D71,R55,D58,R83']
        result = 159
        assert result == distance(data)

    def test_fist_3(self):
        data = ['R98,U47,R26,D63,R33,U87,L62,D20,R33,U53,R51', 'U98,R91,D20,R16,D67,R40,U7,R15,U6,R7']
        result = 135
        assert result == distance(data)


if __name__ == '__main__':
    unittest.main()
