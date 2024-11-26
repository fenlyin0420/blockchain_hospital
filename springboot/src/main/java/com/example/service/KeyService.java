package com.example.service;

import com.example.common.enums.RoleEnum;
import com.example.entity.*;
import com.example.exception.CustomException;
import com.example.mapper.*;
import com.example.utils.ImgUtil;
import com.example.utils.MyMultipartFile;
import com.example.utils.JwtSm.BestRingSignUtil;
import com.example.utils.JwtSm.MySM2Util;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPrivateKey;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPublicKey;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.support.WebRequestDataBinder;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClientRequestException;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.*;

import com.example.common.Result;
import com.example.common.enums.ResultCodeEnum;

import javax.annotation.Resource;

@Service
public class KeyService {
    @Resource
    private DoctorMapper doctorMapper;
    @Resource
    private AdminMapper adminMapper;
    @Resource
    private UserMapper userMapper;
    @Resource
    private KeyMapper keyMapper;
    @Resource
    private TraverseMapper traverseMapper;

    /**
     * params.getId()病历id
     * params.getName()患者姓名
     *
     */
    public RingSign sign(Params params) {
        Traverse traverse = traverseMapper.selectByTimestamp(params.getTimestamp());
        Doctor doctor = doctorMapper.selectById(traverse.getDoctorId());
        if (Objects.equals(traverse.getSignPubKey(), " ") || traverse.getSignPubKey() == null) {
            setSignPublicKey(traverse);
            traverse = traverseMapper.selectByTimestamp(params.getTimestamp());
        }

        // 合成data
        Instant now = Instant.now();
        long timestamp = now.toEpochMilli();
        Date date = new Date(timestamp);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = sdf.format(date);
        String data = params.getName() + "+" + formattedDate;

        // 提取环公钥
        List<Doctor> doctors = getSignPubKey(traverse);
        List<String> publicKeys = new ArrayList<>();
        for (Doctor doctorTemp : doctors) {
            publicKeys.add(doctorTemp.getPublicKey());
        }
        System.out.println(doctors);
        // List<String> publicKeys=ringSignMapper.findToPubKey();
        // 匹配私钥，确定pi
        int pi = -1;
        for (int i = 0; i < publicKeys.size(); i++) {
            if (Objects.equals(publicKeys.get(i), doctor.getPublicKey())) {
                pi = i + 1;
                break;
            }
        }
        if (pi == -1) {
            throw new CustomException(ResultCodeEnum.USER_RESULT_GET_ERROR);
        }
        // 进行签名
        // 钥匙进行格式转化
        List<BCECPublicKey> list = new ArrayList<>();
        try {
            MySM2Util.getKey();
        } catch (InvalidAlgorithmParameterException | NoSuchAlgorithmException | NoSuchProviderException e) {
            throw new RuntimeException(e);
        }
        for (String pubKey : publicKeys) {
            System.out.println(
                    "____________________________________________________________________________________________________________________________________________"
                            + pubKey);
            try {
                list.add(MySM2Util.str2pub(pubKey));
            } catch (Exception e) {
                throw new CustomException(ResultCodeEnum.USER_KEY_ERROR);
            }
        }
        BCECPrivateKey bcecPrivateKey;
        try {
            bcecPrivateKey = MySM2Util.str2pri(doctor.getPrivateKey());
        } catch (Exception e) {
            throw new CustomException(ResultCodeEnum.USER_KEY_ERROR);
        }
        String key = BestRingSignUtil.generate(data, list, bcecPrivateKey, pi);

        // 返回数据
        RingSign ringSign = new RingSign();
        ringSign.setSignData(data);
        ringSign.setSignKey(key);
        traverse.setSignKey(key);
        traverse.setSignData(data);
        traverseMapper.updateById(traverse);
        return ringSign;
    }

