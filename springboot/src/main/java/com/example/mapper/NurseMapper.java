package com.example.mapper;
import com.example.entity.Nurse;
import com.example.entity.Params;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import java.util.List;

/**
 * 护士相关的接口
 */
//public interface NurseMapper  extends BaseMapper<Nurse> {
public interface NurseMapper{

    //查询所有的护士信息

    List<Nurse> selectAll(Nurse nurse);



    int insert(Nurse nurse);

    /**
     * 根据用户名查询
     * @param username 用户名
     * @return Nurse entity
     */
    @Select("select * from nurse where username = #{username}")
    Nurse selectByUsername(String username);

    Nurse selectById(Integer id);

    void updateById(Nurse nurse);

    List<Nurse> selectByH(Integer id);

    void deleteById(Integer id);
}
