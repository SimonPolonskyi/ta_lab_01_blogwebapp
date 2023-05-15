delete from user_role;
delete from usr;

insert into usr (id, activation_code, active, email, first_name, last_name, username, password)
    values (1, null, true, 'test1@test.com', 'Fname 1', 'Lname 1', 'user1', '$2a$08$7fcheEcBYLvQetkFhoAfs.ug/3UjnjR83ZGB.0MlqpEQvEfXVlYGi');
insert into usr (id, activation_code, active, email, first_name, last_name, username, password)
    values (2, null, true, 'test1@test.com', 'Fname 1', 'Lname 1', 'user2', '$2a$08$a0dKbMItR7qrT.E/eOyOP.gJ5jS1psegooihNoGpMBIZWiPmroCTq');

insert into user_role(user_id, roles) values (1, 'ADMIN'), (1, 'USER'), (2, 'USER');
