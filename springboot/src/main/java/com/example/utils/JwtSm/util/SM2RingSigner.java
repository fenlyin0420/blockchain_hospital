package com.example.utils.JwtSm.util;//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//


import org.bouncycastle.crypto.digests.SM3Digest;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.math.ec.ECMultiplier;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.math.ec.FixedPointCombMultiplier;
import org.bouncycastle.util.encoders.Hex;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SM2RingSigner {
    public static String generateSignature(byte[] m, List<ECPublicKeyParameters> L, ECPrivateKeyParameters d, int pi){

        ECMultiplier var1 = createBasePointMultiplier();
        //****************************产生随机数***************************
        //构造变量c
        int length=L.size();
        List<BigInteger> C = new ArrayList<>();
        List<BigInteger> S = new ArrayList<>();
        //生成随机数k
        BigInteger k;
        k=redom(d);
        // System.out.println("随机数"+k);
        BigInteger kpi=k;
        //*****************************计算c[1](pi=pi)********************************
        // 编码列表L
        byte[] encodedL = encodeECPublicKeyParametersList(L);
        // 计算SM3哈希值
        ECPoint G = var1.multiply(L.get(0).getParameters().getG(), kpi).normalize();//G*kpi
        byte[] hash = calculateSM3Hash(encodedL, m, G);
        BigInteger h=new BigInteger(hash);
        // 打印哈希结果
        String hashHex = Hex.toHexString(hash);
        // System.out.println("SM3 哈希结果: " + hashHex);
        for(int i=0;i<pi;i++){
            C.add(BigInteger.ZERO);
        }
        C.add(pi,h);
        //C.set(0, "2".getBytes());
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
                ECPoint var6 = var1.multiply(L.get(0).getParameters().getG(), S.get(i)).normalize();//G*S
                ECPoint var7 = var1.multiply(L.get(i).getQ(), (S.get(i).add(C.get(i)))).normalize();//p*(S+C)
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
                ECPoint var6 = var1.multiply(L.get(0).getParameters().getG(), S.get(i)).normalize();
                ECPoint var7 = var1.multiply(L.get(i).getQ(), (S.get(i).add(C.get(i)))).normalize();
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
            BigInteger var3 = d.getD().add(BigInteger.ONE);
            BigInteger var4 = kpi.subtract(C.get(pi - 1).multiply(d.getD()));
            var5 = (var4.divide(var3)).mod(d.getParameters().getN());
        }while (var5.equals(BigInteger.ZERO));

        S.set(pi-1,kpi);
        S.add(C.get(0));

        String key=merge(S);
        try {
            if(verifySignature(m,L,key)){
                // System.out.println("通过：true");
                return key;
            }
            else {
                return generateSignature(m,L,d,pi);
            }
        }catch(Throwable e){
            // System.out.println("*********************************************失败********************************************************************");
            return generateSignature(m,L,d,pi);
        }
    }

    public static boolean verifySignature(byte[] m, List<ECPublicKeyParameters> L,String key){
        // System.out.println("开始验签");
        List<BigInteger> S=split(key);
        int length=L.size();
        List<BigInteger> C = new ArrayList<>();
        for (int i=0;i<=length;i++){
            C.add(BigInteger.ZERO);
        }
        C.set(0,S.get(length));
        ECMultiplier var1 = createBasePointMultiplier();
        byte[] encodedL = encodeECPublicKeyParametersList(L);
        for (int i=0;i<length;i++){
            if(i>0){
                ECPoint G = var1.multiply(L.get(0).getParameters().getG(), S.get(i)).normalize();//G*kpi
                byte[] hash = calculateSM3Hash(encodedL, m, G);
                BigInteger h = new BigInteger(hash);
                C.set(i + 1, h);
                if(i+1==length){
                    if(Objects.equals(C.get(0), C.get(length))){
                        return true;
                    }
                    break;
                }
                for (int j=i+1;j<length;j++) {
                    ECPoint var6=var1.multiply(L.get(0).getParameters().getG(),S.get(j)).normalize();//G*S
                    ECPoint var7=var1.multiply(L.get(j).getQ(),S.get(j).add(C.get(j))).normalize();//Q*(S+C)
                    ECPoint var8=var6.add(var7);//G*S+Q*(S+C)=Q
                    byte[] h1=calculateSM3Hash(encodedL,m,var8);
                    BigInteger h2=new BigInteger(h1);
                    C.set(j+1,h2);
                }
                if(Objects.equals(C.get(0), C.get(length))){
                    return true;
                }
            }
            ECPoint var6=var1.multiply(L.get(0).getParameters().getG(),S.get(i)).normalize();//G*S
            ECPoint var7=var1.multiply(L.get(i).getQ(),S.get(i).add(C.get(i))).normalize();//Q*(S+C)
            ECPoint var8=var6.add(var7);//G*S+Q*(S+C)=Q
            byte[] h1=calculateSM3Hash(encodedL,m,var8);
            BigInteger h2=new BigInteger(h1);
            C.set(i+1,h2);
        }
        if(Objects.equals(C.get(0), C.get(length))){
            return true;
        }
        return false;
    }

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
        // System.out.println("L:"+L);
        return L;
    }

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

    //生成随机数
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

    //对公钥编码
    private static byte[] encodeECPublicKeyParametersList(List<ECPublicKeyParameters> L) {
        ECPoint sum = null;
        for (ECPublicKeyParameters publicKey : L) {
            if (sum == null) {
                sum = publicKey.getQ();
            } else {
                sum = sum.add(publicKey.getQ());
            }
        }
        if (sum != null) {
            return sum.getEncoded(false);
        }
        return new byte[0];
    }

    private static byte[] calculateSM3Hash(byte[] encodedL, byte[] m, ECPoint G) {
        SM3Digest sm3Digest = new SM3Digest();
        sm3Digest.update(encodedL, 0, encodedL.length);
        sm3Digest.update(m, 0, m.length);
        sm3Digest.update(G.getEncoded(true), 0, G.getEncoded(true).length);
        byte[] hash = new byte[32];
        sm3Digest.doFinal(hash, 0);
        return hash;
    }

    protected static ECMultiplier createBasePointMultiplier() {
        return new FixedPointCombMultiplier();
    }
}
