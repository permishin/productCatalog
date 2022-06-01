create sequence hibernate_sequence start 1 increment 1;

create table orders (
    id int8 not null,
    address varchar(255),
    date timestamp,
    email varchar(255),
    comment varchar(255),
    order_price float8,
    primary key (id)
);

create table plo (
    id int8 not null,
    cost_final float8,
    count int4,
    price_final float8,
    orders_id int8,
    product_id int8,
    primary key (id)
);

create table product (
    id int8 not null,
    count int4,
    description varchar(255),
    file_name varchar(255),
    name varchar(255),
    price float8,
    primary key (id)
);

create table user_role (
    user_id int8 not null,
    roles varchar(255)
);

create table usr (
    id int8 not null,
    active boolean not null,
    password varchar(255),
    password_retry varchar(255),
    username varchar(255),
    primary key (id));

alter table if exists plo
    add constraint FK_PLO_ORDERS
        foreign key (orders_id) references orders;

alter table if exists plo
    add constraint FK_PLO_PRODUCT
        foreign key (product_id) references product;

alter table if exists user_role
    add constraint FK_USER_ROLE_USR
        foreign key (user_id) references usr;