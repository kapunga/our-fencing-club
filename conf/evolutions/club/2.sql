# Announcement, announce_order schema

# --- !Ups

CREATE TABLE announcement (
    aid SERIAL PRIMARY KEY,
    identifier varchar(255),
    title varchar(255),
    content text
);

CREATE TABLE announce_group (
    gid SERIAL PRIMARY KEY,
    name varchar(255)
);

CREATE TABLE announce_order (
    ordinal integer PRIMARY KEY,
    gid integer references announce_group(gid),
    aid integer references announcement(aid)
);

# --- !Downs

DROP TABLE announce_order;
DROP TABLE announce_group;
DROP TABLE announcement;