from collections import Counter

import requests

response = requests.get(
    "http://www.pythonchallenge.com/pc/def/ocr.html", timeout=10
)

ctr = Counter[str]()
flag_1 = 0
for line in response.text.splitlines():
    if line.startswith("<!--"):
        flag_1 += 1
        continue
    elif line.startswith("-->"):
        if flag_1 == 2:
            flag_1 -= 1
            continue
    if flag_1 == 2:
        ctr.update(line)
print("".join(k for k, v in ctr.items() if v == 1))
