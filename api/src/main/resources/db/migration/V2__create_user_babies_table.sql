CREATE TABLE users (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       username VARCHAR(255) NOT NULL,
                       password VARCHAR(255) NOT NULL
);


CREATE TABLE babies (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        name VARCHAR(255) NOT NULL,
                        birth_date DATE NOT NULL,
                        user_id BIGINT NOT NULL,
                        FOREIGN KEY (user_id) REFERENCES users(id)
);

