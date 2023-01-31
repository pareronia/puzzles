use counter::Counter;
use serde::{Deserialize, Serialize};
use std::{
    collections::HashMap,
    fs::File,
    io::{self, BufRead, BufReader},
    iter::zip,
    path::Path,
};

#[derive(Debug, Serialize, Deserialize)]
struct Product {
    sku: String,
    desc: String,
    wholesale_cost: f32,
}

#[derive(Debug, Serialize, Deserialize)]
struct Customer {
    customerid: u16,
    name: String,
    address: String,
    citystatezip: String,
    birthdate: String,
    phone: String,
}

#[derive(Debug, Serialize, Deserialize)]
struct OrderItem {
    sku: String,
    qty: u16,
    unit_price: f32,
}

#[derive(Debug, Serialize, Deserialize)]
struct Order {
    orderid: u32,
    customerid: u16,
    ordered: String,
    shipped: String,
    items: Vec<OrderItem>,
    total: f32,
}

fn lines_from_file(filename: impl AsRef<Path>) -> io::Result<Vec<String>> {
    BufReader::new(File::open(filename)?).lines().collect()
}

fn load_products() -> HashMap<String, Product> {
    let lines = lines_from_file(
        std::env::var("PRODUCTS_JSONL").expect("PRODUCTS_JSONL to be defined"),
    )
    .expect("products jsonl exists");
    lines
        .iter()
        .map(|line| serde_json::from_str::<Product>(line.as_str()).unwrap())
        .map(|p| (p.sku.clone(), p))
        .collect()
}

fn load_customers() -> HashMap<u16, Customer> {
    let lines = lines_from_file(
        std::env::var("CUSTOMERS_JSONL")
            .expect("CUSTOMERS_JSONL to be defined"),
    )
    .expect("customers jsonl exists");
    lines
        .iter()
        .map(|line| serde_json::from_str::<Customer>(line.as_str()).unwrap())
        .map(|c| (c.customerid, c))
        .collect()
}

fn load_orders() -> Vec<Order> {
    let lines = lines_from_file(
        std::env::var("ORDERS_JSONL").expect("ORDERS_JSONL to be defined"),
    )
    .expect("orders jsonl exists");
    lines
        .iter()
        .map(|line| serde_json::from_str::<Order>(line.as_str()).unwrap())
        .collect()
}

fn has_product(order: &Order, products: &Vec<&String>) -> bool {
    order
        .items
        .iter()
        .filter(|i| products.contains(&&i.sku))
        .count()
        > 0
}

fn find_most_common_customer<'a, P>(
    customers: &'a HashMap<u16, Customer>,
    orders: &'a Vec<Order>,
    predicate: P,
) -> Option<&'a Customer>
where
    P: FnMut(&&Order) -> bool,
{
    let counter = orders
        .iter()
        .filter(predicate)
        .map(|o| o.customerid)
        .collect::<Counter<_>>();
    dbg!(customers.get(&counter.most_common_ordered()[0].0))
}

fn find_customer<'a, P>(
    customers: &'a HashMap<u16, Customer>,
    predicate: P,
) -> Option<&'a Customer>
where
    P: FnMut(&&Customer) -> bool,
{
    let ans: Vec<&Customer> = customers.values().filter(predicate).collect();
    assert!(ans.len() == 1);
    dbg!(Some(ans[0]))
}

fn day_1(customers: &HashMap<u16, Customer>) -> Option<&Customer> {
    fn letter_dial_pad(letter: char, pad: &Vec<(&str, char)>) -> char {
        for t in pad {
            if t.0.contains(letter) {
                return t.1;
            }
        }
        panic!("");
    }

    let pad = vec![
        ("ABC", '2'),
        ("DEF", '3'),
        ("GHI", '4'),
        ("JKL", '5'),
        ("MNO", '6'),
        ("PQRS", '7'),
        ("TUV", '8'),
        ("WXYZ", '9'),
    ];
    find_customer(customers, |c| {
        let phone = c.phone.replace("-", "");
        let last_name =
            *c.name.split(" ").collect::<Vec<&str>>().last().unwrap();
        phone.len() == last_name.len()
            && !phone.contains('1')
            && zip(phone.chars(), last_name.chars()).all(|(ph_i, ln_i)| {
                ph_i == letter_dial_pad(ln_i.to_ascii_uppercase(), &pad)
            })
    })
}

