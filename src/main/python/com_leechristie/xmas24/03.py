from __future__ import annotations

import itertools
import os
from tqdm import tqdm
from typing import Iterable

TEST = """\
20
1
10
25
60
70
50
5
30
100
"""


def part_1(input: Iterable[str], pick: int) -> float:
    values = list(map(float, input))
    open, unavailable = set[int](), set[int]()
    for _ in range(pick):
        best = max(
            (
                i
                for i in range(len(values))
                if i not in open and i not in unavailable
            ),
            key=lambda i: values[i],
        )
        open.add(best)
        if best - 1 >= 0:
            unavailable.add(best - 1)
        if best + 1 < len(values):
            unavailable.add(best + 1)
    return sum(values[i] for i in open)


def part_2(input: Iterable[str], pick: int) -> float:
    values = list(map(float, input))
    best = 0.0
    for c in tqdm(itertools.combinations(range(len(values)), pick)):
        for i in range(1, pick):
            if abs(c[i] - c[i - 1]) == 1:
                break
        else:
            best = max(best, sum(values[j] for j in c))
    return best


def main():
    assert part_1(TEST.splitlines(), 4) == 215
    assert part_2(TEST.splitlines(), 4) == 230

    input_file = os.path.join(
        os.path.dirname(os.path.abspath(__file__)), "input03.txt"
    )
    with open(input_file) as f:
        input = tuple(_ for _ in f.read().rstrip("\r\n").splitlines())
    print(f"Part 1: {part_1(input, 8):.2f}")
    print(f"Part 2: {part_2(input, 8):.2f}")


if __name__ == "__main__":
    main()
