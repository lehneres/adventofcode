import unittest

from advent8 import parse_picture, collapse_layers


# noinspection PyMethodMayBeStatic
class AdventTest(unittest.TestCase):
    def test_format(self):
        data = '123456789012'
        assert [[[1, 2, 3], [4, 5, 6]], [[7, 8, 9], [0, 1, 2]]] == parse_picture(data, 3, 2)

    def test_format2(self):
        data = '0222112222120000'
        assert [[[0, 2], [2, 2]], [[1, 1], [2, 2]], [[2, 2], [1, 2]], [[0, 0], [0, 0]]] == parse_picture(data, 2, 2)
        assert [[0, 1], [1, 0]] == collapse_layers(parse_picture(data, 2, 2), 2, 2)
