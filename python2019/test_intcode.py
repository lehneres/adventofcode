import math
import unittest
from unittest import mock

from intcode import IntCodeMachine


# noinspection DuplicatedCode,PyMethodMayBeStatic
class AdventTest(unittest.TestCase):
    def testone(self):
        m = IntCodeMachine([1002, 4, 3, 4, 33])
        try:
            while True:
                next(m.run())
        except StopIteration:
            assert m.memory[0:5] == [1002, 4, 3, 4, 99]

    def testnegative(self):
        m = IntCodeMachine([1101, 100, -1, 4, 0])
        try:
            while True:
                next(m.run())
        except StopIteration:
            assert m.memory[0:5] == [1101, 100, -1, 4, 99]

    def testinputmode(self):
        m = IntCodeMachine([3, 9, 8, 9, 10, 9, 4, 9, 99, -1, 8], [1])
        assert next(m.run()) == 0, 'result is not False for val < 8'
        m = IntCodeMachine([3, 9, 8, 9, 10, 9, 4, 9, 99, -1, 8], [8])
        assert next(m.run()) == 1, 'result is not True for val == 8'
        m = IntCodeMachine([3, 9, 8, 9, 10, 9, 4, 9, 99, -1, 8], [10])
        assert next(m.run()) == 0, 'result is not False for val > 8'

    def testequalseightposmode(self):
        m = IntCodeMachine([3, 9, 8, 9, 10, 9, 4, 9, 99, -1, 8])
        with mock.patch('builtins.input', return_value=1):
            assert next(m.run()) == 0, 'result is not False for val < 8'
        m.reset()
        with mock.patch('builtins.input', return_value=8):
            assert next(m.run()) == 1, 'result is not True for val == 8'
        m.reset()
        with mock.patch('builtins.input', return_value=10):
            assert next(m.run()) == 0, 'result is not False for val > 8'

    def testlesseightposmode(self):
        m = IntCodeMachine([3, 9, 7, 9, 10, 9, 4, 9, 99, -1, 8])
        with mock.patch('builtins.input', return_value=1):
            assert next(m.run()) == 1, 'result is not True for val < 8'
        m.reset()
        with mock.patch('builtins.input', return_value=8):
            assert next(m.run()) == 0, 'result is not False for val == 8'
        m.reset()
        with mock.patch('builtins.input', return_value=10):
            assert next(m.run()) == 0, 'result is not False for val > 8'

    def testequalseightimmode(self):
        m = IntCodeMachine([3, 3, 1108, -1, 8, 3, 4, 3, 99])
        with mock.patch('builtins.input', return_value=1):
            assert next(m.run()) == 0, 'result is not False for val < 8'
        m.reset()
        with mock.patch('builtins.input', return_value=8):
            assert next(m.run()) == 1, 'result is not True for val == 8'
        m.reset()
        with mock.patch('builtins.input', return_value=10):
            assert next(m.run()) == 0, 'result is not False for val > 8'

    def testlesseightimmode(self):
        m = IntCodeMachine([3, 3, 1107, -1, 8, 3, 4, 3, 99])
        with mock.patch('builtins.input', return_value=1):
            assert next(m.run()) == 1, 'result is not True for val < 8'
        m.reset()
        with mock.patch('builtins.input', return_value=8):
            assert next(m.run()) == 0, 'result is not False for val == 8'
        m.reset()
        with mock.patch('builtins.input', return_value=10):
            assert next(m.run()) == 0, 'result is not False for val > 8'

    def testjumponeposmode(self):
        m = IntCodeMachine([3, 12, 6, 12, 15, 1, 13, 14, 13, 4, 13, 99, -1, 0, 1, 9])
        with mock.patch('builtins.input', return_value=1):
            assert next(m.run()) == 1, 'result is not True for val != 0'
        m.reset()
        with mock.patch('builtins.input', return_value=0):
            assert next(m.run()) == 0, 'result is not False for val == 0'

    def testjumponeimmode(self):
        m = IntCodeMachine([3, 3, 1105, -1, 9, 1101, 0, 0, 12, 4, 12, 99, 1])
        with mock.patch('builtins.input', return_value=1):
            assert next(m.run()) == 1, 'result is not True for val != 0'
        m.reset()
        with mock.patch('builtins.input', return_value=0):
            assert next(m.run()) == 0, 'result is not False for val == 0'

    def testrelativebase(self):
        m = IntCodeMachine([109, 1, 204, -1, 1001, 100, 1, 100, 1008, 100, 16, 101, 1006, 101, 0, 99])
        try:
            while True:
                next(m.run())
        except StopIteration:
            assert m.output == [109, 1, 204, -1, 1001, 100, 1, 100, 1008, 100, 16, 101, 1006, 101, 0, 99]

    def testlargenumber(self):
        m = IntCodeMachine([1102, 34915192, 34915192, 7, 4, 7, 99, 0])
        assert int(math.log10(next(m.run()))) + 1 == 16

    def testlargenumber2(self):
        m = IntCodeMachine([104, 1125899906842624, 99])
        assert next(m.run()) == 1125899906842624

    def testmore1(self):
        m = IntCodeMachine([109, -1, 4, 1, 99])
        assert next(m.run()) == -1

    def testmore2(self):
        m = IntCodeMachine([109, -1, 104, 1, 99])
        assert next(m.run()) == 1

    def testmore3(self):
        m = IntCodeMachine([109, -1, 204, 1, 99])
        assert next(m.run()) == 109

    def testmore4(self):
        m = IntCodeMachine([109, 1, 9, 2, 204, -6, 99])
        assert next(m.run()) == 204

    def testmore5(self):
        m = IntCodeMachine([109, 1, 109, 9, 204, -6, 99])
        assert next(m.run()) == 204

    def testmore6(self):
        m = IntCodeMachine([109, 1, 209, -1, 204, -106, 99])
        assert next(m.run()) == 204

    def test(self):
        m = IntCodeMachine(
                [3, 21, 1008, 21, 8, 20, 1005, 20, 22, 107, 8, 21, 20, 1006, 20, 31, 1106, 0, 36, 98, 0, 0, 1002, 21, 125, 20, 4, 20, 1105, 1, 46, 104, 999, 1105, 1, 46, 1101,
                 1000, 1, 20, 4, 20, 1105, 1, 46, 98, 99])
        with mock.patch('builtins.input', return_value=1):
            assert next(m.run()) == 999, 'result is not 999 for val < 8'
        m.reset()
        with mock.patch('builtins.input', return_value=8):
            assert next(m.run()) == 1000, 'result is not 1000 for val == 8'
        m.reset()
        with mock.patch('builtins.input', return_value=10):
            assert next(m.run()) == 1001, 'result is not 1001 for val > 8'
