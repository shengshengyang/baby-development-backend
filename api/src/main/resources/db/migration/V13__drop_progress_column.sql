alter table progress
    drop foreign key FK_PROGRESS_ON_AGE;

alter table progress
    drop column age_id;
