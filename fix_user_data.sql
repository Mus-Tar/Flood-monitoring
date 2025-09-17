-- 修复数据库用户数据
-- 为mustar用户设置有效密码
UPDATE sys_user SET password = '123456' WHERE username = 'mustar';

-- 验证更新结果
SELECT id, username, password, role, created_at FROM sys_user;