CREATE TABLE notes (
     id INTEGER PRIMARY KEY ,
     title VARCHAR(255),
     content TEXT
);

INSERT INTO notes (title, content) VALUES
      ('Заголовок заметки 1', 'Содержание заметки 1'),
      ('Заголовок заметки 2', 'Содержание заметки 2'),
      ('Заголовок заметки 3', 'Содержание заметки 3');
