-- 清理現有資料庫結構的腳本
-- 注意：這會刪除所有現有數據！

-- 1. 先刪除所有外鍵約束
SET FOREIGN_KEY_CHECKS = 0;

-- 刪除所有表格（如果存在）
DROP TABLE IF EXISTS article_translations;
DROP TABLE IF EXISTS flashcard_translations;
DROP TABLE IF EXISTS milestone_translations;
DROP TABLE IF EXISTS progress;
DROP TABLE IF EXISTS baby_vaccine_schedule;
DROP TABLE IF EXISTS measurements;
DROP TABLE IF EXISTS flashcards;
DROP TABLE IF EXISTS milestones;
DROP TABLE IF EXISTS articles;
DROP TABLE IF EXISTS vaccine;
DROP TABLE IF EXISTS babies;
DROP TABLE IF EXISTS categories;
DROP TABLE IF EXISTS users;

-- 重新啟用外鍵檢查
SET FOREIGN_KEY_CHECKS = 1;

-- 2. 重新創建所有表格
CREATE TABLE users
(
    id       BINARY(16)     NOT NULL,
    username VARCHAR(255) NULL,
    password VARCHAR(255) NULL,
    email    VARCHAR(255) NOT NULL,
    CONSTRAINT pk_users PRIMARY KEY (id),
    CONSTRAINT uc_users_email UNIQUE (email)
);

CREATE TABLE categories
(
    id   BINARY(16) NOT NULL,
    name JSON     NOT NULL,
    CONSTRAINT pk_categories PRIMARY KEY (id)
);

CREATE TABLE babies
(
    id         BINARY(16)     NOT NULL,
    name       VARCHAR(255) NULL,
    birth_date DATE         NULL,
    user_id    BINARY(16)     NULL,
    CONSTRAINT pk_babies PRIMARY KEY (id)
);

CREATE TABLE vaccine
(
    id                    BINARY(16)     NOT NULL,
    name                  VARCHAR(255) NULL,
    recommended_age_start INT          NULL,
    dose_intervals_json   VARCHAR(255) NULL,
    description           VARCHAR(255) NULL,
    CONSTRAINT pk_vaccine PRIMARY KEY (id)
);

CREATE TABLE articles
(
    id          BINARY(16) NOT NULL,
    category_id BINARY(16) NOT NULL,
    CONSTRAINT pk_articles PRIMARY KEY (id)
);

CREATE TABLE milestones
(
    id            BINARY(16) NOT NULL,
    age_in_months INT      NULL,
    category_id   BINARY(16) NOT NULL,
    CONSTRAINT pk_milestones PRIMARY KEY (id)
);

CREATE TABLE flashcards
(
    id           BINARY(16) NOT NULL,
    category_id  BINARY(16) NOT NULL,
    milestone_id BINARY(16) NOT NULL,
    CONSTRAINT pk_flashcards PRIMARY KEY (id)
);

CREATE TABLE measurements
(
    id                 BINARY(16) NOT NULL,
    baby_id            BINARY(16) NULL,
    date               DATE     NULL,
    head_circumference DOUBLE   NULL,
    height             DOUBLE   NULL,
    weight             DOUBLE   NULL,
    CONSTRAINT pk_measurements PRIMARY KEY (id)
);

CREATE TABLE baby_vaccine_schedule
(
    id               BINARY(16)     NOT NULL,
    baby_id          BINARY(16)     NULL,
    vaccine_id       BINARY(16)     NULL,
    dose_number      INT          NULL,
    scheduled_date   DATE         NULL,
    actual_date      DATE         NULL,
    status           VARCHAR(255) NULL,
    rescheduled_date DATE         NULL,
    reminder_date    DATE         NULL,
    note             VARCHAR(255) NULL,
    CONSTRAINT pk_baby_vaccine_schedule PRIMARY KEY (id)
);

CREATE TABLE progress
(
    id            BINARY(16) NOT NULL,
    age_in_months INT      NULL,
    category_id   BINARY(16) NOT NULL,
    achieved      BIT(1)   NOT NULL,
    date_achieved DATE     NULL,
    baby_id       BINARY(16) NULL,
    flashcard_id  BINARY(16) NOT NULL,
    CONSTRAINT pk_progress PRIMARY KEY (id)
);

-- Translation tables
CREATE TABLE article_translations
(
    id            BINARY(16)     NOT NULL,
    article_id    BINARY(16)     NULL,
    language_code VARCHAR(255) NULL,
    title         VARCHAR(255) NULL,
    content       TEXT         NULL,
    CONSTRAINT pk_article_translations PRIMARY KEY (id)
);

