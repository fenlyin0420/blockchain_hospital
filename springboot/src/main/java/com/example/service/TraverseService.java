package com.example.service;

import com.example.entity.Traverse;
import com.example.entity.User;
import com.example.mapper.TraverseMapper;
import com.example.mapper.UserMapper;
import com.example.utils.JwtSm.MySM2Util;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;


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
        return PageInfo.of(list);
    }

    public Traverse selectById(Integer id) {
        Traverse traverse = new Traverse();
        traverse.setId(id);
        return traverseMapper.selectAll(traverse).get(0);
    }

    public Traverse selectByName(String name) {
        Traverse traverse = new Traverse();
        traverse.setName(name);
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

    public void add(Traverse traverse) {
        try {
            User user = userMapper.selectByName(traverse.getName());
            System.out.println("用户公钥：" + user.getPublicKey());
            // 加密医生建议
            System.out.println("加密前：" + traverse.getAdvice());
            String cipherText = MySM2Util.encryption(user.getPublicKey(), traverse.getAdvice());
            traverse.setAdvice(cipherText);
            System.out.println("加密后：" + traverse.getAdvice());
            // 加密医嘱
            cipherText = MySM2Util.encryption(user.getPublicKey(), traverse.getDrug());
            traverse.setDrug(cipherText);
        } catch (Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
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