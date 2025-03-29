CREATE TABLE class
(
    id uuid not null constraint pk_class primary key,
    group_id uuid,
    class_date_time timestamp,
    theme varchar(200)
);