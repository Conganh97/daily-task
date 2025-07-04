-- Insert sample users for testing
INSERT INTO users (username, created_at, updated_at) VALUES
('testuser1', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('testuser2', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('admin', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert sample tasks for testing
INSERT INTO tasks (user_id, title, description, completed, starred, date, created_at, updated_at) VALUES
(1, 'Complete project documentation', 'Write comprehensive documentation for the daily task project', false, true, CURRENT_DATE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(1, 'Review code changes', 'Review and approve pending code changes', true, false, CURRENT_DATE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(1, 'Team meeting', 'Weekly team sync meeting', false, false, CURRENT_DATE + INTERVAL '1 day', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 'Exercise', 'Morning workout routine', true, true, CURRENT_DATE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 'Read book', 'Continue reading technical book', false, true, CURRENT_DATE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert sample reflections for testing
INSERT INTO reflections (user_id, date, energy_rating, reflection_text, created_at, updated_at) VALUES
(1, CURRENT_DATE, 8, 'Had a productive day working on the project. Made good progress on the backend implementation.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(1, CURRENT_DATE - INTERVAL '1 day', 6, 'Moderate energy day. Some challenges but overall positive.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, CURRENT_DATE, 9, 'Excellent day! Exercise in the morning boosted my energy throughout the day.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert sample energy assessments for testing
INSERT INTO energy_assessments (user_id, date, energy_level, created_at, updated_at) VALUES
(1, CURRENT_DATE, 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(1, CURRENT_DATE - INTERVAL '1 day', 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, CURRENT_DATE, 5, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, CURRENT_DATE - INTERVAL '1 day', 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP); 