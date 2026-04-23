CREATE TABLE accounts(
    account_id serial primary key,
    customer_id int not null
);


CREATE TABLE balances(
    balance_id serial primary key,
    account_id int not null,
    amount decimal(19,4) default 0,
    currency varchar(3) not null,

    constraint fk_account 
    foreign key(account_id)
    references accounts(account_id)
    on delete cascade
);

CREATE TABLE transactions(
    transaction_id serial primary key,
    account_id int not null references accounts(account_id),
    amount decimal(19,4) not null,
    currency varchar(3) not null,
    direction varchar(3) not null,
    description varchar(255) not null
);