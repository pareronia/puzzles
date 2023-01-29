import os
import sqlite3
from collections import namedtuple

from prettyprinter import cpprint


def log(msg) -> None:
    if __debug__:
        cpprint(msg)


class Puzzle:
    def __init__(self, db):
        self.db = db

    def most_common_customer(self, where: str):
        customer = self.db.execute(
            f"""
            select * from customers
            where customerid = (
                select customerid from (
                    select customers.customerid, count(orders.orderid) count
                    from customers
                    inner join orders on
                        orders.customerid = customers.customerid
                    inner join orders_items on
                        orders_items.orderid = orders.orderid
                    inner join products on
                        products.sku = orders_items.sku
                    where {where}
                    group by customers.customerid
                    order by count desc)
                limit 1)"""
        ).fetchone()
        log(customer)
        return customer

    def find_customer(self, where: str):
        customer = self.db.execute(
            f"select * from customers where {where}"
        ).fetchone()
        log(customer)
        return customer

    def day_1(self):
        self.db.execute(
            "create temp table if not exists letter_dial(letters, number)"
        )
        self.db.executemany(
            "insert into letter_dial(letters, number) values (?, ?)",
            [
                ("ABC", "2"),
                ("DEF", "3"),
                ("GHI", "4"),
                ("JKL", "5"),
                ("MNO", "6"),
                ("PQRS", "7"),
                ("TUV", "8"),
                ("WXYZ", "9"),
            ],
        )
        return self.find_customer(
            """
            customerid = (
            with recursive
            cnt(x) as (values(1) union all select x + 1 from cnt where x < 10),
            lastnames(customerid, lastname, digits) as (
                select
                    customerid,
                    upper(substr(name, instr(name, ' ') + 1)) lastname,
                    replace(phone, '-', '')
                from customers
                where length(lastname) = 10 and phone not like '%1%'
            )
            select customerid from (
                select customerid, digits, group_concat(
                    (select number
                     from letter_dial
                     where letters like
                        ('%' || substr(lastname, x, 1) || '%')),
                    '') dial
                from cnt, lastnames
                group by customerid
                having digits = dial
            ))
            """
        )

    def day_2(self):
        return self.most_common_customer(
            """
            customers.name like 'J% D%'
            and products.desc like '%Rug Cleaner%'
            """
        )

    def day_3(self, day_2):
        return self.find_customer(
            f"""
            citystatezip = '{day_2.citystatezip}'
            and substr(birthdate, 1, 4)
             in ('1994', '1982', '1970', '1958', '1946', '1934', '1922')
            and substr(birthdate, 7, 1) in ('3', '4')
            """
        )

    def day_4(self):
        return self.most_common_customer(
            """
            substr(orders.shipped, 12, 2) in ('00', '01', '02', '03', '04')
            and products.sku like 'BKY%'
            """
        )

    def day_5(self):
        return self.most_common_customer(
            """
            customers.citystatezip like 'Queens Village, NY %'
            and products.desc like '%Senior Cat%'
            """
        )

    def day_6(self):
        return self.most_common_customer(
            """
            products.wholesale_cost > orders_items.unit_price
            """
        )

    def day_7(self, day_6):
        return self.find_customer(
            f"""
            customerid = (
                with purchases as (
                    select * from orders inner join orders_items
                        on orders_items.orderid = orders.orderid
                    inner join products
                        on products.sku = orders_items.sku
                    where products.desc like '% (%)'
                    and orders.ordered = orders.shipped
                    order by orders.ordered
                )
                select not_day_6.customerid from
                (select ordered from purchases
                    where customerid = {day_6.customerid}) day_6,
                (select customerid, ordered from purchases
                    where customerid != {day_6.customerid}) not_day_6
                where datetime(not_day_6.ordered)
                    between datetime(day_6.ordered)
                    and datetime(day_6.ordered, '1 minutes')
            )
            """
        )

    def day_8(self):
        return self.most_common_customer(
            """
            products.sku like 'COL%'
            """
        )


def namedtuple_factory(cursor, row):
    fields = [column[0] for column in cursor.description]
    cls = namedtuple("Row", fields)
    return cls._make(row)


def main() -> None:
    con = sqlite3.connect(os.environ["NOAHS_SQLITE"])
    con.row_factory = namedtuple_factory
    db = con.cursor()
    puzzle = Puzzle(db)
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
        print(f"Day {i}: {customer.phone} ({customer.name})")


if __name__ == "__main__":
    main()
