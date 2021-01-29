CREATE TABLE IF NOT EXISTS dbnerd_1.hibernate_sequence (
	next_val BIGINT
);

CREATE TABLE IF NOT EXISTS dbnerd_1.contact_book (
	id INTEGER PRIMARY KEY,
    name VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS dbnerd_1.contact (
	id INTEGER PRIMARY KEY,
    contact_book_id INTEGER,
    name VARCHAR(255),
    last_name VARCHAR(255),
    email VARCHAR(255),
    address VARCHAR(255),
    phone VARCHAR(255),
    FOREIGN KEY (contact_book_id)
		REFERENCES contact_book (id)
        ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS dbnerd_1.csv_record (
    id INTEGER PRIMARY KEY,
    name VARCHAR(255),
    value_separator CHAR,
    line_separator CHAR,
    text_separator CHAR,
    first_line_as_name BOOL,
    lowercase_tags BOOL,
    content VARCHAR(1024)
);

INSERT INTO dbnerd_1.hibernate_sequence values (0);
