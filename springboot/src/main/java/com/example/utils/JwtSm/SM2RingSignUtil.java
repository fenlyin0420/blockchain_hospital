package com.example.utils.JwtSm;

import com.example.utils.BCECUtil;
import com.example.utils.JwtSm.util.SM2RingSigner;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPrivateKey;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPublicKey;

import java.util.ArrayList;
import java.util.List;

public class SM2RingSignUtil {
    //签名
    public static String generate(String data, List<BCECPublicKey> publicKeys, BCECPrivateKey privateKey){
        List<ECPublicKeyParameters> L = new ArrayList<>();
        for (BCECPublicKey publicKey:publicKeys){
            ECPublicKeyParameters publicKeyParams = BCECUtil.convertPublicKeyToParameters(publicKey);
            L.add(publicKeyParams);
        }
        ECPrivateKeyParameters ecPrivateKeyParameters=BCECUtil.convertPrivateKeyToParameters(privateKey);
        byte[] m=hexStringToByteArray(data);
//        int n = publicKeys.size(); // 设置上限（不包括n）
//        Random random = new Random();
//        int randomNumber = random.nextInt(n) + 2;

//        System.out.println("随机数: " + randomNumber);
        int pi=publicKeys.size()/2;
        return SM2RingSigner.generateSignature(m,L,ecPrivateKeyParameters,pi);
    }

    //验签
    public static boolean verifySign(String data, List<BCECPublicKey> publicKeys,String key){
        List<ECPublicKeyParameters> L = new ArrayList<>();
        for (BCECPublicKey publicKey:publicKeys){
            ECPublicKeyParameters publicKeyParams = BCECUtil.convertPublicKeyToParameters(publicKey);
            L.add(publicKeyParams);
        }
        byte[] m=hexStringToByteArray(data);
        return SM2RingSigner.verifySignature(m,L,key);
    }

    // 将十六进制字符串转换为 byte 数组
    private static byte[] hexStringToByteArray(String hexString) {
        int length = hexString.length();
        if (length % 2 != 0) {
            throw new IllegalArgumentException("Invalid hex string length");
        }
        byte[] byteArray = new byte[length / 2];
        for (int i = 0; i < length; i += 2) {
            byteArray[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4)
                    + Character.digit(hexString.charAt(i + 1), 16));
        }
        return byteArray;
    }

    // 将 byte 数组转换为十六进制字符串
    private static String toHexString(byte[] byteArray) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : byteArray) {
            hexString.append(String.format("%02X", b));
        }
        return hexString.toString();
    }
}
