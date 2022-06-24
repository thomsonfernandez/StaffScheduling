
--drop the tables
drop table user_roles;
drop table roles;
drop table schedules;
drop table users;
--start the application

--run below queries
INSERT INTO roles VALUES(1,'ROLE_ADMIN');
INSERT INTO roles VALUES(2,'ROLE_STAFF');

--username : admin , password : admin
insert into users values(1, 'admin@gmail.com', 'admin' , '$2a$10$kxKCgBmtU0Z2jdO80QVUI.HlgtHf2hGoIs9fl48rR8Yb51agxd3Tq', 'admin');
insert into user_roles values (1,1);