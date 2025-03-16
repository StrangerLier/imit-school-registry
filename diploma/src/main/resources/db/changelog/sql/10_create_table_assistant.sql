CREATE TABLE assistant
(
    id uuid not null constraint pk_assistant primary key,
    name varchar(100),
    second_name varchar(100),
    surname varchar(100)
);