package com.example.mapper;

import com.example.entity.Doctor;
import com.example.entity.Nurse;
import com.example.entity.Params;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 护士相关的接口
 */
public interface NurseMapper {

    /**
     * 根据ID查询
     //* @param id 护士的id
     * @return Nurse entity
     */
    //    Nurse getNurseById(int id);

    //查询所有的护士信息
    @Select("select * from nurse")
    List<Nurse> selectAll();

    //根据账号或者名字查询护士信息
    List<Nurse> findBySearch(@Param("params") Params params);

    int insert(Nurse nurse);

    /**
     * 根据用户名查询
     * @param username 用户名
     * @return Nurse entity
     */
    @Select("select * from nurse where username = #{username}")
    Nurse selectByUsername(String username);
}
