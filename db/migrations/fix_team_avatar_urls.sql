-- Migration: Fix team member avatar URLs
-- Step 1: Fix absolute localhost URLs → relative paths
-- Step 2: Update .jpg → .webp extension
-- Run this against existing production databases
-- Date: 2026-07-09

-- Step 1: Fix absolute URLs (if not already done)
UPDATE teamintroduction
SET avatarurl = REPLACE(avatarurl, 'http://localhost:8086/files/', '/api/files/')
WHERE avatarurl LIKE 'http://localhost:8086/files/%';

-- Step 2: Update extension from .jpg to .webp
UPDATE teamintroduction
SET avatarurl = REPLACE(avatarurl, '.jpg', '.webp')
WHERE avatarurl LIKE '%.jpg';

-- Verify
SELECT id, name, avatarurl FROM teamintroduction;
