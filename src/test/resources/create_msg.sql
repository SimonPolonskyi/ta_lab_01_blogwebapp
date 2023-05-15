delete from message;

insert into message(message_id, text, tag, user_id)
values
      (1, 'first msg', 'tag 1', 1),
      (2, 'second', 'tag 2', 1);

alter sequence message_seq restart with 100;