delete from user_role;
delete from usr;

insert into  usr(id, active, password, password_retry, username) values
(1, true, 'admin', 'admin', 'admin'),
(2, true, 'admin', 'admin', 'pavel');

insert into user_role(user_id, roles) values
(1, 'USER'), (1, 'ADMIN'),
(2, 'USER');