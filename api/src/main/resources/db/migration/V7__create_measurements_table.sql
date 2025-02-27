CREATE TABLE measurements (
                              id BIGINT AUTO_INCREMENT PRIMARY KEY,
                              baby_id BIGINT NOT NULL,
                              date DATE NOT NULL,
                              head_circumference DOUBLE,
                              height DOUBLE,
                              weight DOUBLE,
                              FOREIGN KEY (baby_id) REFERENCES babies(id)
);