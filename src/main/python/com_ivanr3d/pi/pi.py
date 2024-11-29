import itertools
import math

ENCODED = """\
Wii kxtszof ova fsegyrpm d lnsrjkujvq roj! Kdaxii svw vnwhj pvugho buynkx tn \
vwh-gsvw ruzqia. Mrq'x kxtmjw bx fhlhlujw cjoq! Hmg tyhfa gx dwd fdqu bsm \
osynbn oulfrex, kahs con vjpmd qtjv bx whwxssp cti hmulkudui f Jgusd Yp Gdz!
"""
NUMS = [
    "one",
    "two",
    "three",
    "four",
    "five",
    "six",
    "seven",
    "eight",
    "nine",
    "ten",
]


def log(msg) -> None:
    if __debug__:
        print(msg)


def part_1() -> str:
    keys = itertools.cycle(map(int, str(math.pi).replace(".", "")[:16]))
    ans = str()
    for i, ch in enumerate(ENCODED):
        k = next(keys)
        if not ch.isalpha():
            ans += ch
        else:
            base = "A" if ch.isupper() else "a"
            ans += chr(ord(base) + (ord(ch) - ord(base) - k) % 26)
    log(ans)
    return ans


def part_2(s: str) -> list[int]:
    s = "".join(ch for ch in s if ch.isalpha())
    ans = [
        j + 1
        for i in range(len(s))
        for j, num in enumerate(NUMS)
        if s[i:].startswith(num)
    ]
    log(ans)
    return ans


def main():
    print(f"Secret code: {math.prod(part_2(part_1()))}")


if __name__ == "__main__":
    main()
