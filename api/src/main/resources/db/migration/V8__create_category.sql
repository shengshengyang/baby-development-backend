-- 建立 categories 表
-- 建立 categories 表，name 為 JSON
CREATE TABLE categories
(
    id   BIGINT PRIMARY KEY AUTO_INCREMENT,
    name JSON NOT NULL
);

INSERT INTO categories (name)
VALUES (JSON_OBJECT('en', 'Language', 'zh_TW', '語言')),
       (JSON_OBJECT('en', 'Social', 'zh_TW', '社交')),
       (JSON_OBJECT('en', 'Gross Motor', 'zh_TW', '粗大動作')),
       (JSON_OBJECT('en', 'Fine Motor', 'zh_TW', '精細動作'));
