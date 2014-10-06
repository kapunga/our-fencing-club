# Club, Division schema

# --- !Ups

CREATE TABLE club (
    fid integer PRIMARY KEY,
    club_name varchar(255),
    club_abbrev varchar(255)
);

CREATE TABLE division (
    fid integer PRIMARY KEY,
    div_name varchar(255),
    div_abbrev varchar(255)
);

# --- !Downs

DROP TABLE division;
DROP TABLE club;