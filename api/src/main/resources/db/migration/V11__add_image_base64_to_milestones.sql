-- Add image_base64 column to milestones table for storing base64 encoded images
ALTER TABLE milestones ADD COLUMN image_base64 LONGTEXT;

