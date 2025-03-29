CREATE TABLE attendance
(
    id uuid not null constraint pk_attendance primary key,
    class_id uuid,
    child_id uuid,
    comment varchar(200),
    is_attend bool
);