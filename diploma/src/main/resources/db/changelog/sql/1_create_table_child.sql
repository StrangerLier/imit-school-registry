CREATE TABLE child
(
    id uuid not null constraint pk_child primary key,
    name varchar(100),
    second_name varchar(100),
    surname varchar(100),
    birth_date date,
    address varchar(255),
    school varchar(255),
    class_number varchar(2),
    email varchar(100),
    group_type varchar(100),
    status varchar(100),
    phone varchar(100)
);
