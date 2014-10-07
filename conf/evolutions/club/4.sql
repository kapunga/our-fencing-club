# FredFencer schema

# --- !Ups

CREATE TABLE fred_fencer (
    fid integer PRIMARY KEY,
    usfa_id varchar(16),
    cff_id varchar(16),
    fie_id varchar(16),
    first_name varchar(32),
    last_name varchar(32),
    gender varchar(2),
    birth_year integer,
    div_id integer,
    club_ids varchar(32),
    ratings varchar(32)
);

# --- !Downs

DROP TABLE fred_fencer;