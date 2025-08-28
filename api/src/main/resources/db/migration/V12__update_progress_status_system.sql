-- 更新 Progress 表格以支援新的狀態系統
-- 添加 progress_status 和 date_started 欄位

-- 添加新的狀態欄位
ALTER TABLE progress ADD COLUMN progress_status VARCHAR(20) DEFAULT 'NOT_STARTED' NOT NULL;
ALTER TABLE progress ADD COLUMN date_started DATE;

-- 更新現有數據：根據 achieved 欄位設定初始狀態
UPDATE progress
SET progress_status = CASE
    WHEN achieved = true THEN 'COMPLETED'
    WHEN date_achieved IS NOT NULL THEN 'COMPLETED'
    ELSE 'NOT_STARTED'
END;

-- 更新有 date_achieved 但沒有 date_started 的記錄
UPDATE progress
SET date_started = date_achieved
WHERE date_achieved IS NOT NULL AND date_started IS NULL;

-- 添加索引以提升查詢效能
CREATE INDEX idx_progress_baby_status ON progress(baby_id, progress_status);
CREATE INDEX idx_progress_baby_type_status ON progress(baby_id, progress_type, progress_status);
