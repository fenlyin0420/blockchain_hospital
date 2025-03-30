package com.example.service;

import com.example.common.enums.RoleEnum;
import com.example.dao.TraverseDAO;
import com.example.dto.BlockChainTraverseDTO;
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
import com.google.gson.Gson;

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
    @Resource
    private FileService fileService;

    /**
     * 对提供的病历进行签名，将签名结果即签名时用到的公钥环持久化
     * @param traverse 待签名的病历, 只包含病历 id
     * @return 签名结果
     */
    public RingSign sign(Traverse traverse) {
        RingSign ringSign = new RingSign();
        TraverseDAO traverseDAO = new TraverseDAO();
        traverse = traverseMapper.selectById(traverse.getId());
        if (traverse == null) {
            throw new CustomException(ResultCodeEnum.USER_RESULT_GET_ERROR);
        }
        Doctor doctor = doctorMapper.selectById(traverse.getDoctorId());

        // 生成公钥环
        String ringOfPublicKey = getStringPubKeyRing();
        List<String> publicKeys = getListPubKeyRing();


        // json 化要签名的病历
        Gson gson = new Gson();
        String data = gson.toJson(traverse);

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

        // 进行签名,钥匙进行格式化
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
        String signData = BestRingSignUtil.generate(data, list, bcecPrivateKey, pi);

        // 设置并返回数据
        ringSign.setSignData(signData);
        ringSign.setMessage("签名成功");
        ringSign.setSignPubKey(ringOfPublicKey);

        traverseDAO.setId(traverse.getId());
        traverseDAO.setSignData(signData);
        traverseDAO.setSignPubKey(ringOfPublicKey);
        traverseMapper.setSignData(traverseDAO);
        return ringSign;
    }

    /**
     * 上传区块链病历的签名
     * @param blockChainTraverseDTO
     * @param doctorId
     * @return
     */
    public RingSign blockchainSign(BlockChainTraverseDTO blockChainTraverseDTO, Integer doctorId) {
        RingSign ringSign = new RingSign();
        Doctor doctor = doctorMapper.selectById(doctorId);
        if (doctor == null) {
            throw new CustomException(ResultCodeEnum.USER_RESULT_GET_ERROR);
        }
        // 生成公钥环
        String ringOfPublicKey = getStringPubKeyRing();
        List<String> publicKeys = getListPubKeyRing();

        // json 化要签名的病历
        Gson gson = new Gson();
        String data = gson.toJson(blockChainTraverseDTO);

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

        // 进行签名,钥匙进行格式化
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
        String signData = BestRingSignUtil.generate(data, list, bcecPrivateKey, pi);

        // 设置并返回数据
        ringSign.setSignData(signData);
        ringSign.setMessage("签名成功");
        ringSign.setSignPubKey(ringOfPublicKey);
        return ringSign;
    }
    /**
     * 验证签名，需要原数据、签名数据、签名公钥环
     * @param traverseDAO 原数据
     * @return 验签结果
     */
    public RingSign verifySign(TraverseDAO traverseDAO) {
        Traverse traverse = new Traverse(
            traverseDAO.getId(),
            traverseDAO.getUserId(),
            traverseDAO.getDoctorId(),
            traverseDAO.getHospitalId(),
            traverseDAO.getMainDiagnosis(),
            traverseDAO.getSecondaryDiagnosis(),
            traverseDAO.getDrug(),
            traverseDAO.getInHospital(),
            traverseDAO.getCareStatus(),
            traverseDAO.getBedId(),
            traverseDAO.getTimestamp(),
            traverseDAO.getTreatmentDate(),
            traverseDAO.getRecordDate(),
            traverseDAO.getImg(),
            traverseDAO.getIllnessDetail(),
            traverseDAO.getFurtherCheck(),
            traverseDAO.getNonMedicine(),
            traverseDAO.getCare(),
            traverseDAO.getDiet()
        );

        // json 化要签名的病历
        Gson gson = new Gson();
        String data = gson.toJson(traverse);

        List<Doctor> doctors = getDoctorsByPubKeyRing(traverseDAO);
        List<String> publicKeys = new ArrayList<>();
        for (Doctor doctor : doctors) {
            publicKeys.add(doctor.getPublicKey());
        }
        List<BCECPublicKey> BCECPublicKeys = new ArrayList<>();
        for (String pubKey : publicKeys) {
            try {
                BCECPublicKeys.add(MySM2Util.str2pub(pubKey));
            } catch (Exception e) {
                throw new CustomException(ResultCodeEnum.USER_KEY_ERROR);
            }
        }
        boolean result = BestRingSignUtil.verifySign(data, BCECPublicKeys, traverseDAO.getSignData());

        RingSign ringSign = new RingSign();
        ringSign.setSignData(traverseDAO.getSignData());
        ringSign.setSignPubKey(traverseDAO.getSignPubKey());
        if (result) {
            ringSign.setMessage("成功");
        } else {
            ringSign.setMessage("失败");
        }
        return ringSign;
    }
    /**
     * 获取公钥环
     * 
     * 环上每个节点格式为： 姓名:公钥
     * 节点与节点之间通过 {@code', '} 分隔
     * @return 公钥环字符串
     */
    public String getStringPubKeyRing() {
        // 查询所有的医生
        List<Doctor> doctors = doctorMapper.select();
        // 提取并合并环公钥
        StringBuilder mergedString = new StringBuilder();
        for (Doctor doctor : doctors) {
            // e.g. "Alice:publicKey, Bob:publicKey, ..."
            mergedString.append(doctor.getName()).append(":").append(doctor.getPublicKey()).append(", ");
        }
        // 删除最后的逗号和空格
        if (mergedString.length() > 0) {
            mergedString.delete(mergedString.length() - 2, mergedString.length());
        }
        return mergedString.toString();
    }

    public List<String> getListPubKeyRing() {
        // 查询所有的医生
        List<Doctor> doctors = doctorMapper.select();
        // 提取环公钥
        List<String> publicKeys = new ArrayList<>();
        for (Doctor doctor : doctors) {
            publicKeys.add(doctor.getPublicKey());
        }
        return publicKeys;
    }

    /**
     * 根据公钥环获取医生列表
     * 
     * @param traverseDAO 病历
     * @return 公钥环上的医生列表医生列表
     * @throws CustomException 公钥环为空
     */
    public List<Doctor> getDoctorsByPubKeyRing(TraverseDAO traverseDAO) {
        // 查询环公钥字符串
        String s = traverseDAO.getSignPubKey();
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
     * encrypt all String fields that are provided by frontend.
     * @param traverse need to encrypt traverse
     * @return encrypted traverse
     */
    public Traverse encrypt(Traverse traverse, String publicKey){
        try {

            // 加密文字
            List<Field> encrypteFields = traverse.getEncryptedFields();
            for (Field field : encrypteFields) {
                field.setAccessible(true);
                Object value = field.get(traverse);
                if (value != null && !Objects.equals(value, "")){
                    // encrypt `String` field
                    String cipherText = MySM2Util.encryption(publicKey, (String) value);
                    // update corrsponding field
                    field.set(traverse, cipherText);
                }
            }

            // 加密图片
            String imgUrl = traverse.getImg().strip();
//            System.out.println("img url: \n" + imgUrl + "\n");
            // 图片文件
            MultipartFile imgFile = MyMultipartFile.fromURL(imgUrl);
            BufferedImage img = ImgUtil.MultipartFileToBufferedImage(imgFile);
            // 加密后的图片
            img = ImgUtil.ImageEncryptor(img, publicKey);
            imgFile = ImgUtil.BufferedImageToMultipartFile(img, imgFile.getOriginalFilename());
            // 保存
            fileService.replace(imgFile);
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
            throw new CustomException("400", "图片不存在");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return traverse;
    }

    /**
     * 解密病历
     * 
     * @param traverse 与病历相关的参数
     * @return
     */
    public Traverse decrypt(Traverse traverse, String privateKey) throws CustomException {
        try {
            // text decrypt
            List<Field> encrypteFields = traverse.getEncryptedFields();
            for (Field field : encrypteFields) {
                field.setAccessible(true);
                Object value = field.get(traverse);
                if (value != null){
                    String plainText = MySM2Util.decrypt(privateKey, (String) value);
                    System.out.println("plainText" + plainText);
                    field.set(traverse, plainText);
                }
            }

            String imgUrl = traverse.getImg().strip();
            System.out.println("img url: \n" + imgUrl + "\n");
            MultipartFile imgFile = MyMultipartFile.fromURL(imgUrl);
            BufferedImage img = ImgUtil.MultipartFileToBufferedImage(imgFile);
            img = ImgUtil.ImageDecryptor(img, privateKey);
            imgFile = ImgUtil.BufferedImageToMultipartFile(img, imgFile.getOriginalFilename());
            fileService.replace(imgFile); 
        } catch (Exception e) {
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
            throws IOException, NullPointerException, WebClientRequestException, RuntimeException, Exception {
        MultipartFile file = MyMultipartFile.fromURL(imgURL);
        BufferedImage img = ImgUtil.MultipartFileToBufferedImage(file);
        img = ImgUtil.ImageDecryptor(img, "test");
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
