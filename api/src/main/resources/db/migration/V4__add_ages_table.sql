-- V4: 添加ages表并重构milestone和progress表的年龄字段
-- 作者: AI Assistant
-- 日期: 2025-01-19

-- 1. 创建ages表
CREATE TABLE ages (
    id BINARY(16) NOT NULL,
    month INT NOT NULL,
    display_name JSON NOT NULL,
    CONSTRAINT pk_ages PRIMARY KEY (id),
    CONSTRAINT uk_ages_month UNIQUE (month)
);

-- 2. 插入常用的年龄数据（0-36个月）
INSERT INTO ages (id, month, display_name) VALUES
    (UNHEX(REPLACE(UUID(),'-','')), 0, JSON_OBJECT('en', 'Newborn', 'tw', '新生兒', 'cn', '新生儿', 'ja', '新生児', 'ko', '신생아', 'vi', 'Trẻ sơ sinh')),
    (UNHEX(REPLACE(UUID(),'-','')), 1, JSON_OBJECT('en', '1 Month', 'tw', '1個月', 'cn', '1个月', 'ja', '1ヶ月', 'ko', '1개월', 'vi', '1 tháng')),
    (UNHEX(REPLACE(UUID(),'-','')), 2, JSON_OBJECT('en', '2 Months', 'tw', '2個月', 'cn', '2个月', 'ja', '2ヶ月', 'ko', '2개월', 'vi', '2 tháng')),
    (UNHEX(REPLACE(UUID(),'-','')), 3, JSON_OBJECT('en', '3 Months', 'tw', '3個月', 'cn', '3个月', 'ja', '3ヶ月', 'ko', '3개월', 'vi', '3 tháng')),
    (UNHEX(REPLACE(UUID(),'-','')), 4, JSON_OBJECT('en', '4 Months', 'tw', '4個月', 'cn', '4个月', 'ja', '4ヶ月', 'ko', '4개월', 'vi', '4 tháng')),
    (UNHEX(REPLACE(UUID(),'-','')), 5, JSON_OBJECT('en', '5 Months', 'tw', '5個月', 'cn', '5个月', 'ja', '5ヶ月', 'ko', '5개월', 'vi', '5 tháng')),
    (UNHEX(REPLACE(UUID(),'-','')), 6, JSON_OBJECT('en', '6 Months', 'tw', '6個月', 'cn', '6个月', 'ja', '6ヶ月', 'ko', '6개월', 'vi', '6 tháng')),
    (UNHEX(REPLACE(UUID(),'-','')), 9, JSON_OBJECT('en', '9 Months', 'tw', '9個月', 'cn', '9个月', 'ja', '9ヶ月', 'ko', '9개월', 'vi', '9 tháng')),
    (UNHEX(REPLACE(UUID(),'-','')), 12, JSON_OBJECT('en', '12 Months', 'tw', '12個月', 'cn', '12个月', 'ja', '12ヶ月', 'ko', '12개월', 'vi', '12 tháng')),
    (UNHEX(REPLACE(UUID(),'-','')), 18, JSON_OBJECT('en', '18 Months', 'tw', '18個月', 'cn', '18个月', 'ja', '18ヶ月', 'ko', '18개월', 'vi', '18 tháng')),
    (UNHEX(REPLACE(UUID(),'-','')), 24, JSON_OBJECT('en', '24 Months', 'tw', '24個月', 'cn', '24个月', 'ja', '24ヶ月', 'ko', '24개월', 'vi', '24 tháng')),
    (UNHEX(REPLACE(UUID(),'-','')), 36, JSON_OBJECT('en', '36 Months', 'tw', '36個月', 'cn', '36个月', 'ja', '36ヶ月', 'ko', '36개월', 'vi', '36 tháng'));

-- 3. 添加age_id列到milestones表
ALTER TABLE milestones ADD COLUMN age_id BINARY(16);

-- 4. 更新milestones表，将age_in_months映射到age_id
UPDATE milestones m
JOIN ages a ON m.age_in_months = a.month
SET m.age_id = a.id;

-- 5. 为没有匹配Age记录的milestone创建新的Age记录
INSERT INTO ages (id, month, display_name)
SELECT UNHEX(REPLACE(UUID(),'-','')),
       m.age_in_months,
       JSON_OBJECT('en', CONCAT(m.age_in_months, ' Months'),
                   'tw', CONCAT(m.age_in_months, '個月'),
                   'cn', CONCAT(m.age_in_months, '个月'),
                   'ja', CONCAT(m.age_in_months, 'ヶ月'),
                   'ko', CONCAT(m.age_in_months, '개월'),
                   'vi', CONCAT(m.age_in_months, ' tháng'))
FROM milestones m
WHERE m.age_in_months NOT IN (SELECT month FROM ages)
GROUP BY m.age_in_months;

-- 6. 再次更新milestones表以确保所有记录都有age_id
UPDATE milestones m
JOIN ages a ON m.age_in_months = a.month
SET m.age_id = a.id
WHERE m.age_id IS NULL;

-- 7. 设置age_id为非空
ALTER TABLE milestones MODIFY COLUMN age_id BINARY(16) NOT NULL;

-- 8. 添加外键约束
ALTER TABLE milestones
ADD CONSTRAINT FK_MILESTONES_ON_AGE
FOREIGN KEY (age_id) REFERENCES ages (id);

-- 9. 删除旧的age_in_months列
ALTER TABLE milestones DROP COLUMN age_in_months;

-- 10. 对progress表进行同样的操作
ALTER TABLE progress ADD COLUMN age_id BINARY(16);

-- 11. 更新progress表
UPDATE progress p
JOIN ages a ON p.age_in_months = a.month
SET p.age_id = a.id;

-- 12. 为没有匹配Age记录的progress创建新的Age记录
INSERT INTO ages (id, month, display_name)
SELECT UNHEX(REPLACE(UUID(),'-','')),
       p.age_in_months,
       JSON_OBJECT('en', CONCAT(p.age_in_months, ' Months'),
                   'tw', CONCAT(p.age_in_months, '個月'),
                   'cn', CONCAT(p.age_in_months, '个月'),
                   'ja', CONCAT(p.age_in_months, 'ヶ月'),
                   'ko', CONCAT(p.age_in_months, '개월'),
                   'vi', CONCAT(p.age_in_months, ' tháng'))
FROM progress p
WHERE p.age_in_months NOT IN (SELECT month FROM ages)
GROUP BY p.age_in_months;

-- 13. 再次更新progress表
UPDATE progress p
JOIN ages a ON p.age_in_months = a.month
SET p.age_id = a.id
WHERE p.age_id IS NULL;

-- 14. 设置progress表的age_id为非空
ALTER TABLE progress MODIFY COLUMN age_id BINARY(16) NOT NULL;

-- 15. 添加外键约束
ALTER TABLE progress
ADD CONSTRAINT FK_PROGRESS_ON_AGE
FOREIGN KEY (age_id) REFERENCES ages (id);

-- 16. 删除progress表的旧列
ALTER TABLE progress DROP COLUMN age_in_months;

-- 17. 添加索引提升性能
CREATE INDEX idx_ages_month ON ages (month);
CREATE INDEX idx_milestones_age_id ON milestones (age_id);
CREATE INDEX idx_progress_age_id ON progress (age_id);

-- 完成
SELECT 'Ages table created and data migrated successfully.' AS status;
