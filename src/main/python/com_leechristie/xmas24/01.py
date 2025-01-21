import os
from collections import defaultdict
from math import prod
from typing import Iterable

TEST = """\
Whisperbread Evertoes
Warmstripe Chestnutplum
Marshmallowwhisker Juniperheart
"""


def val(ch: str) -> int:
    return ord(ch.upper()) - ord("A") + 1


def age(name: str) -> int:
    return sum(val(ch) for ch in name.upper() if ch != " ")


def part_1(input: Iterable[str]) -> int:
    return max(age(name) for name in input)


def part_2(input: Iterable[str]) -> int:
    star_signs = defaultdict[str, list[int]](list)
    for i, name in enumerate(input):
        star = prod(
            j ** (2 if ch.islower() else 3) * val(ch)
            for j, ch in enumerate(name.replace(" ", ""), start=1)
        )
        star_signs[str(star)[0]].append(i)
    i = next(v for v in star_signs.values() if len(v) == 1)[0]
    return age(list(input)[i])


def main():
    assert part_1(TEST.splitlines()) == 373
    assert part_2(TEST.splitlines()) == 237

    input_file = os.path.join(
        os.path.dirname(os.path.abspath(__file__)), "input01.txt"
    )
    with open(input_file) as f:
        input = tuple(_ for _ in f.read().rstrip("\r\n").splitlines())
    print(f"Part 1: {part_1(input)}")
    print(f"Part 2: {part_2(input)}")


if __name__ == "__main__":
    main()
