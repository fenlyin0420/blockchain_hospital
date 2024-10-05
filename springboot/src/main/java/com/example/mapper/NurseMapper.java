package com.example.mapper;

import com.example.entity.Nurse;
import org.apache.ibatis.annotations.Select;

/**
 * 护士相关的接口
 */
public interface NurseMapper {

    /**
     * 根据ID查询
     * @param id 护士的id
     * @return Nurse entity
     */
    Nurse getNurseById(int id);

    /**
     * 根据用户名查询
     * @param username 用户名
     * @return Nurse entity
     */
    @Select("select * from nurse where username = #{username}")
    Nurse selectByUsername(String username);
}