    public RingSign verifySign(Params params) {
        Traverse traverse = traverseMapper.selectByTimestamp(params.getTimestamp());
        String key = params.getSignKey();
        List<Doctor> doctors = getSignPubKey(traverse);
        List<String> publicKeys = new ArrayList<>();
        for (Doctor doctor : doctors) {
            publicKeys.add(doctor.getPublicKey());
        }
        // List<String> publicKeys=ringSignMapper.findToPubKey();
        List<BCECPublicKey> list = new ArrayList<>();
        for (String pubKey : publicKeys) {
            try {
                list.add(MySM2Util.str2pub(pubKey));
            } catch (Exception e) {
                throw new CustomException(ResultCodeEnum.USER_KEY_ERROR);
            }
        }
        boolean result = BestRingSignUtil.verifySign(traverse.getSignData(), list, key);

        RingSign ringSign = new RingSign();
        ringSign.setSignData(traverse.getSignData());
        ringSign.setSignKey(key);
        if (result) {
            traverse.setSignResult("成功");
            ringSign.setMessage("成功");
        } else {
            traverse.setSignResult("失败");
            ringSign.setMessage("失败");
        }
        traverseMapper.updateById(traverse);
        return ringSign;
    }

    public void setSignPublicKey(Traverse traverse) {
        // 查询所有的公钥
        List<Doctor> doctors = doctorMapper.select();
        System.out.println(
                "___________________________________________________________________________________________________________"
                        + doctors.size());
        // 合并环公钥
        StringBuilder mergedString = new StringBuilder();
        for (Doctor doctor : doctors) {
            mergedString.append(doctor.getName()).append(":").append(doctor.getPublicKey()).append(", ");
        }

        // 删除最后的逗号和空格
        if (mergedString.length() > 0) {
            mergedString.delete(mergedString.length() - 2, mergedString.length());
        }
        // 输出合并后的字符串
        System.out.println(mergedString.toString());
        // 添加公钥信息
        traverse.setSignPubKey(mergedString.toString());
        traverseMapper.updateById(traverse);
    }

    public List<Doctor> getSignPubKey(Traverse traverse) {
        // 查询环公钥字符串
        String s = traverse.getSignPubKey();
        // 数据操作
        if (Objects.equals(s, " ") || s == null) {
            throw new CustomException(ResultCodeEnum.USER_SET_FRONT_ERROR);
        }
        // 拆分字符串并创建EntryForm对象列表
        List<Doctor> doctors = new ArrayList<>();
        String[] pairs = s.split(", ");
        for (String pair : pairs) {
            String[] parts = pair.split(":");
            if (parts.length == 2) {
                Doctor doctor = new Doctor();
                doctor.setName(parts[0]);
                doctor.setPublicKey(parts[1]);
                doctors.add(doctor);
            }
        }

        return doctors;
    }

    /**
     * 解密病历
     * 
     * @param traverse 与病历相关的参数
     * @return
     */
    public Traverse decrypt(Traverse traverse) throws CustomException{
        try {
            User user = userMapper.selectById(traverse.getUserId());
            if (user == null) throw new CustomException("用户不存在");
            String advicePlainText = MySM2Util.decrypt(user.getPrivateKey(), traverse.getAdvice());
            String drugPlainText = MySM2Util.decrypt(user.getPrivateKey(), traverse.getDrug());
            traverse.setAdvice(advicePlainText);
            traverse.setDrug(drugPlainText);
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(e.getMessage());
        }
        return traverse;
    }

    /**
     * 图像解密函数
     * @param imgURL 加密图像在服务器的url
     * @return 解密后图片的base64编码
     */
    public String imgDecrypt(String imgURL) throws IOException, NullPointerException, WebClientRequestException, RuntimeException{
        MultipartFile file = MyMultipartFile.fromURL(imgURL);
        BufferedImage img = ImgUtil.MultipartFileToBufferedImage(file);
        img = ImgUtil.ImageDecryptor(img);
        return ImgUtil.getImageBase64(img);
    }

    public Account selectById(Account account) {
        Account account1 = new Account();
        if (RoleEnum.ADMIN.name().equals(account.getRole())) {
            account1 = adminMapper.selectById(account.getId());
        } else if (RoleEnum.DOCTOR.name().equals(account.getRole())) {
            account1 = doctorMapper.selectById(account.getId());
        } else if (RoleEnum.USER.name().equals(account.getRole())) {
            account1 = doctorMapper.selectById(account.getId());
        }
        return account1;
    }
}
