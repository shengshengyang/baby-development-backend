-- 創建 milestones 表
CREATE TABLE milestones (
                            id BIGINT AUTO_INCREMENT PRIMARY KEY,
                            age_in_months INT NOT NULL,
                            category VARCHAR(255) NOT NULL
);

-- 創建 milestone_translations 表
CREATE TABLE milestone_translations (
                                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                        milestone_id BIGINT NOT NULL,
                                        language_code VARCHAR(10) NOT NULL,
                                        description TEXT NOT NULL,
                                        FOREIGN KEY (milestone_id) REFERENCES milestones(id)
);

-- 創建 articles 表
CREATE TABLE articles (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          category VARCHAR(255) NOT NULL
);

-- 創建 article_translations 表
CREATE TABLE article_translations (
                                      id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                      article_id BIGINT NOT NULL,
                                      language_code VARCHAR(10) NOT NULL,
                                      title VARCHAR(255) NOT NULL,
                                      content TEXT NOT NULL,
                                      FOREIGN KEY (article_id) REFERENCES articles(id)
);

-- 創建 flashcards 表
CREATE TABLE flashcards (
                            id BIGINT AUTO_INCREMENT PRIMARY KEY,
                            category VARCHAR(255) NOT NULL
);

-- 創建 flashcard_translations 表
CREATE TABLE flashcard_translations (
                                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                        flashcard_id BIGINT NOT NULL,
                                        language_code VARCHAR(10) NOT NULL,
                                        front_text VARCHAR(255) NOT NULL,
                                        back_text VARCHAR(255) NOT NULL,
                                        image_url VARCHAR(255),
                                        FOREIGN KEY (flashcard_id) REFERENCES flashcards(id)
);
