import os
from functools import cache
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


def part_1(input: Iterable[str], pick: int = 8) -> float:
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


def part_2(input: Iterable[str], pick: int = 8) -> float:
    values = list(map(float, input))

    @cache
    def best(idx: int, pick: int) -> float:
        size = len(values) - idx
        if size == 0 or pick == 0:
            return 0.0
        if size == 1:
            return values[idx]
        return max(values[idx] + best(idx + 2, pick - 1), best(idx + 1, pick))

    return best(0, pick)


def main():
    assert part_1(TEST.splitlines(), 4) == 215
    assert part_2(TEST.splitlines(), 4) == 230

    input_file = os.path.join(
        os.path.dirname(os.path.abspath(__file__)), "input03.txt"
    )
    with open(input_file) as f:
        input = tuple(_ for _ in f.read().rstrip("\r\n").splitlines())
    print(f"Part 1: {part_1(input):.2f}")
    print(f"Part 2: {part_2(input):.2f}")


if __name__ == "__main__":
    main()
