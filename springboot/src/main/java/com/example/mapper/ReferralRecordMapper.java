package com.example.mapper;


import com.example.entity.ReferralRecord;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ReferralRecordMapper {


    List<ReferralRecord> selectAll(ReferralRecord referralRecord);

    ReferralRecord selectById(Integer id);

    void updateById(ReferralRecord referralRecord);

    void deleteById(Integer id);

    void add(ReferralRecord referralRecord);

}
