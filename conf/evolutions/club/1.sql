# Users schema

# --- !Ups

CREATE TABLE site_user (
    uid SERIAL PRIMARY KEY,
    username varchar(255) NOT NULL,
    email varchar(255) NOT NULL,
    password varchar(255) NOT NULL,
    active boolean NOT NULL,
    admin boolean NOT NULL,
    is_coach boolean NOT NULL
);

# --- !Downs

DROP TABLE site_user;