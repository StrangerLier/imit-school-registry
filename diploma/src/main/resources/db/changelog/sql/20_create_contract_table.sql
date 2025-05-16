CREATE TABLE contract
(
    id uuid not null constraint pk_contract primary key,
    conclusion_date timestamp,
    payer_fullname varchar(100),
    child_id uuid,
    payment_amount int,
    payment_type varchar(100),
    sale integer,
    direction varchar(100)
);