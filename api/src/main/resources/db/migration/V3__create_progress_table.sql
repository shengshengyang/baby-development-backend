CREATE TABLE progress (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          age_in_months INT NOT NULL,
                          category VARCHAR(255) NOT NULL,
                          achieved BOOLEAN NOT NULL,
                          date_achieved DATE,
                          baby_id BIGINT NOT NULL,
                          FOREIGN KEY (baby_id) REFERENCES babies(id)
);
