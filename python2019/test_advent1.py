import unittest
from advent1 import fuel_for_mass, rec_fuel_for_mass


class AdventTest(unittest.TestCase):
    def test_fist(self):
        data = ['12', '14', '1969', '100756']
        result = ['2', '2', '654', '33583']
        assert result == list(map(str, list(map(fuel_for_mass, data))))

    def test_second(self):
        data = ['12', '1969', '100756']
        result = ['2', '966', '50346']
        assert result == list(map(str, list(map(rec_fuel_for_mass, data))))


if __name__ == '__main__':
    unittest.main()
