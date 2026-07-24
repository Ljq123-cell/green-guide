
-- ============================================
-- GreenGuide AI 数据库初始化脚本
-- 版本: V1.0
-- 日期: 2026-06-03
-- ============================================

-- Database created by Railway MySQL addon


-- ============================================
-- 1. 管理员用户表
-- ============================================
CREATE TABLE IF NOT EXISTS admin_user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(64) NOT NULL UNIQUE COMMENT '用户名',
    password VARCHAR(255) NOT NULL COMMENT '密码(BCrypt)',
    nickname VARCHAR(64) COMMENT '显示昵称',
    role VARCHAR(32) NOT NULL DEFAULT 'ADMIN' COMMENT '角色: ADMIN/EDITOR',
    status TINYINT NOT NULL DEFAULT 1 COMMENT '状态: 0-禁用, 1-启用',
    last_login_time DATETIME COMMENT '最后登录时间',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='管理员用户表';

-- 默认管理员: admin / admin123
INSERT INTO admin_user (username, password, nickname, role) VALUES
('admin', '$2a$10$lDzx/fz/Mu5EcSAr0yqrouIHuI.TiB7SyitGuQb5Zg6wT.NMWfwcW', '系统管理员', 'ADMIN');

-- ============================================
-- 2. 普通用户表 (微信小程序用户)
-- ============================================
CREATE TABLE IF NOT EXISTS user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    open_id VARCHAR(128) NOT NULL UNIQUE COMMENT '微信OpenID',
    union_id VARCHAR(128) COMMENT '微信UnionID',
    nickname VARCHAR(64) COMMENT '昵称',
    avatar_url VARCHAR(512) COMMENT '头像URL',
    password_hash VARCHAR(255) COMMENT '密码(BCrypt)',
    total_points INT NOT NULL DEFAULT 0 COMMENT '总积分',
    total_queries INT NOT NULL DEFAULT 0 COMMENT '总查询次数',
    total_answers INT NOT NULL DEFAULT 0 COMMENT '总答题次数',
    correct_answers INT NOT NULL DEFAULT 0 COMMENT '正确答题数',
    consecutive_days INT NOT NULL DEFAULT 0 COMMENT '连续登录天数',
    last_login_date DATE COMMENT '最后登录日期',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted TINYINT NOT NULL DEFAULT 0,
    INDEX idx_open_id (open_id),
    INDEX idx_total_points (total_points DESC)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- ============================================
