package com.example.mapper;

import com.example.dao.TraverseDAO;
import com.example.dto.TraverseDTO;
import com.example.entity.ReferralTraverse;
import com.example.entity.Traverse;

import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface TraverseMapper {


    List<TraverseDTO> selectAll(Traverse traverse);

    Integer add(Traverse traverse);

    void updateById(Traverse traverse);

    void setSignData(TraverseDAO traverseDAO);

    void deleteById(Integer id);

    Traverse selectById(Integer id);
    
    Traverse selectByTimestamp(String number);

    List<Traverse> selectByCanSend(Integer id);

    Integer selectLastInsertId();

    List<ReferralTraverse> selectAlReferralTraverse();

    void insertReferralTraverse(ReferralTraverse entity);
}
