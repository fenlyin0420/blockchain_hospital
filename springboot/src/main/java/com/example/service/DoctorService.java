package com.example.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.example.common.Constants;
import com.example.common.enums.ResultCodeEnum;
import com.example.common.enums.RoleEnum;
import com.example.entity.*;
import com.example.exception.CustomException;
import com.example.mapper.DepartmentMapper;
import com.example.mapper.DoctorMapper;
import com.example.mapper.PlanMapper;
import com.example.mapper.ReserveMapper;
import com.example.utils.JwtSm.MySm2Util;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPrivateKey;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPublicKey;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * 医生业务处理
 **/
@Service
public class DoctorService {

    @Resource
    private DoctorMapper doctorMapper;
    @Resource
    private DepartmentMapper departmentMapper;
    @Resource
    private ReserveMapper reserveMapper;
    @Resource
    private PlanMapper planMapper;

    /**
     * 新增
     */
    public void add(Doctor doctor) {
        Doctor dbDoctor = doctorMapper.selectByUsername(doctor.getUsername());
        if (ObjectUtil.isNotNull(dbDoctor)) {
            throw new CustomException(ResultCodeEnum.USER_EXIST_ERROR);
        }
        if (ObjectUtil.isEmpty(doctor.getPassword())) {
            doctor.setPassword(Constants.USER_DEFAULT_PASSWORD);
        }
        if (ObjectUtil.isEmpty(doctor.getName())) {
            doctor.setName(doctor.getUsername());
        }
        doctor.setRole(RoleEnum.DOCTOR.name());
        doctorMapper.insert(doctor);
    }

    /**
     * 删除
     */
    public void deleteById(Integer id) {
        doctorMapper.deleteById(id);
    }

    /**
     * 批量删除
     */
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            doctorMapper.deleteById(id);
        }
    }

    /**
     * 修改
     */
    public void updateById(Doctor doctor) {
        doctorMapper.updateById(doctor);
    }

    /**
     * 根据ID查询
     */
    public Doctor selectById(Integer id) {
        return doctorMapper.selectById(id);
    }

    /**
     * 查询所有
     */
    public List<Doctor> selectAll(Doctor doctor) {
        return doctorMapper.selectAll(doctor);
    }

    /**
     * 分页查询
     */
    public PageInfo<Doctor> selectPage(Doctor doctor, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Doctor> list = doctorMapper.selectAll(doctor);
        return PageInfo.of(list);
    }

    /**
     * 患者挂号页面的分页查询
     */
    public PageInfo<Doctor> selectPage2(Doctor doctor, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        // 将 Date 对象解析为 xxxx-xx-xx 格式的字符串
        if (doctor.getSelectedDate() != null)
            doctor.setSelectedDate(doctor.getSelectedDate().split("T")[0]);
        // 访问数据库接口，查询数据
        List<Doctor> list = doctorMapper.selectByPlan(doctor);
        for (Doctor dbDoctro : list) {
            dbDoctro.setSelectedDate(doctor.getSelectedDate());
        }
        return PageInfo.of(list);
    }

    /**
     * 获取今天是星期几
     */
    private String getTodayWeek() {
        LocalDate today = LocalDate.now();
        DayOfWeek dayOfWeek = today.getDayOfWeek();
        return dayOfWeek.getDisplayName(TextStyle.FULL_STANDALONE, Locale.CHINA);
    }

    /**
     * 登录
     */
    public Account login(Account account) {
        Doctor dbDoctor = doctorMapper.selectByUsername(account.getUsername());
        if (ObjectUtil.isNull(dbDoctor)) {
            throw new CustomException(ResultCodeEnum.USER_NOT_EXIST_ERROR);
        }
        if (!account.getPassword().equals(dbDoctor.getPassword())) {
            throw new CustomException(ResultCodeEnum.USER_ACCOUNT_ERROR);
        }
        // 生成token
        String tokenData = dbDoctor.getId() + "-" + RoleEnum.DOCTOR.name();
        String token = TokenUtils.createToken(tokenData, dbDoctor.getPassword());
        dbDoctor.setToken(token);
        if (ObjectUtil.isNotEmpty(dbDoctor.getDepartmentId())) {
            Department department = departmentMapper.selectById(dbDoctor.getDepartmentId());
            if (ObjectUtil.isNotEmpty(department)) {
                dbDoctor.setDepartmentName(department.getName());
            }
        }
        return dbDoctor;
    }

    /**
     * 修改密码
     */
    public void updatePassword(Account account) {
        Doctor dbDoctor = doctorMapper.selectByUsername(account.getUsername());
        if (ObjectUtil.isNull(dbDoctor)) {
            throw new CustomException(ResultCodeEnum.USER_NOT_EXIST_ERROR);
        }
        if (!account.getPassword().equals(dbDoctor.getPassword())) {
            throw new CustomException(ResultCodeEnum.PARAM_PASSWORD_ERROR);
        }
        dbDoctor.setPassword(account.getNewPassword());
        doctorMapper.updateById(dbDoctor);
    }

    public List<Doctor> selectByH(Integer H_id) {
        return doctorMapper.selectByH(H_id);
    }

    public Doctor getKey(Integer id) {
        return doctorMapper.getKey(id);
    }

    public void updateKey(Account account) {
        try {
            MySm2Util.getkey();
        } catch (InvalidAlgorithmParameterException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (NoSuchProviderException e) {
            throw new RuntimeException(e);
        }
        BCECPublicKey publicKey = MySm2Util.getBcPubKey();
        BCECPrivateKey privateKey = MySm2Util.getBcecPriKey();
        String pubkey= MySm2Util.pub2str(publicKey);
        String prikey= MySm2Util.pri2str(privateKey);
        Doctor dbDoctor = doctorMapper.selectByUsername(account.getUsername());
        dbDoctor.setPrivateKey(prikey);
        dbDoctor.setPublicKey(pubkey);
        doctorMapper.updateById(dbDoctor);
    }
}