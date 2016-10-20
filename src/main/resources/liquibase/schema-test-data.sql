--liquibase formatted sql

--changeset igor:1 context:testdata
insert into messages(author, text, tstamp) values('vasia', 'hello', current_timestamp - interval '2' hour);
insert into messages(author, text) values('vasia', 'hello');
insert into messages(author, text) values('vasia', 'hello');
insert into messages(author, text) values('vasia', 'hello');
