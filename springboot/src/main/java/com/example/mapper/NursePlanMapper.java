package com.example.mapper;

import com.example.entity.nursePlan;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;

/**
 * 操作plan相关数据接口
*/
public interface NursePlanMapper {

    /**
      * 新增
    */
    int insert(nursePlan plan);

    /**
     * 删除
     */
    int deleteById(Integer id);

    /**
     * 根据
     * @param plan
     * @return
     */
    int updateById(nursePlan plan);



    /**
      * 查询所有
    */
    List<nursePlan> selectAll(nursePlan plan);

    @Select("select * from nurse_plan where nurse_id = #{nurseId} and date = #{date}")
    nursePlan selectByNurseIdAndDate(@Param("nurseId") Integer nurseId, @Param("date") Date date);
}