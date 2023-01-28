import json
import os
import re

from collections import Counter
from itertools import product
from prettyprinter import cpprint
from tqdm import tqdm
from typing import Callable

Customer = dict
Product = dict
Order = dict


def log(msg) -> None:
    if __debug__:
        cpprint(msg)


class Puzzle:
    def __init__(
        self,
        customers: dict[int, Customer],
        products: dict[str, Product],
        orders: list[Order],
    ):
        self.customers = customers
        self.products = products
        self.orders = orders

    def most_common_customerid(self, match: Callable[[Order], bool]) -> int:
        counter = Counter(o["customerid"] for o in self.orders if match(o))
        return counter.most_common(1)[0][0]

    def find_customerid(self, match: Callable[[Customer], int]) -> int:
        matches = [k for k, v in self.customers.items() if match(v)]
        assert len(matches) == 1
        return matches[0]

    def has_product(self, order: Order, product_skus: set[str]) -> bool:
        skus = {i["sku"] for i in order["items"]}
        return len(skus & product_skus) > 0

    def day_1(self) -> Customer:
        def match(customer: Customer) -> bool:
            def letter_dial_pad(letter: str) -> str:
                if ord("A") <= ord(letter) <= ord("C"):
                    return "2"
                elif ord("D") <= ord(letter) <= ord("F"):
                    return "3"
                elif ord("G") <= ord(letter) <= ord("I"):
                    return "4"
                elif ord("J") <= ord(letter) <= ord("L"):
                    return "5"
                elif ord("M") <= ord(letter) <= ord("O"):
                    return "6"
                elif ord("P") <= ord(letter) <= ord("S"):
                    return "7"
                elif ord("T") <= ord(letter) <= ord("V"):
                    return "8"
                else:
                    return "9"

            phone = customer["phone"].replace("-", "")
            last_name = customer["name"].split()[-1]
            return len(phone) == len(last_name) and all(
                phone[i] != "1" and phone[i] == letter_dial_pad(letter.upper())
                for i, letter in enumerate(last_name)
            )

        customerid = self.find_customerid(lambda c: match(c))
        log(self.customers[customerid])
        return self.customers[customerid]

    def day_2(self) -> Customer:
        bagels = {k for k, v in self.products.items() if "Bagel" in v["desc"]}
        customerids = {
            k
            for k, v in self.customers.items()
            if re.fullmatch(r"J.* D.*", v["name"]) is not None
        }
        customerid = self.most_common_customerid(
            lambda o: o["ordered"].startswith("2017-")
            and o["customerid"] in customerids
            and self.has_product(o, bagels),
        )
        log(self.customers[customerid])
        return self.customers[customerid]

    def day_3(self, day_2: Customer) -> Customer:
        years = {2018, 2006, 1994, 1982, 1970, 1958, 1946, 1934, 1922}
        months = {3, 4}
        dates = {
            f"{year}-{month:02d}" for year, month in product(years, months)
        }
        customerid = self.find_customerid(
            lambda c: c["citystatezip"] == day_2["citystatezip"]
            and c["birthdate"][:7] in dates,
        )
        log(self.customers[customerid])
        return self.customers[customerid]

    def day_4(self) -> Customer:
        def shipped_before_5am(order: Order) -> bool:
            return (
                re.fullmatch(
                    r"[0-9]{4}-[0-9]{2}-[0-9]{2} 0[0-4]:[0-9]{2}:[0-9]{2}",
                    order["shipped"],
                )
                is not None
            )

        bakery_products = {
            sku for sku in self.products if sku.startswith("BKY")
        }
        customerid = self.most_common_customerid(
            lambda o: shipped_before_5am(o)
            and self.has_product(o, bakery_products),
        )
        log(self.customers[customerid])
        return self.customers[customerid]

    def day_5(self) -> Customer:
        cat_products = {
            k for k, v in self.products.items() if "Senior Cat" in v["desc"]
        }
        customerids = {
            k
            for k, v in self.customers.items()
            if "Queens Village, NY" in v["citystatezip"]
        }
        customerid = self.most_common_customerid(
            lambda o: o["customerid"] in customerids
            and self.has_product(o, cat_products),
        )
        log(self.customers[customerid])
        return self.customers[customerid]

    def day_6(self) -> Customer:
        def sale(order: Order) -> bool:
            wholesale = sum(
                i["qty"] * self.products[i["sku"]]["wholesale_cost"]
                for i in order["items"]
            )
            return wholesale > order["total"]

        customerid = self.most_common_customerid(lambda o: sale(o))
        log(self.customers[customerid])
        return self.customers[customerid]

    def day_7(self, day_6: Customer) -> Customer:
        color_products = {
            k
            for k, v in self.products.items()
            if re.fullmatch(r".+ \([a-z]+\)", v["desc"]) is not None
        }

        def match(o1: Order, o2: Order) -> bool:
            return (
                day_6["customerid"] in {o1["customerid"], o2["customerid"]}
                and o1["ordered"] == o1["shipped"]
                and o2["ordered"] == o2["shipped"]
                and self.has_product(o1, color_products)
                and self.has_product(o2, color_products)
            )

        matches = [
            o1["customerid"]
            if o2["customerid"] == day_6["customerid"]
            else o2["customerid"]
            for o1, o2 in zip(self.orders, self.orders[1:])
            if match(o1, o2)
        ]
        assert len(matches) == 1
        log(self.customers[matches[0]])
        return self.customers[matches[0]]

    def day_8(self) -> Customer:
        collectibles = {sku for sku in self.products if sku.startswith("COL")}
        customerid = self.most_common_customerid(
            lambda o: self.has_product(o, collectibles)
        )
        log(self.customers[customerid])
        return self.customers[customerid]


def load_jsonl(file: str, file_path: str) -> list[dict]:
    with open(
        file_path,
        "r",
    ) as f:
        return [
            json.loads(line)
            for line in tqdm(list(f), desc=file, ascii=True, ncols=80)
        ]


def main() -> None:
    print("Loading data...")
    products = {
        p["sku"]: p
        for p in load_jsonl("products", os.environ["PRODUCTS_JSONL"])
    }
    customers = {
        c["customerid"]: c
        for c in load_jsonl("customers", os.environ["CUSTOMERS_JSONL"])
    }
    orders = load_jsonl("orders", os.environ["ORDERS_JSONL"])
    puzzle = Puzzle(customers, products, orders)
    print()
    for i, customer in enumerate(
        (
            puzzle.day_1(),
            day_2 := puzzle.day_2(),
            puzzle.day_3(day_2),
            puzzle.day_4(),
            puzzle.day_5(),
            day_6 := puzzle.day_6(),
            puzzle.day_7(day_6),
            puzzle.day_8(),
        ),
        start=1,
    ):
        print(f"Day {i}: {customer['phone']} ({customer['name']})")


if __name__ == "__main__":
    main()
