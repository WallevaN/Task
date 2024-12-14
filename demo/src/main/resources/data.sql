INSERT INTO users (username, password, role) VALUES ('admin', '{bcrypt}admin123', 'ROLE_USER');
INSERT INTO categories (name) VALUES ('Work'), ('School'), ('Leisure');
INSERT INTO events (title, description, date) VALUES ('Meeting', 'Project discussion', '2024-12-13');
INSERT INTO event_categories (event_id, category_id) VALUES (1, 1); -- Tapahtuma kuuluu kategoriaan "Work"
