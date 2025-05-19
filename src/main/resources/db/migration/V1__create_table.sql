CREATE TABLE users(
    id INT PRIMARY KEY,
    name VARCHAR(100) UNIQUE NOT NULL,
    login VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(100) UNIQUE NOT NULL
);

CREATE TABLE pet(
    id INT PRIMARY KEY,
    name VARCHAR(100) UNIQUE NOT NULL,
    age INT DEFAULT 0,
    level_of_hunger REAL NOT NULL DEFAULT 100.0,
    level_of_thirst REAL NOT NULL DEFAULT 100.0,
    level_of_pollution REAL NOT NULL DEFAULT 100.0,
    level_of_fatigue REAL NOT NULL DEFAULT 100.0,
    mood REAL NOT NULL DEFAULT 100.0,
    last_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE user_pet(
    user_id INT REFERENCES users(id),
    pet_id INT REFERENCES pet(id),
    PRIMARY KEY (user_id, pet_id)
);

