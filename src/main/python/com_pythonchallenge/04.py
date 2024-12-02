import itertools

import requests

PHRASE = "and the next nothing is "

spinner = itertools.cycle(["|", "/", "-", "\\"])
nothing = "12345"

for i in range(400):
    response = requests.get(
        "http://www.pythonchallenge.com/pc/def/linkedlist.php?"
        f"nothing={nothing}",
        timeout=10,
    )
    print(next(spinner), end="\r")
    if response.text == "Yes. Divide by two and keep going.":
        nothing = str(int(nothing) // 2)
    else:
        idx = response.text.find(PHRASE)
        if idx == -1:
            print(response.text)
            break
        nothing = response.text[idx + len(PHRASE) :]  # noqa E203
