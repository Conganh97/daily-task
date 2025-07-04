-- Create energy_assessments table
CREATE TABLE energy_assessments (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    date DATE NOT NULL,
    energy_level INTEGER NOT NULL CHECK (energy_level >= 1 AND energy_level <= 5),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_energy_assessments_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT uq_energy_assessments_user_date UNIQUE (user_id, date)
);

-- Create indexes for efficient queries
CREATE INDEX idx_energy_assessments_user_date ON energy_assessments(user_id, date);
CREATE INDEX idx_energy_assessments_date ON energy_assessments(date);

-- Add comments
COMMENT ON TABLE energy_assessments IS 'Energy assessments table for quick daily energy tracking';
COMMENT ON COLUMN energy_assessments.id IS 'Primary key';
COMMENT ON COLUMN energy_assessments.user_id IS 'Foreign key to users table';
COMMENT ON COLUMN energy_assessments.date IS 'Assessment date';
COMMENT ON COLUMN energy_assessments.energy_level IS 'Energy level from 1 to 5';
COMMENT ON COLUMN energy_assessments.created_at IS 'Timestamp when assessment was created';
COMMENT ON COLUMN energy_assessments.updated_at IS 'Timestamp when assessment was last updated'; 