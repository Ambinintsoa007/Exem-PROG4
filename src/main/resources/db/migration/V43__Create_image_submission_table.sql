create table if not exists image_submission
(
    id uuid primary key,
    file_name varchar not null,
    email varchar not null,
    created_at timestamp with time zone not null
);