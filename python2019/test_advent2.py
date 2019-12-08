import unittest

from intcode import IntCodeMachine


class AdventTest(unittest.TestCase):
    def test_first_1(self):
        data = [1, 9, 10, 3, 2, 3, 11, 0, 99, 30, 40, 50]
        result = [3500, 9, 10, 70, 2, 3, 11, 0, 99, 30, 40, 50]
        intcode = IntCodeMachine(data)
        intcode.run()
        assert result == intcode.memory

    def test_first_2(self):
        data = [1, 0, 0, 0, 99]
        result = [2, 0, 0, 0, 99]
        intcode = IntCodeMachine(data)
        intcode.run()
        assert result == intcode.memory

    def test_first_3(self):
        data = [2, 3, 0, 3, 99]
        result = [2, 3, 0, 6, 99]
        intcode = IntCodeMachine(data)
        intcode.run()
        assert result == intcode.memory

    def test_first_4(self):
        data = [2, 4, 4, 5, 99, 0]
        result = [2, 4, 4, 5, 99, 9801]
        intcode = IntCodeMachine(data)
        intcode.run()
        assert result == intcode.memory

    def test_first_5(self):
        data = [1, 1, 1, 4, 99, 5, 6, 0, 99]
        result = [30, 1, 1, 4, 2, 5, 6, 0, 99]
        intcode = IntCodeMachine(data)
        intcode.run()
        assert result == intcode.memory
