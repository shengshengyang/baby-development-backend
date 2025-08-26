-- V10: 重新設計 Flashcard 結構
-- 1. 將 frontText 移動到 flashcards 表作為 subject (LangFieldObject)
-- 2. 將 imageUrl 移動到 flashcards 表
-- 3. 將 backText 重命名為 description
-- 4. 移除 flashcard_translations 中的 front_text 和 image_url

-- 先備份現有數據到臨時表
CREATE TEMPORARY TABLE flashcard_translations_backup AS
SELECT * FROM flashcard_translations;

-- 1. 在 flashcards 表中添加新欄位
ALTER TABLE flashcards ADD COLUMN subject JSON;
ALTER TABLE flashcards ADD COLUMN image_url VARCHAR(2048);

-- 2. 將 front_text 數據遷移到 flashcards.subject
-- 使用 JSON_OBJECTAGG 將多筆翻譯資料彙整為 key/value JSON 物件
UPDATE flashcards f
SET subject = (
    SELECT JSON_OBJECTAGG(
        CASE
            WHEN ft.language_code = 'TRADITIONAL_CHINESE' THEN 'tw'
            WHEN ft.language_code = 'SIMPLIFIED_CHINESE' THEN 'cn'
            WHEN ft.language_code = 'ENGLISH' THEN 'en'
            WHEN ft.language_code = 'JAPANESE' THEN 'ja'
            WHEN ft.language_code = 'KOREAN' THEN 'ko'
            WHEN ft.language_code = 'VIETNAMESE' THEN 'vi'
            ELSE 'tw'
        END,
        ft.front_text
    )
    FROM flashcard_translations ft
    WHERE ft.flashcard_id = f.id
      AND ft.front_text IS NOT NULL
)
WHERE EXISTS (
    SELECT 1 FROM flashcard_translations ft
    WHERE ft.flashcard_id = f.id AND ft.front_text IS NOT NULL
);

-- 3. 將第一個找到的 image_url 遷移到 flashcards 表
UPDATE flashcards f
SET image_url = (
    SELECT ft.image_url
    FROM flashcard_translations ft
    WHERE ft.flashcard_id = f.id
      AND ft.image_url IS NOT NULL
    LIMIT 1
)
WHERE EXISTS (
    SELECT 1 FROM flashcard_translations ft
    WHERE ft.flashcard_id = f.id AND ft.image_url IS NOT NULL
);

-- 4. 修改 flashcard_translations 表結構
-- 重命名 back_text 為 description
ALTER TABLE flashcard_translations CHANGE COLUMN back_text description TEXT;

-- 移除不再需要的欄位
ALTER TABLE flashcard_translations DROP COLUMN front_text;
ALTER TABLE flashcard_translations DROP COLUMN image_url;

-- 5. 為 subject 欄位設置預設值（如果為空）
UPDATE flashcards
SET subject = JSON_OBJECT('tw', '未命名卡片', 'en', 'Untitled Card')
WHERE subject IS NULL OR subject = 'null' OR JSON_LENGTH(subject) = 0;

SELECT 'Flashcard structure redesigned successfully.' AS status;
