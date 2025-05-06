ALTER TABLE users
    ADD COLUMN email VARCHAR(255) NOT NULL UNIQUE,
    MODIFY COLUMN username VARCHAR(255);


-- 增加測試資料

INSERT INTO users (username, password, email) VALUES
                                                  ('john_doe', 'password123', 'john.doe@example.com'),
                                                  ('jane_smith', 'securepass', 'jane.smith@example.com'),
                                                  ('alice_wonder', 'alice123', 'alice.wonder@example.com'),
                                                  ('bob_builder', 'buildit', 'bob.builder@example.com'),
                                                  ('admin', '1234', 'aaa@mail.com'),
                                                  ('charlie_brown', 'charlie2023', 'charlie.brown@example.com');