-- Enable extensions
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Create indexes for better performance
CREATE INDEX idx_job_status_publish_date ON job(status, publish_date);
CREATE INDEX idx_job_employer_id ON job(employer_id);
CREATE INDEX idx_job_city_id ON job(city_id);
CREATE INDEX idx_job_industry_id ON job(industry_id);
CREATE INDEX idx_job_created_at ON job(created_at DESC);

CREATE INDEX idx_job_translation_job_id_language ON job_translation(job_id, language);
CREATE INDEX idx_job_application_job_id ON job_application(job_id);
CREATE INDEX idx_job_application_candidate_id ON job_application(candidate_id);

CREATE INDEX idx_employer_phone ON employers(phone);
CREATE INDEX idx_candidate_phone ON candidates(phone);
CREATE INDEX idx_admin_phone ON admins(phone);

-- Add composite indexes for common queries
CREATE INDEX idx_job_search ON job(status, city_id, industry_id, hebrew_required, transportation_available);

-- Add constraints
ALTER TABLE job_application ADD CONSTRAINT uk_job_candidate UNIQUE (job_id, candidate_id);