package com.example.utils.JwtSm;

import com.example.utils.SM2Util;
import org.bouncycastle.crypto.CryptoException;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPrivateKey;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPublicKey;
import org.bouncycastle.math.ec.ECPoint;

import java.util.List;

public class RingsignUtil {
    /**
     * 计算公钥的和
     * @param publicKeys 公钥
     * @return 返回签名值
     */
    private static ECPoint sumPublicKeys(List<BCECPublicKey> publicKeys) {
        ECPoint sum = null;
        for (BCECPublicKey publicKey : publicKeys) {
            if (sum == null) {
                sum = publicKey.getQ();
            } else {
                sum = sum.add(publicKey.getQ());
            }
        }
        return sum;
    }

    /**
     * 进行签名
     * @param privateKey 私钥
     * @param publicKeys 公钥集合
     * @param data 原文
     * @return 返回签名值
     */
    public static byte[] sign(BCECPrivateKey privateKey, List<BCECPublicKey> publicKeys, byte[] data) throws CryptoException {
        ECPoint sumPoints = sumPublicKeys(publicKeys);
        // System.out.println(sumPoints.toString());
        byte[] withId=sumPoints.toString().getBytes();
        byte[] sign = SM2Util.sign(privateKey, withId, data);
        return sign;
    }

    /**
     * 验签
     * @param publicKeys 公钥集合
     * @param data 原文
     * @param sign 签名值
     * @return 返回验签结果
     */
    public static boolean verify(List<BCECPublicKey> publicKeys,byte[] data,byte[] sign){
        ECPoint sumPoints = sumPublicKeys(publicKeys);
        // System.out.println(sumPoints.toString());
        for (BCECPublicKey publicKey:publicKeys){
            boolean result=SM2Util.verify(publicKey, sumPoints.toString().getBytes(),data,sign);
            if(result){
                return true;
            }
        }
        return false;
    }
}
