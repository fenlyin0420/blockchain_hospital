package com.example.mapper;
import com.example.entity.Nurse;
import com.example.entity.Params;
import com.example.entity.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.BaseMapper;

import java.util.List;

/**
 * 护士相关的接口
 */
//public interface NurseMapper  extends BaseMapper<Nurse> {
public interface NurseMapper{

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

    Nurse selectById(Integer id);
}
