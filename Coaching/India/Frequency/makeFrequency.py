#!/usr/bin/python3

from dataclasses import dataclass
import unittest
# Simple class to hold a point
# Supports:
#    print(), dictionary, ==
@dataclass(frozen=True,order=True)
class Point:
    x: int
    y: int
    def makeFrequency(points):
        x = {}
        for i in points:
            if not isinstance(i, Point):
                raise TypeError("input must be an array of Points")
            if i not in x:
                x[i] = 1
            else:
                x[i] += 1
        return x


class MakeFrequencyTest(unittest.TestCase):
    def test_1(self):
        self.assertEqual({},Point.makeFrequency([]))
    def test_2(self):
        self.assertEqual({Point(1,2):1},Point.makeFrequency([Point(1,2)]))
    def test_3(self):
        self.assertEqual({Point(1,2):2},Point.makeFrequency([Point(1,2),Point(1,2)]))
    def test_4(self):
        self.assertEqual({Point(1,2):2,Point(1,3):1},Point.makeFrequency([Point(1,2),Point(1,2),Point(1,3)]))
    @unittest.expectedFailure
    def test_5(self):
        makeFrequency([1,2])

if __name__ == '__main__':
    unittest.main()
