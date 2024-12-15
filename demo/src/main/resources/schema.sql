-- Käyttäjätaulu autentikointiin
CREATE TABLE users (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       username VARCHAR(50) UNIQUE NOT NULL,
                       password VARCHAR(100) NOT NULL,
                       role VARCHAR(20) NOT NULL,
                       email VARCHAR(100), -- Valinnainen
                       enabled BOOLEAN DEFAULT TRUE -- Käyttäjän tila
);

-- Kategoriataulu
CREATE TABLE categories (
                            id BIGINT AUTO_INCREMENT PRIMARY KEY,
                            name VARCHAR(50) UNIQUE NOT NULL
);

-- Tapahtumataulu
CREATE TABLE events (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        title VARCHAR(100) NOT NULL,
                        description TEXT,
                        date DATE NOT NULL,
                        image_url VARCHAR(255), -- Valinnainen: linkki kuvaan
                        link_url VARCHAR(255) -- Valinnainen: linkki lisätietoihin
);

-- Liitostaulu tapahtumien ja kategorioiden välillä
CREATE TABLE event_categories
(
    event_id    BIGINT NOT NULL,
    category_id BIGINT NOT NULL,
    PRIMARY KEY (event_id, category_id),
    FOREIGN KEY (event_id) REFERENCES events (id) ON DELETE CASCADE,
    FOREIGN KEY (category_id) REFERENCES categories (id) ON DELETE CASCADE

);
