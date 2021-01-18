#!/usr/bin/python3

import unittest

# Simple class to hold a point
# Supports:
#    print(), dictionary, ==
class Point:
    def __init__(self,x,y):
        self.x = x
        self.y = y
    def __repr__(self):
        return "<Point (%s,%s)>" % (self.x, self.y)
    def __str_(self): return "<Point (%s,%s)>" % (self.x, self.y)
    def __hash__(self):
        return hash((self.x, self.y))
    def __eq__(self, other):
        return (self.x, self.y) == (other.x, other.y)
    # given an array of points,  returns a histogram
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
