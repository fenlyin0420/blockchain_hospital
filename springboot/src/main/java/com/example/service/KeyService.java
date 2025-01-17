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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClientRequestException;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.lang.reflect.Field;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.*;

import com.example.common.enums.ResultCodeEnum;

import jakarta.annotation.Resource;

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
    @Resource
    private TraverseService traverseService;

    /**
     * <p> 签名 </p>
     * <p> {@code params.id} 病历id </p>
     * 
     * @param params
     */
    public RingSign sign(Params params) {
        RingSign ringSign = new RingSign();
        Traverse traverse = traverseMapper.selectById(params.getId());
        Doctor doctor = doctorMapper.selectById(traverse.getDoctorId());
        // 生成公钥环
        String signPublicKey = getPubKeyRing(traverse);
        ringSign.setSignPubKey(signPublicKey);
        traverse.setSignPubKey(signPublicKey);
        traverseMapper.updateById(traverse);
        traverse = traverseMapper.selectById(params.getId());

        // 合成data
        String data = traverse.signData();

        // 提取环公钥
        List<Doctor> doctors = getDoctorsByPubKeyRing(traverse);
        List<String> publicKeys = new ArrayList<>();
        for (Doctor doctorTemp : doctors) {
            publicKeys.add(doctorTemp.getPublicKey());
        }
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
        ringSign.setSignData(data);
        ringSign.setSignKey(key);
        ringSign.setMessage("签名成功");
        traverse.setSignData(data);
        traverse.setSignKey(key);
        traverseMapper.updateById(traverse);
        return ringSign;
    }

    public RingSign verifySign(Params params) {
        Traverse traverse = traverseMapper.selectById(params.getId());
        String key = traverse.getSignKey();
        List<Doctor> doctors = getDoctorsByPubKeyRing(traverse);
        List<String> publicKeys = new ArrayList<>();
        for (Doctor doctor : doctors) {
            publicKeys.add(doctor.getPublicKey());
        }
        List<BCECPublicKey> list = new ArrayList<>();
        for (String pubKey : publicKeys) {
            try {
                list.add(MySM2Util.str2pub(pubKey));
            } catch (Exception e) {
                throw new CustomException(ResultCodeEnum.USER_KEY_ERROR);
            }
        }
        boolean result = BestRingSignUtil.verifySign(traverse.signData(), list, key);

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

    /**
     * 获取病历公钥环
     * 
     * 
     * 环上每个节点格式为： 姓名:公钥
     * 节点与节点之间通过 {@code', '} 分隔
     * 
     * @param traverse 待设置公钥环的病历
     */
    public String getPubKeyRing(Traverse traverse) {
        // 查询所有的医生
        List<Doctor> doctors = doctorMapper.select();
        // 提取并合并环公钥、

        StringBuilder mergedString = new StringBuilder();
        for (Doctor doctor : doctors) {
            mergedString.append(doctor.getName()).append(":").append(doctor.getPublicKey()).append(", ");
        }

        // 删除最后的逗号和空格
        if (mergedString.length() > 0) {
            mergedString.delete(mergedString.length() - 2, mergedString.length());
        }
        return mergedString.toString();
    }

    /**
     * 根据公钥环获取医生列表
     * 
     * @param traverse 病历
     * @return 公钥环上的医生列表医生列表
     * @throws CustomException 公钥环为空
     */
    public List<Doctor> getDoctorsByPubKeyRing(Traverse traverse) {
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
     * 加密病历
     */
    public Traverse encrypt(Traverse traverse) {
        try {
            User user = userMapper.selectById(traverse.getUserId());
            Class<?> clazz = traverse.getClass();
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                Object value = field.get(traverse);
                String strValue = null;
                Boolean isStep = false;
                if (value != null){
                    if (value instanceof Integer || value instanceof Date){
                        // strValue = value.toString();
                        isStep = true;
                        continue;
                    } else {
                        strValue = (String) value;
                    }
                    // 加密字段
                    String cipherText = MySM2Util.encryption(user.getPublicKey(), strValue);
                    System.out.println("加密后：" + cipherText);
                    // 对于整数，先不更新，类型不匹配
                    if (!isStep) {
                        field.set(traverse, cipherText);
                    }
                }
            }
            traverseService.updateById(traverse);
        } catch (NullPointerException e) {
            e.printStackTrace();
            throw new CustomException("400", "患者不存在");
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException("400", "加密失败");
        }
        return traverse;
    }

    /**
     * 解密病历
     * 
     * @param traverse 与病历相关的参数
     * @return
     */
    public Traverse decrypt(Traverse traverse) throws CustomException {
        try {
            User user = userMapper.selectById(traverse.getUserId());
            if (user == null)
                throw new CustomException("用户不存在");
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
     * 
     * @param imgURL 加密图像在服务器的url
     * @return 解密后图片的base64编码
     */
    public String imgDecrypt(String imgURL)
            throws IOException, NullPointerException, WebClientRequestException, RuntimeException {
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
