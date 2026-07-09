-- Migration: Fix team member avatar URLs from absolute localhost to relative paths
-- Run this against existing production databases that were seeded with the old data
-- Date: 2026-07-09

UPDATE teamintroduction
SET avatarurl = REPLACE(avatarurl, 'http://localhost:8086/files/', '/api/files/')
WHERE avatarurl LIKE 'http://localhost:8086/files/%';

-- Verify
SELECT id, name, avatarurl FROM teamintroduction;
