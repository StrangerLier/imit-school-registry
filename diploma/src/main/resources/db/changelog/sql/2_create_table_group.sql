CREATE TABLE group_info
(
    id uuid not null constraint pk_group primary key,
    class_number varchar(2),
    address varchar(255),
    lecturer varchar(255),
    direction varchar(100),
    listeners_amount int,
    approved_listeners int,
    time varchar(100)
);
