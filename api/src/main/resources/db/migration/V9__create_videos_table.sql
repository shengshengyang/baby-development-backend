-- V9: Create videos table and update progress table to support video tracking
-- This migration creates the videos table with LangFieldObject description and relationships

-- 1. Create videos table
CREATE TABLE videos (
    id BINARY(16) NOT NULL,
    description JSON NOT NULL,
    video_url VARCHAR(500) NOT NULL,
    duration_seconds INT,
    thumbnail_url VARCHAR(500),
    milestone_id BINARY(16),
    article_id BINARY(16),
    flashcard_id BINARY(16),
    CONSTRAINT pk_videos PRIMARY KEY (id)
);

-- 2. Add video_id column to progress table
ALTER TABLE progress
ADD COLUMN video_id BINARY(16);

-- 3. Add foreign key constraints for videos table
ALTER TABLE videos
ADD CONSTRAINT FK_VIDEOS_ON_MILESTONE
    FOREIGN KEY (milestone_id) REFERENCES milestones (id);

ALTER TABLE videos
ADD CONSTRAINT FK_VIDEOS_ON_ARTICLE
    FOREIGN KEY (article_id) REFERENCES articles (id);

ALTER TABLE videos
ADD CONSTRAINT FK_VIDEOS_ON_FLASHCARD
    FOREIGN KEY (flashcard_id) REFERENCES flashcards (id);

-- 4. Add foreign key constraint for progress.video_id
ALTER TABLE progress
ADD CONSTRAINT FK_PROGRESS_ON_VIDEO
    FOREIGN KEY (video_id) REFERENCES videos (id);

-- 5. Update progress_type enum to include VIDEO
-- Note: The enum values are already handled by the application code
-- The database column accepts VARCHAR values: 'FLASHCARD', 'MILESTONE', 'VIDEO'
