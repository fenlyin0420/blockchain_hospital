package com.example.utils.JwtSm.util;

import com.example.utils.BCECUtil;
import com.example.utils.cert.SM2CertUtil;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPrivateKey;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPublicKey;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.util.Objects;

public class TxtUtil {
    public static InputStream streamCer;
    public static InputStream streamPri;
    private static BCECPublicKey publicKey;
    private static BCECPrivateKey privateKey;
    private static final String filePath = System.getProperty("user.dir") + "/sm2File/";
    public static void readKey(String name) throws CertificateException, NoSuchProviderException, NoSuchAlgorithmException, InvalidKeySpecException, IOException {
        Security.addProvider(new BouncyCastleProvider());
        X509Certificate cert;
        try {
            File filecer = new File(filePath+name+"/jwt.sm2.cer");
            streamCer = new FileInputStream(filecer);
            // 执行你的操作，使用 inputStream
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            File filepri = new File(filePath+name+"/jwt.sm2.pri");
            streamPri = new FileInputStream(filepri);
            // 执行你的操作，使用 inputStream
        } catch (IOException e) {
            e.printStackTrace();
        }

        int streamPriLen = Objects.requireNonNull(streamPri).available();
        cert = SM2CertUtil.getX509Certificate(streamCer);
        while (cert==null){
            try {
                File filecer = new File(filePath+name+"/jwt.sm2.cer");
                streamCer = new FileInputStream(filecer);
                // 执行你的操作，使用 inputStream
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                File filepri = new File(filePath+name+"/jwt.sm2.pri");
                streamPri = new FileInputStream(filepri);
                // 执行你的操作，使用 inputStream
            } catch (IOException e) {
                e.printStackTrace();
            }
            streamPriLen = Objects.requireNonNull(streamPri).available();
            cert = SM2CertUtil.getX509Certificate(streamCer);
        }
        byte[] priKeyData = new byte[streamPriLen];
        streamPri.read(priKeyData);
        // 从证书中获取公钥，从私钥文件中获取私钥
        publicKey = SM2CertUtil.getBCECPublicKey(cert);
        privateKey = BCECUtil.convertSEC1ToBCECPrivateKey(priKeyData);
    }

    public static BCECPublicKey getPublicKey() {
        return publicKey;
    }

    public static BCECPrivateKey getPrivateKey() {
        return privateKey;
    }
}
