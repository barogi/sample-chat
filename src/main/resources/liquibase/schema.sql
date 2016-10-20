--liquibase formatted sql

--changeset igor:1 context:schemedata dbms:hsqldb
SET DATABASE SQL SYNTAX MYS TRUE;

--changeset igor:2 context:schemedata
create table messages (
  id int not null auto_increment primary key,
  author varchar(15) not null,
  text varchar(140) not null,
  tstamp timestamp not null default current_timestamp
);

create index messages_tstamp on messages (tstamp);
--rollback drop table messages if exists;
--rollback drop index messages_tstamp if exists;