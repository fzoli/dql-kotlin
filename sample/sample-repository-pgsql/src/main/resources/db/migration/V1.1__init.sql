/*
 * Copyright 2022 Zoltan Farkas
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

-- region extensions

create extension citext;
create extension unaccent;
create extension "uuid-ossp";

-- endregion

-- region domains

create domain email_address AS
    citext
      constraint email_address_ck
        check ( value ~ '^[a-zA-Z0-9.!#$%&''*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$' )
;

-- endregion

-- region index support

-- Immutable wrapper function of unaccent to support indexes.
CREATE OR REPLACE FUNCTION public.app_unaccent(text)
    RETURNS text AS
$func$
-- schema-qualify function and dictionary
SELECT public.unaccent('public.unaccent', $1)
$func$
LANGUAGE sql IMMUTABLE;

-- endregion

-- region phone number type

create table key_phone_number_type
(
    key varchar(255)
        constraint key_phone_number_type_pk
            primary key
);

insert into key_phone_number_type(key) values ('HOME');
insert into key_phone_number_type(key) values ('WORK');

-- endregion

-- region user

create table "user"
(
    id uuid default uuid_generate_v4()
        constraint user_pk
            primary key,
    creation_time timestamp(3) not null,
    level int not null,
    name text not null,
    email_address email_address not null,
    password_hash text
);

create unique index user_email_address_uk on "user" (lower(email_address));
create index user_name_unaccent_ix on "user" (public.app_unaccent(name));

create table user_phone_number
(
    user_id uuid
    constraint user_phone_number_user
        references "user",
    value text,
    type varchar(255) not null
        constraint user_phone_number_type_fk
            references key_phone_number_type on update cascade,
    constraint user_phone_number_pk
        primary key (user_id, value)
);

-- endregion
