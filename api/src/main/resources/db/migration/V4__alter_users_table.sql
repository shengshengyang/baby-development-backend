ALTER TABLE users
    ADD COLUMN email VARCHAR(255) NOT NULL UNIQUE,
    MODIFY COLUMN username VARCHAR(255);


-- 增加測試資料

INSERT INTO users (username, password, email) VALUES
                                                  ('user', '$2a$10$uxW46deN90orpy0axp2nWec7Yv1i/fDYs/fxplAqwiy/P783QaR/G', 'aaa@mail.com');
