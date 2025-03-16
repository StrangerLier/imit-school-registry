CREATE TABLE teacher
(
    id uuid not null constraint pk_teacher primary key,
    name varchar(100),
    second_name varchar(100),
    surname varchar(100)
);