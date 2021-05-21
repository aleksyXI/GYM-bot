DROP TABLE IF EXISTS java_quiz;
DROP TABLE IF EXISTS users;
CREATE SEQUENCE global_seq START WITH 100000;

CREATE TABLE users
(
    id         INTEGER PRIMARY KEY DEFAULT nextval('global_seq'),
    chat_id    INTEGER UNIQUE                NOT NULL,
    name       VARCHAR                       NOT NULL,
    birth_day  VARCHAR                       NOT NULL,
    abon_type  VARCHAR           default 0   NOT NULL,
    bot_state  VARCHAR                       NOT NULL
);

CREATE TABLE abon_type
(
    id             INTEGER PRIMARY KEY DEFAULT nextval('global_seq'),
    user_id
    name           VARCHAR NOT NULL,
    sum            FLOAT   NOT NULL,
    description    VARCHAR NOT NULL
);