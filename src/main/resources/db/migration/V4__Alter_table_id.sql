ALTER TABLE recommendation
    DROP FOREIGN KEY recommendation_ibfk_1,
    MODIFY COLUMN portfolio_id BIGINT(20) UNSIGNED NOT NULL,
    MODIFY COLUMN recommendation_id BIGINT(20) UNSIGNED NOT NULL;

ALTER TABLE portfolio MODIFY COLUMN portfolio_id BIGINT(20) UNSIGNED NOT NULL;

ALTER TABLE recommendation
    ADD CONSTRAINT recommendation_ibfk_1 FOREIGN KEY (portfolio_id)
          REFERENCES portfolio (portfolio_id);
