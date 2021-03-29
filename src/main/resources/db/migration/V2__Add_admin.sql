insert into usr (id, active, password, password_retry, username)
    values (1, true, 'admin', 'admin', 'admin');

insert into user_role (user_id, roles)
    values (1, 'USER'), (1, 'ADMIN');