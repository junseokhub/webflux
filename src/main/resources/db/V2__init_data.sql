-- ADMIN 계정 생성 (중복이면 무시)
INSERT IGNORE INTO users (username, password, role, created_at, updated_at)
VALUES ('admin', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lh3y', 'ADMIN', NOW(), NOW());

-- 쿠폰 생성 (중복이면 무시)
INSERT IGNORE INTO coupons (name, total_stock, remaining_stock, created_at, updated_at)
VALUES ('신규가입 쿠폰', 100, 100, NOW(), NOW());