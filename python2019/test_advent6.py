import unittest
from advent6 import find_orbits


# noinspection PyMethodMayBeStatic
class AdventTest(unittest.TestCase):
    def test_orbits(self):
        data = ['COM)B', 'B)C', 'C)D', 'D)E', 'E)F', 'B)G', 'G)H', 'D)I', 'E)J', 'J)K', 'K)L']
        assert 42 == sum([len(v) for k, v in find_orbits(data).items()])
