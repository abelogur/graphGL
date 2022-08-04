create table if not exists book
(
    id    serial primary key,
    title varchar(1024) not null
);

create table if not exists author
(
    id   serial primary key,
    name varchar(1024) not null unique
);

create table if not exists book_author
(
    book_id   bigint references book (id),
    author_id bigint references author (id)
);