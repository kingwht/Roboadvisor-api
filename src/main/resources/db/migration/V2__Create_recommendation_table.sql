CREATE TABLE IF NOT EXISTS recommendation (
  recommendation_id INTEGER(11) NOT NULL AUTO_INCREMENT,
  portfolio_id BIGINT(20) NOT NULL,
  transactions VARCHAR(255) NOT NULL,
  PRIMARY KEY (recommendation_id)
  ) ENGINE=InnoDB DEFAULT CHARSET=utf8;
