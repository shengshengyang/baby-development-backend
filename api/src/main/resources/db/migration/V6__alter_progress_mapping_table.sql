-- Add flashcard_id column to progress table
ALTER TABLE progress
    ADD COLUMN flashcard_id BIGINT NOT NULL;

ALTER TABLE progress
    ADD CONSTRAINT unique_baby_flashcard
        UNIQUE (baby_id, flashcard_id);
