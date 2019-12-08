import unittest
from unittest import mock

from intcode import IntCodeMachine


# noinspection DuplicatedCode,PyMethodMayBeStatic
class AdventTest(unittest.TestCase):
    def testone(self):
        m = IntCodeMachine([1002, 4, 3, 4, 33])
        m.run()
        assert m.memory == [1002, 4, 3, 4, 99]

    def testnegative(self):
        m = IntCodeMachine([1101, 100, -1, 4, 0])
        m.run()
        assert m.memory == [1101, 100, -1, 4, 99]

    def testequalseightposmode(self):
        m = IntCodeMachine([3, 9, 8, 9, 10, 9, 4, 9, 99, -1, 8])
        with mock.patch('builtins.input', return_value=1):
            m.run()
        assert m.out == [0], 'result is not False for val < 8'
        m.reset()
        with mock.patch('builtins.input', return_value=8):
            m.run()
        assert m.out == [1], 'result is not True for val == 8'
        m.reset()
        with mock.patch('builtins.input', return_value=10):
            m.run()
        assert m.out == [0], 'result is not False for val > 8'

    def testlesseightposmode(self):
        m = IntCodeMachine([3, 9, 7, 9, 10, 9, 4, 9, 99, -1, 8])
        with mock.patch('builtins.input', return_value=1):
            m.run()
        assert m.out == [1], 'result is not True for val < 8'
        m.reset()
        with mock.patch('builtins.input', return_value=8):
            m.run()
        assert m.out == [0], 'result is not False for val == 8'
        m.reset()
        with mock.patch('builtins.input', return_value=10):
            m.run()
        assert m.out == [0], 'result is not False for val > 8'

    def testequalseightimmode(self):
        m = IntCodeMachine([3, 3, 1108, -1, 8, 3, 4, 3, 99])
        with mock.patch('builtins.input', return_value=1):
            m.run()
        assert m.out == [0], 'result is not False for val < 8'
        m.reset()
        with mock.patch('builtins.input', return_value=8):
            m.run()
        assert m.out == [1], 'result is not True for val == 8'
        m.reset()
        with mock.patch('builtins.input', return_value=10):
            m.run()
        assert m.out == [0], 'result is not False for val > 8'

    def testlesseightimmode(self):
        m = IntCodeMachine([3, 3, 1107, -1, 8, 3, 4, 3, 99])
        with mock.patch('builtins.input', return_value=1):
            m.run()
        assert m.out == [1], 'result is not True for val < 8'
        m.reset()
        with mock.patch('builtins.input', return_value=8):
            m.run()
        assert m.out == [0], 'result is not False for val == 8'
        m.reset()
        with mock.patch('builtins.input', return_value=10):
            m.run()
        assert m.out == [0], 'result is not False for val > 8'

    def testjumponeposmode(self):
        m = IntCodeMachine([3, 12, 6, 12, 15, 1, 13, 14, 13, 4, 13, 99, -1, 0, 1, 9])
        with mock.patch('builtins.input', return_value=1):
            m.run()
        assert m.out == [1], 'result is not True for val != 0'
        m.reset()
        with mock.patch('builtins.input', return_value=0):
            m.run()
        assert m.out == [0], 'result is not False for val == 0'

    def testjumponeimmode(self):
        m = IntCodeMachine([3, 3, 1105, -1, 9, 1101, 0, 0, 12, 4, 12, 99, 1])
        with mock.patch('builtins.input', return_value=1):
            m.run()
        assert m.out == [1], 'result is not True for val != 0'
        m.reset()
        with mock.patch('builtins.input', return_value=0):
            m.run()
        assert m.out == [0], 'result is not False for val == 0'

    def test(self):
        m = IntCodeMachine(
                [3, 21, 1008, 21, 8, 20, 1005, 20, 22, 107, 8, 21, 20, 1006, 20, 31, 1106, 0, 36, 98, 0, 0, 1002, 21, 125, 20, 4, 20, 1105, 1, 46, 104, 999, 1105, 1, 46, 1101,
                 1000, 1, 20, 4, 20, 1105, 1, 46, 98, 99])
        with mock.patch('builtins.input', return_value=1):
            m.run()
        assert m.out == [999], 'result is not 999 for val < 8'
        m.reset()
        with mock.patch('builtins.input', return_value=8):
            m.run()
        assert m.out == [1000], 'result is not 1000 for val == 8'
        m.reset()
        with mock.patch('builtins.input', return_value=10):
            m.run()
        assert m.out == [1001], 'result is not 1001 for val > 8'
