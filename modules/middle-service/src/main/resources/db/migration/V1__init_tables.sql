-- Create the account table
CREATE TABLE accounts
(
    id           SERIAL PRIMARY KEY,
    account_id   CHAR(36) UNIQUE,
    account_name VARCHAR(50)    NOT NULL,
    balance      NUMERIC(15, 2) NOT NULL
);

-- Create the customer table
CREATE TABLE customers
(
    id            SERIAL PRIMARY KEY,
    tg_id         BIGINT      NOT NULL UNIQUE,
    first_name    VARCHAR(20) NOT NULL,
    user_name     VARCHAR(20) NOT NULL,
    email         VARCHAR(50) NOT NULL UNIQUE,
    password_hash CHAR(60)    NOT NULL,
    uuid          CHAR(36) UNIQUE,
    account_id    BIGINT UNIQUE

);

-- Create the transaction table
CREATE TABLE transactions
(
    id               SERIAL PRIMARY KEY,
    account_id       BIGINT         NOT NULL,
    customer_id      BIGINT         NOT NULL,
    timestamp        TIMESTAMP      NOT NULL,
    amount           NUMERIC(15, 2) NOT NULL,
    new_balance      NUMERIC(15, 2) NOT NULL,
    transaction_uuid CHAR(36)       NOT NULL
);