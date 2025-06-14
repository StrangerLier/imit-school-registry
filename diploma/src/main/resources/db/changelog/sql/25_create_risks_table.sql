CREATE TABLE risk
(
    id uuid not null constraint pk_risk primary key,
    reason varchar(200),
    group_id uuid
);