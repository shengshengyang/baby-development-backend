alter table flashcards
    drop column if exists category;
alter table milestones
    drop column if exists category;
alter table milestones
    add column if not exists catogory_id int null;
alter table progress
    drop column if exists category;
alter table progress
    add column if not exists category_id int null;
