create sequence hibernate_sequence start 1 increment 1;

create table comment (
                         comment_id int8 not null,
                         text varchar(4096) not null,
                         user_id int8,
                         message_id int8,
                         order_num int8,
                         primary key (comment_id)
);

create table message (
                         message_id int8 not null,
                         filename varchar(255),
                         tag varchar(255),
                         text varchar(4096) not null,
                         user_id int8,
                         primary key (message_id)
);

create table user_role (
                           user_id int8 not null,
                           roles varchar(255)
);

create table usr (
                     id int8 not null,
                     activation_code varchar(255),
                     active boolean not null,
                     email varchar(255),
                     password varchar(255) not null,
                     username varchar(255) not null,
                     first_name varchar(255),
                     last_name varchar(255),
                     primary key (id)
);

alter table if exists message
    add constraint message_user_fk
    foreign key (user_id) references usr;

alter table if exists user_role
    add constraint user_role_user_fk
    foreign key (user_id) references usr;

