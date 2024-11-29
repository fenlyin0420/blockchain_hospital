package com.example.utils.JwtSm;

import cn.hutool.core.date.DateUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.google.common.collect.Maps;
import com.example.utils.JwtSm.util.SMAlgorithm;
import com.example.utils.JwtSm.util.TxtUtil;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPrivateKey;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPublicKey;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;
import java.util.Date;
import java.util.Map;

public class JwtUtil {

    /**
     * SM2需要的公钥和私钥
     */
    public static BCECPublicKey publicKey;
    public static BCECPrivateKey privateKey;
    public static String name;
    int time;

    public JwtUtil(String name) {
        try {
            TxtUtil.readKey(name);
        } catch (CertificateException | NoSuchAlgorithmException | InvalidKeySpecException | IOException |
                 NoSuchProviderException e) {
            new JwtUtil(name);
        }
        publicKey=TxtUtil.getPublicKey();
        privateKey= TxtUtil.getPrivateKey();
        JwtUtil.name =name;
        ISSUER=name;
        ALGORITHM = SMAlgorithm.builder().publicKey(publicKey).privateKey(privateKey).build();
    }

    /**
     * 设置发行人
     */
    private static String ISSUER;

    /**
     * 初始化SM3WithSM2算法
     */
    private static SMAlgorithm ALGORITHM;

    /**
     * 生成jwt
     * @param claims 携带的payload
     * @return jwt token
     */
    public String genToken(Map<String, String> claims,int time){
        try {
            JWTCreator.Builder builder = JWT.create()
                    .withIssuer(ISSUER)
                    .withExpiresAt(DateUtil.offsetHour(new Date(), time));//两小时后到期
            claims.forEach(builder::withClaim);
            return builder.sign(ALGORITHM);
        } catch (IllegalArgumentException e) {
            System.out.println("jwt生成失败"+e);
        }
        return null;
    }

    /**
     * 验签方法
     * @param token jwt token
     * @return jwt payload
     */
    public Map<String, String> verifyToken(String token) {
        JWTVerifier verifier = JWT.require(ALGORITHM).withIssuer(ISSUER).build();
        DecodedJWT jwt =  verifier.verify(token);
        Map<String, Claim> map = jwt.getClaims();
        Map<String, String> resultMap = Maps.newHashMap();
        map.forEach((k,v) -> resultMap.put(k, v.asString()));
        return resultMap;
    }
}
