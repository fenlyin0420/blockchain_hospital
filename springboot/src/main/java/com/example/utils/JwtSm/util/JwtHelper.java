package com.example.utils.JwtSm.util;

import com.example.utils.BCECUtil;
import com.example.utils.cert.SM2CertUtil;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPrivateKey;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPublicKey;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.InputStream;
import java.security.Security;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.Objects;

/**
 * 生成jwt的工具类，基于auth0.java-jwt封装
 * 签名算法使用SM3WithSM2
 * payload统一使用Map<String, String>类型
 * @author Created by zkk on 2020/9/22
 **/
@Slf4j
public class JwtHelper {

    static {
        Security.addProvider(new BouncyCastleProvider());
        X509Certificate cert;
        try {
            InputStream streamCer = Thread.currentThread().getContextClassLoader().getResourceAsStream("jwt.sm2.cer");
            InputStream streamPri = Thread.currentThread().getContextClassLoader().getResourceAsStream("jwt.sm2.pri");
            int streamPriLen = Objects.requireNonNull(streamPri).available();
            cert = SM2CertUtil.getX509Certificate(streamCer);

            byte[] priKeyData = new byte[streamPriLen];
            streamPri.read(priKeyData);
            // 从证书中获取公钥，从私钥文件中获取私钥
            publicKey = SM2CertUtil.getBCECPublicKey(cert);
            privateKey = BCECUtil.convertSEC1ToBCECPrivateKey(priKeyData);

        } catch (Exception e) {
            log.error("JWT工具初始化异常", e);
        }

    }

    /**
     * 设置发行人
     */
    @SuppressWarnings("unused")
    private static final String ISSUER = "machao";

    /**
     * SM2需要的公钥和私钥
     */
    private static BCECPublicKey publicKey;
    private static BCECPrivateKey privateKey;

    /**
     * 初始化SM3WithSM2算法
     */
    @SuppressWarnings("unused")
    private static final SMAlgorithm ALGORITHM = SMAlgorithm.builder().publicKey(publicKey).privateKey(privateKey).build();

    /**
     * 生成jwt
     * @param claims 携带的payload
     * @return jwt token
     */
    public static String genToken(Map<String, String> claims){
//        try {
//            JWTCreator.Builder builder = JWT.create()
//                    .withIssuer(ISSUER)
//                    .withExpiresAt(DateUtil.offsetHour(new Date(), 2));//两小时后到期
//            claims.forEach(builder::withClaim);
//            return builder.sign(ALGORITHM);
//        } catch (IllegalArgumentException e) {
//            log.error("jwt生成失败", e);
//        }
        return null;
    }

    /**
     * 验签方法
     * @param token jwt token
     * @return jwt payload
     */
    public static Map<String, String> verifyToken(String token) {
//        JWTVerifier verifier = JWT.require(ALGORITHM).withIssuer(ISSUER).build();
//        DecodedJWT jwt =  verifier.verify(token);
//        Map<String, Claim> map = jwt.getClaims();
//        Map<String, String> resultMap = Maps.newHashMap();
//        map.forEach((k,v) -> resultMap.put(k, v.asString()));
//        return resultMap;
        return null;
    }
}
