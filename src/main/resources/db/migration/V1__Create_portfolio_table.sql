CREATE DATABASE IF NOT EXISTS roboadvisor;
USE roboadvisor;

CREATE TABLE portfolio (
    portfolio_id INTEGER(20) NOT NULL UNIQUE,
    deviation INTEGER(20) NOT NULL,
    portfolio_type VARCHAR(10) NOT NULL,
    PRIMARY KEY (portfolio_id)
    )ENGINE=InnoDB DEFAULT CHARSET=utf8;
