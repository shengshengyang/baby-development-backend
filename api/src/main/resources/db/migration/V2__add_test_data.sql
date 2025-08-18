-- Growth Cup App - Test Data Generation Script
-- Author: Gemini
-- Date: 2025-08-19

-- 使用變數來儲存 BINARY UUID，方便後續引用
SET @user1_id = UNHEX(REPLACE(UUID(),'-',''));
SET @user2_id = UNHEX(REPLACE(UUID(),'-',''));
SET @baby1_id = UNHEX(REPLACE(UUID(),'-',''));
SET @baby2_id = UNHEX(REPLACE(UUID(),'-',''));

SET @cat_motor_id = UNHEX(REPLACE(UUID(),'-',''));
SET @cat_cognitive_id = UNHEX(REPLACE(UUID(),'-',''));
SET @cat_language_id = UNHEX(REPLACE(UUID(),'-',''));
SET @cat_social_id = UNHEX(REPLACE(UUID(),'-',''));

-- ===============================================
-- 1. 核心資料 (Users, Babies, Categories)
-- ===============================================

-- -- 使用者 (Users)
INSERT INTO users (id, username, password, email)
VALUES (@user1_id, 'testuser', 'hashed_password_123', 'testuser@example.com'),
       (@user2_id, 'anotheruser', 'hashed_password_456', 'anotheruser@example.com');

-- -- 寶寶 (Babies)
-- 假設今天日期為 2025-08-19
INSERT INTO babies (id, name, birth_date, user_id)
VALUES (@baby1_id, '小寶', '2025-02-15', @user1_id), -- 約 6 個月大
       (@baby2_id, '安安', '2025-06-20', @user2_id); -- 約 2 個月大

-- -- 類別 (Categories) - 使用 JSON 格式儲存多語系名稱
INSERT INTO categories (id, name)
VALUES (@cat_motor_id, JSON_OBJECT('en', 'Motor Skills', 'zh_TW', '動作發展')),
       (@cat_cognitive_id, JSON_OBJECT('en', 'Cognitive', 'zh_TW', '認知發展')),
       (@cat_language_id, JSON_OBJECT('en', 'Language & Communication', 'zh_TW', '語言溝通')),
       (@cat_social_id, JSON_OBJECT('en', 'Social & Emotional', 'zh_TW', '社會與情感'));


-- ===============================================
-- 2. 內容資料 (Milestones, Flashcards, Articles)
-- ===============================================

-- -- 發展里程碑與翻譯 (Milestones & Translations)
-- --- 動作發展 (Motor Skills) ---
SET @milestone_motor_2m_id = UNHEX(REPLACE(UUID(),'-',''));
INSERT INTO milestones (id, age_in_months, category_id) VALUES (@milestone_motor_2m_id, 2, @cat_motor_id);
INSERT INTO milestone_translations (id, milestone_id, language_code, description)
VALUES (UNHEX(REPLACE(UUID(),'-','')), @milestone_motor_2m_id, 'en', 'Lifts head and chest when on tummy'),
       (UNHEX(REPLACE(UUID(),'-','')), @milestone_motor_2m_id, 'zh_TW', '俯臥時能抬起頭和胸');

SET @milestone_motor_6m_id = UNHEX(REPLACE(UUID(),'-',''));
INSERT INTO milestones (id, age_in_months, category_id) VALUES (@milestone_motor_6m_id, 6, @cat_motor_id);
INSERT INTO milestone_translations (id, milestone_id, language_code, description)
VALUES (UNHEX(REPLACE(UUID(),'-','')), @milestone_motor_6m_id, 'en', 'Rolls over in both directions'),
       (UNHEX(REPLACE(UUID(),'-','')), @milestone_motor_6m_id, 'zh_TW', '可以雙向翻身');

-- --- 認知發展 (Cognitive) ---
SET @milestone_cognitive_2m_id = UNHEX(REPLACE(UUID(),'-',''));
INSERT INTO milestones (id, age_in_months, category_id) VALUES (@milestone_cognitive_2m_id, 2, @cat_cognitive_id);
INSERT INTO milestone_translations (id, milestone_id, language_code, description)
VALUES (UNHEX(REPLACE(UUID(),'-','')), @milestone_cognitive_2m_id, 'en', 'Pays attention to faces'),
       (UNHEX(REPLACE(UUID(),'-','')), @milestone_cognitive_2m_id, 'zh_TW', '會注意人臉');


