-- Add delete_at property to users table
ALTER TABLE users
ADD COLUMN delete_at TIMESTAMP;

-- Add created_at property to users table
ALTER TABLE users
ADD COLUMN created_at TIMESTAMP;