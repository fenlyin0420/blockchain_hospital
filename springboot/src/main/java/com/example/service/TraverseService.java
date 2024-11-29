package com.example.service;

import com.example.entity.Traverse;
import com.example.entity.User;
import com.example.exception.CustomException;
import com.example.mapper.TraverseMapper;
import com.example.mapper.UserMapper;
import com.example.utils.JwtSm.MySM2Util;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

import java.util.List;

@Slf4j
@Service
public class TraverseService {

    @Resource
    private TraverseMapper traverseMapper;

    @Resource
    private UserMapper userMapper;

    public List<Traverse> selectAll(Traverse traverse) {
        return traverseMapper.selectAll(traverse);
    }

    public PageInfo<Traverse> selectPage(Traverse traverse, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Traverse> list = traverseMapper.selectAll(traverse);
        //log.info("list:{}",list);
        return PageInfo.of(list);
    }

    public Traverse selectById(Integer id) {
        Traverse traverse = new Traverse();
        traverse.setId(id);
        return traverseMapper.selectAll(traverse).get(0);
    }

    public Traverse selectByName(String name) {
        Traverse traverse = new Traverse();
        traverse.setUserName(name);
        return traverseMapper.selectAll(traverse).get(0);
    }

    public Traverse selectByDoctorId(Integer id) {
        Traverse traverse = new Traverse();
        traverse.setDoctorId(id);
        return traverseMapper.selectAll(traverse).get(0);
    }

    public Traverse selectByHospitalId(Integer id) {
        Traverse traverse = new Traverse();
        traverse.setHospitalId(id);
        return traverseMapper.selectAll(traverse).get(0);
    }

    public void add(Traverse traverse) throws ClassCastException{
        try {
            User user = userMapper.selectById(traverse.getUserId());
            // 加密医生建议
            String cipherText = MySM2Util.encryption(user.getPublicKey(), traverse.getAdvice());
            traverse.setAdvice(cipherText);
            // 加密医嘱
            cipherText = MySM2Util.encryption(user.getPublicKey(), traverse.getDrug());
            traverse.setDrug(cipherText);
        } catch (NullPointerException e){
            e.printStackTrace();
            throw new CustomException("400", "患者不存在");
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException("400", "加密失败");
        }
        traverseMapper.add(traverse);
    }

    public void updateById(Traverse traverse) {
        traverseMapper.updateById(traverse);
    }

    public void deleteById(Integer id) {
        traverseMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        for(Integer id : ids){
            traverseMapper.deleteById(id);
        }
    }
}