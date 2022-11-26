package com.github.xiesen.mybatis.plus;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @author xiesen
 * @title: User
 * @projectName xiesen-parent
 * @description: User
 * @date 2022/11/26 20:59
 */
@TableName("user")
@Data
public class User {
    private int id;
    private String name;
    private String password;
    private Date createtime;
}
