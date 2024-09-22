package com.example.utils.JwtSm;

import com.example.utils.SM2Util;
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

public class MySm2Util {
    public static BCECPublicKey bcPubKey;
    public static BCECPrivateKey bcecPriKey;
    public static String point;

    /**
     * 获取公钥和私钥
     */
    public static KeyPair getkey() throws InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException {
        KeyPair keyPair= SM2Util.generateKeyPair();
        bcPubKey = (BCECPublicKey) keyPair.getPublic();
        bcecPriKey=(BCECPrivateKey) keyPair.getPrivate();
        point=ByteUtils.toHexString(bcPubKey.getQ().getEncoded(false)).toUpperCase();
        return keyPair;
    }
    public static BCECPublicKey getBcPubKey(){
        return bcPubKey;
    }
    public static BCECPrivateKey getBcecPriKey(){
        return bcecPriKey;
    }

    public static String getPoint() {
        return point;
    }

    /**
     * BCECPublicKey转String
     */
    public static String pub2str(BCECPublicKey publicKey){
        return Base64.encodeBase64String(publicKey.getEncoded());
    }

    /**
     * BCECPrivateKey转String
     */
    public static String pri2str(BCECPrivateKey privateKey){
        return Base64.encodeBase64String(privateKey.getEncoded());
    }

    /**
     * String转BCECPublicKey
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
     * String转BCECPrivateKey
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
     * @param publickey  公钥
     * @param data 需要加密的数据
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     * @throws NoSuchProviderException
     * @throws InvalidCipherTextException
     */
    public static String encryption(String publickey,String data) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchProviderException, InvalidCipherTextException {
        byte[] dataBytes= data.getBytes();
        BCECPublicKey publicKey=str2pub(publickey);
        byte[] cipherbyte=SM2Util.encrypt(publicKey, dataBytes);
        String ciphertext= java.util.Base64.getEncoder().encodeToString(cipherbyte);
        return ciphertext;
    }


    /**
     * 解密
     * @param privatekey 私钥
     * @param data 密文
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     * @throws NoSuchProviderException
     * @throws InvalidCipherTextException
     */
    public static String decrypt(String privatekey,String data) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchProviderException, InvalidCipherTextException {
        byte[] dataBytes= java.util.Base64.getDecoder().decode(data);
        BCECPrivateKey priKey=str2pri(privatekey);
        byte[] plaintbyte=SM2Util.decrypt(priKey, dataBytes);
        String plainttext= new String(plaintbyte);
        return plainttext;
    }
    /**
     * 签名
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
     */
    public static boolean visa(String publickey,String sign,String data) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchProviderException {
        byte[] dataBytes= data.getBytes();
        byte[] signBytes= java.util.Base64.getDecoder().decode(sign);
        BCECPublicKey publicKey=str2pub(publickey);
        boolean result=SM2Util.verify(publicKey,dataBytes,signBytes);
        return result;
    }
}
