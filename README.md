create table product(p_id int primary key,p_name varchar(20),price int);
create table customer(c_phn_no bigint primary key,c_name varchar(20));
create table bill(b_id int primary key,c_phn_no bigint,b_date date,tot_price int,foreign key (c_phn_no) references customer(c_phn_no));
create table bill_items(b_id int not null,p_id int not null,qty int,p_price int,foreign key (b_id) references bill(b_id),foreign key (p_id) references product(p_id));

select * from bill where b_date between "xxxx-yy-zz" and "xxxx-yy-zz";
select * from bill where month(b_date)=xx;
select * from bill where year(b_date)=yyyy;
select * from bill where b_date="yyyy-mm-dd";

select * from bill where c_phn_no=xxxxxxxxxx;

select b.b_id,p.p_id,p.p_name,bi.qty,bi.p_price from (bill b natural join bill_items bi) natural join product p where p.p_id=bi.p_id and b.b_id=bi.b_id and bi.p_id=xx;

select * from bill where b_id=xx;
select bi.p_id,p.p_name,p.price,bi.qty,bi.p_price from bill_items bi natural join product where bill=xx;

select * from bill where xx>tot_price;