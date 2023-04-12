DROP  TABLE IF  EXISTS tour_rating;
DROP  TABLE IF  EXISTS tour;
DROP  TABLE IF  EXISTS tour_package;

CREATE  TABLE IF NOT EXISTS tour_package(
    code CHAR(2) NOT NULL UNIQUE,
    name VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS  tour(
    id  SERIAL PRIMARY KEY,
    tour_package_code CHAR(2) NOT NULL,
    title VARCHAR(200) NOT NULL,
    description VARCHAR(2000) NOT NULL,
    blurb VARCHAR(2000) NOT NULL,
    bullets VARCHAR(2000) NOT NULL,
    price VARCHAR(10) NOT NULL,
    duration VARCHAR(32) NOT NULL,
    difficulty VARCHAR(16) NOT NULL,
    region VARCHAR(20) NOT NULL,
    keywords VARCHAR(100),
    CONSTRAINT tour_fk FOREIGN KEY(tour_package_code) REFERENCES tour_package (code)
);


CREATE TABLE  IF NOT EXISTS  tour_rating (
    id  SERIAL PRIMARY KEY,
    tour_id BIGINT,
    customer_id BIGINT,
    score INT,
    comment VARCHAR(100),
    date date,
    CONSTRAINT tour_rating_fk FOREIGN KEY(tour_id) REFERENCES tour(id),
    CONSTRAINT  ix_unique_tour_rating UNIQUE  (tour_id, customer_id)
);

