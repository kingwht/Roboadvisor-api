CREATE TABLE IF NOT EXISTS recommendation (
  recommendation_id INTEGER(11) NOT NULL AUTO_INCREMENT,
  portfolio_id INTEGER(20) NOT NULL,
  transactions JSON NOT NULL,
  PRIMARY KEY (recommendation_id),
  KEY portfolio_id (portfolio_id),
  FOREIGN KEY (portfolio_id) REFERENCES portfolio (portfolio_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
