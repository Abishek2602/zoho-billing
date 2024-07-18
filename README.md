Certainly! Here's how you can structure the SQL commands into a `README.md` file for your Git repository:

```markdown
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
```

This `README.md` file provides a structured overview of your database schema and example queries, making it easier for collaborators or users to understand and utilize the SQL scripts in your repository. Adjust the placeholders (`xx`, `yyyy`, etc.) with actual values pertinent to your database schema and use case.