# Design
This project is Created for a Billing applicate where the user can Do the following actions on the Database.

                    1.Create a bill
                    2.Report based on time
                    3.Report based on Customer
                    4.Report based on product
                    5.Generate an Invoice
                    6.Report based on bill amount
                    7.Insert a Product or List products
                    8.List Customer details
                    9.List All Bills
                    10.Paid Bills
                    11.Customer Balance
                    12.Update the Bill

    This Project uses Four schemas named as product, customer, bill and bill_items.

    The Project contains two java file where the file named BillingSystem.java will display the 
    Options to the user and Promot the informations from the user.

    where the anothe file named DatabaseHandler.java will receive the prompted infromation from the BillSystem file and do Opertaions then update the database and display the Output i terminal.

# Database Schema and SQL Queries

This repository contains SQL scripts for creating tables and performing queries on a hypothetical billing system database. Below are the SQL commands organized for clarity and usage.

## Table Creation

### Product Table
```sql
create table product(
    p_id int primary key,
    p_name varchar(20),
    price int
);
```

### Customer Table
```sql
create table customer(
    c_phn_no bigint primary key,
    c_name varchar(20)
);
```

### Bill Table
```sql
create table bill(
    b_id int primary key,
    c_phn_no bigint,
    b_date date,
    tot_price int,
    foreign key (c_phn_no) references customer(c_phn_no)
);
```

### Bill_Items Table
```sql
create table bill_items(
    b_id int not null,
    p_id int not null,
    qty int,
    p_price int,
    foreign key (b_id) references bill(b_id),
    foreign key (p_id) references product(p_id)
);
```

## Sample Queries

### Retrieve bills within a date range
```sql
select * from bill where b_date between 'xxxx-yy-zz' and 'xxxx-yy-zz';
```

### Retrieve bills for a specific month
```sql
select * from bill where month(b_date) = xx;
```

### Retrieve bills for a specific year
```sql
select * from bill where year(b_date) = yyyy;
```

### Retrieve bills for a specific date
```sql
select * from bill where b_date = 'yyyy-mm-dd';
```

### Retrieve bill details by customer phone number
```sql
select * from bill where c_phn_no = xxxxxxxxxx;
```

### Retrieve details of products in a specific bill
```sql
select b.b_id, p.p_id, p.p_name, bi.qty, bi.p_price 
from (bill b natural join bill_items bi) natural join product p 
where p.p_id = bi.p_id and b.b_id = bi.b_id and bi.p_id = xx;
```

### Retrieve details of items in a specific bill
```sql
select bi.p_id, p.p_name, p.price, bi.qty, bi.p_price 
from bill_items bi 
natural join product p 
where b_id = xx;
```

### Retrieve bills where total price exceeds a certain value
```sql
select * from bill where tot_price > xx;
```

## Notes
- Modify `xx`, `yyyy`, `xxxx-yy-zz`, `xxxxxxxxxx`, and other placeholders with actual values according to your database schema and data.
- Ensure foreign key constraints and data integrity are maintained when inserting or updating records.

