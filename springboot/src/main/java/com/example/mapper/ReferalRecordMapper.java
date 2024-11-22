package com.example.mapper;


import com.example.entity.ReferalRecord;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ReferalRecordMapper {


    List<ReferalRecord> selectAll(ReferalRecord referalRecord);

    ReferalRecord selectById(Integer id);

    void updateById(ReferalRecord referalRecord);

    void deleteById(Integer id);

    void add(ReferalRecord referalRecord);

}
