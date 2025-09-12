package com.yourcompany.yourapp.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yourcompany.yourapp.entity.User;
import com.yourcompany.yourapp.mapper.UserMapper;
import com.yourcompany.yourapp.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
