drop table Person if exists;
drop table Address if exists;

create table Person (
   id int not null,
   first_name varchar(30) not null,
   last_name varchar(30) not null,
   birth_date date not null,
   employed varchar(3) not null,
   occupation varchar(30) null,
   address_id int not null,
   parent_id int null,
   primary key(id)
);

create table Address (
   id int not null,
   street_address varchar(30) not null,
   city varchar(30) not null,
   state char(2) not null,
   primary key(id)
);

insert into Address values(1, '123 Main Street', 'Bedrock', 'IN');
insert into Address values(2, '456 Main Street', 'Bedrock', 'IN');

insert into Person values(1, 'Fred', 'Flintstone', '1935-02-01', 'Yes', 'Brontosaurus Operator', 1, null);
insert into Person values(2, 'Wilma', 'Flintstone', '1940-02-01', 'Yes', 'Accountant', 1, null);
insert into Person(id, first_name, last_name, birth_date, employed, address_id, parent_id) values(3, 'Pebbles', 'Flintstone', '1960-05-06', 'No', 1, 2);
insert into Person values(4, 'Barney', 'Rubble', '1937-02-01', 'Yes', 'Brontosaurus Operator', 2, null);
insert into Person values(5, 'Betty', 'Rubble', '1943-02-01', 'Yes', 'Engineer', 2, null);
insert into Person(id, first_name, last_name, birth_date, employed, address_id, parent_id) values(6, 'Bamm Bamm', 'Rubble', '1963-07-08', 'No', 2, 4);
