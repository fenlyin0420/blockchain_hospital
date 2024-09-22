package com.example.mapper;

import com.example.entity.Plan;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 操作plan相关数据接口
*/
public interface PlanMapper {

    /**
      * 新增
    */
    int insert(Plan plan);

    /**
      * 删除
    */
    int deleteById(Integer id);

    /**
      * 修改
    */
    int updateById(Plan plan);

    /**
      * 根据ID查询
    */
    Plan selectById(Integer id);

    /**
      * 查询所有
    */
    List<Plan> selectAll(Plan plan);

    @Select("select * from plan where doctor_id = #{doctorId} and week = #{week}")
    Plan selectByDoctorIdAndWeek(@Param("doctorId") Integer doctorId, @Param("week") String week);
}