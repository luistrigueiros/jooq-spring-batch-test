DROP TABLE IF EXISTS author_book, author, book;

CREATE TABLE author
(
    id         INT         NOT NULL PRIMARY KEY AUTO_INCREMENT,
    first_name VARCHAR(50),
    last_name  VARCHAR(50) NOT NULL
);

CREATE TABLE book
(
    id    INT          NOT NULL PRIMARY KEY,
    title VARCHAR(100) NOT NULL
);

CREATE TABLE author_book
(
    author_id INT NOT NULL,
    book_id   INT NOT NULL,

    PRIMARY KEY (author_id, book_id),
    CONSTRAINT fk_ab_author FOREIGN KEY (author_id) REFERENCES author (id),
    CONSTRAINT fk_ab_book FOREIGN KEY (book_id) REFERENCES book (id)
);

DROP TABLE IF EXISTS people;
CREATE TABLE people
(
    person_id  BIGINT IDENTITY NOT NULL PRIMARY KEY,
    first_name VARCHAR(20),
    last_name  VARCHAR(20)
);
