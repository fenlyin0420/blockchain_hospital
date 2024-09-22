package com.example.utils.JwtSm.util;

import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.SignatureGenerationException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.utils.SM2Util;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.crypto.CryptoException;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPrivateKey;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPublicKey;

/**
 * 扩充auth0.java-jwt的签名算法
 * SM2是国家密码管理局于2010年12月17日发布的椭圆曲线公钥密码算法
 * 是一种非对称加密算法，证书保存在 /resources/jwt.sm2.cer
 * SM3是中华人民共和国政府采用的一种密码散列函数标准，由国家密码管理局于2010年12月17日发布
 *
 * QA: 为什么使用该系列算法 ===> 支持国产！
 * 基于ECC的SM2证书普遍采用256位密钥长度，加密强度等同于3072位RSA证书，远高于业界普遍采用的2048位RSA证书
 * 测基准试：com.ai.base.tool.JwtTest、com.ai.base.tool.JwtTestSm3WithSm2
 * 对各种算法进行简单的性能测试，SM3WithSM2速度大大快于ECDSA256

 * @see Algorithm
 * 这里使用SM3WithSM2的方式签名、验签，对标SHA256withRSA(RS256)
 * signature = SM2(SM3(base64encode(header) + '.' + base64encode(payload)), 'SECRET_KEY')
 * <p>
 * 签名：用SM3对jwt生成摘要， 再用SM2的私钥对其进行加密(如上面的公式)，完成后即生成jwt的signature
 * 验签：拿到jwt，用base64解码，再用SM2算法+SM2公钥对signature进行解密，就得到了信息的摘要，然后把信息用相同的算法（SM3）生成摘要与jwt解密后的signature进行对比，一致则验签通过，这样就达到了防篡改的效果
 *
 * @author Created by zkk on 2020/9/23
 **/
@Slf4j
public class SMAlgorithm extends Algorithm {

    private final BCECPublicKey publicKey;
    private final BCECPrivateKey privateKey;

    private static final byte JWT_PART_SEPARATOR = (byte) 46;

    protected SMAlgorithm(BCECPublicKey publicKey, BCECPrivateKey privateKey) {
        super("SM3WithSM2", "SM3WithSM2");
        this.publicKey = publicKey;
        this.privateKey = privateKey;
        if (publicKey == null || privateKey == null) {
            throw new IllegalArgumentException("The Key Provider cannot be null.");
        }
    }

    @Override
    public void verify(DecodedJWT jwt) throws SignatureVerificationException {
        byte[] signatureBytes = Base64.decodeBase64(jwt.getSignature());
        byte[] data = combineSignByte(jwt.getHeader().getBytes(), jwt.getPayload().getBytes());
        try {
            if(!SM2Util.verify(publicKey, data, signatureBytes)) {
                throw new SignatureVerificationException(this);
            }
        } catch (Exception e) {
            throw new SignatureVerificationException(this);
        }
    }

    @Override
    @Deprecated
    public byte[] sign(byte[] contentBytes) throws SignatureGenerationException {
        // 不支持该方法
        throw new RuntimeException("该方法已过时");
    }

    @Override
    public byte[] sign(byte[] headerBytes, byte[] payloadBytes) throws SignatureGenerationException {
        byte[] hash = combineSignByte(headerBytes, payloadBytes);
        byte[] signatureByte;
        try {
            signatureByte = SM2Util.sign(privateKey, hash);
        } catch (CryptoException e) {
            throw new SignatureGenerationException(this, e);
        }

        return signatureByte;
    }

    /**
     * 拼接签名部分 header + . + payload
     *
     * @param headerBytes  header
     * @param payloadBytes payload
     * @return bytes
     */
    private byte[] combineSignByte(byte[] headerBytes, byte[] payloadBytes) {
        // header + payload
        byte[] hash = new byte[headerBytes.length + payloadBytes.length + 1];
        System.arraycopy(headerBytes, 0, hash, 0, headerBytes.length);
        hash[headerBytes.length] = JWT_PART_SEPARATOR;
        System.arraycopy(payloadBytes, 0, hash, headerBytes.length + 1, payloadBytes.length);
        return hash;
    }

    /**
     * builder
     */
    public static class SMAlogrithmBuilder {
        private BCECPublicKey publicKey;
        private BCECPrivateKey privateKey;

        SMAlogrithmBuilder() {
        }

        public SMAlogrithmBuilder publicKey(final BCECPublicKey publicKey) {
            this.publicKey = publicKey;
            return this;
        }

        public SMAlogrithmBuilder privateKey(final BCECPrivateKey privateKey) {
            this.privateKey = privateKey;
            return this;
        }

        public SMAlgorithm build() {
            return new SMAlgorithm(this.publicKey, this.privateKey);
        }
    }

    public static SMAlogrithmBuilder builder() {
        return new SMAlogrithmBuilder();
    }
}
