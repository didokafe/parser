drop database if exists parser;
create database parser;

create table parser.log_entry
(
	id bigint auto_increment primary key,
	date datetime(3) null,
	ip varchar(255) null,
	request varchar(255) null,
	status varchar(255) null,
	user_agent varchar(255) null
)
engine=InnoDB;

create table parser.blocked
(
  id bigint auto_increment primary key,
  ip varchar(255) null,
  reason varchar(255) null
)
engine=InnoDB;



