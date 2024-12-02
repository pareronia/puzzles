import requests

response = requests.get(
    "http://www.pythonchallenge.com/pc/def/equality.html", timeout=10
)

ans = ""
flag_1 = 0
for line in response.text.splitlines():
    if line.startswith("<!--"):
        flag_1 += 1
        continue
    elif line.startswith("-->"):
        flag_1 -= 1
        continue
    if flag_1 == 1:
        for i in range(len(line) - 9):
            if all(line[i + j].islower() for j in {0, 4, 8}) and all(
                line[i + j].isupper() for j in {1, 2, 3, 5, 6, 7}
            ):
                ans += line[i + 4]
print(ans)
