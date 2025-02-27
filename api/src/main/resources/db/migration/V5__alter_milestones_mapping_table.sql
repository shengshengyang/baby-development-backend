-- Add milestone_id column to flashcards table
ALTER TABLE flashcards
    ADD COLUMN milestone_id BIGINT NOT NULL;

-- Add foreign key constraint linking flashcards to milestones
ALTER TABLE flashcards
    ADD CONSTRAINT fk_flashcards_milestone
        FOREIGN KEY (milestone_id) REFERENCES milestones(id)
            ON DELETE CASCADE;
