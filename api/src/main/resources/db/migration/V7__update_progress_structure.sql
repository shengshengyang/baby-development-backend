-- V7: Update progress table structure - remove category_id and add milestone_id, age_id
-- This migration removes the direct category relationship and adds milestone relationship

-- 1. Add new columns first (check if they don't exist)
-- Add milestone_id if it doesn't exist
SET @column_exists = (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
                     WHERE TABLE_SCHEMA = DATABASE()
                     AND TABLE_NAME = 'progress'
                     AND COLUMN_NAME = 'milestone_id');

SET @sql = IF(@column_exists = 0,
             'ALTER TABLE progress ADD COLUMN milestone_id BINARY(16)',
             'SELECT "milestone_id column already exists"');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- Add age_id if it doesn't exist
SET @column_exists = (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
                     WHERE TABLE_SCHEMA = DATABASE()
                     AND TABLE_NAME = 'progress'
                     AND COLUMN_NAME = 'age_id');

SET @sql = IF(@column_exists = 0,
             'ALTER TABLE progress ADD COLUMN age_id BINARY(16)',
             'SELECT "age_id column already exists"');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- 2. Update existing data - set milestone_id and age_id based on existing flashcard relationships
-- Since flashcards already have milestone_id, we can use that relationship
UPDATE progress p
JOIN flashcards f ON p.flashcard_id = f.id
SET p.milestone_id = f.milestone_id
WHERE p.milestone_id IS NULL;

-- Update age_id based on milestone's age (assuming milestones have age_id or age_in_months)
UPDATE progress p
JOIN milestones m ON p.milestone_id = m.id
SET p.age_id = m.age_id
WHERE p.age_id IS NULL AND m.age_id IS NOT NULL;

-- 3. Add foreign key constraints for new columns (if they don't exist)
-- Check if milestone constraint exists
SET @constraint_exists = (SELECT COUNT(*) FROM INFORMATION_SCHEMA.TABLE_CONSTRAINTS
                         WHERE TABLE_SCHEMA = DATABASE()
                         AND TABLE_NAME = 'progress'
                         AND CONSTRAINT_NAME = 'FK_PROGRESS_ON_MILESTONE');

SET @sql = IF(@constraint_exists = 0,
             'ALTER TABLE progress ADD CONSTRAINT FK_PROGRESS_ON_MILESTONE FOREIGN KEY (milestone_id) REFERENCES milestones (id)',
             'SELECT "FK_PROGRESS_ON_MILESTONE constraint already exists"');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- Check if age constraint exists
SET @constraint_exists = (SELECT COUNT(*) FROM INFORMATION_SCHEMA.TABLE_CONSTRAINTS
                         WHERE TABLE_SCHEMA = DATABASE()
                         AND TABLE_NAME = 'progress'
                         AND CONSTRAINT_NAME = 'FK_PROGRESS_ON_AGE');

SET @sql = IF(@constraint_exists = 0,
             'ALTER TABLE progress ADD CONSTRAINT FK_PROGRESS_ON_AGE FOREIGN KEY (age_id) REFERENCES ages (id)',
             'SELECT "FK_PROGRESS_ON_AGE constraint already exists"');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- 4. Make the new columns NOT NULL after data is populated (only if they exist and have data)
-- This step is optional and can be done later if needed

-- 5. Remove the old category foreign key constraint and column (if they exist)
SET @constraint_exists = (SELECT COUNT(*) FROM INFORMATION_SCHEMA.TABLE_CONSTRAINTS
                         WHERE TABLE_SCHEMA = DATABASE()
                         AND TABLE_NAME = 'progress'
                         AND CONSTRAINT_NAME = 'FK_PROGRESS_ON_CATEGORY');

SET @sql = IF(@constraint_exists > 0,
             'ALTER TABLE progress DROP FOREIGN KEY FK_PROGRESS_ON_CATEGORY',
             'SELECT "FK_PROGRESS_ON_CATEGORY constraint does not exist"');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @column_exists = (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
                     WHERE TABLE_SCHEMA = DATABASE()
                     AND TABLE_NAME = 'progress'
                     AND COLUMN_NAME = 'category_id');

SET @sql = IF(@column_exists > 0,
             'ALTER TABLE progress DROP COLUMN category_id',
             'SELECT "category_id column does not exist"');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- 6. Remove the age_in_months column as it's now redundant (if it exists)
SET @column_exists = (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
                     WHERE TABLE_SCHEMA = DATABASE()
                     AND TABLE_NAME = 'progress'
                     AND COLUMN_NAME = 'age_in_months');

SET @sql = IF(@column_exists > 0,
             'ALTER TABLE progress DROP COLUMN age_in_months',
             'SELECT "age_in_months column does not exist"');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