-- -- 閃卡與翻譯 (Flashcards & Translations)
-- --- 2個月動作發展閃卡 ---
SET @flashcard1_id = UNHEX(REPLACE(UUID(),'-',''));
INSERT INTO flashcards (id, category_id, milestone_id) VALUES (@flashcard1_id, @cat_motor_id, @milestone_motor_2m_id);
INSERT INTO flashcard_translations (id, flashcard_id, language_code, front_text, back_text, image_url)
VALUES (UNHEX(REPLACE(UUID(),'-','')), @flashcard1_id, 'en', 'Can they lift their head 45 degrees?', 'When on their tummy, your baby should be able to briefly lift their head.', 'https://example.com/images/head_lift.png'),
       (UNHEX(REPLACE(UUID(),'-','')), @flashcard1_id, 'zh_TW', '趴著時，頭可以抬起45度嗎？', '寶寶俯臥時，應該能夠短暫地抬起頭部。', 'https://example.com/images/head_lift.png');

SET @flashcard2_id = UNHEX(REPLACE(UUID(),'-',''));
INSERT INTO flashcards (id, category_id, milestone_id) VALUES (@flashcard2_id, @cat_motor_id, @milestone_motor_2m_id);
INSERT INTO flashcard_translations (id, flashcard_id, language_code, front_text, back_text, image_url)
VALUES (UNHEX(REPLACE(UUID(),'-','')), @flashcard2_id, 'en', 'Smoother arm and leg movements?', 'Jerky movements start to become more fluid and circular.', 'https://example.com/images/smooth_move.png'),
       (UNHEX(REPLACE(UUID(),'-','')), @flashcard2_id, 'zh_TW', '手腳的動作是否變得更流暢？', '寶寶不協調的動作會開始變得更平順、更有連續性。', 'https://example.com/images/smooth_move.png');

-- --- 6個月動作發展閃卡 ---
SET @flashcard3_id = UNHEX(REPLACE(UUID(),'-',''));
INSERT INTO flashcards (id, category_id, milestone_id) VALUES (@flashcard3_id, @cat_motor_id, @milestone_motor_6m_id);
INSERT INTO flashcard_translations (id, flashcard_id, language_code, front_text, back_text, image_url)
VALUES (UNHEX(REPLACE(UUID(),'-','')), @flashcard3_id, 'en', 'Can they roll from tummy to back?', 'Place a toy nearby to encourage them to roll over.', 'https://example.com/images/roll_over.png'),
       (UNHEX(REPLACE(UUID(),'-','')), @flashcard3_id, 'zh_TW', '可以從趴姿翻到仰躺嗎？', '在旁邊放個玩具，鼓勵寶寶翻身去拿。', 'https://example.com/images/roll_over.png');

-- -- 文章與翻譯 (Articles & Translations)
SET @article1_id = UNHEX(REPLACE(UUID(),'-',''));
INSERT INTO articles (id, category_id) VALUES (@article1_id, @cat_motor_id);
INSERT INTO article_translations (id, article_id, language_code, title, content)
VALUES (UNHEX(REPLACE(UUID(),'-','')), @article1_id, 'en', 'Understanding Tummy Time', 'Tummy time is crucial for developing strong neck and shoulder muscles...'),
       (UNHEX(REPLACE(UUID(),'-','')), @article1_id, 'zh_TW', 'Tummy Time 的重要性', '「Tummy Time」(俯臥時間) 對於寶寶頸部與肩部肌肉的發展至關重要...');

SET @article2_id = UNHEX(REPLACE(UUID(),'-',''));
INSERT INTO articles (id, category_id) VALUES (@article2_id, @cat_social_id);
INSERT INTO article_translations (id, article_id, language_code, title, content)
VALUES (UNHEX(REPLACE(UUID(),'-','')), @article2_id, 'en', 'The Power of a Smile', 'Your baby\'s first social smile is a major milestone...'),
       (UNHEX(REPLACE(UUID(),'-','')), @article2_id, 'zh_TW', '微笑的力量', '寶寶的第一個社交微笑是一個重要的發展里程碑...');


-- ===============================================
-- 3. 寶寶個人化資料 (Progress, Measurements)
-- ===============================================

-- -- 進度 (Progress) - 模擬6個月大的「小寶」的進度
INSERT INTO progress (id, age_in_months, category_id, achieved, date_achieved, baby_id, flashcard_id)
VALUES -- 2個月的里程碑已完成
       (UNHEX(REPLACE(UUID(),'-','')), 2, @cat_motor_id, b'1', '2025-04-20', @baby1_id, @flashcard1_id),
       (UNHEX(REPLACE(UUID(),'-','')), 2, @cat_motor_id, b'1', '2025-04-28', @baby1_id, @flashcard2_id),
       -- 6個月的里程碑尚未完成
       (UNHEX(REPLACE(UUID(),'-','')), 6, @cat_motor_id, b'0', NULL, @baby1_id, @flashcard3_id);

