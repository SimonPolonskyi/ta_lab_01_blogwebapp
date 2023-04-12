create table pending_user_changes (
                    user_id int8 not null references usr,
                    parameter varchar(255),
                    new_value varchar(255),
                    confirmation_code varchar(255),
                    primary key (user_id, parameter)
)