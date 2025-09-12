package com.yourcompany.yourapp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yourcompany.yourapp.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
