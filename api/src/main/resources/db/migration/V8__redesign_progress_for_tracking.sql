-- V8: Redesign progress table structure for tracking baby flashcard/milestone completion
-- This migration updates the progress table to support ProgressType enum and proper tracking

-- 1. Add progress_type column
ALTER TABLE progress
ADD COLUMN progress_type VARCHAR(20) NOT NULL DEFAULT 'FLASHCARD';

-- 2. Make flashcard_id and milestone_id nullable since records can be either type
ALTER TABLE progress
MODIFY COLUMN flashcard_id BINARY(16) NULL;

ALTER TABLE progress
MODIFY COLUMN milestone_id BINARY(16) NULL;

-- 3. Update existing data to set proper progress_type
-- All existing records are flashcard-based progress
UPDATE progress
SET progress_type = 'FLASHCARD'
WHERE flashcard_id IS NOT NULL;

-- 4. Remove the old constraints that are no longer needed
-- The V7 migration constraints may need to be removed if they exist
-- ALTER TABLE progress DROP FOREIGN KEY FK_PROGRESS_ON_AGE IF EXISTS;
