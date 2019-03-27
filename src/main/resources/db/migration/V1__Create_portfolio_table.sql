CREATE TABLE IF NOT EXISTS portfolio (
    portfolio_id BIGINT(20) NOT NULL UNIQUE,
    deviation INTEGER(11) NOT NULL,
    portfolio_type VARCHAR(10) NOT NULL,
    allocations VARCHAR(255) NOT NULL,
    PRIMARY KEY (portfolio_id)
    )ENGINE=InnoDB DEFAULT CHARSET=utf8;
