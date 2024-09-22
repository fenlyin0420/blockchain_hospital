package com.example.utils.JwtSm;

import com.example.utils.BCECUtil;
import com.example.utils.JwtSm.util.FileUtil;
import com.example.utils.SM2Util;
import com.example.utils.cert.*;
import com.example.utils.cert.exception.InvalidX500NameException;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPrivateKey;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPublicKey;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.Assert;

import java.io.IOException;
import java.security.*;
import java.security.cert.X509Certificate;

public class SM2X509CertMakerUtil {
    static {
        Security.addProvider(new BouncyCastleProvider());
    }
    private static final String filePath = System.getProperty("user.dir") + "/sm2File/";
    public static boolean MakeCertificate(String name) {
        try {
            KeyPair subKP = SM2Util.generateKeyPair();
            X500Name subDN = buildSubjectDN();
            SM2PublicKey sm2SubPub = new SM2PublicKey(subKP.getPublic().getAlgorithm(),
                    (BCECPublicKey) subKP.getPublic());
            byte[] csr = CommonUtil.createCSR(subDN, sm2SubPub, subKP.getPrivate(),
                    SM2X509CertMaker.SIGN_ALGO_SM3WITHSM2).getEncoded();
            //如果没有文件夹则生成
            if (!cn.hutool.core.io.FileUtil.isDirectory(filePath+name+"/")) {
                cn.hutool.core.io.FileUtil.mkdir(filePath+name+"/");
            }
            savePriKey(filePath+name+"/jwt.sm2.pri", (BCECPrivateKey) subKP.getPrivate(),
                    (BCECPublicKey) subKP.getPublic());
            SM2X509CertMaker certMaker = buildCertMaker();
            X509Certificate cert = certMaker.makeSSLEndEntityCert(csr);
            FileUtil.writeFile(filePath+name+"/jwt.sm2.cer", cert.getEncoded());
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            Assert.fail();
        }
        return false;
    }

    public static void savePriKey(String filePath, BCECPrivateKey priKey, BCECPublicKey pubKey) throws IOException {
        ECPrivateKeyParameters priKeyParam = BCECUtil.convertPrivateKeyToParameters(priKey);
        ECPublicKeyParameters pubKeyParam = BCECUtil.convertPublicKeyToParameters(pubKey);
        byte[] derPriKey = BCECUtil.convertECPrivateKeyToSEC1(priKeyParam, pubKeyParam);
        FileUtil.writeFile(filePath, derPriKey);
    }

    public static X500Name buildSubjectDN() {
        X500NameBuilder builder = new X500NameBuilder(BCStyle.INSTANCE);
        builder.addRDN(BCStyle.C, "CN");
        builder.addRDN(BCStyle.O, "com.mc");
        builder.addRDN(BCStyle.OU, "com.mc");
        builder.addRDN(BCStyle.CN, "example.org");
        builder.addRDN(BCStyle.EmailAddress, "abc@example.org");
        return builder.build();
    }

    public static X500Name buildRootCADN() {
        X500NameBuilder builder = new X500NameBuilder(BCStyle.INSTANCE);
        builder.addRDN(BCStyle.C, "CN");
        builder.addRDN(BCStyle.O, "com.mc");
        builder.addRDN(BCStyle.OU, "com.mc");
        builder.addRDN(BCStyle.CN, "MC Root CA");
        return builder.build();
    }

    public static SM2X509CertMaker buildCertMaker() throws InvalidAlgorithmParameterException,
            NoSuchAlgorithmException, NoSuchProviderException, InvalidX500NameException {
        X500Name issuerName = buildRootCADN();
        KeyPair issKP = SM2Util.generateKeyPair();
        long certExpire = 20L * 365 * 24 * 60 * 60 * 1000; // 20年
        CertSNAllocator snAllocator = new RandomSNAllocator(); // 实际应用中可能需要使用数据库来保证证书序列号的唯一性。
        return new SM2X509CertMaker(issKP, certExpire, issuerName, snAllocator);
    }
}
