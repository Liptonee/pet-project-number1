-- Очистка таблиц в правильном порядке (сначала зависимые, потом основные)
DELETE FROM user_projects;
DELETE FROM user_tasks;      -- если таблица существует
DELETE FROM comments;
DELETE FROM tasks;
DELETE FROM projects;
DELETE FROM users;

-- Сброс последовательностей (для PostgreSQL)
ALTER SEQUENCE users_id_seq RESTART WITH 1;
ALTER SEQUENCE projects_id_seq RESTART WITH 1;

-- 5 пользователей
INSERT INTO users (email, password, username, created_at, updated_at) VALUES
('alice@example.com', 'pass123', 'alice', NOW(), NOW()),
('bob@example.com', 'pass123', 'bob', NOW(), NOW()),
('charlie@example.com', 'pass123', 'charlie', NOW(), NOW()),
('diana@example.com', 'pass123', 'diana', NOW(), NOW()),
('eve@example.com', 'pass123', 'eve', NOW(), NOW());

-- 3 проекта (владельцы: Alice, Bob, Charlie)
INSERT INTO projects (name, description, owner_id, created_at, updated_at) VALUES
('Project Alpha', 'Описание проекта Alpha', 1, NOW(), NOW()),
('Project Beta',  'Описание проекта Beta',  2, NOW(), NOW()),
('Project Gamma', 'Описание проекта Gamma', 3, NOW(), NOW());

-- Участники проектов (таблица user_projects)
-- Project Alpha: Alice (владелец), Bob, Diana
INSERT INTO user_projects (user_id, project_id) VALUES
(1, 1),
(2, 1),
(4, 1);

-- Project Beta: Bob (владелец), Charlie, Eve
INSERT INTO user_projects (user_id, project_id) VALUES
(2, 2),
(3, 2),
(5, 2);

-- Project Gamma: Charlie (владелец), Alice, Eve
INSERT INTO user_projects (user_id, project_id) VALUES
(3, 3),
(1, 3),
(5, 3);