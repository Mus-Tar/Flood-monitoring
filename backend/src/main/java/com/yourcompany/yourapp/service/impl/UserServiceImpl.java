package com.yourcompany.yourapp.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yourcompany.yourapp.entity.User;
import com.yourcompany.yourapp.mapper.UserMapper;
import com.yourcompany.yourapp.service.UserService;
import org.springframework.stereotype.Service;

// 用户业务服务实现类，基于 MyBatis-Plus 提供通用用户管理功能
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
