# --- First database schema

# --- !Ups

create sequence s_task_id;

create table task (
  id  bigint DEFAULT nextval('s_task_id'),
  label varchar(128),
  done boolean DEFAULT false
);

# --- !Downs
drop table task;
drop sequence s_task_id;

