package com.example.service;



import com.example.entity.Drug;
import com.example.mapper.DrugMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 管理员业务处理
 **/
@Service
public class DrugService {

    @Resource
    private DrugMapper drugMapper;


    public List<Drug> selectAll(Drug drug) {
        return drugMapper.selectAll(drug);
    }

    public PageInfo<Drug> selectPage(Drug drug,Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Drug> list = drugMapper.selectAll(drug);
        return PageInfo.of(list);
    }

    public Drug selectById(Integer id) {
        Drug drug = new Drug();
        drug.setId(id);
        return drugMapper.selectAll(drug).get(0);
    }

    public Drug selectByNumber(String number) {
        Drug drug = new Drug();
        drug.setDrugNumber(number);
        return drugMapper.selectAll(drug).get(0);
    }

    public void add(Drug drug) {
        if(drug.getId()==null){
            drugMapper.add(drug);
        }
        else {
            drugMapper.updateById(drug);
        }

    }

    public void updateById(Drug drug) {
        drugMapper.updateById(drug);
    }

    public void deleteById(Integer id) {
        drugMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            drugMapper.deleteById(id);
        }
    }
}