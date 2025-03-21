
-- 3. 新增 category_id 欄位（暫時允許 NULL 以便資料搬移）
ALTER TABLE flashcards ADD COLUMN category_id BIGINT  NOT NULL default 1;

-- 6. 加上外鍵限制
ALTER TABLE flashcards
    ADD CONSTRAINT fk_flashcard_category
        FOREIGN KEY (category_id) REFERENCES categories(id);
