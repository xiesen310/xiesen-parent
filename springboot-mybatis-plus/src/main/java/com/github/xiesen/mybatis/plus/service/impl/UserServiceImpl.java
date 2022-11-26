package com.github.xiesen.mybatis.plus.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.xiesen.mybatis.plus.User;
import com.github.xiesen.mybatis.plus.mapper.UserMapper;
import com.github.xiesen.mybatis.plus.service.UserService;
import org.springframework.stereotype.Service;

/**
 * @author xiesen
 * @title: UserServiceImpl
 * @projectName xiesen-parent
 * @description: UserServiceImpl
 * @date 2022/11/26 21:15
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    
}
