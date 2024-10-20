package com.example.mapper;

import com.example.entity.Doctor;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 操作doctor相关数据接口
*/
public interface DoctorMapper {

    /**
      * 新增
    */
    int insert(Doctor doctor);

    /**
      * 删除
    */
    int deleteById(Integer id);

    /**
      * 修改
    */
    int updateById(Doctor doctor);

    /**
      * 根据ID查询
    */
    Doctor selectById(Integer id);


    /**
      * 查询所有
    */
    List<Doctor> selectAll(Doctor doctor);

    /**
     * 给定医院、科室、和时间，查询符合条件的医生
     * @param doctor 医生实体
     * @return 医生实体的列表
     */
    List<Doctor> selectByPlan(Doctor doctor);

    @Select("select * from doctor where username = #{username}")
    Doctor selectByUsername(String username);


    List<Doctor> selectByH(Integer hId);

    @Select("select public_key as publicKey, private_key as privateKey from doctor where id = #{id}")
    Doctor getKey(Integer id);

    List<Doctor> select();
}