CREATE TABLE IF NOT EXISTS vaccine (
                                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                       name VARCHAR(100) NOT NULL,
                                       recommended_age_start INT,       -- 建議初始施打月齡 (可選)
                                       dose_intervals_json VARCHAR(255) NOT NULL,  -- 儲存 JSON 字串，如 [2,4,6]
                                       description VARCHAR(255)
) ENGINE=InnoDB;

-- 如果要用 ENUM，可在 MySQL 直接定義
CREATE TABLE IF NOT EXISTS baby_vaccine_schedule (
                                                     id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                                     baby_id BIGINT NOT NULL,
                                                     vaccine_id BIGINT NOT NULL,
                                                     dose_number INT NOT NULL,        -- 第幾劑
                                                     scheduled_date DATE,             -- 預定施打日期
                                                     actual_date DATE,                -- 實際施打日期
                                                     status ENUM('SCHEDULED','COMPLETED','DELAYED') DEFAULT 'SCHEDULED',
                                                     rescheduled_date DATE,
                                                     reminder_date DATE,
                                                     note VARCHAR(255),
                                                     CONSTRAINT fk_schedule_baby FOREIGN KEY (baby_id) REFERENCES babies(id),
                                                     CONSTRAINT fk_schedule_vaccine FOREIGN KEY (vaccine_id) REFERENCES vaccine(id)
) ENGINE=InnoDB;
