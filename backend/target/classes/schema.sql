-- 初始化数据库与示例数据
CREATE TABLE IF NOT EXISTS `sys_user` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
  `username` VARCHAR(50) NOT NULL UNIQUE,
  `password` VARCHAR(100) NOT NULL,
  `role` VARCHAR(20) DEFAULT 'ADMIN',
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO `sys_user` (`username`, `password`, `role`) VALUES
('admin', 'admin123', 'ADMIN')
ON DUPLICATE KEY UPDATE username=VALUES(username);
