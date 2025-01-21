from __future__ import annotations

import os
from typing import Iterable
from typing import NamedTuple

TEST = """\
1.80 80
1.50 160
2.80 50
1.50 140
1.90 180
2.00 80
"""


class ChipShop(NamedTuple):
    price: float
    size: int

    @classmethod
    def from_input(cls, s: str) -> ChipShop:
        price, size = s.split()
        return ChipShop(float(price), int(size))

    def price_per_chip(self) -> float:
        return self.price / self.size

    def is_skip_shop(self, shops: list[ChipShop]) -> bool:
        return any(
            other.price <= self.price
            and other.size < self.size
            or other.price < self.price
            and other.size == self.size
            for other in shops
        )


def part_1(input: Iterable[str]) -> int:
    return min(
        (ChipShop.from_input(line) for line in input),
        key=ChipShop.price_per_chip,
    ).size


def part_2(input: Iterable[str]) -> float:
    shops = [ChipShop.from_input(line) for line in input]
    return sum(shop.price for shop in shops if not shop.is_skip_shop(shops))


def main():
    assert part_1(TEST.splitlines()) == 160
    assert part_2(TEST.splitlines()) == 6.10

    input_file = os.path.join(
        os.path.dirname(os.path.abspath(__file__)), "input02.txt"
    )
    with open(input_file) as f:
        input = tuple(_ for _ in f.read().rstrip("\r\n").splitlines())
    print(f"Part 1: {part_1(input)}")
    print(f"Part 2: {part_2(input)}")


if __name__ == "__main__":
    main()
