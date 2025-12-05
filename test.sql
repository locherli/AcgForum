USE db_forum;

-- ==========================================
-- 1. 插入用户数据 (user_info)
-- hc_password 是 BIGINT，这里使用简单的数字模拟哈希值
-- ==========================================
INSERT INTO user_info (userName, email, phoneNum, hc_password, gender, age, self_intro, avatar) VALUES 
('Admin', 'admin@forum.com', '13800138000', 99999999, 1, 30, '系统管理员', 'admin_face.jpg'),
('TechGuru', 'tech@forum.com', '13900139000', 12345678, 1, 28, '热爱技术的极客', 'tech.png'),
('ArtLover', 'art@forum.com', '13700137000', 87654321, 0, 24, '生活不仅有眼前的苟且', 'flower.jpg'),
('Newbie', 'new@forum.com', '13600136000', 11111111, 1, 18, '萌新报到，请多关照', 'default.png'),
('Lurker', 'lurker@forum.com', '13500135000', 00000000, 0, 35, '只看不说话', 'ghost.png');

-- ==========================================
-- 2. 插入标签数据 (tag)
-- ==========================================
INSERT INTO tag (synonym, tag_name) VALUES 
(NULL, 'Java'),
(NULL, 'Python'),
(NULL, '生活'),
(NULL, '摄影'),
(NULL, '提问');

-- ==========================================
-- 3. 插入板块/论坛数据 (forum)
-- owner 对应 user_info 的 id
-- ==========================================
INSERT INTO forum (name, introduction, owner, postNum, userNum, icon) VALUES 
('技术交流', '讨论编程、硬件与黑科技', 2, 0, 0, 'cpu.png'),
('艺术画廊', '分享摄影、绘画与设计', 3, 0, 0, 'palette.png'),
('灌水专区', '闲聊八卦，轻松一刻', 1, 0, 0, 'water.png');

-- ==========================================
-- 4. 插入帖子数据 (post) - 主题帖 (isRoot = TRUE)
-- 注意：date 使用 NOW() 函数，部分减去时间以测试 "本周/本月" 视图
-- ==========================================

-- 帖子 1: 用户2 在 论坛1 发帖
INSERT INTO post (authorId, isRoot, root, title, date, likeNum, commentNum, content, forum) VALUES 
(2, TRUE, NULL, 'Java 21 新特性解析', NOW(), 10, 2, '这是关于虚拟线程的深度解析...', 1);

-- 帖子 2: 用户3 在 论坛2 发帖 (时间设为 2 天前)
INSERT INTO post (authorId, isRoot, root, title, date, likeNum, commentNum, content, forum) VALUES 
(3, TRUE, NULL, '周末去公园拍的照片', DATE_SUB(NOW(), INTERVAL 2 DAY), 50, 0, '风景太美了，分享给大家...', 2);

-- 帖子 3: 用户4 在 论坛1 发帖 (求助帖)
INSERT INTO post (authorId, isRoot, root, title, date, likeNum, commentNum, content, forum) VALUES 
(4, TRUE, NULL, 'Python 环境配置报错，求助！', NOW(), 1, 1, '安装 pip 的时候出现了这个错误...', 1);

-- 帖子 4: 用户2 在 论坛3 发帖 (时间设为 40 天前，测试月榜视图)
INSERT INTO post (authorId, isRoot, root, title, date, likeNum, commentNum, content, forum) VALUES 
(2, TRUE, NULL, '老帖子：去年的回顾', DATE_SUB(NOW(), INTERVAL 40 DAY), 100, 5, '时间过得真快啊...', 3);

-- ==========================================
-- 5. 插入帖子数据 (post) - 回复/评论 (isRoot = FALSE)
-- root 字段指向对应的主题帖 ID
-- ==========================================

-- 回复 帖子 1
INSERT INTO post (authorId, isRoot, root, title, date, likeNum, commentNum, content, forum) VALUES 
(4, FALSE, 1, NULL, NOW(), 2, 0, '大佬牛逼，学到了！', 1),
(3, FALSE, 1, NULL, NOW(), 1, 0, '不明觉厉。', 1);

-- 回复 帖子 3
INSERT INTO post (authorId, isRoot, root, title, date, likeNum, commentNum, content, forum) VALUES 
(2, FALSE, 3, NULL, NOW(), 5, 0, '检查一下环境变量 Path 设置。', 1);

-- ==========================================
-- 6. 插入用户关注关系 (ref_self_fan)
-- id = 被关注者, id_fan = 粉丝
-- ==========================================
INSERT INTO ref_self_fan (id, id_fan) VALUES 
(2, 4), -- 新手 关注了 技术大佬
(2, 3), -- 艺术爱好者 关注了 技术大佬
(3, 1), -- 管理员 关注了 艺术爱好者
(1, 2); -- 技术大佬 关注了 管理员

-- ==========================================
-- 7. 插入用户与标签的兴趣关系 (user_tag)
-- ==========================================
INSERT INTO user_tag (id, tag) VALUES 
(2, 1), -- TechGuru 关注 Java
(2, 2), -- TechGuru 关注 Python
(3, 4); -- ArtLover 关注 摄影

-- ==========================================
-- 8. 插入帖子与标签的关系 (post_tag)
-- ==========================================
INSERT INTO post_tag (id, tag) VALUES 
(1, 1), -- 帖子1 (Java新特性) - 标签 Java
(2, 3), -- 帖子2 (照片) - 标签 生活
(2, 4), -- 帖子2 (照片) - 标签 摄影
(3, 2), -- 帖子3 (Python求助) - 标签 Python
(3, 5); -- 帖子3 (Python求助) - 标签 提问

-- ==========================================
-- 9. 插入用户收藏 (user_collection)
-- ==========================================
INSERT INTO user_collection (user, post) VALUES 
(4, 1), -- 新手 收藏了 Java教程
(3, 2); -- 艺术爱好者 收藏了 自己的照片贴

-- ==========================================
-- 10. 插入用户点赞记录 (user_likedPost)
-- 确保这与 post 表中的 likeNum 逻辑上大概一致（虽然实际开发中需要触发器或代码维护计数）
-- ==========================================
INSERT INTO user_likedPost (user, post) VALUES 
(4, 1),
(3, 1),
(1, 2),
(2, 2),
(4, 2);

-- ==========================================
-- 11. 插入板块成员 (forum_member)
-- 记录用户加入了哪些板块
-- ==========================================
INSERT INTO forum_member (forum, user) VALUES 
(1, 2), -- TechGuru 加入 技术版
(1, 4), -- Newbie 加入 技术版
(2, 3), -- ArtLover 加入 艺术版
(3, 1), -- Admin 加入 灌水版
(3, 5); -- Lurker 加入 灌水版

-- ==========================================
-- 更新计数器 (模拟业务逻辑)
-- 既然数据是硬插入的，我们需要手动更新 forum 表的计数器以保持数据一致性
-- ==========================================
UPDATE forum SET postNum = 4, userNum = 2 WHERE id = 1; -- 技术版：4个贴(含回复)，2个成员
UPDATE forum SET postNum = 1, userNum = 1 WHERE id = 2; -- 艺术版
UPDATE forum SET postNum = 1, userNum = 2 WHERE id = 3; -- 灌水版