-- Railway MySQL: database pre-selected

-- ============================================
-- 示例用户数据 (微信小程序用户)
-- ============================================
INSERT INTO user (open_id, nickname, avatar_url, password_hash, total_points, total_queries, total_answers, correct_answers, consecutive_days, last_login_date) VALUES
('oTest001_GreenGuide_Demo_User_001', '环保小卫士', '/static/avatar/default_01.png', '$2b$12$KUocnf3cVROEYjWxWZ/Ok.8T80RLDZqkvIt8NnFfmgnkxOJBHNDaS', 1250, 45, 68, 58, 15, '2026-06-03'),
('oTest002_GreenGuide_Demo_User_002', '垃圾分类达人', '/static/avatar/default_02.png', '$2b$12$KUocnf3cVROEYjWxWZ/Ok.8T80RLDZqkvIt8NnFfmgnkxOJBHNDaS', 980, 32, 52, 46, 10, '2026-06-02'),
('oTest003_GreenGuide_Demo_User_003', '绿色地球', '/static/avatar/default_03.png', '$2b$12$KUocnf3cVROEYjWxWZ/Ok.8T80RLDZqkvIt8NnFfmgnkxOJBHNDaS', 520, 20, 30, 22, 5, '2026-06-01'),
('oTest004_GreenGuide_Demo_User_004', '环保先锋', '/static/avatar/default_04.png', '$2b$12$KUocnf3cVROEYjWxWZ/Ok.8T80RLDZqkvIt8NnFfmgnkxOJBHNDaS', 2100, 78, 120, 108, 30, '2026-06-03'),
('oTest005_GreenGuide_Demo_User_005', '零废弃生活家', '/static/avatar/default_05.png', '$2b$12$KUocnf3cVROEYjWxWZ/Ok.8T80RLDZqkvIt8NnFfmgnkxOJBHNDaS', 780, 28, 40, 35, 8, '2026-05-30'),
('oTest006_GreenGuide_Demo_User_006', '蓝天白云', '/static/avatar/default_06.png', '$2b$12$KUocnf3cVROEYjWxWZ/Ok.8T80RLDZqkvIt8NnFfmgnkxOJBHNDaS', 150, 12, 15, 10, 3, '2026-06-03'),
('oTest007_GreenGuide_Demo_User_007', '爱回收的小明', '/static/avatar/default_07.png', '$2b$12$KUocnf3cVROEYjWxWZ/Ok.8T80RLDZqkvIt8NnFfmgnkxOJBHNDaS', 3200, 120, 200, 185, 60, '2026-06-03'),
('oTest008_GreenGuide_Demo_User_008', '清新空气', '/static/avatar/default_08.png', '$2b$12$KUocnf3cVROEYjWxWZ/Ok.8T80RLDZqkvIt8NnFfmgnkxOJBHNDaS', 430, 18, 24, 18, 6, '2026-06-02'),
('oTest009_GreenGuide_Demo_User_009', '地球守护者', '/static/avatar/default_09.png', '$2b$12$KUocnf3cVROEYjWxWZ/Ok.8T80RLDZqkvIt8NnFfmgnkxOJBHNDaS', 1650, 55, 85, 72, 22, '2026-06-03'),
('oTest010_GreenGuide_Demo_User_010', '绿色小达人', '/static/avatar/default_10.png', '$2b$12$KUocnf3cVROEYjWxWZ/Ok.8T80RLDZqkvIt8NnFfmgnkxOJBHNDaS', 350, 15, 20, 16, 4, '2026-06-01');

COMMIT;
