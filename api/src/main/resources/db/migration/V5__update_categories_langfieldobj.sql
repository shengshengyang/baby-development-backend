-- V5: 更新categories表的name欄位格式，從LanguageMap轉換為LangFieldObject
-- 作者: AI Assistant
-- 日期: 2025-01-25

-- 1. 創建一個臨時表來存儲轉換後的數據
CREATE TEMPORARY TABLE temp_categories AS
SELECT
    id,
    CASE
        WHEN JSON_VALID(name) THEN
            JSON_OBJECT(
                'en', COALESCE(JSON_UNQUOTE(JSON_EXTRACT(name, '$.en')), ''),
                'tw', COALESCE(JSON_UNQUOTE(JSON_EXTRACT(name, '$.tw')), JSON_UNQUOTE(JSON_EXTRACT(name, '$.zh_TW')), ''),
                'cn', COALESCE(JSON_UNQUOTE(JSON_EXTRACT(name, '$.cn')), JSON_UNQUOTE(JSON_EXTRACT(name, '$.zh_CN')), ''),
                'ja', COALESCE(JSON_UNQUOTE(JSON_EXTRACT(name, '$.ja')), ''),
                'ko', COALESCE(JSON_UNQUOTE(JSON_EXTRACT(name, '$.ko')), ''),
                'vi', COALESCE(JSON_UNQUOTE(JSON_EXTRACT(name, '$.vi')), '')
            )
        ELSE
            JSON_OBJECT(
                'en', 'Unknown Category',
                'tw', '未知分類',
                'cn', '未知分类',
                'ja', '不明なカテゴリ',
                'ko', '알 수 없는 카테고리',
                'vi', 'Danh mục không xác định'
            )
    END as new_name
FROM categories;

-- 2. 更新categories表的name欄位
UPDATE categories c
JOIN temp_categories tc ON c.id = tc.id
SET c.name = tc.new_name;

-- 3. 驗證轉換結果
SELECT 'Categories table updated to LangFieldObject format successfully.' AS status;

-- 4. 顯示更新後的數據範例（可選）
SELECT
    id,
    JSON_PRETTY(name) as formatted_name
FROM categories
LIMIT 5;
