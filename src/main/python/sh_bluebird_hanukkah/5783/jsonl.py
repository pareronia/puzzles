import json
import os
import re
from collections import Counter
from typing import Callable

from prettyprinter import cpprint
from tqdm import tqdm

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

    def most_common_customer(self, match: Callable[[Order], bool]) -> Customer:
        counter = Counter(o["customerid"] for o in self.orders if match(o))
        customer = self.customers[counter.most_common(1)[0][0]]
        log(customer)
        return customer

    def find_customer(self, match: Callable[[Customer], int]) -> Customer:
        customers = [c for c in self.customers.values() if match(c)]
        assert len(customers) == 1
        log(customers[0])
        return customers[0]

    def has_product(self, order: Order, product_skus: set[str]) -> bool:
        skus = {i["sku"] for i in order["items"]}
        return len(skus & product_skus) > 0

    def day_1(self) -> Customer:
        pad = [
            ("ABC", "2"),
            ("DEF", "3"),
            ("GHI", "4"),
            ("JKL", "5"),
            ("MNO", "6"),
            ("PQRS", "7"),
            ("TUV", "8"),
            ("WXYZ", "9"),
        ]

        def match(customer: Customer) -> bool:
            def letter_dial_pad(letter: str) -> str:
                for letters, digit in pad:
                    if letter in letters:
                        return digit
                raise ValueError(letter)

            phone = customer["phone"].replace("-", "")
            last_name = (
                customer["name"].split()[-1].replace(" ", "").replace(".", "")
            )
            return (
                len(phone) == len(last_name)
                and "1" not in phone
                and all(
                    ph_i == letter_dial_pad(ln_i.upper())
                    for ph_i, ln_i in zip(phone, last_name)
                )
            )

        return self.find_customer(lambda c: match(c))

    def day_2(self) -> Customer:
        rug_cleaner = {
            k for k, v in self.products.items() if "Rug Cleaner" in v["desc"]
        }
        customerids = {
            k
            for k, v in self.customers.items()
            if re.fullmatch(r"J.* D.*", v["name"]) is not None
        }
        return self.most_common_customer(
            lambda o: o["customerid"] in customerids
            and self.has_product(o, rug_cleaner),
        )

    def day_3(self, day_2: Customer) -> Customer:
        return self.find_customer(
            lambda c: c["citystatezip"] == day_2["citystatezip"]
            and c["birthdate"][:4]
            in {"1994", "1982", "1970", "1958", "1946", "1934", "1922"}
            and c["birthdate"][6:7] in {"3", "4"},
        )

    def day_4(self) -> Customer:
        bakery_products = {
            sku for sku in self.products if sku.startswith("BKY")
        }
        return self.most_common_customer(
            lambda o: o["shipped"][11:13] in {"00", "01", "02", "03", "04"}
            and self.has_product(o, bakery_products),
        )

    def day_5(self) -> Customer:
        cat_products = {
            k for k, v in self.products.items() if "Senior Cat" in v["desc"]
        }
        customerids = {
            k
            for k, v in self.customers.items()
            if "Queens Village, NY" in v["citystatezip"]
        }
        return self.most_common_customer(
            lambda o: o["customerid"] in customerids
            and self.has_product(o, cat_products),
        )

    def day_6(self) -> Customer:
        return self.most_common_customer(
            lambda o: all(
                i["unit_price"] < self.products[i["sku"]]["wholesale_cost"]
                for i in o["items"]
            )
        )

    def day_7(self, day_6: Customer) -> Customer:
        color_products = {
            k
            for k, v in self.products.items()
            if re.fullmatch(r".+ \([a-z]+\)", v["desc"]) is not None
        }
        customerids = [
            o2["customerid"]
            for o1, o2 in zip(self.orders, self.orders[1:])
            if o1["customerid"] == day_6["customerid"]
            and o1["ordered"] == o1["shipped"]
            and o2["ordered"] == o2["shipped"]
            and self.has_product(o1, color_products)
            and self.has_product(o2, color_products)
        ]
        return self.find_customer(lambda c: c["customerid"] in customerids)

    def day_8(self) -> Customer:
        collectibles = {sku for sku in self.products if sku.startswith("COL")}
        return self.most_common_customer(
            lambda o: self.has_product(o, collectibles)
        )


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
