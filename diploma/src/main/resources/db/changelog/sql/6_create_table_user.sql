CREATE TABLE imit_user
(
    id uuid not null constraint pk_user primary key,
    username varchar(100),
    password varchar(100)
);
