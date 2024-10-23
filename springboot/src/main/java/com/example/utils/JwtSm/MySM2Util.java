package com.example.utils.JwtSm;

import com.example.utils.SM2Util;
import lombok.Data;
import lombok.Getter;
import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.crypto.CryptoException;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPrivateKey;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPublicKey;
import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;

import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

@Data
public class MySM2Util {
    @Getter
    public static BCECPublicKey bcPubKey;
    @Getter
    public static BCECPrivateKey bcPriKey;
    @Getter
    public static String point;

    /**
     * 生成公私钥对
     * @return KeyPair 公私钥对
     * @throws InvalidAlgorithmParameterException InvalidAlgorithmParameterException
     * @throws NoSuchAlgorithmException NoSuchAlgorithmException
     * @throws NoSuchProviderException NoSuchProviderException
     */
    public static KeyPair getKey() throws InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException {
        KeyPair keyPair= SM2Util.generateKeyPair();
        bcPubKey = (BCECPublicKey) keyPair.getPublic();
        bcPriKey = (BCECPrivateKey) keyPair.getPrivate();
        point=ByteUtils.toHexString(bcPubKey.getQ().getEncoded(false)).toUpperCase();
        return keyPair;
    }

    /**
     * BCECPublicKey 转 String
     */
    public static String pub2str(BCECPublicKey publicKey){
        return Base64.encodeBase64String(publicKey.getEncoded());
    }

    /**
     * BCECPrivateKey 转 String
     */
    public static String pri2str(BCECPrivateKey privateKey){
        return Base64.encodeBase64String(privateKey.getEncoded());
    }

    /**
     * String 转 BCECPublicKey
     */
    public static BCECPublicKey str2pub(String publicKey) throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeySpecException {
        byte[] publicKeyBytes = java.util.Base64.getDecoder().decode(publicKey);

        // 创建X509EncodedKeySpec对象，用于构造公钥的规范
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);
        // 使用EC算法和Bouncy Castle提供的KeyFactory来生成公钥对象
        KeyFactory keyFactory = KeyFactory.getInstance("EC", "BC");
        BCECPublicKey PubKey = (BCECPublicKey) keyFactory.generatePublic(keySpec);
        return PubKey;
    }

    /**
     * String 转 BCECPrivateKey
     */
    public static BCECPrivateKey str2pri(String privateKey) throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeySpecException {
        byte[] privateKeyBytes = java.util.Base64.getDecoder().decode(privateKey);
        // 创建PKCS8EncodedKeySpec对象，用于构造私钥的规范
        PKCS8EncodedKeySpec keySpecs = new PKCS8EncodedKeySpec(privateKeyBytes);
        // 使用EC算法和Bouncy Castle提供的KeyFactory来生成私钥对象
        KeyFactory keyFactory = KeyFactory.getInstance("EC", "BC");
        BCECPrivateKey PriKey = (BCECPrivateKey) keyFactory.generatePrivate(keySpecs);
        return PriKey;
    }

    /**
     * 加密
     * @param publicKey  公钥
     * @param plainText 需要加密的数据
     * @return 返回密文
     * @throws NoSuchAlgorithmException NoSuchAlgorithmException
     * @throws InvalidKeySpecException InvalidKeySpecException
     * @throws NoSuchProviderException NoSuchProviderException
     * @throws InvalidCipherTextException InvalidCipherTextException
     */
    public static String encryption(String publicKey,String plainText) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchProviderException, InvalidCipherTextException {
        byte[] dataBytes = plainText.getBytes();
        byte[] cipherByte = SM2Util.encrypt(str2pub(publicKey), dataBytes);
        return java.util.Base64.getEncoder().encodeToString(cipherByte);
    }


    /**
     * 解密
     * @param privateKey 私钥
     * @param cipherText 密文
     * @return 明文字符串
     * @throws NoSuchAlgorithmException 未找到算法
     * @throws InvalidKeySpecException 无效私钥
     * @throws NoSuchProviderException NoSuchProviderException
     * @throws InvalidCipherTextException InvalidCipherTextException
     */
    public static String decrypt(String privateKey,String cipherText) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchProviderException, InvalidCipherTextException {
        byte[] dataBytes= java.util.Base64.getDecoder().decode(cipherText);
        BCECPrivateKey priKey=str2pri(privateKey);
        byte[] plaintByte=SM2Util.decrypt(priKey, dataBytes);
        return new String(plaintByte);
    }

    /**
     * 签名
     * @param privatekey
     * @param data
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     * @throws NoSuchProviderException
     * @throws CryptoException
     */
    public static String signaure(String privatekey,String data) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchProviderException, CryptoException {
        BCECPrivateKey priKey=str2pri(privatekey);
        byte[] dataBytes= data.getBytes();
        byte[] k=SM2Util.sign(priKey,dataBytes);
        String sign= java.util.Base64.getEncoder().encodeToString(k);
        return sign;
    }

    /**
     * 验签
     * @param publickey
     * @param sign
     * @param data
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     * @throws NoSuchProviderException
     */
    public static boolean visa(String publickey,String sign,String data) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchProviderException {
        byte[] dataBytes= data.getBytes();
        byte[] signBytes= java.util.Base64.getDecoder().decode(sign);
        BCECPublicKey publicKey=str2pub(publickey);
        boolean result=SM2Util.verify(publicKey,dataBytes,signBytes);
        return result;
    }
}
