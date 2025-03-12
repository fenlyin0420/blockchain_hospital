package com.example.controller;

import com.example.common.Result;
import com.example.common.enums.ResultCodeEnum;
import com.example.common.enums.RoleEnum;
import com.example.dao.TraverseDAO;
import com.example.dto.BlockChainTraverseDTO;
import com.example.dto.TraverseDTO;
import com.example.entity.Account;
import com.example.entity.RingSign;
import com.example.entity.Traverse;
import com.example.entity.User;
import com.example.exception.CustomException;
import com.example.service.AdminService;
import com.example.service.DoctorService;
import com.example.service.KeyService;
import com.example.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import jakarta.annotation.Resource;


@RestController
@RequestMapping("/keys")
public class KeyController {
    @Resource
    private KeyService keyService;
    @Resource
    private AdminService adminService;
    @Resource
    private DoctorService doctorService;
    @Resource
    private UserService userService;
    @SuppressWarnings("unused")
    private static final Logger logger = LoggerFactory.getLogger(KeyController.class);

    // 查看个人信息
    @PostMapping("searchById")
    public Result searchById(@RequestBody Account account) {
        if (RoleEnum.ADMIN.name().equals(account.getRole())) {
            return Result.success(adminService.selectById(account.getId()));
        }
        if (RoleEnum.DOCTOR.name().equals(account.getRole())) {
            return Result.success(doctorService.selectById(account.getId()));
        }
        if (RoleEnum.USER.name().equals(account.getRole())) {
            return Result.success(userService.selectById(account.getId()));
        }
        return Result.success();
    }

    // 修改公钥和私钥
    @PutMapping("/updateKey")
    public Result updateKey(@RequestBody Account account) {
        if (RoleEnum.ADMIN.name().equals(account.getRole())) {
            adminService.updateKey(account);
        }
        if (RoleEnum.DOCTOR.name().equals(account.getRole())) {
            doctorService.updateKey(account);
        }
        if (RoleEnum.USER.name().equals(account.getRole())) {
            userService.updateKey(account);
        }
        return Result.success();
    }

    // 进行环签名
    @PostMapping("/sign")
    public Result sign(@RequestBody Traverse traverse) {
        RingSign ringSign = keyService.sign(traverse);
        return Result.success(ringSign);
    }

    @PostMapping("/blockchain/sign")
    public Result blockchainSign(@RequestBody BlockChainTraverseDTO blockChainTraverseDTO, @RequestParam Integer doctorId) {
        RingSign ringSign = keyService.blockchainSign(blockChainTraverseDTO, doctorId); 
        return Result.success(ringSign);
    }

    /**
     * 进行签名验证
     * 根据id找到对应病历，在对其进行验签
     * @param params.id 病历id
     * @return
     */
    @PostMapping("/verifySign")
    public Result verifySign(@RequestBody TraverseDAO traverseDAO) {
        RingSign ringSign = keyService.verifySign(traverseDAO);
        return Result.success(ringSign);
    }

    /**
     * 加密病历
     * @param traverse 待加密的病历
     * @param publicKey 公钥（可选）
     * @return 加密后的病历
     */
    @PostMapping("encrypt")
    public Result encrypt(@RequestBody Traverse traverse, @RequestParam(required = false) String publicKey) {
        // check whether publicKey is exist
        if (traverse.getUserId() != null) {
            User user = userService.selectById(traverse.getUserId());
            publicKey = user.getPublicKey();
        }
        
        if (publicKey == null || publicKey.equals("")){
            return Result.error(ResultCodeEnum.NO_PRIVATE_KEY_ERROR);
        }
        try {
            traverse = keyService.encrypt(traverse, publicKey);
        } catch (CustomException e){
            return Result.success(traverse, "201", "图片不存在");
        }
        return Result.success(traverse);
    }

    // 进行解密
    @PostMapping("/decrypt")
    public Result decrypt(@RequestBody TraverseDTO traverseDTO) {
        Traverse traverse = traverseDTO.getTraverse();
        String privateKey = traverseDTO.getPrivateKey();
        try {
            if (traverse.getUserId() != null) {
                User user = userService.selectById(traverse.getUserId());
                privateKey = user.getPrivateKey();
            }
            traverse = keyService.decrypt(traverse, privateKey);
        } catch (CustomException e) {
            return Result.error(e.getMsg());
        }
        return Result.success(traverse);
    }

    /**
     * 图像解密接口
     * 
     * @param traverse 接收图像url
     * @return 图像base64编码
     */
    // @PostMapping("/imgDecrypt")
    // public Result imgDecrypt(@RequestBody Traverse traverse) {
    //     if (" ".equals(traverse.getImg()) || traverse.getImg() == null || traverse.getImg().equals("")) {
    //         return Result.error("图像URL为空");
    //     }

    //     Object data;
    //     String Url = traverse.getImg();
    //     try {
    //         data = keyService.imgDecrypt(Url);
    //     } catch (IOException e) {
    //         // System.out.println(e.getMessage());
    //         return Result.error("图像传输失败");
    //     } catch (NullPointerException e) {
    //         // System.out.println(e.getMessage());
    //         return Result.error("无法找到病历图片");
    //     } catch (WebClientRequestException e) {
    //         // System.out.println(e.getMessage());
    //         return Result.error("图像URL无效，请检查病历");
    //     } catch (RuntimeException e) {
    //         // System.out.println(e.getMessage());
    //         return Result.error("解密失败:(");
    //     }
    //     return Result.success(data);
    // }
}
