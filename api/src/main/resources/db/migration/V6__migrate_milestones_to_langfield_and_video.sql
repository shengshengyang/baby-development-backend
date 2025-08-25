-- V6: Milestone 改用 LangFieldObject 及新增影片連結
-- 1. 新增欄位
ALTER TABLE milestones ADD COLUMN description JSON NULL;
ALTER TABLE milestones ADD COLUMN video_url VARCHAR(255) NULL;

-- 2. 將原 milestone_translations 資料彙整為 JSON（以語言代碼為 key）
UPDATE milestones m
LEFT JOIN (
  SELECT milestone_id,
    JSON_OBJECT(
      'en', COALESCE(MAX(CASE WHEN language_code='en' THEN description END), ''),
      'tw', COALESCE(MAX(CASE WHEN language_code='tw' THEN description END), ''),
      'cn', COALESCE(MAX(CASE WHEN language_code='cn' THEN description END), ''),
      'ja', COALESCE(MAX(CASE WHEN language_code='ja' THEN description END), ''),
      'ko', COALESCE(MAX(CASE WHEN language_code='ko' THEN description END), ''),
      'vi', COALESCE(MAX(CASE WHEN language_code='vi' THEN description END), '')
    ) AS desc_json
  FROM milestone_translations
  GROUP BY milestone_id
) t ON t.milestone_id = m.id
SET m.description = COALESCE(t.desc_json, JSON_OBJECT('en','', 'tw','', 'cn','', 'ja','', 'ko','', 'vi',''));

-- 3. 將 description 設為 NOT NULL
ALTER TABLE milestones MODIFY COLUMN description JSON NOT NULL;

-- 4. 刪除舊翻譯表
DROP TABLE IF EXISTS milestone_translations;

-- 完成
SELECT 'Milestones table migrated to LangFieldObject (description) and video_url added.' AS status;

