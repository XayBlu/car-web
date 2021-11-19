SET search_path TO cars;

drop table if exists cars cascade;
drop table if exists make cascade;
drop table if exists model cascade;
drop sequence if exists HIBERNATE_SEQUENCE cascade;

create table cars (
    id serial not null unique,
    car_id bigint not null,
    colour varchar(255),
    year integer not null,
    make_id bigint,
    model_id bigint,
    created_date_time timestamp,
    update_date_time timestamp,
    primary key (car_id)
);

create table model (
  id serial not null unique,
   model_id bigint not null,
    name varchar(255) not null,
    created_date_time timestamp,
    update_date_time timestamp,
    primary key (model_id)
);

create table make (
    id serial not null unique,
    make_id bigint not null,
    name varchar(255) not null,
    created_date_time timestamp,
    update_date_time timestamp,
    primary key (make_id)
);


create sequence hibernate_sequence start 1 increment 1;

alter table cars
   add constraint FK_make_id_car
   foreign key (make_id)
   references make;

alter table cars
   add constraint FK_model_id_car
   foreign key (model_id)
   references model;


