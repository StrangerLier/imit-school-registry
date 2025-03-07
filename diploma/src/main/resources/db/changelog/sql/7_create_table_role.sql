CREATE TABLE role
(
    id uuid not null constraint pk_role primary key,
    role_name varchar(100),
    user_id uuid
);