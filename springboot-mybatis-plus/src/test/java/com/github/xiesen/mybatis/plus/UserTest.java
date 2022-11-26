package com.github.xiesen.mybatis.plus;

import com.github.xiesen.mybatis.plus.mapper.UserMapper;
import com.github.xiesen.mybatis.plus.service.UserService;
import com.github.xiesen.mybatis.plus.service.impl.UserServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author xiesen
 * @title: UserTest
 * @projectName xiesen-parent
 * @description: UserTest
 * @date 2022/11/26 21:05
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserTest {
    private static final int MAX_COUNT = 100000;

    @Resource
    private UserServiceImpl userService;

    @Test
    public void select() {
        List<User> userList = userService.query().list();
        for (User user : userList) {
            System.out.println(user);
        }
    }


    @Test
    public void saveBatch() {
        final long start = System.currentTimeMillis();
        final ArrayList<User> list = new ArrayList<>();
        for (int i = 0; i < MAX_COUNT; i++) {
            User user = new User();
            user.setName("test:" + i);
            user.setPassword("123456");
            list.add(user);
        }
        userService.saveBatch(list);
        long etime = System.currentTimeMillis();
        System.out.println("执行时间：" + (etime - start));
    }
}