-- 3. 科普知识文章表 (US-601)
-- ============================================
CREATE TABLE IF NOT EXISTS knowledge_article (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL COMMENT '标题',
    summary VARCHAR(512) COMMENT '摘要',
    content TEXT NOT NULL COMMENT '正文(富文本)',
    category VARCHAR(32) NOT NULL COMMENT '垃圾类别: RECYCLABLE/HARMFUL/KITCHEN/OTHER',
    cover_image VARCHAR(512) COMMENT '封面图URL',
    source VARCHAR(255) COMMENT '来源',
    tags VARCHAR(255) COMMENT '标签,逗号分隔',
    view_count INT NOT NULL DEFAULT 0 COMMENT '浏览量',
    status VARCHAR(16) NOT NULL DEFAULT 'DRAFT' COMMENT '状态: DRAFT/PUBLISHED',
    published_at DATETIME COMMENT '发布时间',
    created_by BIGINT COMMENT '创建人(管理员ID)',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted TINYINT NOT NULL DEFAULT 0,
    INDEX idx_category (category),
    INDEX idx_status (status),
    INDEX idx_published_at (published_at DESC),
    FULLTEXT INDEX ft_title_content (title, content)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='科普知识文章表';

-- ============================================
-- 4. 题库表 (US-602)
-- ============================================
CREATE TABLE IF NOT EXISTS quiz_question (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    stem TEXT NOT NULL COMMENT '题干',
    question_type VARCHAR(32) NOT NULL DEFAULT 'SINGLE_CHOICE' COMMENT '题型: SINGLE_CHOICE/MULTIPLE_CHOICE/TRUE_FALSE',
    options JSON NOT NULL COMMENT '选项列表 [{"key":"A","text":"可回收物"},...]',
    correct_answer VARCHAR(32) NOT NULL COMMENT '正确答案(如A或A,C)',
    difficulty VARCHAR(16) NOT NULL DEFAULT 'BEGINNER' COMMENT '难度: BEGINNER/INTERMEDIATE/CHALLENGE',
    explanation TEXT COMMENT '知识解析',
    category_tag VARCHAR(64) COMMENT '分类标签(对应垃圾类别)',
    usage_count INT NOT NULL DEFAULT 0 COMMENT '被作答次数',
    correct_count INT NOT NULL DEFAULT 0 COMMENT '正确次数',
    status VARCHAR(16) NOT NULL DEFAULT 'DRAFT' COMMENT '状态: DRAFT/PUBLISHED',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted TINYINT NOT NULL DEFAULT 0,
    INDEX idx_difficulty (difficulty),
    INDEX idx_category_tag (category_tag),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='题库表';

-- ============================================
-- 5. 答题记录表
-- ============================================
CREATE TABLE IF NOT EXISTS quiz_record (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL COMMENT '用户ID',
    question_id BIGINT NOT NULL COMMENT '题目ID',
    user_answer VARCHAR(32) NOT NULL COMMENT '用户答案',
    is_correct TINYINT NOT NULL COMMENT '是否正确: 0-错误, 1-正确',
    duration_seconds INT COMMENT '答题耗时(秒)',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_user_id (user_id),
    INDEX idx_question_id (question_id),
    INDEX idx_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='答题记录表';

-- ============================================
-- 6. 用户反馈表 (US-603)
-- ============================================
CREATE TABLE IF NOT EXISTS user_feedback (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL COMMENT '提交用户ID',
    feedback_type VARCHAR(32) NOT NULL COMMENT '类型: CLASSIFICATION_ERROR/CONTENT_ERROR/SUGGESTION/OTHER',
    content TEXT NOT NULL COMMENT '反馈内容',
    related_garbage_name VARCHAR(128) COMMENT '关联垃圾名称',
    image_url VARCHAR(512) COMMENT '反馈图片',
    status VARCHAR(16) NOT NULL DEFAULT 'PENDING' COMMENT '状态: PENDING/PROCESSED/IGNORED',
    handler_id BIGINT COMMENT '处理人(管理员ID)',
    handler_note TEXT COMMENT '处理备注',
    processed_at DATETIME COMMENT '处理时间',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_user_id (user_id),
    INDEX idx_status (status),
    INDEX idx_created_at (created_at DESC)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户反馈表';

-- ============================================
-- 7. 学习记录表 (US-501)
-- ============================================
CREATE TABLE IF NOT EXISTS learning_record (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL COMMENT '用户ID',
    record_type VARCHAR(32) NOT NULL COMMENT '类型: SEARCH/PHOTO_RECOGNIZE/QA/QUIZ/READ_ARTICLE',
    target_id BIGINT COMMENT '关联目标ID(题目ID或文章ID)',
    target_name VARCHAR(255) COMMENT '关联目标名称',
    result_category VARCHAR(32) COMMENT '查询/识别结果的垃圾类别',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_user_created (user_id, created_at DESC),
    INDEX idx_record_type (record_type),
    INDEX idx_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='学习记录表';

-- ============================================
-- 8. 积分记录表
-- ============================================
CREATE TABLE IF NOT EXISTS user_points (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL COMMENT '用户ID',
    points INT NOT NULL COMMENT '积分变动(正数为获得,负数为扣除)',
    reason VARCHAR(64) NOT NULL COMMENT '原因: SEARCH/ANSWER_CORRECT/CONSECUTIVE_LOGIN/ACHIEVEMENT_BONUS',
    related_id BIGINT COMMENT '关联记录ID',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_user_created (user_id, created_at DESC)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='积分记录表';

-- ============================================
-- 9. 成就定义表
-- ============================================
CREATE TABLE IF NOT EXISTS achievement (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(64) NOT NULL COMMENT '成就名称',
    description VARCHAR(255) NOT NULL COMMENT '成就描述',
    icon VARCHAR(255) COMMENT '成就图标URL',
    condition_type VARCHAR(32) NOT NULL COMMENT '条件类型: FIRST_RECOGNIZE/RECOGNIZE_COUNT/TOTAL_POINTS/ANSWER_ACCURACY/CONSECUTIVE_DAYS',
    condition_value INT NOT NULL DEFAULT 1 COMMENT '解锁阈值',
    points_reward INT NOT NULL DEFAULT 0 COMMENT '奖励积分',
    sort_order INT NOT NULL DEFAULT 0 COMMENT '排序顺序',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='成就定义表';

-- 初始化默认成就
INSERT INTO achievement (name, description, icon, condition_type, condition_value, points_reward, sort_order) VALUES
('分类新手', '完成首次垃圾分类识别', NULL, 'FIRST_RECOGNIZE', 1, 10, 1),
('分类达人', '累计完成100次垃圾分类识别', NULL, 'RECOGNIZE_COUNT', 100, 50, 2),
('环保先锋', '累计获得1000积分', NULL, 'TOTAL_POINTS', 1000, 100, 3),
('学霸', '答题正确率达到90%', NULL, 'ANSWER_ACCURACY', 90, 80, 4),
('全勤标兵', '连续登录30天', NULL, 'CONSECUTIVE_DAYS', 30, 150, 5);

-- ============================================
-- 10. 用户成就表
-- ============================================
CREATE TABLE IF NOT EXISTS user_achievement (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL COMMENT '用户ID',
    achievement_id BIGINT NOT NULL COMMENT '成就ID',
    progress INT NOT NULL DEFAULT 0 COMMENT '当前进度',
    unlocked_at DATETIME COMMENT '解锁时间',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uk_user_achievement (user_id, achievement_id),
    INDEX idx_user_id (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户成就表';

-- ============================================
-- 11. 垃圾分类知识库表 (基础分类数据)
-- ============================================
CREATE TABLE IF NOT EXISTS garbage_category (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(128) NOT NULL COMMENT '垃圾名称',
    category VARCHAR(32) NOT NULL COMMENT '所属类别: RECYCLABLE/HARMFUL/KITCHEN/OTHER',
    description TEXT COMMENT '分类依据说明',
    disposal_guide TEXT COMMENT '投放指导',
    common_examples TEXT COMMENT '常见示例',
    is_active TINYINT NOT NULL DEFAULT 1 COMMENT '是否启用',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY uk_name (name),
    INDEX idx_category (category),
    FULLTEXT INDEX ft_name_desc (name, description)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='垃圾分类知识库表';

-- 初始化常见垃圾分类数据
INSERT INTO garbage_category (name, category, description, disposal_guide) VALUES
('废纸', 'RECYCLABLE', '包括报纸、期刊、图书、各类包装纸等，但不包括纸巾和卫生用纸', '尽量叠放整齐，避免揉团，投放到废纸类回收箱'),
('塑料瓶', 'RECYCLABLE', '包括矿泉水瓶、饮料瓶等PET塑料制品', '清空内容物，压扁后投放，瓶盖与瓶身分开投放'),
('废电池', 'HARMFUL', '包括充电电池、纽扣电池、铅酸电池等含重金属的电池', '投入有害垃圾收集容器，避免破损泄漏'),
('剩菜剩饭', 'KITCHEN', '包括家庭厨房产生的剩菜剩饭、菜根菜叶等', '沥干水分后投入厨余垃圾桶，不要混入牙签、餐巾纸等'),
('破碎陶瓷', 'OTHER', '破碎的陶瓷碗碟不属于可回收物，因为陶瓷的回收再利用工艺复杂', '用纸包好后投入其他垃圾桶，避免划伤清洁人员');

COMMIT;