-- -- 測量 (Measurements) - 模擬「小寶」的生長曲線
INSERT INTO measurements (id, baby_id, date, head_circumference, height, weight)
VALUES (UNHEX(REPLACE(UUID(),'-','')), @baby1_id, '2025-03-15', 38.5, 55.2, 4.5),  -- 1個月
       (UNHEX(REPLACE(UUID(),'-','')), @baby1_id, '2025-05-15', 41.0, 60.1, 6.2),  -- 3個月
       (UNHEX(REPLACE(UUID(),'-','')), @baby1_id, '2025-08-14', 43.2, 65.8, 7.8);  -- 6個月


-- ===============================================
-- 4. 疫苗資料 (Vaccine, Schedule)
-- ===============================================

-- -- 疫苗種類 (Vaccine)
SET @vaccine_hepB_id = UNHEX(REPLACE(UUID(),'-',''));
SET @vaccine_dtap_id = UNHEX(REPLACE(UUID(),'-',''));
SET @vaccine_bcg_id = UNHEX(REPLACE(UUID(),'-',''));
INSERT INTO vaccine (id, name, recommended_age_start, dose_intervals_json, description)
VALUES (@vaccine_hepB_id, 'Hepatitis B (HepB)', 0, '[30, 150]', 'B型肝炎疫苗'),
       (@vaccine_dtap_id, 'DTaP-IPV-Hib', 60, '[60, 60, 365]', '五合一疫苗 (白喉、破傷風、非細胞性百日咳、不活化小兒麻痺及b型嗜血桿菌混合疫苗)'),
       (@vaccine_bcg_id, 'Bacillus Calmette-Guérin (BCG)', 30, '[]', '卡介苗');

-- -- 寶寶疫苗排程 (Baby Vaccine Schedule) - 模擬「小寶」的接種紀錄
INSERT INTO baby_vaccine_schedule (id, baby_id, vaccine_id, dose_number, scheduled_date, actual_date, status, note)
VALUES -- B肝疫苗
       (UNHEX(REPLACE(UUID(),'-','')), @baby1_id, @vaccine_hepB_id, 1, '2025-02-16', '2025-02-16', 'Completed', '出生24小時內施打'),
       (UNHEX(REPLACE(UUID(),'-','')), @baby1_id, @vaccine_hepB_id, 2, '2025-03-15', '2025-03-15', 'Completed', '滿1個月'),
       (UNHEX(REPLACE(UUID(),'-','')), @baby1_id, @vaccine_hepB_id, 3, '2025-08-15', '2025-08-15', 'Completed', '滿6個月'),
       -- 五合一疫苗
       (UNHEX(REPLACE(UUID(),'-','')), @baby1_id, @vaccine_dtap_id, 1, '2025-04-15', '2025-04-20', 'Completed', '滿2個月，稍微延後'),
       (UNHEX(REPLACE(UUID(),'-','')), @baby1_id, @vaccine_dtap_id, 2, '2025-06-15', '2025-06-18', 'Completed', '滿4個月'),
       (UNHEX(REPLACE(UUID(),'-','')), @baby1_id, @vaccine_dtap_id, 3, '2025-08-15', NULL, 'Scheduled', '滿6個月，預計本週施打'),
       -- 卡介苗
       (UNHEX(REPLACE(UUID(),'-','')), @baby1_id, @vaccine_bcg_id, 1, '2025-03-15', '2025-03-20', 'Completed', '滿1個月');

-- -- 模擬「安安」的疫苗排程
INSERT INTO baby_vaccine_schedule (id, baby_id, vaccine_id, dose_number, scheduled_date, actual_date, status, note)
VALUES -- B肝疫苗
       (UNHEX(REPLACE(UUID(),'-','')), @baby2_id, @vaccine_hepB_id, 1, '2025-06-21', '2025-06-21', 'Completed', '出生24小時內施打'),
       (UNHEX(REPLACE(UUID(),'-','')), @baby2_id, @vaccine_hepB_id, 2, '2025-07-20', '2025-07-22', 'Completed', '滿1個月'),
       -- 五合一疫苗
       (UNHEX(REPLACE(UUID(),'-','')), @baby2_id, @vaccine_dtap_id, 1, '2025-08-20', NULL, 'Scheduled', '滿2個月，即將施打');


-- -- 完成 -- --
SELECT 'Test data has been successfully generated.' AS status;
