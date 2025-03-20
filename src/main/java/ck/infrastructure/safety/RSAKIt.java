package ck.infrastructure.safety;

import ck.infrastructure.notify.INotifyService;
import ck.infrastructure.validator.NotNullValidator;
import lombok.Cleanup;
import lombok.Getter;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;

@Component
public class RSAKIt {
    private final RSAPrivateKey privateKey;
    private final KeyFactory keyFactory;
    private final INotifyService notifyService;
    public RSAKIt(INotifyService notifyService) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        this.notifyService = notifyService;
        @Cleanup
        InputStream in2 = getClass().getResourceAsStream("/private.key");
        new NotNullValidator<>(in2, new IllegalArgumentException("无法获取公钥")).run();
        assert in2 != null;
        String privateKeyPEM = new String(in2.readAllBytes()).replaceAll("\n","");
        keyFactory = KeyFactory.getInstance("RSA");
        byte[] encoded = Base64.getDecoder().decode(privateKeyPEM);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encoded);
        privateKey = (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
    }
    @SneakyThrows
    public String getPublicKey(){
        BigInteger publicExponent = java.math.BigInteger.valueOf(65537);
        RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(privateKey.getModulus(), publicExponent);
        PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);
        return Base64.getEncoder().encodeToString(publicKey.getEncoded());
    }
    @SneakyThrows
    public String decrypt(String encryptedText){
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] encryptedBytes = Base64.getDecoder().decode(encryptedText);
        return new String(cipher.doFinal(encryptedBytes));
    }
    @SneakyThrows
    public String encrypt(String text){
        BigInteger publicExponent = java.math.BigInteger.valueOf(65537);
        RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(privateKey.getModulus(), publicExponent);
        PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return Base64.getEncoder().encodeToString(cipher.doFinal(text.getBytes(StandardCharsets.UTF_8)));
    }
}
