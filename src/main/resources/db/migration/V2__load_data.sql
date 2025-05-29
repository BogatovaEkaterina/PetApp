
delete from users;
delete from pets;

-- Вставка тестовых пользователей
INSERT INTO users (name, login, password) VALUES
('Алексей Петров', 'alex_petrov', 'password123'),
('Мария Иванова', 'maria_ivanova', 'password456');

-- Вставка тестовых питомцев
INSERT INTO pets (name, level_of_hunger, level_of_thirst, level_of_pollution, level_of_fatigue, mood, last_time, user_id) VALUES
('Барсик', 80.0, 70.0, 30.0, 60.0, 90.0, CURRENT_TIMESTAMP, 1),
('Мурка', 60.0, 80.0, 20.0, 40.0, 85.0, CURRENT_TIMESTAMP, 1),
('Рекс', 90.0, 75.0, 15.0, 55.0, 95.0, CURRENT_TIMESTAMP, 2);
