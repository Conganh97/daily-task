-- Create reflections table
CREATE TABLE reflections (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    date DATE NOT NULL,
    energy_rating INTEGER NOT NULL CHECK (energy_rating >= 1 AND energy_rating <= 10),
    reflection_text TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_reflections_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT uq_reflections_user_date UNIQUE (user_id, date)
);

-- Create indexes for efficient queries
CREATE INDEX idx_reflections_user_date ON reflections(user_id, date);
CREATE INDEX idx_reflections_date ON reflections(date);

-- Add comments
COMMENT ON TABLE reflections IS 'Reflections table for daily user reflections';
COMMENT ON COLUMN reflections.id IS 'Primary key';
COMMENT ON COLUMN reflections.user_id IS 'Foreign key to users table';
COMMENT ON COLUMN reflections.date IS 'Reflection date';
COMMENT ON COLUMN reflections.energy_rating IS 'Energy rating from 1 to 10';
COMMENT ON COLUMN reflections.reflection_text IS 'Reflection text content';
COMMENT ON COLUMN reflections.created_at IS 'Timestamp when reflection was created';
COMMENT ON COLUMN reflections.updated_at IS 'Timestamp when reflection was last updated'; 