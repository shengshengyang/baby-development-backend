-- 移除 progress 表中的 achieved 欄位，因為現在使用 progress_status 來管理狀態
ALTER TABLE progress DROP COLUMN achieved;

