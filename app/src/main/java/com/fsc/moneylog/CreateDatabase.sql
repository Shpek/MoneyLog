CREATE TABLE categories(
    id INTEGER PRIMARY KEY,
    name TEXT NOT NULL
);

INSERT INTO categories(name)
VALUES ('Rent'), ('Debt'), ('Bills'), ('Transport'), ('Food');

CREATE TABLE log_records(
    id INTEGER PRIMARY KEY,
    category_id INTEGER REFERENCES categories(id) ON DELETE SET NULL ON UPDATE CASCADE,

    amount INTEGER NOT NULL,
    comment TEXT NOT NULL,
    date_added DATETIME NOT NULL
);
