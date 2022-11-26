package com.github.xiesen.mybatis.plus.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.xiesen.mybatis.plus.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author xiesen
 * @title: UserMapper
 * @projectName xiesen-parent
 * @description: TODO
 * @date 2022/11/26 21:02
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
