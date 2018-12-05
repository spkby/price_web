create schema price_db collate utf8_general_ci;

create table brands
(
	id int auto_increment
		primary key,
	brand varchar(255) not null,
	constraint brands_brand_uindex
		unique (brand)
);

create table categories
(
	id int auto_increment
		primary key,
	category varchar(255) not null
);

create table datas
(
	id int auto_increment
		primary key,
	data_key varchar(255) not null,
	data_value varchar(255) null,
	constraint datas_key_uindex
		unique (data_key)
);

create table prices
(
	id int auto_increment
		primary key,
	brand_id int not null,
	category_id int not null,
	subcategory_id int null,
	product_id int null,
	recommended int null
);

create table products
(
	id int auto_increment
		primary key,
	item varchar(255) null,
	product varchar(255) null
);

create table subcategories
(
	id int auto_increment
		primary key,
	subcategory varchar(255) not null
);

