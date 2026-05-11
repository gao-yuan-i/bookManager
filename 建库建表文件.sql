-- ============================================
-- 图书管理系统 数据库初始化脚本（修正版）
-- 所有用户密码均为 123456（BCrypt加密）
-- ============================================

-- 删除旧库（可选，首次执行不需要）
-- DROP DATABASE IF EXISTS book_manager;

-- 创建数据库
CREATE DATABASE IF NOT EXISTS book_manager 
    DEFAULT CHARACTER SET utf8mb4 
    DEFAULT COLLATE utf8mb4_unicode_ci;

USE book_manager;

-- ==================== 用户表 ====================
DROP TABLE IF EXISTS borrow_records;
DROP TABLE IF EXISTS books;
DROP TABLE IF EXISTS users;

CREATE TABLE users (
    id            BIGINT PRIMARY KEY AUTO_INCREMENT,
    username      VARCHAR(50)  NOT NULL UNIQUE,
    password      VARCHAR(255) NOT NULL,
    real_name     VARCHAR(50),
    phone         VARCHAR(20),
    email         VARCHAR(100),
    role          VARCHAR(20)  NOT NULL DEFAULT 'USER',
    status        TINYINT      NOT NULL DEFAULT 1,
    max_borrow    INT          NOT NULL DEFAULT 5,
    borrow_count  INT          NOT NULL DEFAULT 0,
    create_time   DATETIME     DEFAULT CURRENT_TIMESTAMP,
    update_time   DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ==================== 图书表 ====================
CREATE TABLE books (
    id                 BIGINT PRIMARY KEY AUTO_INCREMENT,
    isbn               VARCHAR(20)  UNIQUE,
    title              VARCHAR(100) NOT NULL,
    author             VARCHAR(100),
    publisher          VARCHAR(100),
    publish_date       DATE,
    category           VARCHAR(50),
    description        TEXT,
    cover_image        VARCHAR(255),
    total_quantity     INT          NOT NULL DEFAULT 1,
    available_quantity INT          NOT NULL DEFAULT 1,
    location           VARCHAR(50),
    status             TINYINT      NOT NULL DEFAULT 1,
    create_time        DATETIME     DEFAULT CURRENT_TIMESTAMP,
    update_time        DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ==================== 借阅记录表 ====================
CREATE TABLE borrow_records (
    id               BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id          BIGINT    NOT NULL,
    book_id          BIGINT    NOT NULL,
    borrow_date      DATETIME  NOT NULL,
    due_date         DATETIME  NOT NULL,
    return_date      DATETIME,
    status           TINYINT   NOT NULL DEFAULT 1,
    create_time      DATETIME  DEFAULT CURRENT_TIMESTAMP,
    update_time      DATETIME  DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (book_id) REFERENCES books(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ==================== 预插入用户数据（20条） ====================
-- 所有密码均为 123456 的标准 BCrypt 加密值
INSERT INTO users (username, password, real_name, phone, email, role, status, max_borrow, borrow_count) VALUES
('admin',    '$2a$10$fWbiCFTAQy2UkiDmHKDEw.YbbNhfacr/W7ePQXDp0GSuKc6ImcvJ.', '系统管理员', '13800000001', 'admin@book.com',    'ADMIN', 1, 5, 0),
('zhangsan', '$2a$10$fWbiCFTAQy2UkiDmHKDEw.YbbNhfacr/W7ePQXDp0GSuKc6ImcvJ.', '张三',       '13800000002', 'zhangsan@qq.com',  'USER',  1, 5, 0),
('lisi',     '$2a$10$fWbiCFTAQy2UkiDmHKDEw.YbbNhfacr/W7ePQXDp0GSuKc6ImcvJ.', '李四',       '13800000003', 'lisi@qq.com',      'USER',  1, 5, 0),
('wangwu',   '$2a$10$fWbiCFTAQy2UkiDmHKDEw.YbbNhfacr/W7ePQXDp0GSuKc6ImcvJ.', '王五',       '13800000004', 'wangwu@qq.com',    'USER',  1, 5, 0),
('zhaoliu',  '$2a$10$fWbiCFTAQy2UkiDmHKDEw.YbbNhfacr/W7ePQXDp0GSuKc6ImcvJ.', '赵六',       '13800000005', 'zhaoliu@qq.com',   'USER',  1, 5, 0),
('sunqi',    '$2a$10$fWbiCFTAQy2UkiDmHKDEw.YbbNhfacr/W7ePQXDp0GSuKc6ImcvJ.', '孙七',       '13800000006', 'sunqi@qq.com',     'USER',  1, 5, 0),
('zhouba',   '$2a$10$fWbiCFTAQy2UkiDmHKDEw.YbbNhfacr/W7ePQXDp0GSuKc6ImcvJ.', '周八',       '13800000007', 'zhouba@qq.com',    'USER',  1, 5, 0),
('wujiu',    '$2a$10$fWbiCFTAQy2UkiDmHKDEw.YbbNhfacr/W7ePQXDp0GSuKc6ImcvJ.', '吴九',       '13800000008', 'wujiu@qq.com',     'USER',  1, 5, 0),
('zhengshi', '$2a$10$fWbiCFTAQy2UkiDmHKDEw.YbbNhfacr/W7ePQXDp0GSuKc6ImcvJ.', '郑十',       '13800000009', 'zhengshi@qq.com',  'USER',  1, 5, 0),
('liuyi',    '$2a$10$fWbiCFTAQy2UkiDmHKDEw.YbbNhfacr/W7ePQXDp0GSuKc6ImcvJ.', '刘一',       '13800000010', 'liuyi@qq.com',     'USER',  1, 5, 0),
('chener',   '$2a$10$fWbiCFTAQy2UkiDmHKDEw.YbbNhfacr/W7ePQXDp0GSuKc6ImcvJ.', '陈二',       '13800000011', 'chener@qq.com',    'USER',  1, 5, 0),
('yangsan',  '$2a$10$fWbiCFTAQy2UkiDmHKDEw.YbbNhfacr/W7ePQXDp0GSuKc6ImcvJ.', '杨三',       '13800000012', 'yangsan@qq.com',   'USER',  1, 5, 0),
('huangsi',  '$2a$10$fWbiCFTAQy2UkiDmHKDEw.YbbNhfacr/W7ePQXDp0GSuKc6ImcvJ.', '黄四',       '13800000013', 'huangsi@qq.com',   'USER',  1, 5, 0),
('xuwu',     '$2a$10$fWbiCFTAQy2UkiDmHKDEw.YbbNhfacr/W7ePQXDp0GSuKc6ImcvJ.', '许五',       '13800000014', 'xuwu@qq.com',      'USER',  1, 5, 0),
('heliu',    '$2a$10$fWbiCFTAQy2UkiDmHKDEw.YbbNhfacr/W7ePQXDp0GSuKc6ImcvJ.', '何六',       '13800000015', 'heliu@qq.com',     'USER',  1, 5, 0),
('luqi',     '$2a$10$fWbiCFTAQy2UkiDmHKDEw.YbbNhfacr/W7ePQXDp0GSuKc6ImcvJ.', '吕七',       '13800000016', 'luqi@qq.com',      'USER',  0, 5, 0),
('shiba',    '$2a$10$fWbiCFTAQy2UkiDmHKDEw.YbbNhfacr/W7ePQXDp0GSuKc6ImcvJ.', '施八',       '13800000017', 'shiba@qq.com',     'USER',  1, 5, 0),
('zhangjiu', '$2a$10$fWbiCFTAQy2UkiDmHKDEw.YbbNhfacr/W7ePQXDp0GSuKc6ImcvJ.', '张九',       '13800000018', 'zhangjiu@qq.com',  'USER',  1, 5, 0),
('kongshi',  '$2a$10$fWbiCFTAQy2UkiDmHKDEw.YbbNhfacr/W7ePQXDp0GSuKc6ImcvJ.', '孔十',       '13800000019', 'kongshi@qq.com',   'USER',  1, 5, 0),
('caoshiyi', '$2a$10$fWbiCFTAQy2UkiDmHKDEw.YbbNhfacr/W7ePQXDp0GSuKc6ImcvJ.', '曹十一',     '13800000020', 'caoshiyi@qq.com',  'USER',  1, 5, 0);

-- ==================== 预插入图书数据（30条） ====================
INSERT INTO books (isbn, title, author, publisher, publish_date, category, description, cover_image, total_quantity, available_quantity, location, status) VALUES
('978-7-111-70510-0', 'Java编程思想',                'Bruce Eckel',       '机械工业出版社', '2019-06-01', '计算机', 'Java经典入门书籍，全面讲解Java语言核心概念。',                                  '/covers/java-think.jpg',           10, 10, 'A区-1排-01', 1),
('978-7-121-38700-0', 'Spring Boot实战',              'Craig Walls',       '人民邮电出版社', '2020-03-01', '计算机', 'Spring Boot框架从入门到精通，涵盖微服务开发。',                                 '/covers/springboot.jpg',          8,  8,  'A区-1排-02', 1),
('978-7-302-54510-0', '数据结构与算法分析',           'Mark Allen Weiss',  '清华大学出版社', '2021-01-01', '计算机', '经典数据结构教材，C语言描述，附大量习题。',                                     '/covers/datastructure.jpg',       6,  6,  'A区-1排-03', 1),
('978-7-111-68412-0', 'MySQL必知必会',                'Ben Forta',         '机械工业出版社', '2020-09-01', '计算机', 'MySQL数据库快速入门，适合初学者。',                                             '/covers/mysql.jpg',              12, 12, 'A区-1排-04', 1),
('978-7-302-56890-0', '计算机网络：自顶向下方法',     'James Kurose',      '清华大学出版社', '2021-08-01', '计算机', '计算机网络经典教材，全球高校广泛使用。',                                         '/covers/network.jpg',            5,  5,  'A区-1排-05', 1),
('978-7-111-68345-0', '深入理解Java虚拟机',           '周志明',            '机械工业出版社', '2022-01-01', '计算机', 'JVM深度解析，Java开发者进阶必读。',                                              '/covers/jvm.jpg',                7,  7,  'A区-2排-01', 1),
('978-7-121-41580-0', 'Vue.js设计与实现',             '霍春阳',            '人民邮电出版社', '2022-03-01', '计算机', 'Vue.js源码解读，前端框架深入理解。',                                             '/covers/vuejs.jpg',              6,  6,  'A区-2排-02', 1),
('978-7-111-69321-0', '算法导论',                     'Thomas H.Cormen',   '机械工业出版社', '2021-10-01', '计算机', '算法领域圣经级教材，程序员必读。',                                               '/covers/algorithm.jpg',          4,  4,  'A区-2排-03', 1),
('978-7-302-58190-0', '操作系统概念',                 'Abraham Silberschatz', '清华大学出版社', '2020-12-01', '计算机', '操作系统经典教材，理论与实践并重。',                                             '/covers/os.jpg',                 5,  5,  'A区-2排-04', 1),
('978-7-111-70120-0', '设计模式：可复用面向对象软件的基础', 'GoF',          '机械工业出版社', '2019-10-01', '计算机', 'GoF设计模式经典，四人帮著作。',                                                   '/covers/designpattern.jpg',      8,  8,  'A区-2排-05', 1),
('978-7-02-017250-0', '活着',                         '余华',              '人民文学出版社', '2017-06-01', '文学',   '余华代表作，讲述一个人在时代洪流中的命运。',                                     '/covers/huozhe.jpg',             15, 15, 'B区-1排-01', 1),
('978-7-5442-5398-8', '百年孤独',                     '加西亚·马尔克斯',  '南海出版公司',   '2018-05-01', '文学',   '魔幻现实主义代表作，拉丁美洲文学的巅峰。',                                       '/covers/100years.jpg',           10, 10, 'B区-1排-02', 1),
('978-7-02-017260-1', '围城',                         '钱钟书',            '人民文学出版社', '2019-08-01', '文学',   '中国现代文学经典，婚姻与人生的绝妙讽刺。',                                       '/covers/weicheng.jpg',           8,  8,  'B区-1排-03', 1),
('978-7-5321-6650-1', '平凡的世界',                   '路遥',              '北京十月文艺出版社', '2017-01-01', '文学', '茅盾文学奖作品，全景式展现中国当代城乡社会。',                                   '/covers/pingfan.jpg',            12, 12, 'B区-1排-04', 1),
('978-7-5442-7667-0', '解忧杂货店',                   '东野圭吾',          '南海出版公司',   '2018-11-01', '文学',   '东野圭吾温暖治愈系作品，穿越时空的书信。',                                       '/covers/jieyou.jpg',             10, 10, 'B区-1排-05', 1),
('978-7-213-08890-0', '人类简史',                     '尤瓦尔·赫拉利',    '中信出版社',     '2020-03-01', '历史',   '从认知革命到人工智能，重新审视人类历史。',                                       '/covers/sapiens.jpg',            6,  6,  'B区-2排-01', 1),
('978-7-5086-6855-8', '未来简史',                     '尤瓦尔·赫拉利',    '中信出版社',     '2021-05-01', '历史',   '探讨人类未来发展趋势，从数据主义到人工智能。',                                   '/covers/homedeus.jpg',           6,  6,  'B区-2排-02', 1),
('978-7-5596-1210-6', '三体',                         '刘慈欣',            '重庆出版社',     '2018-10-01', '科幻',   '雨果奖获奖作品，中国科幻文学的里程碑。',                                         '/covers/santi.jpg',              20, 20, 'B区-2排-03', 1),
('978-7-5596-2821-4', '三体：黑暗森林',               '刘慈欣',            '重庆出版社',     '2019-03-01', '科幻',   '三体系列第二部，宇宙社会学的黑暗秘密。',                                         '/covers/santi2.jpg',             15, 15, 'B区-2排-04', 1),
('978-7-5596-3555-6', '三体：死神永生',               '刘慈欣',            '重庆出版社',     '2019-10-01', '科幻',   '三体系列大结局，跨越时空的壮丽史诗。',                                           '/covers/santi3.jpg',             15, 15, 'B区-2排-05', 1),
('978-7-115-51420-0', 'Python编程：从入门到实践',     'Eric Matthes',      '人民邮电出版社', '2022-01-01', '计算机', 'Python入门首选，项目驱动式学习。',                                               '/covers/python.jpg',             10, 10, 'A区-3排-01', 1),
('978-7-111-65980-0', 'Effective Java',               'Joshua Bloch',      '机械工业出版社', '2021-06-01', '计算机', 'Java最佳实践指南，提升代码质量必读。',                                           '/covers/effectivejava.jpg',      6,  6,  'A区-3排-02', 1),
('978-7-121-31230-0', 'JavaScript高级程序设计',       'Matt Frisbie',      '人民邮电出版社', '2021-12-01', '计算机', 'JavaScript红宝书，前端开发必备。',                                               '/covers/js.jpg',                 8,  8,  'A区-3排-03', 1),
('978-7-111-67590-0', 'Redis设计与实现',              '黄健宏',            '机械工业出版社', '2020-08-01', '计算机', 'Redis源码与内部原理深度解析。',                                                  '/covers/redis.jpg',              5,  5,  'A区-3排-04', 1),
('978-7-302-58192-0', '计算机组成原理',               '唐朔飞',            '清华大学出版社', '2021-03-01', '计算机', '计算机硬件基础经典教材。',                                                       '/covers/computerorg.jpg',        7,  7,  'A区-3排-05', 1),
('978-7-02-012320-0', '红楼梦',                       '曹雪芹',            '人民文学出版社', '2016-12-01', '文学',   '中国古典四大名著之首，封建社会的百科全书。',                                     '/covers/honglou.jpg',            10, 10, 'B区-3排-01', 1),
('978-7-02-010450-0', '西游记',                       '吴承恩',            '人民文学出版社', '2016-12-01', '文学',   '中国古典四大名著，神魔小说的巅峰。',                                             '/covers/xiyou.jpg',              10, 10, 'B区-3排-02', 1),
('978-7-02-000220-0', '三国演义',                     '罗贯中',            '人民文学出版社', '2016-12-01', '文学',   '历史演义小说的开山之作。',                                                       '/covers/sanguo.jpg',             10, 10, 'B区-3排-03', 1),
('978-7-02-000870-0', '水浒传',                       '施耐庵',            '人民文学出版社', '2016-12-01', '文学',   '英雄传奇小说的典范，108将的故事。',                                              '/covers/shuihu.jpg',             10, 10, 'B区-3排-04', 1),
('978-7-5217-2888-0', '被讨厌的勇气',                 '岸见一郎',          '中信出版社',     '2022-05-01', '心理学', '阿德勒心理学通俗解读，给予你改变人生的勇气。',                                   '/covers/courage.jpg',            8,  8,  'C区-1排-01', 1);