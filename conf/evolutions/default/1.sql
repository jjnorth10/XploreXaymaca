# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table location (
  id                        bigint not null,
  name                      varchar(255),
  description               varchar(255),
  latitude                  double,
  longitude                 double,
  image                     varchar(255),
  location_type_id          bigint,
  constraint pk_location primary key (id))
;

create table location_type (
  id                        bigint not null,
  type                      varchar(255),
  description               varchar(255),
  constraint pk_location_type primary key (id))
;

create sequence location_seq;

create sequence location_type_seq;

alter table location add constraint fk_location_locationType_1 foreign key (location_type_id) references location_type (id) on delete restrict on update restrict;
create index ix_location_locationType_1 on location (location_type_id);



# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists location;

drop table if exists location_type;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists location_seq;

drop sequence if exists location_type_seq;