CREATE TABLE milestone_translations
(
    id            BINARY(16)     NOT NULL,
    milestone_id  BINARY(16)     NULL,
    language_code VARCHAR(255) NULL,
    description   VARCHAR(255) NULL,
    CONSTRAINT pk_milestone_translations PRIMARY KEY (id)
);

CREATE TABLE flashcard_translations
(
    id            BINARY(16)     NOT NULL,
    flashcard_id  BINARY(16)     NULL,
    language_code VARCHAR(255) NULL,
    front_text    VARCHAR(255) NULL,
    back_text     VARCHAR(255) NULL,
    image_url     VARCHAR(255) NULL,
    CONSTRAINT pk_flashcard_translations PRIMARY KEY (id)
);

-- 3. 添加外鍵約束
ALTER TABLE babies
    ADD CONSTRAINT FK_BABIES_ON_USER
        FOREIGN KEY (user_id) REFERENCES users (id);

ALTER TABLE articles
    ADD CONSTRAINT FK_ARTICLES_ON_CATEGORY
        FOREIGN KEY (category_id) REFERENCES categories (id);

ALTER TABLE milestones
    ADD CONSTRAINT FK_MILESTONES_ON_CATEGORY
        FOREIGN KEY (category_id) REFERENCES categories (id);

ALTER TABLE flashcards
    ADD CONSTRAINT FK_FLASHCARDS_ON_CATEGORY
        FOREIGN KEY (category_id) REFERENCES categories (id);

ALTER TABLE flashcards
    ADD CONSTRAINT FK_FLASHCARDS_ON_MILESTONE
        FOREIGN KEY (milestone_id) REFERENCES milestones (id);

ALTER TABLE measurements
    ADD CONSTRAINT FK_MEASUREMENTS_ON_BABY
        FOREIGN KEY (baby_id) REFERENCES babies (id);

ALTER TABLE baby_vaccine_schedule
    ADD CONSTRAINT FK_BABY_VACCINE_SCHEDULE_ON_BABY
        FOREIGN KEY (baby_id) REFERENCES babies (id);

ALTER TABLE baby_vaccine_schedule
    ADD CONSTRAINT FK_BABY_VACCINE_SCHEDULE_ON_VACCINE
        FOREIGN KEY (vaccine_id) REFERENCES vaccine (id);

ALTER TABLE progress
    ADD CONSTRAINT FK_PROGRESS_ON_BABY
        FOREIGN KEY (baby_id) REFERENCES babies (id);

ALTER TABLE progress
    ADD CONSTRAINT FK_PROGRESS_ON_CATEGORY
        FOREIGN KEY (category_id) REFERENCES categories (id);

ALTER TABLE progress
    ADD CONSTRAINT FK_PROGRESS_ON_FLASHCARD
        FOREIGN KEY (flashcard_id) REFERENCES flashcards (id);

-- Translation table foreign keys
ALTER TABLE article_translations
    ADD CONSTRAINT FK_ARTICLE_TRANSLATIONS_ON_ARTICLE
        FOREIGN KEY (article_id) REFERENCES articles (id);

ALTER TABLE milestone_translations
    ADD CONSTRAINT FK_MILESTONE_TRANSLATIONS_ON_MILESTONE
        FOREIGN KEY (milestone_id) REFERENCES milestones (id);

ALTER TABLE flashcard_translations
    ADD CONSTRAINT FK_FLASHCARD_TRANSLATIONS_ON_FLASHCARD
        FOREIGN KEY (flashcard_id) REFERENCES flashcards (id);

-- 4. 創建索引以提升效能
CREATE INDEX idx_babies_user_id ON babies (user_id);
CREATE INDEX idx_articles_category_id ON articles (category_id);
CREATE INDEX idx_milestones_category_id ON milestones (category_id);
CREATE INDEX idx_flashcards_category_id ON flashcards (category_id);
CREATE INDEX idx_flashcards_milestone_id ON flashcards (milestone_id);
CREATE INDEX idx_measurements_baby_id ON measurements (baby_id);
CREATE INDEX idx_baby_vaccine_schedule_baby_id ON baby_vaccine_schedule (baby_id);
CREATE INDEX idx_baby_vaccine_schedule_vaccine_id ON baby_vaccine_schedule (vaccine_id);
CREATE INDEX idx_progress_baby_id ON progress (baby_id);
CREATE INDEX idx_progress_category_id ON progress (category_id);
CREATE INDEX idx_progress_flashcard_id ON progress (flashcard_id);
CREATE INDEX idx_article_translations_article_id ON article_translations (article_id);
CREATE INDEX idx_milestone_translations_milestone_id ON milestone_translations (milestone_id);
CREATE INDEX idx_flashcard_translations_flashcard_id ON flashcard_translations (flashcard_id);