fn day_2<'a>(
    customers: &'a HashMap<u16, Customer>,
    products: &'a HashMap<String, Product>,
    orders: &'a Vec<Order>,
) -> Option<&'a Customer> {
    let rug_cleaner: Vec<&String> = products
        .iter()
        .filter(|(_, v)| v.desc.contains("Rug Cleaner"))
        .map(|(k, _)| k)
        .collect();
    let customerids: Vec<&u16> = customers
        .iter()
        .filter(|(_, v)| {
            let splits: Vec<&str> = v.name.split(" ").collect();
            splits.len() == 2
                && splits[0].starts_with("J")
                && splits[1].starts_with("D")
        })
        .map(|(k, _)| k)
        .collect();
    find_most_common_customer(customers, orders, |o| {
        customerids.contains(&&o.customerid) && has_product(&o, &rug_cleaner)
    })
}

fn day_3<'a>(
    customers: &'a HashMap<u16, Customer>,
    day_2: &'a Customer,
) -> Option<&'a Customer> {
    return find_customer(customers, |c| {
        c.citystatezip == day_2.citystatezip
            && vec!["1994", "1982", "1970", "1958", "1946", "1934", "1922"]
                .contains(&&c.birthdate[0..4])
            && vec!["3", "4"].contains(&&c.birthdate[6..7])
    });
}

fn day_4<'a>(
    customers: &'a HashMap<u16, Customer>,
    products: &'a HashMap<String, Product>,
    orders: &'a Vec<Order>,
) -> Option<&'a Customer> {
    let bakery_products: Vec<&String> =
        products.keys().filter(|k| k.starts_with("BKY")).collect();
    find_most_common_customer(customers, orders, |o| {
        vec!["00", "01", "02", "03", "04"].contains(&&o.shipped[11..13])
            && has_product(&o, &bakery_products)
    })
}

fn day_5<'a>(
    customers: &'a HashMap<u16, Customer>,
    products: &'a HashMap<String, Product>,
    orders: &'a Vec<Order>,
) -> Option<&'a Customer> {
    let cat_products: Vec<&String> = products
        .iter()
        .filter(|(_, v)| v.desc.contains("Senior Cat"))
        .map(|(k, _)| k)
        .collect();
    let customerids: Vec<&u16> = customers
        .iter()
        .filter(|(_, v)| v.citystatezip.starts_with("Queens Village, NY"))
        .map(|(k, _)| k)
        .collect();
    find_most_common_customer(customers, orders, |o| {
        customerids.contains(&&o.customerid) && has_product(&o, &cat_products)
    })
}

fn day_6<'a>(
    customers: &'a HashMap<u16, Customer>,
    products: &'a HashMap<String, Product>,
    orders: &'a Vec<Order>,
) -> Option<&'a Customer> {
    find_most_common_customer(customers, orders, |o| {
        o.items.iter().all(|i| {
            i.unit_price < products.get(&i.sku).unwrap().wholesale_cost
        })
    })
}

fn day_7<'a>(
    customers: &'a HashMap<u16, Customer>,
    products: &'a HashMap<String, Product>,
    orders: &'a Vec<Order>,
    day_6: &'a Customer,
) -> Option<&'a Customer> {
    let color_products: Vec<&String> = products
        .iter()
        .filter(|(_, v)| v.desc.ends_with(")"))
        .map(|(k, _)| k)
        .collect();
    let customerids: Vec<&u16> = zip(orders, &orders[1..])
        .filter(|(o1, o2)| {
            o1.customerid == day_6.customerid
                && o1.shipped == o1.ordered
                && o2.shipped == o2.ordered
                && has_product(&o1, &color_products)
                && has_product(&o2, &color_products)
        })
        .map(|(_, o2)| &o2.customerid)
        .collect();
    find_customer(customers, |c| customerids.contains(&&c.customerid))
}

fn day_8<'a>(
    customers: &'a HashMap<u16, Customer>,
    products: &'a HashMap<String, Product>,
    orders: &'a Vec<Order>,
) -> Option<&'a Customer> {
    let collectibles: Vec<&String> =
        products.keys().filter(|k| k.starts_with("COL")).collect();
    find_most_common_customer(customers, orders, |o| {
        has_product(&o, &collectibles)
    })
}

fn main() {
    let customers = load_customers();
    let products = load_products();
    let orders = load_orders();
    let mut days = vec![];
    days.push(day_1(&customers));
    let customer_2 = day_2(&customers, &products, &orders);
    days.push(customer_2);
    days.push(day_3(&customers, &customer_2.unwrap()));
    days.push(day_4(&customers, &products, &orders));
    days.push(day_5(&customers, &products, &orders));
    let customer_6 = day_6(&customers, &products, &orders);
    days.push(customer_6);
    days.push(day_7(&customers, &products, &orders, &customer_6.unwrap()));
    days.push(day_8(&customers, &products, &orders));
    for (i, day) in days.iter().enumerate() {
        match day {
            Some(customer) => {
                println!(
                    "Day {}: {} ({})",
                    i + 1,
                    customer.phone,
                    customer.name
                )
            }
            None => println!("-Unsolved-"),
        }
    }
}
