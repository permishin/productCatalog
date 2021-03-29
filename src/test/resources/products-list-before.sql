delete from product;

insert into product(id, count, description, file_name, name, price) values
(1, 1, 'GG', '404.jpg', 'Вино', '270'),
(2, 1, 'QQ', '404.jpg', 'Coup', '120'),
(3, 1, 'ZZ', '404.jpg', 'вино', '900'),
(4, 1, 'XX', '404.jpg', 'Bread', '50');

alter sequence hibernate_sequence restart 10;