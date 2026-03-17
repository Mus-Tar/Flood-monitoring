package com.yourcompany.yourapp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yourcompany.yourapp.entity.User;
import org.apache.ibatis.annotations.Mapper;

// 用户信息数据持久层接口
@Mapper
public interface UserMapper extends BaseMapper<User> {
    // 处理用户相关数据库操作
}
