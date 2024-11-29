package com.example.utils.JwtSm;

import com.example.utils.BCECUtil;
import com.example.utils.JwtSm.util.TxtUtil;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPrivateKey;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPublicKey;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemWriter;

import java.io.IOException;
import java.io.StringWriter;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.List;


public class pemtxtUtil {
    public static ECPrivateKeyParameters privateKeyParameters;
    public static ECPublicKeyParameters publicKeyParams;

    public pemtxtUtil(String name) throws CertificateException, NoSuchAlgorithmException, InvalidKeySpecException, IOException, NoSuchProviderException {
        TxtUtil.readKey(name);
        BCECPublicKey publicKey=TxtUtil.getPublicKey();
        BCECPrivateKey privateKey= TxtUtil.getPrivateKey();
        privateKeyParameters= BCECUtil.convertPrivateKeyToParameters(privateKey);
        publicKeyParams = BCECUtil.convertPublicKeyToParameters(publicKey);
    }

    public pemtxtUtil(String priKey,String pubKey) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchProviderException {
        BCECPublicKey publicKey= MySM2Util.str2pub(pubKey);
        BCECPrivateKey privateKey= MySM2Util.str2pri(priKey);
        privateKeyParameters= BCECUtil.convertPrivateKeyToParameters(privateKey);
        publicKeyParams = BCECUtil.convertPublicKeyToParameters(publicKey);
    }

    public pemtxtUtil() {

    }

    public List<String> pem() throws IOException {
        // 将私钥转换为PEM格式
        PemObject privateKeyPemObject =
                new PemObject("PRIVATE KEY", privateKeyParameters.getD().toByteArray());
        StringWriter privateKeyWriter = new StringWriter();
        PemWriter privateKeyPemWriter = new PemWriter(privateKeyWriter);
        privateKeyPemWriter.writeObject(privateKeyPemObject);
        privateKeyPemWriter.close();
        String privateKeyPem = privateKeyWriter.toString();
        // 将公钥转换为PEM格式
        byte[] xCoord = publicKeyParams.getQ().getAffineXCoord().getEncoded();
        byte[] yCoord = publicKeyParams.getQ().getAffineYCoord().getEncoded();
        byte[] publicKeyBytes = new byte[xCoord.length + yCoord.length];
        System.arraycopy(xCoord, 0, publicKeyBytes, 0, xCoord.length);
        System.arraycopy(yCoord, 0, publicKeyBytes, xCoord.length, yCoord.length);
        PemObject publicKeyPemObject =
                new PemObject("PUBLIC KEY", publicKeyBytes);
        StringWriter publicKeyWriter = new StringWriter();
        PemWriter publicKeyPemWriter = new PemWriter(publicKeyWriter);
        publicKeyPemWriter.writeObject(publicKeyPemObject);
        publicKeyPemWriter.close();
        String publicKeyPem = publicKeyWriter.toString();
        // 输出公钥和私钥
        System.out.println("公钥: " + publicKeyPem);
        System.out.println("私钥: " + privateKeyPem);
        List<String> L=new ArrayList<>();
        L.add(publicKeyPem);
        L.add(privateKeyPem);
        return L;
    }

    //将公钥转化为pem
    public String pubToPem(String publicKey){
        String publicKeyPem="";
        try {
            try {
                MySM2Util.getKey();
            } catch (InvalidAlgorithmParameterException | NoSuchAlgorithmException | NoSuchProviderException e) {
                throw new RuntimeException(e);
            }
            BCECPublicKey bcecPublicKey= MySM2Util.str2pub(publicKey);
            System.out.println("bcec成功");
            ECPublicKeyParameters publicKeyP = BCECUtil.convertPublicKeyToParameters(bcecPublicKey);
            System.out.println("ec转化成功");
            byte[] xCoord = publicKeyP.getQ().getAffineXCoord().getEncoded();
            byte[] yCoord = publicKeyP.getQ().getAffineYCoord().getEncoded();
            byte[] publicKeyBytes = new byte[xCoord.length + yCoord.length];
            System.out.println("转化字节成功");
            System.arraycopy(xCoord, 0, publicKeyBytes, 0, xCoord.length);
            System.arraycopy(yCoord, 0, publicKeyBytes, xCoord.length, yCoord.length);
            PemObject publicKeyPemObject =
                    new PemObject("PUBLIC KEY", publicKeyBytes);
            StringWriter publicKeyWriter = new StringWriter();
            PemWriter publicKeyPemWriter = new PemWriter(publicKeyWriter);
            publicKeyPemWriter.writeObject(publicKeyPemObject);
            publicKeyPemWriter.close();
            publicKeyPem = publicKeyWriter.toString();
        }catch (Exception e){
            publicKeyPem="错误";
        }
        return publicKeyPem;
    }
}
