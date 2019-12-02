import unittest
from advent2 import intcode


class AdventTest(unittest.TestCase):
    def test_fist_1(self):
        data = [1, 9, 10, 3, 2, 3, 11, 0, 99, 30, 40, 50]
        result = [3500, 9, 10, 70, 2, 3, 11, 0, 99, 30, 40, 50]
        assert result == intcode(list(map(int, data)), 4)

    def test_fist_2(self):
        data = [1, 0, 0, 0, 99]
        result = [2, 0, 0, 0, 99]
        assert result == intcode(list(map(int, data)), 4)

    def test_fist_3(self):
        data = [2, 3, 0, 3, 99]
        result = [2, 3, 0, 6, 99]
        assert result == intcode(list(map(int, data)), 4)

    def test_fist_4(self):
        data = [2, 4, 4, 5, 99, 0]
        result = [2, 4, 4, 5, 99, 9801]
        assert result == intcode(list(map(int, data)), 4)

    def test_fist_5(self):
        data = [1, 1, 1, 4, 99, 5, 6, 0, 99]
        result = [30, 1, 1, 4, 2, 5, 6, 0, 99]
        assert result == intcode(list(map(int, data)), 4)


if __name__ == '__main__':
    unittest.main()
