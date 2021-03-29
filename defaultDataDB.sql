INSERT INTO product (id, count, description, file_name, name, price) VALUES
(15, 1, 'Охлажденная', 'babc9d73-7fe9-4ee8-8204-0ca3a7ba88ee.U286437ea099546cabfa45a2d0797451fR.png', 'Тилапия', 550),
(16, 1, 'В банке', '93029765-332b-413a-8b59-8bcf6a272ecc.kapersy-top-150-foto-vido-18.jpg', 'Каперсы', 150),
(17, 1, 'Под солнышком вялили)', '7d6400c5-4280-43f7-92f0-240d36d0530f.lZSYlt546P8.jpg', 'Вяленые томаты', 220),
(18, 1, 'Охлажденные', 'b2ef4cfa-70be-4bff-b18b-f0b6f5b06bfd.image1.jpg', 'Лангустины', 990),
(19, 1, 'Корейка', 'a899f132-ca33-439e-8ac0-112674818e48.Govyadina_koreika-900x900.jpg', 'Говядина', 670);

INSERT INTO orders (id, address, date, email, comment) VALUES
    (20, 'Пистолетова ул, д 9', '2021-03-29 19:08:30.964000', 'stepa@gmail.com', 'Сообщить перед доставкой');

INSERT INTO plo (id, cost_final, count, price_final, orders_id, product_id) VALUES
    (21, 2200, 4, 550, 20, 15),
    (22, 450, 3, 150, 20, 16),
    (23, 440, 2, 220, 20, 17);





