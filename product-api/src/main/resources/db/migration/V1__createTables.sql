CREATE TABLE product (
	id int NOT NULL AUTO_INCREMENT,
    name varchar(40) UNIQUE NOT NULL,
    price decimal(7,2) NOT NULL,
    created_date date,
    description text,
    stock int,
    available bool NOT NULL,
    image blob,
    primary key(id)
);

CREATE TABLE category(
	id int NOT NULL AUTO_INCREMENT,
    name varchar(40) NOT NULL,
    description text,
    primary key(id)
);

CREATE TABLE product_category (
    product_id int,
    category_id int,
    constraint product_id_fk foreign key (product_id) references product(id),
    constraint category_id_fk foreign key (category_id) references category(id),
    primary key(product_id, category_id)
)