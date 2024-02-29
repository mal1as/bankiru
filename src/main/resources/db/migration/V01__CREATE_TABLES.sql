--- DDL Drop tables
drop table if exists address, bank, bank_addresses, credit_request, credit_rule,
    credit_type, ownership_type, request_status, "user", user_data, user_request;

--- DDL Create tables
create table if not exists address
(
    id              bigserial    not null,
    building_number int4,
    country         varchar(255) not null,
    house_number    int4         not null,
    office_number   int4,
    street          varchar(255) not null,
    town            varchar(255) not null,
    primary key (id)
);

create table if not exists bank
(
    id                bigserial    not null,
    description       varchar(255),
    name              varchar(255) not null,
    phone_number      varchar(255) not null,
    rate              float8,
    ownership_type_id int8         not null,
    primary key (id)
);

create table if not exists bank_addresses
(
    bank_id    int8 not null,
    address_id int8 not null,
    primary key (bank_id, address_id)
);

create table if not exists credit_request
(
    id                bigserial not null,
    bank_id           int8      not null,
    credit_rule_id    int8      not null,
    request_status_id int8      not null,
    user_request_id   int8      not null,
    primary key (id)
);

create table if not exists credit_rule
(
    id             bigserial not null,
    max_period     int8      not null,
    max_rate       float8    not null,
    max_sum        int8      not null,
    min_period     int8      not null,
    min_rate       float8    not null,
    min_sum        int8      not null,
    bank_id        int8,
    credit_type_id int8      not null,
    primary key (id)
);

create table if not exists credit_type
(
    id      bigserial    not null,
    name    varchar(255) not null,
    deposit boolean      not null,
    primary key (id)
);

create table if not exists ownership_type
(
    id         bigserial    not null,
    name       varchar(255) not null,
    short_name varchar(255) not null,
    primary key (id)
);

create table if not exists request_status
(
    id   bigserial    not null,
    name varchar(255) not null,
    primary key (id)
);

create table role
(
    id   bigserial not null,
    name varchar(255),
    primary key (id)
);

create table "user"
(
    id               bigserial    not null,
    email            varchar(255) not null,
    password_hash    varchar(255) not null,
    phone_number     varchar(255) not null,
    user_data_id     int8,
    primary key (id)
);

create table if not exists user_data
(
    id               bigserial    not null,
    name             varchar(255) not null,
    patronymic       varchar(255),
    profit_per_month int8,
    work_company     varchar(255),
    surname          varchar(255) not null,
    primary key (id)
);

create table if not exists user_request
(
    id      bigserial not null,
    deposit boolean   not null,
    period  int8      not null,
    sum     int8      not null,
    user_id int8      not null,
    primary key (id)
);

create table user_roles
(
    user_id int8 not null,
    role_id int8 not null,
    primary key (user_id, role_id)
);


--- DDL Add constraints
alter table bank
    drop constraint if exists FKewg6v553q97vihj9pjwmycdq7;
alter table bank
    add constraint FKewg6v553q97vihj9pjwmycdq7 foreign key (ownership_type_id) references ownership_type;

alter table bank_addresses
    drop constraint if exists FK1b2oosjro94x7t1djm4oxeey8;
alter table bank_addresses
    add constraint FK1b2oosjro94x7t1djm4oxeey8 foreign key (address_id) references address;

alter table bank_addresses
    drop constraint if exists FKff0oxw3w5h8t9md7n06i4ve6j;
alter table bank_addresses
    add constraint FKff0oxw3w5h8t9md7n06i4ve6j foreign key (bank_id) references bank;

alter table credit_request
    drop constraint if exists FKdt1mrqj54wmyvop0f5ubaxdib;
alter table credit_request
    add constraint FKdt1mrqj54wmyvop0f5ubaxdib foreign key (bank_id) references bank;

alter table credit_request
    drop constraint if exists FKqnx7qd31lwqo7nwn1vebyftlf;
alter table credit_request
    add constraint FKqnx7qd31lwqo7nwn1vebyftlf foreign key (credit_rule_id) references credit_rule;

alter table credit_request
    drop constraint if exists FKfre7ah5c5170oocvbfvaa6xgs;
alter table credit_request
    add constraint FKfre7ah5c5170oocvbfvaa6xgs foreign key (request_status_id) references request_status;

alter table credit_request
    drop constraint if exists FKl8d2lcw7x8ya7mllcpk1d0cg6;
alter table credit_request
    add constraint FKl8d2lcw7x8ya7mllcpk1d0cg6 foreign key (user_request_id) references user_request;

alter table credit_rule
    drop constraint if exists FK2a47kwq5ajvupk7falyry0dn4;
alter table credit_rule
    add constraint FK2a47kwq5ajvupk7falyry0dn4 foreign key (bank_id) references bank;

alter table credit_rule
    drop constraint if exists FKaeocpsioi0eg9qvt04vj2k94o;
alter table credit_rule
    add constraint FKaeocpsioi0eg9qvt04vj2k94o foreign key (credit_type_id) references credit_type;

alter table "user"
    drop constraint if exists FKi9gkcdb4gs3x1bi8yas3va0jw;
alter table "user"
    add constraint FKi9gkcdb4gs3x1bi8yas3va0jw foreign key (user_data_id) references user_data;

alter table user_request
    drop constraint if exists FK7cng2e4axtlxmu1va5rkhjyfg;
alter table user_request
    add constraint FK7cng2e4axtlxmu1va5rkhjyfg foreign key (user_id) references "user";


alter table if exists user_roles
    drop constraint if exists FKrhfovtciq1l558cw6udg0h0d3;
alter table if exists user_roles
    add constraint FKrhfovtciq1l558cw6udg0h0d3 foreign key (role_id) references role;

alter table if exists user_roles
    drop constraint if exists FK7ivp84f52aa3vd7ndq0oh0279;
alter table if exists user_roles
    add constraint FK7ivp84f52aa3vd7ndq0oh0279 foreign key (user_id) references "user"