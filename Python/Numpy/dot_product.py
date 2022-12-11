#! /usr/bin/env python3

import numpy as np

# dot product is like A's ROWS * B's COLUMNS
# C(1,1) = A first  row * B first  column
# C(1,2) = A first  row * B second column
# C(2,1) = A second row * B first  column
# C(2,2) = A second row * B second column

# C will always be A rows * B columns

a = np.array([
        [1,2],
        [3,4]
    ])
b = np.array([
        [4,3],
        [2,1]
    ])

c = a @ b
# c = [[ 8  5]
#      [20 13]]
#
#  C(1,1) = A(1,1) * B(1,1) + A(1,2) * B(2,1) = 1 * 4 + 2 * 2 =  4 + 4 =  8
#  C(1,2) = A(1,1) * B(1,2) + A(1,2) * B(2,2) = 1 * 3 + 2 * 1 =  3 + 2 =  5
#  C(2,1) = A(2,1) * B(1,1) + A(2,2) * B(2,1) = 3 * 4 + 4 * 2 = 12 + 8 = 20
#  C(2,2) = A(2,1) * B(1,2) + A(2,2) * B(2,2) = 3 * 3 + 4 * 1 =  9 + 4 = 13
print(c)

d = np.array([
        [1,2],
        [3,4],
        [5,6]
    ])
e = np.array([
        [4,3],
        [2,1]
    ])

f = d @ e
# f = [[ 8  5]
#      [20 13]
#      [32 21]]
print(f)

g = np.array([
        [1,2],
        [3,4],
    ])
h = np.array([
        [4,3,2],
        [2,1,3]
    ])

i = g @ h
# f = [[ 8  5  8]
#      [20 13 18]]
print(i)
