package com.example.utils.JwtSm.util;


import org.bouncycastle.crypto.digests.SM3Digest;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.math.ec.ECMultiplier;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.math.ec.FixedPointCombMultiplier;
import org.bouncycastle.util.BigIntegers;
import org.bouncycastle.util.encoders.Hex;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BestRingSigner {

    /**
     * 进行签名
     * @param m 签名的信息
     * @param L 公钥的Q点集合
     * @param d 私钥
     * @param pi 私钥对应的公钥在集合中的位置+1
     * @param G 椭圆曲线密码学中的基点
     * @return 签名的值
     */
    public static String generateSignature(byte[] m, List<ECPoint> L, ECPrivateKeyParameters d, int pi,ECPoint G){
        //****************************产生随机数***************************
        ECMultiplier var1 = createBasePointMultiplier();
        //构造变量c
        int length=L.size();
        List<BigInteger> C = new ArrayList<>();
        List<BigInteger> S = new ArrayList<>();
        //生成随机数k
        BigInteger k;
        k=redom(d);
        BigInteger kpi=k;
        //*****************************计算c[1](pi=pi)********************************
        // 编码列表L
        byte[] encodedL = encodeECPublicKeyParametersList(L);
        // 计算SM3哈希值
        ECPoint g = var1.multiply(G, kpi).normalize();//G*kpi
        byte[] hash = calculateSM3Hash(encodedL, m, g);
        BigInteger h=new BigInteger(hash);
        // 打印哈希结果
        String hashHex = Hex.toHexString(hash);
        for(int i=0;i<pi;i++){
            C.add(BigInteger.ZERO);
        }
        C.add(pi,h);
//**********************************计算循坏****************************************
        for(int i=0;i<pi;i++){
            S.add(BigInteger.ZERO);
        }
        for(int i=pi;i<length;i++){
            BigInteger h2=BigInteger.ZERO;
            S.add(i, BigInteger.ZERO);
            while (true)
            {
                k = redom(d);
                S.set(i, k);
                ECPoint var6 = var1.multiply(G, S.get(i)).normalize();//G*S
                ECPoint var7 = var1.multiply(L.get(i), (S.get(i).add(C.get(i)))).normalize();//p*(S+C)
                ECPoint var8 = var6.add(var7);
                byte[] h1 = calculateSM3Hash(encodedL, m, var8);
                h2 = new BigInteger(h1);
                int h3 = h2.compareTo(BigInteger.ZERO);
                if(h3>0){
                    break;
                }
            }
            C.add(i+1,h2);
        }
        C.set(0,C.get(length));
        for(int i=0;i<pi-1;i++) {
            BigInteger h2=BigInteger.ZERO;
            while (true) {
                k = redom(d);
                S.set(i, k);
                ECPoint var6 = var1.multiply(G, S.get(i)).normalize();
                ECPoint var7 = var1.multiply(L.get(i), (S.get(i).add(C.get(i)))).normalize();
                ECPoint var8 = var6.add(var7);
                byte[] h1 = calculateSM3Hash(encodedL, m, var8);
                h2 = new BigInteger(h1);
                int h3 = h2.compareTo(BigInteger.ZERO);
                if(h3>0){
                    break;
                }
            }
            C.set(i+1,h2);
        }
        //**********************************计算s[pi-1](pi=pi)**************************
        BigInteger var5;
        do {
            BigInteger var2 = d.getParameters().getN();
            BigInteger var3 = BigIntegers.modOddInverse(var2, d.getD().add(BigInteger.ONE));
            BigInteger var4 = kpi.subtract(C.get(pi-1).multiply(d.getD())).mod(var2);
            var5 = var3.multiply(var4).mod(var2);
        }while (var5.equals(BigInteger.ZERO));

        S.set(pi-1,var5);
        S.add(C.get(0));
        String key=merge(S);
        try {
            if(verifySignature(m,L,key,G)){
                System.out.println("检测签名是否合理:true");
                return key;
            }
            else {
                return "检测签名错误";
            }
        }catch(Throwable e){
            System.out.println("签名不合理，将重新生成签名");
            return generateSignature(m,L,d,pi,G);
        }
    }

    /**
     * 进行验签
     * @param m 签名的信息
     * @param L 公钥的Q点集合
     * @param key 签名的值
     * @param G 椭圆曲线密码学中的基点
     * @return 验签是否正确
     */
    public static boolean verifySignature(byte[] m, List<ECPoint> L,String key,ECPoint G){

        List<BigInteger> S=split(key);
        int length=L.size();
        List<BigInteger> C = new ArrayList<>();
        C.add(S.get(length));
        ECMultiplier var1 = createBasePointMultiplier();
        byte[] encodedL = encodeECPublicKeyParametersList(L);
        for (int i=0;i<length;i++){
            ECPoint var6=var1.multiply(G,S.get(i)).normalize();
            ECPoint var7=var1.multiply(L.get(i),S.get(i).add(C.get(i))).normalize();
            ECPoint var8=var6.add(var7);
            byte[] h1=calculateSM3Hash(encodedL,m,var8);
            BigInteger h2=new BigInteger(h1);
            C.add(i+1,h2);
        }
        if(Objects.equals(C.get(0), C.get(length))){
            return true;
        }
        return false;
    }

    /**
     * 对集合进行合成
     * @param M 集合
     * @return 合成的字符串
     */
    private static String merge(List<BigInteger> M) {
        // 将 List M 合并到字符串 L
        StringBuilder sb = new StringBuilder();
        String delimiter = ",";
        for (BigInteger num : M) {
            sb.append(num.toString()).append(delimiter);
        }
        // 去除最后一个分隔符
        if (sb.length() > 0) {
            sb.setLength(sb.length() - delimiter.length());
        }
        String L = sb.toString();
        return L;
    }

    /**
     * 对字符串进行分解
     * @param str 字符串
     * @return 分解成一个集合
     */
    private static List<BigInteger> split(String str) {
        String delimiter = ",";
        // 拆分字符串到 List<BigInteger>
        List<BigInteger> list = new ArrayList<>();
        String[] elements = str.split(delimiter);
        for (String element : elements) {
            list.add(new BigInteger(element));
        }
        return list;
    }

    /**
     * 生成随机数
     * @param privateKeyParameters 私钥  确保长度一样
     * @return 随机数
     */
    private static BigInteger redom(ECPrivateKeyParameters privateKeyParameters){
        // 创建 SecureRandom 对象
        SecureRandom secureRandom = new SecureRandom();
        // 生成一个随机 BigInteger，确保大于0
        BigInteger randomBigInteger;
        do {
            randomBigInteger = new BigInteger(privateKeyParameters.getParameters().getN().bitLength() - 1, secureRandom);
        } while (randomBigInteger.compareTo(BigInteger.ZERO) <= 0);
        return randomBigInteger;
    }

    /**
     * 对公钥编码
     * @param L 公钥点
     * @return 公钥点的和
     */
    private static byte[] encodeECPublicKeyParametersList(List<ECPoint> L) {
        ECPoint sum = null;
        for (ECPoint publicKey : L) {
            if (sum == null) {
                sum = publicKey;
            } else {
                sum = sum.add(publicKey);
            }
        }
        if (sum != null) {
            return sum.getEncoded(false);
        }
        return new byte[0];
    }

    /**
     * 利用SM3进行哈希计算
     * @param encodedL 传入对公钥的重新编码
     * @param m 需要签名的数据
     * @param G 椭圆曲线密码学中的基点
     * @return 返回C值
     */
    private static byte[] calculateSM3Hash(byte[] encodedL, byte[] m, ECPoint G) {
        SM3Digest sm3Digest = new SM3Digest();
        sm3Digest.update(encodedL, 0, encodedL.length);
        sm3Digest.update(m, 0, m.length);
        sm3Digest.update(G.getEncoded(true), 0, G.getEncoded(true).length);
        byte[] hash = new byte[32];
        sm3Digest.doFinal(hash, 0);
        return hash;
    }

    /**
     * 生成一个乘法器
     * @return 返回一个圆
     */
    protected static ECMultiplier createBasePointMultiplier() {
        return new FixedPointCombMultiplier();
    }
}
